package com.explore;

import org.junit.Test;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ForkJoinPool;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class CompletableFutureTests {

  //Unit Tests to demonstrate working of CompletableFuture

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

  public  CompletableFuture<Integer> createWithException() {

    return CompletableFuture.supplyAsync(() -> {
      throw new RuntimeException("Something went Wrong");
      //return 2;
    });
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
        .thenApply(data-> data+2)    //similar to map
        .thenAccept(data -> {                   // thenAccept similar to forEach
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

    CompletableFuture<Integer> future = create(2000);

    int x= future.getNow(-1);   //if future value is present use it,otherwise take default value
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

    CompletableFuture<Void> future= create(0)
        .thenAccept(data-> printIt(data));
    //printIt  will be done by Main Thread , bcoz data is available at the time we call thenAccept

    sleep(200);

  }


  @Test
  public void test7()
  {
    //thread of execution
      //it will not use coomon pool, use a seperate dedicated pool.
    createWithPool(0)
        .thenAccept(data-> printIt(data));
    //printIt  will be done by Main Thread , bcoz data is available at the time we call thenAccept

    sleep(200);
  }

  @Test
  public void test8()
  {
    //Creating the pipeline first then,, completing it later

    //future object has 2 dependents...think of it as as tree
    //future object has 2 branches...future1 and future2
    //completing the future (future.complete) ..will complete both of them


    CompletableFuture<Integer>  future= new CompletableFuture<Integer>();

        CompletableFuture<Integer> future1= future
                     .thenApply(data -> data*2)
                         .thenApply(data-> data+1);


        CompletableFuture<Void> future2= future.thenAccept(data-> System.out.println(data));;



    future1.thenAccept(data-> System.out.println(data)); //adding to future1 pipeline.
    future.complete(2);




  }


  public int handleException(Throwable throwable)
  {
    System.out.println("Got error "+throwable);
    System.out.println("Resolving with 0");
    return 0;
  }


  @Test
  public void test9()
  {
    createWithException()
         .thenApply(data->data+1)
        .exceptionally(throwable -> handleException(throwable))
        .thenApply(data->data+1)
        .thenAccept(data-> System.out.println(data));

  }

  public int propogateException(Throwable throwable)
  {
    System.out.println("Got error "+throwable);
    System.out.println("Unable to resolve");

    throw new RuntimeException("Something really went wrong");
  }


  @Test
  public void test10()
  {
    createWithException()
        .thenApply(data->data+1)
        .exceptionally(throwable -> propogateException(throwable))
        .thenApply(data->data+1)
        .thenAccept(data-> System.out.println(data))
        .exceptionally(throwable -> {
          System.out.println(throwable);
          return null;
        });

    //on error , it goes to the nearest  exception block
    //after resolving , it goes to nearest then block

  }

  @Test
  public void test11()
  {
    //completeExceptionally

    CompletableFuture<Integer> future = new CompletableFuture<>();

        future
         .thenApply(data->data+1)
        .exceptionally(throwable -> propogateException(throwable))
        .thenApply(data->data+1)
        .thenAccept(data-> System.out.println(data))
        .exceptionally(throwable -> {
          System.out.println(throwable);
          return null;
        });


        future.completeExceptionally(new RuntimeException("Manually passing a error, to test error channel"));
        assertTrue(future.isCompletedExceptionally());

  }


  public CompletableFuture<Integer> createNum(int number)
  {
    return CompletableFuture.supplyAsync(()->number);
  }

  @Test
  public void testThenCombine()
  {
     createNum(2)
         .thenCombine(createNum(3),(result1,result2)-> result1+result2)
         .thenAccept(data-> System.out.println(data));


  }

  public CompletableFuture<Integer>  inc(int number)
  {
    return CompletableFuture.supplyAsync(()->number+1);
  }

  @Test
  public void testThenCompose()
  {
    createNum(2)
        .thenApply(data-> inc(data) )   //this returns a completableFuture
        .thenAccept(data-> System.out.println(data));

    createNum(2)
        .thenCompose(data-> inc(data) )   //this returns a completableFuture
        .thenAccept(data-> System.out.println(data));

    }
}
