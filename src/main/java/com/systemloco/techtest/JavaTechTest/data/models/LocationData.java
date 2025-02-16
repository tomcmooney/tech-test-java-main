package com.systemloco.techtest.JavaTechTest.data.models;

import org.jetbrains.annotations.NotNull;

import org.springframework.data.mongodb.core.mapping.Field;

public record LocationData(
                @NotNull @Field("lat") Double lat,
                @NotNull @Field("lon") Double lon,
                @NotNull @Field("cep") Double cep) {
}
