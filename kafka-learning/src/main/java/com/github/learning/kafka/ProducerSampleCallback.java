package com.github.learning.kafka;

import java.util.Properties;

import org.apache.kafka.clients.producer.Callback;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.apache.kafka.common.serialization.StringSerializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ProducerSampleCallback {
	public static void main(String[] args) {
		
		final Logger logger = LoggerFactory.getLogger(ProducerSampleCallback.class);
		// create Producer properties
		String bootstrapServers = "127.0.0.1:9092";
		Properties properties = new Properties();
		properties.setProperty(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
		properties.setProperty(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
		properties.setProperty(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
		
		
		//create the producer
		KafkaProducer<String, String> producer = new KafkaProducer<String, String>(properties);
		
		
		for (int i=20;i<30;i++) {
			
			//create a producer record
			ProducerRecord<String, String> record = new ProducerRecord<String, String>("first_topic", "Producer Message"+Integer.toString(i));
			
			// send data -asynchronous
			producer.send(record, new Callback() {
				
				public void onCompletion(RecordMetadata metadata, Exception exception) {
					//executes every time a record is successfully sent or exception is thrown
					if(exception == null) {
						//the record was successfully sent
						logger.info("Received new metadata. \n" + 
						"Topic:" + metadata.topic() +"\n" +
						"Partition:"+ metadata.partition() + "\n" +
						"Offset:" + metadata.offset() + "\n" +
						"Timestamp:" + metadata.timestamp());
					}else {
						logger.error("Error while producing", exception);
					}
				}
			});

			//flush data
			producer.flush();
		}
		//close
		producer.close();
		
	}

}