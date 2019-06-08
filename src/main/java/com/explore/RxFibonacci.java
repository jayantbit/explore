package com.explore;


import io.reactivex.Observable;

public class RxFibonacci {

   static Observable<Integer> fibs()
   {

      return Observable.create(subscriber-> {

         System.out.println("calculating Fibo");
         int a=0;
         int b=1;
         int c;
         subscriber.onNext(a);
         subscriber.onNext(b);

         while(!subscriber.isDisposed())
         {
            c=a+b;
            subscriber.onNext(c);
            a=b;
            b=c;
            Thread.sleep(1000);
         }

         subscriber.onComplete();

      });

   }
}
