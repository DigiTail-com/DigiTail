package com.digitail.model;

public enum Status {
    APPROVED("Одобрено"),
    NOT_APPROVED("Не одобрено"),
    AWAITING("В ожидании");

    private String status;

    Status(String status) {
        this.status = status;
    }

    public String getCommand() {
        return status;
    }
}
