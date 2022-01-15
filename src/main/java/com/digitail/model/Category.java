package com.digitail.model;

public enum Category {
    PICTURE_COLOR("изображения");

    private String category;

    Category(String category) {
        this.category = category;
    }

    public String getCommand() {
        return category;
    }
}
