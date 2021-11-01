package com.modac.server.domain;

public enum TaskType {
    공부("공부"),
    운동("운동"),
    명상("명상"),
    독서("독서"),
    자격증("자격증"),
    자기계발("자기계발"),
    커리어("커리어"),
    시험("시험"),
    외국어("외국어"),
    인강("인강"),
    취미("취미"),
    기타("기타");

    private String text;

    TaskType(String text) {
        this.text = text;
    }

    public static TaskType fromString(String text) {
        for (TaskType type : TaskType.values()) {
            if (type.text.equalsIgnoreCase(text)) {
                return type;
            }
        }
        return null;
    }
}
