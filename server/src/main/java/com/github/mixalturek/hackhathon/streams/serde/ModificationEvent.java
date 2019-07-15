package com.github.mixalturek.hackhathon.streams.serde;

/**
 * Parsed modification event and converted to the internal representation.
 */
public class ModificationEvent {
    private final Op op;
    private final String taskId;

    public ModificationEvent(Op op, String taskId) {
        this.op = op;
        this.taskId = taskId;
    }

    public Op getOp() {
        return op;
    }

    public String getTaskId() {
        return taskId;
    }

    @Override
    public String toString() {
        return "ModificationEvent{" +
                "op=" + op +
                ", taskId='" + taskId + '\'' +
                '}';
    }
}
