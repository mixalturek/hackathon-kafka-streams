package com.github.mixalturek.hackhathon.streams.serde;

/**
 * Task ID + tUnit ID.
 */
@SuppressWarnings("unused")
public class SegmentId {
    private String taskId;
    private String tUnitId;

    public SegmentId() {

    }

    public SegmentId(String taskId, String tUnitId) {
        this.taskId = taskId;
        this.tUnitId = tUnitId;
    }

    public String getTaskId() {
        return taskId;
    }

    public void setTaskId(String taskId) {
        this.taskId = taskId;
    }

    public String gettUnitId() {
        return tUnitId;
    }

    public void settUnitId(String tUnitId) {
        this.tUnitId = tUnitId;
    }

    @Override
    public String toString() {
        return "SegmentId{" +
                "taskId='" + taskId + '\'' +
                ", tUnitId='" + tUnitId + '\'' +
                '}';
    }
}
