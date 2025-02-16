package com.systemloco.techtest.JavaTechTest.api.controllers;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import org.bson.types.ObjectId;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.stereotype.Component;

import com.systemloco.techtest.JavaTechTest.data.models.LocationData;
import com.systemloco.techtest.JavaTechTest.data.models.SensorData;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class DevicesRepository {
        @NotNull
        private final MongoTemplate template;

        @Nullable
        public Collection<Result> invoke() {
                Collection<Result> allDevices = new ArrayList<>();
                final var criteria = Criteria
                                .where("deactivated").is(false);
                final var pipeline = Aggregation.newAggregation(
                                Aggregation.match(criteria),
                                Aggregation.limit(1),
                                Aggregation.lookup(
                                                "profile",
                                                "profileId",
                                                "_id",
                                                "profile"),
                                Aggregation.unwind("profile", true));
                var device = template
                                .aggregate(pipeline, "device", Result.class)
                                .getUniqueMappedResult();
                allDevices.add(device);
                System.out.println("allDevices=" + allDevices);
                return allDevices;
        }

        public record Result(
                        @NotNull @Field("device") Long device,
                        @NotNull @Field("model") String model,
                        @NotNull @Field("firmware") String firmware,
                        @NotNull @Field("labels") List<String> labels,
                        @Nullable @Field("name") String name,
                        @Nullable @Field("lastReported") Date lastReported,
                        @Nullable @Field("location") LocationData location,
                        @Nullable @Field("sensors") SensorData sensors,

                        @Nullable @Field("profile._id") ObjectId profileId,
                        @Nullable @Field("profile.name") String profileName,
                        @Nullable @Field("profile.description") String profileDescription) {
        }

}
