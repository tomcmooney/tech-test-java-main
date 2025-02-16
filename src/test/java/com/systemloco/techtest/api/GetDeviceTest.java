package com.systemloco.techtest.api;

import com.systemloco.techtest.JavaTechTest.Application;
import io.restassured.RestAssured;
import org.bson.Document;
import org.bson.types.ObjectId;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.AutoConfigureDataMongo;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.test.context.ActiveProfiles;

import java.sql.Date;
import java.time.Instant;
import java.util.List;
import java.util.Map;

import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.is;

@SpringBootTest(classes = Application.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureDataMongo
@ActiveProfiles("test")
@DisplayName("GET /api/devices/{deviceId}")
public class GetDeviceTest {
        @LocalServerPort
        int port;

        @BeforeEach
        public void beforeEach() {
                RestAssured.port = port;
        }

        @BeforeAll
        public static void beforeAll(
                        @Autowired final MongoTemplate template) {
                final var profileId1 = ObjectId.get();
                template.getCollection("profile").insertOne(new Document(Map.of(
                                "_id", profileId1,
                                "name", "test profile name 1",
                                "description", "test profile description 1")));
                template.getCollection("device").insertOne(new Document(Map.of(
                                "device", 72300000000000001L,
                                "model", "LTP_HGD4-1-0",
                                "name", "test device 1",
                                "deactivated", false,
                                "labels", List.of("label 1", "label 2"),
                                "firmware", "Z_1.2.3",
                                "profileId", profileId1,
                                "lastReported", Date.from(Instant.parse("2025-01-02T00:00:00Z")),
                                "location", Map.of(
                                                "lat", -54.0,
                                                "lon", -2.7,
                                                "cep", 200.0),
                                "sensors", Map.of(
                                                "temperature", 25.0,
                                                "humidity", 67.5,
                                                "orientation", Map.of(
                                                                "x", 1.0,
                                                                "y", 0.0,
                                                                "z", 0.0)))));

                final var profileId2 = ObjectId.get();
                template.getCollection("profile").insertOne(new Document(Map.of(
                                "_id", profileId2,
                                "name", "test profile name 2",
                                "description", "test profile description 2")));
                template.getCollection("device").insertOne(new Document(Map.of(
                                "device", 72300000000000002L,
                                "model", "LTP_HGD4-1-0",
                                "name", "test device 2",
                                "deactivated", true,
                                "labels", List.of("label 1", "label 2"),
                                "firmware", "Z_1.2.3",
                                "profileId", profileId2,
                                "lastReported", Date.from(Instant.parse("2025-01-02T00:00:00Z")),
                                "location", Map.of(
                                                "lat", -54.0,
                                                "lon", -2.7,
                                                "cep", 200.0),
                                "sensors", Map.of(
                                                "temperature", 25.0,
                                                "humidity", 67.5,
                                                "orientation", Map.of(
                                                                "x", 1.0,
                                                                "y", 0.0,
                                                                "z", 0.0)))));
        }

        @AfterAll
        public static void afterAll(
                        @Autowired final MongoTemplate template) {
                template.getCollection("profile").deleteMany(new Document());
                template.getCollection("device").deleteMany(new Document());
        }

        @Test
        void whenDeviceExists_returnsOk_withDeviceDetails() {
                RestAssured
                                .given()
                                .get("/api/device/72300000000000001")
                                .then()
                                .statusCode(200)
                                .body(
                                                "deviceId", is("72300000000000001"),
                                                "name", is("test device 1"),
                                                "firmware", is("Z_1.2.3"),
                                                "labels", containsInAnyOrder("label 1", "label 2"),
                                                "lastReported", is("2025-01-02T00:00:00.000+00:00"),
                                                "model", is(Map.of(
                                                                "name", "LTP_HGD4-1-0",
                                                                "family", "LTP_HGD4",
                                                                "version", 1,
                                                                "revision", 0)),
                                                "profile.name", is("test profile name 1"),
                                                "profile.description", is("test profile description 1"),
                                                "location", is(Map.of(
                                                                "lat", -54.0F,
                                                                "lon", -2.7F,
                                                                "cep", 200.0F)),
                                                "sensors", is(Map.of(
                                                                "temperature", 25.0F,
                                                                "humidity", 67.5F,
                                                                "orientation", Map.of(
                                                                                "x", 1.0F,
                                                                                "y", 0.0F,
                                                                                "z", 0.0F))));
        }

        @Test
        void whenDeviceIsDeactivated_returnsNotFound() {
                RestAssured
                                .given()
                                .get("/api/device/72300000000000002")
                                .then()
                                .statusCode(404);
        }

        @Test
        void whenDeviceDoesNotExist_returnsNotFound() {
                RestAssured
                                .given()
                                .get("/api/device/72300000000000003")
                                .then()
                                .statusCode(404);
        }
}
