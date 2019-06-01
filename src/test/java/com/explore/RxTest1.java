package com.explore;


import java.util.Random;
import java.util.concurrent.TimeUnit;

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
   }


   @Test
   public void test5() {
      Random random = new Random();

      Observable.intervalRange(0, 20, 500, 500, TimeUnit.MILLISECONDS)
       .flatMap(number -> Observable.just(random.nextInt(20)).delay(random.nextInt(500), TimeUnit.MILLISECONDS))
       .blockingSubscribe(System.out::println);
   }

   @Test
   public void fiboTest() {

      Random random = new Random();

    //  int num=RxFibonacci.fibs().elementAt(10).blockingGet();
     // System.out.println(num);

      RxFibonacci.fibs().skip(5).take(10).subscribe(System.out::println);

      System.out.println();

      Observable.intervalRange(0, 20, 500, 500, TimeUnit.MILLISECONDS)
       .flatMap(number -> Observable.just(random.nextInt(20)).delay(random.nextInt(500), TimeUnit.MILLISECONDS))
       .flatMapMaybe(pos->RxFibonacci.fibs().elementAt(pos))
       .blockingSubscribe(System.out::println);




   }
}
