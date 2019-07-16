package com.github.mixalturek.hackhathon.streams.serde;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

/**
 * Raw modification event that is parsed from JSON in Kafka value.
 */
@SuppressWarnings("unused")
@JsonIgnoreProperties(ignoreUnknown = true)
public class Modification {
    private Op op;

    // It's JSON encoded in a string field, https://stackoverflow.com/a/53822537.
    @JsonDeserialize(using = AfterDeserializer.class)
    private After after;

    public Op getOp() {
        return op;
    }

    public After getAfter() {
        return after;
    }

    @Override
    public String toString() {
        return "Modification{" +
                "op=" + op +
                ", after=" + after +
                '}';
    }
}
