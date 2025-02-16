package com.systemloco.techtest.JavaTechTest.api.services;

import com.fasterxml.jackson.annotation.*;

import com.systemloco.techtest.JavaTechTest.api.serialisers.Truncated;
import com.systemloco.techtest.JavaTechTest.data.models.*;
import com.systemloco.techtest.JavaTechTest.data.repositories.DevicesRepository;

import java.util.*;
import java.util.regex.Pattern;

import lombok.*;

import org.bson.types.ObjectId;

import org.jetbrains.annotations.*;

import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DevicesService {

        @NotNull
        private final DevicesRepository repository;

        @Nullable
        public Collection<Response> findAllActiveDevices() {
                return repository.findAllActiveDevices()
                                .map(Response::fromData)
                                .toList();
        }

        @Nullable
        public Response findActiveDeviceById(@NotNull final String deviceId) {
                return Optional.ofNullable(repository.findActiveDeviceById(deviceId))
                                .map(Response::fromData)
                                .orElse(null);
        }

        @Builder(access = AccessLevel.PRIVATE)
        @JsonInclude(JsonInclude.Include.NON_NULL)
        public record Response(
                        @NotNull @JsonProperty("deviceId") String deviceId,
                        @NotNull @JsonProperty("name") String name,
                        @Nullable @JsonProperty("lastReported") Date lastReported,
                        @NotNull @JsonProperty("firmware") String firmware,
                        @NotNull @JsonProperty("labels") List<String> labels,
                        @NotNull @JsonProperty("model") Model model,
                        @Nullable @JsonProperty("profile") Profile profile,
                        @Nullable @JsonProperty("location") Location location,
                        @Nullable @JsonProperty("sensors") Sensors sensors) {
                @NotNull
                public static Response fromData(
                                @NotNull final DevicesRepository.Result data) {
                        final var deviceId = data.device().toString();
                        return builder()
                                        .deviceId(deviceId)
                                        .name(Optional
                                                        .ofNullable(data.name())
                                                        .orElse(deviceId))
                                        .lastReported(data.lastReported())
                                        .firmware(data.firmware())
                                        .labels(data.labels())
                                        .model(Model.fromModelName(
                                                        data.model()))
                                        .profile(Profile.fromData(
                                                        data.profileId(),
                                                        data.profileName(),
                                                        data.profileDescription()))
                                        .location(Location.fromData(
                                                        data.location()))
                                        .sensors(Sensors.fromData(
                                                        data.sensors()))
                                        .build();
                }

                @Builder(access = AccessLevel.PRIVATE)
                public record Model(
                                @NotNull @JsonProperty("name") String name,
                                @NotNull @JsonProperty("family") String family,
                                @NotNull @JsonProperty("version") Integer version,
                                @NotNull @JsonProperty("revision") Integer revision) {
                        @NotNull
                        public static Model fromModelName(
                                        @NotNull final String modelName) {
                                final var matcher = Pattern
                                                .compile("(.*)-([0-9]*)-([0-9]*)")
                                                .matcher(modelName);
                                final var hasMatch = matcher.find();
                                return builder()
                                                .name(modelName)
                                                .family(hasMatch ? matcher.group(1) : modelName)
                                                .version(hasMatch ? Integer.parseInt(matcher.group(2)) : 1)
                                                .revision(hasMatch ? Integer.parseInt(matcher.group(3)) : 0)
                                                .build();
                        }
                }

                @Builder(access = AccessLevel.PRIVATE)
                @JsonInclude(JsonInclude.Include.NON_NULL)
                public record Profile(
                                @NotNull @JsonProperty("id") String id,
                                @NotNull @JsonProperty("name") String name,
                                @NotNull @JsonProperty("description") String description) {
                        @Nullable
                        public static Profile fromData(
                                        @Nullable final ObjectId id,
                                        @Nullable final String name,
                                        @Nullable final String description) {
                                if (id == null || name == null) {
                                        return null;
                                }
                                return builder()
                                                .id(id.toString())
                                                .name(name)
                                                .description(Optional
                                                                .ofNullable(description)
                                                                .orElse(""))
                                                .build();
                        }
                }

                @Builder(access = AccessLevel.PRIVATE)
                public record Location(
                                @NotNull @JsonProperty("lat") @Truncated(decimalPlaces = 4) Double lat,
                                @NotNull @JsonProperty("lon") @Truncated(decimalPlaces = 4) Double lon,
                                @NotNull @JsonProperty("cep") @Truncated(decimalPlaces = 2) Double cep) {
                        @Nullable
                        public static Location fromData(
                                        @Nullable final LocationData data) {
                                if (data == null) {
                                        return null;
                                }
                                return builder()
                                                .lat(data.lat())
                                                .lon(data.lon())
                                                .cep(data.cep())
                                                .build();
                        }
                }

                @Builder(access = AccessLevel.PRIVATE)
                @JsonInclude(JsonInclude.Include.NON_NULL)
                public record Sensors(
                                @Nullable @JsonProperty("temperature") @Truncated Double temperature,
                                @Nullable @JsonProperty("humidity") @Truncated Double humidity,
                                @Nullable @JsonProperty("orientation") Vector orientation) {
                        @Nullable
                        public static Sensors fromData(
                                        @Nullable final SensorData data) {
                                if (data == null) {
                                        return null;
                                }
                                return builder()
                                                .temperature(data.temperature())
                                                .humidity(data.humidity())
                                                .orientation(Vector.fromData(
                                                                data.orientation()))
                                                .build();
                        }

                        @Builder(access = AccessLevel.PRIVATE)
                        public record Vector(
                                        @NotNull @JsonProperty("x") @Truncated Double x,
                                        @NotNull @JsonProperty("y") @Truncated Double y,
                                        @NotNull @JsonProperty("z") @Truncated Double z) {
                                @Nullable
                                public static Vector fromData(
                                                @Nullable final SensorData.Vector data) {
                                        if (data == null) {
                                                return null;
                                        }
                                        return builder()
                                                        .x(data.x())
                                                        .y(data.y())
                                                        .z(data.z())
                                                        .build();
                                }
                        }
                }
        }
}
