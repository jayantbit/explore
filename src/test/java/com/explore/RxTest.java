package com.explore;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.IntStream;

import org.junit.Test;

import io.reactivex.Flowable;
import io.reactivex.Observable;
import io.reactivex.Scheduler;
import io.reactivex.Single;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subscribers.TestSubscriber;

public class RxTest {


   @Test
   public void test0()
   {
      Observable
       .just("Hello world").subscribe(System.out::println);
   }


   @Test
   public void test1()
   {
      long start=System.currentTimeMillis();
      AtomicInteger ac= new AtomicInteger(0);

      Flowable<String> flow=Flowable.fromCallable(()->{
       Thread.sleep(1000);
       ac.incrementAndGet();
       return "Hello";
      });


    flow.subscribe(System.out::println);
    flow.subscribe(s-> {System.out.println(s+" world");});

    System.out.println("Blocking call");

      System.out.println("Ending Test");
      long end=System.currentTimeMillis();
      System.out.println("Time Taken "+ (end-start));
      System.out.println("Callable called "+ac.get()+" times");
      //time taken 2 sec
   }

   @Test
   public void test2()
   {
      long start=System.currentTimeMillis();
      AtomicInteger ac= new AtomicInteger(0);

      Flowable<String> flow=Flowable.fromCallable(()->{
         Thread.sleep(1000);
         ac.incrementAndGet();
         return "Hello";
      });


      flow.subscribeOn(Schedulers.io()).subscribe(System.out::println);
      flow.subscribeOn(Schedulers.io()).subscribe(s-> {System.out.println(s+" world");});

      System.out.println("Non Blocking call");
      sleep(1100); //giving time to finish async calls.
      System.out.println("Ending Test");
      long end=System.currentTimeMillis();
      System.out.println("Time Taken "+ (end-start));
      System.out.println("Callable called "+ac.get()+" times");
      //time taken 1 sec
   }




   @Test
   public void test3()
   {
      Observable.create(emitter -> {
         while (!emitter.isDisposed()) {
            long time = System.currentTimeMillis();
            emitter.onNext(time);
            if (time % 2 != 0) {
               emitter.onError(new IllegalStateException("Odd millisecond!"));
               break;
            }
         }
      })
       .subscribe(System.out::println, Throwable::printStackTrace);

   }


   @Test
   public void test4()
   {

      Flowable.range(1, 10)
       .observeOn(Schedulers.computation())
       .map(v -> {
          System.out.println(Thread.currentThread().getName()); return v * v;})
       .blockingSubscribe(System.out::println);

      //map operation runs on the same thread

   }

   @Test
   public void test5()
   {  //2947+5580

      // parallelism in RxJava means running independent flows and merging their results back into a single flow.
      //here map runs on different threads
      Flowable.range(1, 10)
       .flatMap(v ->
        Flowable.just(v)
         .subscribeOn(Schedulers.computation())
         .map(w -> {
            System.out.println(Thread.currentThread().getName()); return v * v;})
       )
       .subscribe(System.out::println);




      //another way to handle parallel calls
      Flowable.range(1, 10)
       .parallel()
       .runOn(Schedulers.computation())
       .map(v -> v * v)
       .sequential()
       .blockingSubscribe(System.out::println);
   }

   @Test
   public void test6()
   {
      AtomicInteger count = new AtomicInteger();

      Observable.range(1, 10)
       .doOnNext(ignored -> count.incrementAndGet())
       .ignoreElements()
       .andThen(Single.defer(() -> Single.just(count.get())))
       .subscribe(System.out::println);


      //prints 10
   }


   @Test
   public void test7()
   {
      AtomicInteger count = new AtomicInteger();

      Observable.range(1, 10)
       .doOnNext(ignored -> count.incrementAndGet())
       .ignoreElements()
       .andThen(Single.just(count.get()))
       .subscribe(System.out::println);


      //prints 0
      //this prints 0 because Single.just(count.get()) is evaluated at assembly time when the dataflow hasn't even run yet
   }



   @Test
   public void test8()
   {
      Observable<String> obs = Observable.fromArray("a","b","c","d");
      obs.subscribe(System.out::println);

   }




   static void  sleep(long ms)
   {
      try {
         Thread.sleep(ms);
      } catch (InterruptedException e) {
         e.printStackTrace();
      }
   }
}
