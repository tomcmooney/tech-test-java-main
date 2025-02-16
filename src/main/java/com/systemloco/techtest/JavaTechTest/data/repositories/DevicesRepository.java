package com.systemloco.techtest.JavaTechTest.data.repositories;

import java.util.Date;
import java.util.List;
import java.util.stream.Stream;

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
        public Stream<Result> findAllActiveDevices() {
                var criteria = Criteria
                                .where("deactivated").is(false);

                var pipeline = Aggregation.newAggregation(
                                Aggregation.match(criteria),
                                Aggregation.limit(Long.MAX_VALUE),
                                Aggregation.lookup(
                                                "profile",
                                                "profileId",
                                                "_id",
                                                "profile"),
                                Aggregation.unwind("profile", true));

                return template.aggregate(pipeline, "device", Result.class)
                                .getMappedResults()
                                .stream()
                                .sorted((r1, r2) -> r2.lastReported().compareTo(r1.lastReported()));
        }

        @Nullable
        public Result findActiveDeviceById(@NotNull final String deviceId) {
                final var criteria = Criteria
                                .where("device").is(Long.valueOf(deviceId))
                                .and("deactivated").is(false);

                final var pipeline = Aggregation.newAggregation(
                                Aggregation.match(criteria),
                                Aggregation.limit(1),
                                Aggregation.lookup(
                                                "profile",
                                                "profileId",
                                                "_id",
                                                "profile"),
                                Aggregation.unwind("profile", true));

                return template.aggregate(pipeline, "device", Result.class)
                                .getUniqueMappedResult();
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
