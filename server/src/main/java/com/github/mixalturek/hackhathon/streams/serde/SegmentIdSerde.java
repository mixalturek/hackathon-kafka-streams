package com.github.mixalturek.hackhathon.streams.serde;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.kafka.common.serialization.Deserializer;
import org.apache.kafka.common.serialization.Serde;
import org.apache.kafka.common.serialization.Serializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

public class SegmentIdSerde implements Serde<SegmentId> {
    private static final Logger LOGGER = LoggerFactory.getLogger(SegmentIdSerde.class);
    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    @Override
    public Serializer<SegmentId> serializer() {
        return (topic, data) -> {
            try {
                return OBJECT_MAPPER.writeValueAsBytes(data);
            } catch (JsonProcessingException e) {
                LOGGER.error("Serialization of SegmentId failed", e);
                return null;
            }
        };
    }

    @Override
    public Deserializer<SegmentId> deserializer() {
        return (topic, data) -> {
            try {
                return OBJECT_MAPPER.readValue(data, SegmentId.class);
            } catch (IOException e) {
                LOGGER.error("Deserialization of SegmentId failed", e);
                return null;
            }
        };
    }
}
