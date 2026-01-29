package com.trinity.TrinityProject.annotations;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Repeatable(value = CustomColorRepeatable.class)
public @interface CustomColor {
    int[] rgb() default {0, 255, 0};
}
