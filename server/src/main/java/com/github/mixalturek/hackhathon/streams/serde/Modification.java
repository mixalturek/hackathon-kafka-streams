package com.github.mixalturek.hackhathon.streams.serde;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * Raw modification event that is parsed from JSON present in Kafka value.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
class Modification {
    private Op op;
    private String after;

    public Op getOp() {
        return op;
    }

    public String getAfter() {
        return after;
    }
}
