package com.oriseq.dtm.enums;

public enum InspectionScene {
    INSPECTION("inspection", "送检任务管理：仅返回非全部完成的样本"),
    ARCHIVE("archive", "归档任务管理：返回全部完成的样本");

    private final String code;
    private final String description;

    InspectionScene(String code, String description) {
        this.code = code;
        this.description = description;
    }

    public static InspectionScene fromCode(String code) {
        for (InspectionScene inspectionScene : InspectionScene.values()) {
            if (inspectionScene.getCode().equals(code)) {
                return inspectionScene;
            }
        }
        throw new IllegalArgumentException("Unknown scene code: " + code);
    }

    public String getCode() {
        return code;
    }

    public String getDescription() {
        return description;
    }
}
