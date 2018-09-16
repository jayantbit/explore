package com.explore;

import org.junit.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Vector;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.Assert.assertTrue;

public class ConcurrencyTest {

  //to prove that AtomicInteger is thread safe can handle concurrent update operations

  @Test
  public void test1()  throws Exception {

    AtomicInteger ac= new AtomicInteger(0);

    ForkJoinPool fp = new ForkJoinPool(2);
    List<Integer> list=  new CopyOnWriteArrayList<>(); // a concurrent list



   //10 threads will concurrently incrment ac and and the answer to list
    for(int i=0;i<10;i++) {
      AtomicIntTestThread t1 = new AtomicIntTestThread(ac,list);
      fp.submit(t1);
      }

    fp.awaitTermination(1,TimeUnit.MICROSECONDS);


     Collections.sort(list);
     int pre=-1;

     for (Integer x : list) {
      System.out.println(x);

       {
         if (pre != -1) assertTrue(x - pre >= 1);
       }

      pre=x;
    }

    //it will print 1 to some number N , in order and with no repitation
    // 1 2 3 4 5 6....
    }



  @Test
  public void test2()  throws Exception {

    ForkJoinPool fp = new ForkJoinPool(10);
    List<Integer> list= new CopyOnWriteArrayList<>();

    Integer integer= new Integer(0);


    //10 threads will concurrently increment integer and and the answer to list
    for(int i=0;i<10;i++) {
      IntTestThread t1 = new IntTestThread(integer,list);
      fp.submit(t1);
    }

    fp.awaitTermination(1,TimeUnit.MICROSECONDS);

    Collections.sort(list);

    for (Integer x : list) {
      System.out.println(x);
    }

    //it will print numbers with repitation and not in order
    //1 1 2 2 2  3 3 ..
  }


  static  class AtomicIntTestThread implements  Runnable
  {

    AtomicInteger ac;
    List<Integer> list;


    AtomicIntTestThread(AtomicInteger ac, List<Integer> list) {
      this.ac = ac;
      this.list= list;
      }

    public void run()
    {
      while(true) {
        list.add(ac.getAndIncrement());
        }
    }

    }


  static  class IntTestThread implements  Runnable
  {

    Integer ac;
    List<Integer> list;


    IntTestThread(Integer ac, List<Integer> list) {
      this.ac = ac;
      this.list= list;
    }

    public void run()
    {
      while(true) {

        ac++;
        list.add(ac);
      }
    }

  }


}
