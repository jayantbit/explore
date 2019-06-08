package com.explore;


import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import io.reactivex.schedulers.Schedulers;
import io.reactivex.schedulers.TestScheduler;
import org.junit.Test;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

import io.reactivex.BackpressureStrategy;
import io.reactivex.Observable;

public class RxTest1 {

   //https://www.youtube.com/watch?v=ZJwIrfqAnVM&t=781s
   //reactivex.io

   @Test
   public void test0() {
      Observable.just("Hello World").subscribe(System.out::println);
   }

   @Test
   public void test1() {
      Random random = new Random();

      Observable.just(10, 20, 30)
       .map(number -> random.nextInt(number))
       .subscribe(System.out::println);
   }

   @Test
   public void test2() {
      Random random = new Random();

      Observable.intervalRange(0, 20, 500, 500, TimeUnit.MILLISECONDS)
       .map(number -> random.nextInt(20))
       .subscribe(System.out::println);
      //no output
      //we have to give it some time to finish..use blocking subscribe instead


   }

   @Test
   public void test3() {
      Random random = new Random();

      Observable.intervalRange(0, 20, 500, 500, TimeUnit.MILLISECONDS)
       .map(number -> random.nextInt(20))
       .blockingSubscribe(System.out::println);
      //no output
      //we have to give it some time to finish..use blocking subscribe instead


   }

   @Test
   public void test4() {
      Random random = new Random();

      Observable.just(1, 2, 3)
       .flatMap(x -> Observable.just(x * 10))
       .blockingSubscribe(System.out::println);
      //use flatmap when ur map function returns a observable
      //otherwise use map..
      //concatMap is similar to flatMap..it will also preserve order
   }


   @Test
   public void test5() {
      Random random = new Random();

      Observable.intervalRange(0, 20, 500, 500, TimeUnit.MILLISECONDS)
       .flatMap(number -> Observable.just(random.nextInt(20)).delay(random.nextInt(500), TimeUnit.MILLISECONDS))
       .blockingSubscribe(System.out::println);
   }

   private Observable<Integer> randomInput()
   {
      Random random = new Random();
      return Observable.intervalRange(0, 20, 500, 500, TimeUnit.MILLISECONDS)
              .flatMap(number -> Observable.just(random.nextInt(20)).delay(random.nextInt(500), TimeUnit.MILLISECONDS));

   }

   @Test
   public void fiboTest() {



    //  int num=RxFibonacci.fibs().elementAt(10).blockingGet();
     // System.out.println(num);

      RxFibonacci.fibs().skip(5).take(10).subscribe(System.out::println);

      System.out.println();

     randomInput()
             .flatMapMaybe(pos->RxFibonacci.fibs().elementAt(pos))
       .blockingSubscribe(System.out::println);

   }



   @Test
   public void flatMapTest()
   {
      final List<String> items = Arrays.asList("a", "b", "c", "d", "e", "f");

      final TestScheduler scheduler = new TestScheduler();
      //allows manually advancing a virtual time

      Observable.fromIterable(items)
              .flatMap( s -> {
                 final int delay = new Random().nextInt(2);
                 return Observable.just(s + "x")
                         .delay(delay, TimeUnit.SECONDS,scheduler);
              })
              //.toList()
              .doOnNext(System.out::println)   //doOnNext will consume and also propogate the value
              .subscribe();
             // .subscribe(System.out::println);


      scheduler.advanceTimeBy(1,TimeUnit.MINUTES);

      //order will not be maintained
      //FlatMap merges the emissions of these Observables, so that they may interleave

      //using switchMap will give only fx

      /*
      SwitchMap
      whenever a new item is emitted by the source Observable,
      it will unsubscribe to and stop mirroring the Observable that was generated from the previously-emitted item,
      and begin only mirroring the current one.
      */

      //using concatMap will printoutput in order

   }

   @Test
   public void concatMapTest()
   {
      final List<String> items = Arrays.asList("a", "b", "c", "d", "e", "f");

      final TestScheduler scheduler = new TestScheduler();
      //allows manually advancing a virtual time

      Observable.fromIterable(items)
              .concatMap( s -> {
                 final int delay = new Random().nextInt(2);
                 return Observable.just(s + "x")
                         .delay(delay, TimeUnit.SECONDS,scheduler);
              })
              .subscribe(System.out::println);


      scheduler.advanceTimeBy(1,TimeUnit.MINUTES);


   }

   @Test
   public void flatMapAndConcatMapCompare() throws Exception {

      //concatMap has one big flaw: it waits for each observable to finish all the work until next one is processed
      //it will break asynchrony
      //source: https://medium.com/appunite-edu-collection/rxjava-flatmap-switchmap-and-concatmap-differences-examples-6d1f3ff88ee0


      final List<String> items = Arrays.asList("a", "b", "c", "d", "e", "f");

      final TestScheduler scheduler1 = new TestScheduler();
      final TestScheduler scheduler2 = new TestScheduler();

      Observable.fromIterable(items)
              .flatMap(s -> Observable.just(s + "x")
                      .delay(5, TimeUnit.SECONDS, scheduler1)
                      .doOnNext(str -> System.out.print(scheduler1.now(TimeUnit.SECONDS) + " ")))
              .toList()
              .doOnSuccess(strings -> System.out.println("\nEND:" + scheduler1.now(TimeUnit.SECONDS)))
              .subscribe();

      scheduler1.advanceTimeBy(1, TimeUnit.MINUTES);

      Observable.fromIterable(items)
              .concatMap(s -> Observable.just(s + "x")
                      .delay(5, TimeUnit.SECONDS, scheduler2)
                      .doOnNext(str -> System.out.print(scheduler2.now(TimeUnit.SECONDS) + " ")))
              .toList()
              .doOnSuccess(strings -> System.out.println("\nEND:" + scheduler2.now(TimeUnit.SECONDS)))
              .subscribe();

      scheduler2.advanceTimeBy(1, TimeUnit.MINUTES);

      /*
      flatMap: not preserving the order of items, works asynchronously
      switchMap: unsubscribing from previous observable after emitting new one
      concatMap: preserving the order of items, works synchronously
       */


   }


}
