package com.explore;

import io.reactivex.Observable;
import io.reactivex.schedulers.Schedulers;

import java.io.BufferedReader;
import java.io.InputStreamReader;

public class RxReader {


    /*
    observeOn  : run the next set of opeartors on the specified thread
    subscribeOn : run the function that generates the observables on the specified thread
     */
     private static Observable<String> lines(BufferedReader br)
    {
        return  Observable.<String>create(subscriber ->
        {

            String line;
            while((line=br.readLine())!=null)
            {
               subscriber.onNext(line);
               if(subscriber.isDisposed())
                   break;
            }
        }).subscribeOn(Schedulers.io());
    }

    static Observable<String> readLines()
    {
        return lines(new BufferedReader(new InputStreamReader(System.in)));
    }



}
