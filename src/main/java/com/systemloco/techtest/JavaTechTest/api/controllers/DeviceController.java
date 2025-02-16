package com.systemloco.techtest.JavaTechTest.api.controllers;

import com.systemloco.techtest.JavaTechTest.api.usecases.DeviceByIdUseCase;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/device/{id}")
public class DeviceController {
        @NotNull
        private final DeviceByIdUseCase byId;

        @GetMapping
        @ResponseBody
        DeviceByIdUseCase.Response byId(
                        @PathVariable("id") @NotNull final String deviceId) {
                final var device = byId.invoke(
                                deviceId);
                return Optional
                                .ofNullable(device)
                                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        }
}
