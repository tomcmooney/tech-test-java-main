package com.systemloco.techtest.api;

import com.systemloco.techtest.JavaTechTest.Application;
import io.restassured.RestAssured;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.AutoConfigureDataMongo;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.test.context.ActiveProfiles;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;
import java.sql.Date;
import java.time.Instant;
import java.util.List;
import java.util.Map;

import org.bson.Document;
import org.bson.types.ObjectId;

@SpringBootTest(classes = Application.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureDataMongo
@ActiveProfiles("test")
@DisplayName("GET /api/devices")
public class GetDevicesTest {
        @Autowired
        private MongoTemplate template;

        @LocalServerPort
        int port;

        @BeforeEach
        public void beforeEach() {
                RestAssured.port = port;
        }

        @AfterEach
        public void afterEach() {
                template.getCollection("profile").deleteMany(new Document());
                template.getCollection("device").deleteMany(new Document());
        }

        @Test
        void whenGetDevices_returnsOk_withEmptyDevicesList() {
                RestAssured
                                .given()
                                .get("/api/devices")
                                .then()
                                .statusCode(200)
                                .body(".", hasSize(0));
        }

        @Test
        void whenGetDevices_returnsOK_withOneDeviceInDevicesList() {
                // Given a devices collection with one device in it
                final var profileId = ObjectId.get();
                template.getCollection("profile").insertOne(new Document(Map.of(
                                "_id", profileId,
                                "name", "test profile name",
                                "description", "test profile description")));
                template.getCollection("device").insertOne(new Document(Map.of(
                                "device", 72300000000000001L,
                                "model", "LTP_HGD4-1-0",
                                "name", "test device 1",
                                "deactivated", false,
                                "labels", List.of("label 1", "label 2"),
                                "firmware", "Z_1.2.3",
                                "profileId", profileId,
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

                // When we get the devices list
                RestAssured
                                .given()
                                .get("/api/devices")
                                // The the devices list contains 1 device
                                .then()
                                .statusCode(200)
                                .body(".", hasSize(1))
                                .body("[0].deviceId", equalTo("72300000000000001"))
                                .body("[0].name", equalTo("test device 1"))
                                .body("[0].lastReported", equalTo("2025-01-02T00:00:00.000+00:00"))
                                .body("[0].firmware", equalTo("Z_1.2.3"))
                                .body("[0].labels", equalTo(List.of("label 1", "label 2")))
                                .body("[0].model.name", equalTo("LTP_HGD4-1-0"))
                                .body("[0].model.family", equalTo("LTP_HGD4"))
                                .body("[0].model.version", equalTo(1))
                                .body("[0].model.revision", equalTo(0))
                                .body("[0].profile.id", equalTo(profileId.toString()))
                                .body("[0].profile.name", equalTo("test profile name"))
                                .body("[0].profile.description", equalTo("test profile description"))
                                .body("[0].location.lat", equalTo(-54.0F))
                                .body("[0].location.lon", equalTo(-2.7F))
                                .body("[0].location.cep", equalTo(200.0F))
                                .body("[0].sensors.temperature", equalTo(25.0F))
                                .body("[0].sensors.humidity", equalTo(67.5F))
                                .body("[0].sensors.orientation.x", equalTo(1.0F))
                                .body("[0].sensors.orientation.y", equalTo(0.0F))
                                .body("[0].sensors.orientation.z", equalTo(0.0F));
        }

        @Test
        void whenGetDevices_returnsOK_withTwoDevicesInDevicesListSortedByLastReportedDescending() {
                // Given a devices collection with a device in it
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
                                "lastReported", Date.from(Instant.parse("2025-01-02T00:00:00.000+00:00")),
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
                // And another device with a more recent lastReported date
                final var profileId2 = ObjectId.get();
                template.getCollection("profile").insertOne(new Document(Map.of(
                                "_id", profileId2,
                                "name", "test profile name 2",
                                "description", "test profile description 2")));
                template.getCollection("device").insertOne(new Document(Map.of(
                                "device", 72300000000000002L,
                                "model", "LTP_HGD4-1-0",
                                "name", "test device 2",
                                "deactivated", false,
                                "labels", List.of("label 1", "label 2"),
                                "firmware", "Z_1.2.3",
                                "profileId", profileId2,
                                "lastReported", Date.from(Instant.parse("2025-01-03T00:00:00.000+00:00")),
                                "location", Map.of(
                                                "lat", -54.0,
                                                "lon", -2.7,
                                                "cep", 200.0),
                                "sensors", Map.of(
                                                "temperature", 25.0,
                                                "humidity", 67.5,
                                                "orientation", Map.of(
                                                                "x", 2.0,
                                                                "y", 0.0,
                                                                "z", 0.0)))));

                // When we get the devices list
                RestAssured
                                .given()
                                .get("/api/devices")
                                // The the devices list contains 2 devices sorted by lastReported descending
                                // i.e. most recent first
                                .then()
                                .statusCode(200)
                                .body(".", hasSize(2))
                                .body("[0].deviceId", equalTo("72300000000000002"))
                                .body("[0].name", equalTo("test device 2"))
                                .body("[0].lastReported", equalTo("2025-01-03T00:00:00.000+00:00"))
                                .body("[0].firmware", equalTo("Z_1.2.3"))
                                .body("[0].labels", equalTo(List.of("label 1", "label 2")))
                                .body("[0].model.name", equalTo("LTP_HGD4-1-0"))
                                .body("[0].model.family", equalTo("LTP_HGD4"))
                                .body("[0].model.version", equalTo(1))
                                .body("[0].model.revision", equalTo(0))
                                .body("[0].profile.id", equalTo(profileId2.toString()))
                                .body("[0].profile.name", equalTo("test profile name 2"))
                                .body("[0].profile.description", equalTo("test profile description 2"))
                                .body("[0].location.lat", equalTo(-54.0F))
                                .body("[0].location.lon", equalTo(-2.7F))
                                .body("[0].location.cep", equalTo(200.0F))
                                .body("[0].sensors.temperature", equalTo(25.0F))
                                .body("[0].sensors.humidity", equalTo(67.5F))
                                .body("[0].sensors.orientation.x", equalTo(2.0F))
                                .body("[0].sensors.orientation.y", equalTo(0.0F))
                                .body("[0].sensors.orientation.z", equalTo(0.0F))
                                .body("[1].deviceId", equalTo("72300000000000001"))
                                .body("[1].name", equalTo("test device 1"))
                                .body("[1].lastReported", equalTo("2025-01-02T00:00:00.000+00:00"))
                                .body("[1].firmware", equalTo("Z_1.2.3"))
                                .body("[1].labels", equalTo(List.of("label 1", "label 2")))
                                .body("[1].model.name", equalTo("LTP_HGD4-1-0"))
                                .body("[1].model.family", equalTo("LTP_HGD4"))
                                .body("[1].model.version", equalTo(1))
                                .body("[1].model.revision", equalTo(0))
                                .body("[1].profile.id", equalTo(profileId1.toString()))
                                .body("[1].profile.name", equalTo("test profile name 1"))
                                .body("[1].profile.description", equalTo("test profile description 1"))
                                .body("[1].location.lat", equalTo(-54.0F))
                                .body("[1].location.lon", equalTo(-2.7F))
                                .body("[1].location.cep", equalTo(200.0F))
                                .body("[1].sensors.temperature", equalTo(25.0F))
                                .body("[1].sensors.humidity", equalTo(67.5F))
                                .body("[1].sensors.orientation.x", equalTo(1.0F))
                                .body("[1].sensors.orientation.y", equalTo(0.0F))
                                .body("[1].sensors.orientation.z", equalTo(0.0F));

        }

        @Test
        void whenGetDevices_returnsOK_withOneActiveDeviceInTheDevicesListAndNoDeactivatedDevices() {
                // Given a devices collection with one active device in it
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
                                "lastReported", Date.from(Instant.parse("2025-01-02T00:00:00.000+00:00")),
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

                // And one deactivated device
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
                                "lastReported", Date.from(Instant.parse("2025-01-03T00:00:00.000+00:00")),
                                "location", Map.of(
                                                "lat", -54.0,
                                                "lon", -2.7,
                                                "cep", 200.0),
                                "sensors", Map.of(
                                                "temperature", 25.0,
                                                "humidity", 67.5,
                                                "orientation", Map.of(
                                                                "x", 2.0,
                                                                "y", 0.0,
                                                                "z", 0.0)))));

                // When we get the devices list
                RestAssured
                                .given()
                                .get("/api/devices")
                                // The the devices list contains 1 active device only
                                .then()
                                .statusCode(200)
                                .body(".", hasSize(1))
                                .body("[0].deviceId", equalTo("72300000000000001"))
                                .body("[0].name", equalTo("test device 1"))
                                .body("[0].lastReported", equalTo("2025-01-02T00:00:00.000+00:00"))
                                .body("[0].firmware", equalTo("Z_1.2.3"))
                                .body("[0].labels", equalTo(List.of("label 1", "label 2")))
                                .body("[0].model.name", equalTo("LTP_HGD4-1-0"))
                                .body("[0].model.family", equalTo("LTP_HGD4"))
                                .body("[0].model.version", equalTo(1))
                                .body("[0].model.revision", equalTo(0))
                                .body("[0].profile.id", equalTo(profileId1.toString()))
                                .body("[0].profile.name", equalTo("test profile name 1"))
                                .body("[0].profile.description", equalTo("test profile description 1"))
                                .body("[0].location.lat", equalTo(-54.0F))
                                .body("[0].location.lon", equalTo(-2.7F))
                                .body("[0].location.cep", equalTo(200.0F))
                                .body("[0].sensors.temperature", equalTo(25.0F))
                                .body("[0].sensors.humidity", equalTo(67.5F))
                                .body("[0].sensors.orientation.x", equalTo(1.0F))
                                .body("[0].sensors.orientation.y", equalTo(0.0F))
                                .body("[0].sensors.orientation.z", equalTo(0.0F));
        }

}
