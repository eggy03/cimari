/*
 * SPDX-License-Identifier: MIT
 * SPDX-FileCopyrightText: 2025 The ferrumx-windows contributors
 * SPDX-FileCopyrightText: 2026 Cimari contributors
 */
package io.github.eggy03.cimari.entity.mainboard;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.github.eggy03.cimari.annotation.WmiClass;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;
import tools.jackson.databind.ObjectMapper;

/**
 * Immutable representation of a motherboard device on a Windows system.
 * <p>
 * Fields correspond to properties retrieved from the {@code Win32_Baseboard} WMI class.
 * </p>
 *
 * <h2>Usage example</h2>
 * <pre>{@code
 * Win32Baseboard board = Win32Baseboard.builder()
 *     .manufacturer("ASUS")
 *     .serialNumber("ABC123456")
 *     .build();
 *
 * // Create a modified copy
 * Win32Baseboard updated = board.toBuilder()
 *     .serialNumber("XYZ987654")
 *     .build();
 * }</pre>
 * <p>
 * {@link Win32PortConnector} contains details about ports on this mainboard.
 *
 *
 * @see <a href="https://learn.microsoft.com/en-us/windows/win32/cimwin32prov/win32-baseboard">Win32_Baseboard Documentation</a>
 * @since 1.0.0
 */
@WmiClass(className = "Win32_Baseboard")
@NullMarked
public class Win32Baseboard {

    /**
     * Name of the organization responsible for producing the baseboard.
     */
    @JsonProperty("Manufacturer")
    @Nullable
    String manufacturer;

    /**
     * Name by which the baseboard is known.
     */
    @JsonProperty("Model")
    @Nullable
    String model;

    /**
     * Baseboard part number defined by the manufacturer.
     */
    @JsonProperty("Product")
    @Nullable
    String product;

    /**
     * Manufacturer-allocated number used to identify the baseboard.
     */
    @JsonProperty("SerialNumber")
    @Nullable
    String serialNumber;

    /**
     * Version of the baseboard.
     */
    @JsonProperty("Version")
    @Nullable
    String version;

    /**
     * Retrieves the entity in a JSON pretty-print formatted string
     *
     * @return the {@link String} value of the object in JSON pretty-print format
     */
    @Override
    public String toString() {
        return new ObjectMapper()
                .writerWithDefaultPrettyPrinter()
                .writeValueAsString(this);
    }
}