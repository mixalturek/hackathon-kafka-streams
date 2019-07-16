package com.github.mixalturek.hackhathon.streams.serde;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

@SuppressWarnings("unused")
@JsonIgnoreProperties(ignoreUnknown = true)
public class After {
    private String taskId;
    private List<Integer> levels;
    private List<TUnits> tUnits;

    public String getTaskId() {
        return taskId;
    }

    public List<Integer> getLevels() {
        return levels;
    }

    public List<TUnits> gettUnits() {
        return tUnits;
    }

    @Override
    public String toString() {
        return "After{" +
                "taskId='" + taskId + '\'' +
                ", levels=" + levels +
                ", tUnits=" + tUnits +
                '}';
    }
}
