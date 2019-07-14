package com.github.mixalturek.streams.config;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.StreamsConfig;

import java.util.Properties;

public class StreamsServerConfig {
    public static StreamsServerConfig load() {
        // TODO: Read the configuration from a file.
        return new StreamsServerConfig();
    }

    private Properties getKafkaStreams() {
        Properties props = new Properties();
        props.put(StreamsConfig.APPLICATION_ID_CONFIG, "homework-v0.2");
        props.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        props.put(StreamsConfig.DEFAULT_KEY_SERDE_CLASS_CONFIG, Serdes.String().getClass());
        props.put(StreamsConfig.DEFAULT_VALUE_SERDE_CLASS_CONFIG, Serdes.String().getClass());
        props.put(StreamsConfig.NUM_STREAM_THREADS_CONFIG, 1);
        props.put(ConsumerConfig.MAX_POLL_INTERVAL_MS_CONFIG, 500);
        return props;
    }
}
