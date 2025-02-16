package com.systemloco.techtest.api;

import com.systemloco.techtest.JavaTechTest.Application;
import io.restassured.RestAssured;
import org.junit.jupiter.api.*;
import org.springframework.boot.test.autoconfigure.data.mongo.AutoConfigureDataMongo;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.context.ActiveProfiles;

import static org.hamcrest.Matchers.blankString;
import static org.hamcrest.Matchers.hasSize;

@SpringBootTest(classes = Application.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureDataMongo
@ActiveProfiles("test")
@DisplayName("GET /api/devices")
public class GetDevicesTest {
        @LocalServerPort
        int port;

        @BeforeEach
        public void beforeEach() {
                RestAssured.port = port;
        }

        @Test
        public void whenGetDevices_returnsOk_withEmptyDevicesList() {
                RestAssured
                                .given()
                                .get("/api/devices")
                                .then()
                                .statusCode(200)
                                .body("$.", hasSize(0));
        }
}
