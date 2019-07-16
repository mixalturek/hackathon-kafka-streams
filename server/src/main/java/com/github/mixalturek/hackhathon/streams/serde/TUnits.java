package com.github.mixalturek.hackhathon.streams.serde;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@SuppressWarnings("unused")
@JsonIgnoreProperties(ignoreUnknown = true)
public class TUnits {
    private String tUnitId;
    private int confirmedLevel;

    public String gettUnitId() {
        return tUnitId;
    }

    public int getConfirmedLevel() {
        return confirmedLevel;
    }

    @Override
    public String toString() {
        return "TUnits{" +
                "tUnitId='" + tUnitId + '\'' +
                ", confirmedLevel=" + confirmedLevel +
                '}';
    }
}
