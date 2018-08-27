package com.explore.kafka;

import java.util.*;
import java.util.stream.IntStream;

import org.apache.kafka.clients.producer.*;

public class SampleProducer {

   public static void main(String[] args) {

     String topicName = "test";
      String key = "Key1";
      String value = "Value-1";

      Properties props = new Properties();
      props.put("bootstrap.servers", "localhost:9092");
      props.put("key.serializer","org.apache.kafka.common.serialization.StringSerializer");
      props.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");

      Producer<String, String> producer = new KafkaProducer <>(props);


    long startTime = System.currentTimeMillis();

    ProducerCallback callback = new ProducerCallback();

      IntStream.range(0,1000)
          .forEach( x->
          {
              String data= String.valueOf(x);

            System.out.println("Sending " + data);

              ProducerRecord <String, String > record = new ProducerRecord<>(topicName, String.valueOf(x%2), data);



            try {
              producer.send(record,callback);
              //Thread.sleep(500);
            } catch (Exception e) {
              e.printStackTrace();
            }
          });

      long endTime =System.currentTimeMillis();
     System.out.println("Time taken "+ (endTime -startTime));

      System.out.println("Producer call completed");

      producer.close();

   }

}


