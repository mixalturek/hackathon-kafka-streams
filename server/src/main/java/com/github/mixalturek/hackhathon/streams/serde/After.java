package com.github.mixalturek.hackhathon.streams.serde;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
class After {
    private String taskId;

    public String getTaskId() {
        return taskId;
    }
}
