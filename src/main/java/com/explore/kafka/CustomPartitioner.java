package com.explore.kafka;

import java.util.*;
import org.apache.kafka.clients.producer.*;
import org.apache.kafka.common.*;
import org.apache.kafka.common.utils.*;
import org.apache.kafka.common.record.*;

public class CustomPartitioner implements Partitioner {

    public void configure(Map<String, ?> configs) {

    }

     public int partition(String topic, Object key, byte[] keyBytes, Object value, byte[] valueBytes, Cluster cluster) {
           List<PartitionInfo> partitions = cluster.partitionsForTopic(topic);
           int numPartitions = partitions.size();
           return 0;
    }

    public void close() {}

}
