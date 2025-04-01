package com.greenguard.green_guard_application.model.dto;

public class SingleFieldRequestDTO<T> {
    private T value;

    public T getValue() {
        return value;
    }

    public void setValue(T value) {
        this.value = value;
    }
}
