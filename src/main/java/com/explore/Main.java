package com.explore;

import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import org.junit.Test;

import java.util.concurrent.CompletableFuture;


public class Main {

  public static CompletableFuture<Integer> compute()
  {
    return  CompletableFuture.supplyAsync(()-> {
      sleep(2000);
      return 2;
    });
  }

  static void  sleep(long ms)
  {
    try {
      Thread.sleep(ms);
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
  }


  public static void main(String[] args) {
   userInputTest();

  }


  public void thenApplyDemo()
  {
    compute()
            .thenApply(data-> {return data+2; })
            .thenAccept(data -> {
              System.out.println(data);
            })

            .thenRun(()-> {
              System.out.println("Work was done");
            });

    System.out.println("Here");

    sleep(4000);

    System.out.println("End of main");
  }


  public static void userInputTest()
  {
    Disposable d=RxReader.readLines()
            .observeOn(Schedulers.trampoline())
            .map(Integer::parseInt)
            .flatMapMaybe(pos->RxFibonacci.fibs().elementAt(pos))
            .subscribe(System.out::println);

    System.out.println("Non blocking");

    while(!d.isDisposed())
    {

    }
  }

}
