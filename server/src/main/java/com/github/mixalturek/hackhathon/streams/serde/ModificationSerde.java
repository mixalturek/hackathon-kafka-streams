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
public class ModificationSerde implements Serde<ModificationEvent> {
    private static final Logger LOGGER = LoggerFactory.getLogger(ModificationSerde.class);
    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    @Override
    public Deserializer<ModificationEvent> deserializer() {
        return (topic, data) -> {
            if (data == null) {
                return null;
            }

            try {
                Modification modification = OBJECT_MAPPER.readValue(data, Modification.class);
                if (modification.getAfter() == null) {
                    return null;
                }

                After after = OBJECT_MAPPER.readValue(modification.getAfter(), After.class);

                return new ModificationEvent(modification.getOp(), after.getTaskId());
            } catch (IOException e) {
                LOGGER.debug("Parsing of modification event failed", e);
                return null;
            }
        };
    }

    @Override
    public Serializer<ModificationEvent> serializer() {
        return (topic, data) -> {
            LOGGER.error("Modification event should be never serialized");
            throw new UnsupportedOperationException("Modification event should be never serialized");
        };
    }
}
