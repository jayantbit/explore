package com.explore;

import org.junit.Test;

import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.TimeUnit;
import java.util.stream.IntStream;

public class StreamConcurrencyTests {


  //demonstrates concurrency aspects of java8 streams

  void  sleep(long ms)
  {
    try {
      Thread.sleep(ms);
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
  }

  private int transform(int x)
  {
    sleep(1000);
    System.out.println("Transform: "+Thread.currentThread());
    return x+1;
  }


  private void exec(IntStream stream)
  {
    long startTime=System.currentTimeMillis();

    stream.
        map(e->transform(e))
        .forEach(System.out::println);


    System.out.println(String.format("Execution time  %d" ,System.currentTimeMillis()-startTime));
  }


  @Test
  public void test1()
  {
      exec(IntStream.range(0,5));
      exec(IntStream.range(0,10).parallel());
      //main also participates in doing the work along with fork join pool
  }

  @Test
  public void test2()
  {
    IntStream.range(0,3)
         .parallel()
        .map(e->transform(e))
        .sequential()
        .forEach(System.out::println);

    //last one wins.. entire stream is either sequential or parallel..map will not be called parallely
    //Rx java , provides segments...where u can run one segemnt sequentailly and other parallely
  }

  @Test
  public void test3()
  {

    IntStream.range(0,10)
        .parallel()
        .map(e->transform(e))
        .forEachOrdered(System.out::println);

    //some methods have a ordered counterpart..
    //they will not make the entire pipeline orderred..transform will still happen in parallel
    //only it will wait and print results in ordered format.
    //it imposes ordering  but not sequential execution.
    //this is only work if the stream gurantess ordering...in this case the stream is on list .so it works..
    //if it was on set...then wouldn have given ordering
  }

  @Test
  public void test4()
  {
    int sum =IntStream.range(0,10)
               .parallel()
               .reduce(0,(result,val)-> result+val);

    int sum2 =IntStream.range(0,10)
        .parallel()
        .reduce(1,(result,val)-> result+val);

    System.out.println(sum);
    System.out.println(sum2);

    //reduce with non identity seed value, will give wrong result if run in parallel
  }

  @Test
  public void test5()
  {
    System.out.println(Runtime.getRuntime().availableProcessors());   //shows 8 core
    System.out.println(ForkJoinPool.commonPool());  //parellelism 7



    IntStream.range(0,20)
        .parallel()
        .map(e->transform(e))
        .forEach(System.out::println);

    //ceil(20/8)=3
    //so will run in 3 batches
    //-Djava.util.concurrent.ForkJoinPool.common.parallelism=100 to ovveride paralism for fork join common pool
    //by increasing this value too high as compared to number of cores..ur processing may become slower bcoz of thrashing
  }


  public void process(IntStream stream) throws Exception
  {
    ForkJoinPool pool = new ForkJoinPool(20);
    pool.submit(()->stream.forEach(e-> {}));

    pool.shutdown();
    pool.awaitTermination(12,TimeUnit.SECONDS);
  }

  @Test
  public void test6() throws Exception
  {
      process(IntStream
          .range(0,10)
          .parallel()
          .map(e->transform(e))
      );
      //this stream was created in main...but will be executed by pool created in process function..
      //its decided by where terminal operation is executed.



  }
}
