package com.github.mixalturek.hackhathon.streams.serde;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.kafka.common.serialization.Deserializer;
import org.apache.kafka.common.serialization.Serde;
import org.apache.kafka.common.serialization.Serializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

/**
 * Parser of JSONs that flow through Kafka in record values.
 */
public class ModificationSerde implements Serde<Modification> {
    private static final Logger LOGGER = LoggerFactory.getLogger(ModificationSerde.class);
    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    @Override
    public Serializer<Modification> serializer() {
        return (topic, data) -> {
            LOGGER.error("Modification event should be never serialized");
            throw new UnsupportedOperationException("Modification should be never serialized");
        };
    }

    @Override
    public Deserializer<Modification> deserializer() {
        return (topic, data) -> {
            if (data == null) {
                return null;
            }

            try {
                return OBJECT_MAPPER.readValue(data, Modification.class);
            } catch (IOException e) {
                LOGGER.warn("Deserialization of Modification failed", e);
                return null;
            }
        };
    }
}
