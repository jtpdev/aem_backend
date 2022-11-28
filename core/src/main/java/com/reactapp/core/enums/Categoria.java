package com.reactapp.core.enums;

import java.util.Arrays;
import static org.apache.http.util.TextUtils.isBlank;

/**
 * Just to be simple
 * */
public enum Categoria {
    CATEGORIA_A,
    CATEGORIA_B;

    public static Categoria from(String value) {
        if (isBlank(value)) return null;
        return Arrays.stream(Categoria.values())
                .filter(c -> c.name().equals(value))
                .findFirst()
                .orElse(null);
    }
}
