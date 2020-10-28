package org.apache.camel.kafkaconnector;

import java.util.Properties;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.StringSerializer;

public class SimpleProducer {

    public static void main(String[] args) {

        Properties props = new Properties();
        props.put("bootstrap.servers", "localhost:9092");
        props.put("key.serializer", StringSerializer.class.getName());
        props.put("value.serializer", StringSerializer.class.getName());

        KafkaProducer<String, String> prod = new KafkaProducer<String, String>(props);
       
        
        ProducerRecord<String, String> rec = new ProducerRecord<String, String>(args[0], args[1], args[2]);
        
        rec.headers().add("CamelHeader.detail", args[3].getBytes());

        prod.send(rec);

        prod.close();
    }
}
