package com.explore;

import org.junit.Test;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ForkJoinPool;

import static org.junit.Assert.assertEquals;

public class CmpTests {

  //Test to deomonstrate working of CompletableFuture

 public int compute(int sleepms)
 {
   System.out.println("Compute: "+Thread.currentThread());
   sleep(sleepms);
   return 2;
 }

  public  CompletableFuture<Integer> create(int sleepms) {
    return CompletableFuture.supplyAsync(() -> compute(sleepms));
  }

  public  CompletableFuture<Integer> createWithPool(int sleepms) {
    ForkJoinPool forkJoinPool = new ForkJoinPool(10);
   return CompletableFuture.supplyAsync(() -> compute(sleepms),forkJoinPool);
  }


  void  sleep(long ms)
  {
    try {
      Thread.sleep(ms);
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
  }



  @Test
  public void test1()
  {
    create(2000)
        .thenApply(data-> {return data+2; })
        .thenAccept(data -> {
          System.out.println(data);
        })

        .thenRun(()-> {
          System.out.println("Work was done");
        })
        .thenRun(()-> {
          System.out.println("This thenRun ladder can continue");
        });

    System.out.println("Came Here... Non blocking call");

    sleep(4000);

    System.out.println("End of main");
  }


  @Test
  public void test2() throws Exception
  {
    int x= create(2000).get();

    System.out.println("Here....blocking call");

    System.out.println(x);
  }

  @Test
  public void test3() throws Exception
  {
    int x= create(2000).getNow(-1);   //if future value is present use it,otherwise take default value
    System.out.println(x);
    assertEquals(x,-1);
    //should print default value
  }

  @Test
  public void test4() throws Exception
  {
     CompletableFuture<Integer> future = create(0);
     sleep(100);
    int x=future.getNow(-1);   //if future value is present use it,otherwise take default value
    System.out.println(x);
    assertEquals(x,2);
    //should return actual value,
  }


  public void printIt(int data)
  {
    System.out.println("PrintIt "+Thread.currentThread());
    System.out.println(data);
  }

  @Test
  public void test5()
  {
    //thread of execution

    create(1000)
        .thenAccept(data-> printIt(data));
    //printIt  will be done by a thread in forkJoinPool , bcoz data is not available at the time we call thenAccept

    sleep(2000);

  }

  @Test
  public void test6()
  {
    //thread of execution

    create(0)
        .thenAccept(data-> printIt(data));
    //printIt  will be done by Main Thread , bcoz data is available at the time we call thenAccept

    sleep(200);

  }


  @Test
  public void test7()
  {
    //thread of execution

    createWithPool(0)
        .thenAccept(data-> printIt(data));
    //printIt  will be done by Main Thread , bcoz data is available at the time we call thenAccept

    sleep(200);
  }
}
