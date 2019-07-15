package com.github.mixalturek.hackhathon.streams;

import com.github.mixalturek.hackhathon.streams.serde.ModificationSerde;
import com.github.mixalturek.hackhathon.streams.serde.Op;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.Topology;
import org.apache.kafka.streams.kstream.Consumed;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Properties;

/**
 * Kafka streams processing.
 */
public class HackathonStreams {
    private static final Logger LOGGER = LoggerFactory.getLogger(HackathonStreams.class);

    public KafkaStreams startKafkaStreams(Properties streamsProperties, String inputTopic) {
        Topology topology = buildTopology(inputTopic);

        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("Kafka streams topology\n{}", topology.describe());
        }

        KafkaStreams streams = new KafkaStreams(topology, streamsProperties);
        streams.start();

        return streams;
    }

    private Topology buildTopology(String inputTopic) {
        StreamsBuilder builder = new StreamsBuilder();

        builder.stream(inputTopic, Consumed.with(new Serdes.ByteArraySerde(), new ModificationSerde()))
                .filter((k, v) -> v != null && v.getOp() == Op.c)
                .foreach((k, v) -> LOGGER.debug("Consumed: {}, {}", k, v));

        return builder.build();
    }
}
