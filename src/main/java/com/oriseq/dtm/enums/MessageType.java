package com.oriseq.dtm.enums;

/**
 * 消息类型枚举
 */
public enum MessageType {
    NOTIFICATION(1, "通知"),
    MESSAGE(2, "消息"),
    TODO(3, "待办");

    private final int value;
    private final String label;

    MessageType(int value, String label) {
        this.value = value;
        this.label = label;
    }

    public static MessageType valueOf(int value) {
        for (MessageType type : MessageType.values()) {
            if (type.getValue() == value) {
                return type;
            }
        }
        throw new IllegalArgumentException("Invalid message type value: " + value);
    }

    public int getValue() {
        return value;
    }

    public String getLabel() {
        return label;
    }
}