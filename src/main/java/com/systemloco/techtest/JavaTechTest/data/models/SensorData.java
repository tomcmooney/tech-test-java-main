package com.systemloco.techtest.JavaTechTest.data.models;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.data.mongodb.core.mapping.Field;

public record SensorData(
        @Nullable @Field("temperature") Double temperature,
        @Nullable @Field("humidity") Double humidity,
        @Nullable @Field("orientation") Vector orientation
) {
    public record Vector(
            @NotNull @Field("x") Double x,
            @NotNull @Field("y") Double y,
            @NotNull @Field("z") Double z
    ) {}
}
