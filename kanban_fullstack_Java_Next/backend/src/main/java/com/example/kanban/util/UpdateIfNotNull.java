package com.example.kanban.util;

import java.util.function.Consumer;

public class UpdateIfNotNull {

    public static <T> void updateIfNotNull(T value, Consumer<T> setter) {
        if (value != null) {
            setter.accept(value);
        }
    }
}
