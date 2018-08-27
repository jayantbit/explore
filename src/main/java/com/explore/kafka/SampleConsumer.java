package com.explore.kafka;

import java.util.*;
import java.util.function.Supplier;

import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.ConsumerRecord;

public class SampleConsumer {

        public static void main(String[] args) throws Exception{

                String topicName = "test";
                String groupName = "test";

                Properties props = new Properties();
                props.put("bootstrap.servers", "localhost:9092");
                props.put("group.id", groupName);
                props.put("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
                props.put("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");


                KafkaConsumer<String, String> consumer = new KafkaConsumer<>(props);

                RebalanceListner rebalanceListner = new RebalanceListner(consumer);

                consumer.subscribe(Arrays.asList(topicName),rebalanceListner);



                while (true){
                        ConsumerRecords<String, String> records = consumer.poll(100);
                        for (ConsumerRecord<String, String> record : records)
                        {
                                System.out.println( "Received " + record.value() +" from partition "+record.partition());
                                rebalanceListner.addOffset(record.topic(), record.partition(),record.offset());
                        }
                }

        }
}
