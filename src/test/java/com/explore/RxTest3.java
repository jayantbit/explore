package com.explore;

import io.reactivex.*;
import io.reactivex.disposables.Disposable;
import io.reactivex.observers.TestObserver;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.schedulers.TestScheduler;
import org.junit.Test;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

import java.util.concurrent.TimeUnit;

import static java.util.concurrent.TimeUnit.SECONDS;


public class RxTest3 {

    //Rxjava2 API will not accept this sub
    Subscriber<String> sub = new Subscriber<String>() {
        @Override
        public void onSubscribe(Subscription s) {

        }

        @Override
        public void onNext(String s) {
            System.out.println(s);
        }

        @Override
        public void onError(Throwable t) {

        }

        @Override
        public void onComplete() {
            System.out.println("Done");
        }
    };

    Observable observable = Observable.<String>create(new ObservableOnSubscribe<String>() {
        @Override
        public void subscribe(ObservableEmitter<String> emitter) throws Exception {
          emitter.onNext("Hello");
          emitter.onComplete();
        }
    });

    @Test
    public void test0()
    {

        observable.subscribe(System.out::println,System.out::println,()-> {System.out.println("Done");});

    }

    @Test
    public void test1()
    {
        TestScheduler scheduler = new TestScheduler();

        //will emit a count every second
        TestObserver<Long> o = Observable.interval(1, SECONDS, scheduler)
                .test();

        o.assertNoValues();
        scheduler.advanceTimeBy(1, SECONDS);
        o.assertValues(0L);
        scheduler.advanceTimeBy(1, SECONDS);
        o.assertValues(0L, 1L);
        o.dispose(); // Dispose the connection.
        scheduler.advanceTimeBy(100, SECONDS);
        o.assertValues(0L, 1L);

    }

    @Test
    public void disposeableTest()
    {
        Observable.just(1, 2, 3)
                .takeWhile(integer -> integer < 3)
                .test()
                .assertResult(1, 2);

        Observable.just(1, 2, 3)
                .takeUntil(integer -> integer < 3)
                .test()
                .assertResult(1);
    }

    @Test
    public void unsubscribeTest()
    {
       Disposable disposable= RxFibonacci.fibs()
               .subscribeOn(Schedulers.computation())  //otherwise it will be blocking call
               .observeOn(Schedulers.trampoline())
                .subscribe(num-> {

                    System.out.println(num);
                    System.out.println(Thread.currentThread().getName());
                });



        System.out.println("here");
       RxTest.sleep(4000);
       disposable.dispose(); //disposing off after 4 seconds

    }

    @Test
    public void zipTest()
    {

       long timeNow=System.currentTimeMillis();

        Observable.zip(RxFibonacci.fibs().subscribeOn(Schedulers.computation()),
                Observable.interval(2, SECONDS),
                (obs, timer) -> obs)
                .subscribe(item -> {
                    System.out.println(System.currentTimeMillis() - timeNow);
                    System.out.println(item);
                    System.out.println(" ");
                });

        System.out.println("here");
        RxTest.sleep(10000);

    }

}
