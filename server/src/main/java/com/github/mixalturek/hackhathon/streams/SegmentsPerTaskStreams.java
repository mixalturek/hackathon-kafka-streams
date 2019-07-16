package com.github.mixalturek.hackhathon.streams;

import com.github.mixalturek.hackhathon.streams.serde.*;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.KeyValue;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.Topology;
import org.apache.kafka.streams.kstream.Consumed;
import org.apache.kafka.streams.kstream.Grouped;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Properties;

/**
 * Kafka streams processing for computation of completed segments per task.
 */
public class SegmentsPerTaskStreams {
    private static final Logger LOGGER = LoggerFactory.getLogger(SegmentsPerTaskStreams.class);

    public KafkaStreams startStreamProcessing(Properties streamsProperties, String inputTopic, String outputTopic) {
        Topology topology = buildTopology(inputTopic, outputTopic);

        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("Kafka streams topology\n{}", topology.describe());
        }

        KafkaStreams streams = new KafkaStreams(topology, streamsProperties);
        streams.start();

        return streams;
    }

    private Topology buildTopology(String inputTopic, String outputTopic) {
        StreamsBuilder builder = new StreamsBuilder();

        // TODO: The type of key is not defined, but byte[] should be safe, it's internal representation in Kafka.
        builder.stream(inputTopic, Consumed.with(Serdes.ByteArray(), new ModificationSerde()))
                .peek((k, v) -> LOGGER.trace("Consumed and parsed: {}, {}", k, v))
                .filter((k, v) -> filterModification(v))
                .map((k, v) -> mapModification(v))
                .peek((k, v) -> LOGGER.trace("State of segment: {}, {}", k, v))
                .groupByKey(Grouped.with(new SegmentIdSerde(), Serdes.Integer()))

                // TODO: "With proper implementation, re-sending the sample data into the source topic should be an
                // idempotent operation with regards to the expected result."
                //
                // Query current state of a segment in KTable and transform "1 - completed" and "0 - not completed"
                // to "1 - change from not completed to completed", "0 - no change", "-1 - change from completed to
                // not completed". The rest will then remain untouched.

                // The latest update will survive
                .reduce((a, b) -> b)
                .toStream()
                .peek((k, v) -> LOGGER.trace("Latest state of segment: {}, {}", k, v))
                .map(this::extractTaskId)
                .groupByKey(Grouped.with(Serdes.String(), Serdes.Integer()))
                .reduce(Integer::sum)
                .toStream()
                .peek((k, v) -> LOGGER.trace("Confirmed segments per task: {}, {}", k, v))
                .to(outputTopic);

        return builder.build();
    }

    /**
     * Filter out input event that contain invalid data - possible issue on the producer side.
     */
    private boolean filterModification(Modification modification) {
        if (modification == null || modification.getOp() != Op.c) {
            return false;
        }

        boolean allowed = modification.getAfter() != null
                && !modification.getAfter().getLevels().isEmpty()
                && !modification.getAfter().gettUnits().isEmpty();

        if (!allowed) {
            LOGGER.warn("Mandatory fields are missing in Modification consumed from Kafka: {}", modification);
            // TODO: What to do with it?
        }

        return allowed;
    }

    /**
     * Map modification event to segment ID and confirmed (1) or not confirmed (0) stage.
     */
    private KeyValue<SegmentId, Integer> mapModification(Modification modification) {
        // Tightly coupled with filterModification()
        After after = modification.getAfter();
        // TODO: For the sake of this assignment, you can assume that each tgroup contains exactly one tunit.
        SegmentId key = new SegmentId(after.getTaskId(), after.gettUnits().get(0).gettUnitId());
        Integer state = after.getLevels().get(0) == after.gettUnits().get(0).getConfirmedLevel() ? 1 : 0;
        return new KeyValue<>(key, state);
    }

    /**
     * Extract task ID from segment ID.
     */
    private KeyValue<String, Integer> extractTaskId(SegmentId segmentId, Integer status) {
        return new KeyValue<>(segmentId.getTaskId(), status);
    }
}
