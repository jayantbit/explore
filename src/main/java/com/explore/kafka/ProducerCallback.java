package com.explore.kafka;

import org.apache.kafka.clients.producer.Callback;
import org.apache.kafka.clients.producer.RecordMetadata;

public class ProducerCallback implements Callback {

  @Override
  public  void onCompletion(RecordMetadata recordMetadata, Exception e) {
    if (e != null)
    System.out.println("SampleProducer failed with an exception");

  }

}
