/*
 * SPDX-License-Identifier: MIT
 * SPDX-FileCopyrightText: 2025 The ferrumx-windows contributors
 * SPDX-FileCopyrightText: 2026 Cimari contributors
 */
package io.github.eggy03.cimari.entity.display;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.github.eggy03.cimari.annotation.ShallowImmutable;
import io.github.eggy03.cimari.annotation.WmiClass;
import lombok.Builder;
import lombok.Value;
import org.jetbrains.annotations.Nullable;
import tools.jackson.databind.ObjectMapper;

/**
 * Immutable representation of a GPU device on a Windows system.
 * <p>
 * Fields correspond to properties retrieved from the {@code Win32_VideoController} WMI class.
 * </p>
 *
 * <p>
 * Hardware that is not compatible with Windows Display Driver Model (WDDM) returns inaccurate
 * property values for instances of this class.
 * </p>
 * <h2>Usage example</h2>
 * <pre>{@code
 * // Build a new instance
 * Win32VideoController gpu = Win32VideoController.builder()
 *     .deviceId("GPU1")
 *     .name("AMD Radeon HD 5450")
 *     .currentRefreshRate(60)
 *     .build();
 *
 * // Modify using toBuilder (copy-on-write)
 * Win32VideoController updated = gpu.toBuilder()
 *     .currentRefreshRate(144)
 *     .build();
 * }</pre>
 *
 *
 * @see <a href="https://learn.microsoft.com/en-us/windows/win32/cimwin32prov/win32-videocontroller">Win32_VideoController Documentation</a>
 * @since 1.0.0
 */

@Value
@Builder(toBuilder = true)
@ShallowImmutable
@WmiClass(className = "Win32_VideoController")
public class Win32VideoController {

    /**
     * Identifier (unique to the computer system) for this video controller.
     */
    @JsonProperty("DeviceID")
    @Nullable
    String deviceId;

    /**
     * Label by which the video controller is known.
     */
    @JsonProperty("Name")
    @Nullable
    String name;

    /**
     * Windows Plug and Play device identifier of the video controller.
     * <p>
     * Example: "*PNP030b"
     * </p>
     */
    @JsonProperty("PNPDeviceID")
    @Nullable
    String pnpDeviceId;

    /**
     * Number of bits used to display each pixel.
     */
    @JsonProperty("CurrentBitsPerPixel")
    @Nullable
    Integer currentBitsPerPixel;

    /**
     * Current number of horizontal pixels.
     */
    @JsonProperty("CurrentHorizontalResolution")
    @Nullable
    Integer currentHorizontalResolution;

    /**
     * Current number of vertical pixels.
     */
    @JsonProperty("CurrentVerticalResolution")
    @Nullable
    Integer currentVerticalResolution;

    /**
     * Frequency at which the video controller refreshes the image for the monitor.
     */
    @JsonProperty("CurrentRefreshRate")
    @Nullable
    Integer currentRefreshRate;

    /**
     * Maximum refresh rate of the video controller in hertz.
     */
    @JsonProperty("MaxRefreshRate")
    @Nullable
    Integer maxRefreshRate;

    /**
     * Minimum refresh rate of the video controller in hertz.
     */
    @JsonProperty("MinRefreshRate")
    @Nullable
    Integer minRefreshRate;

    /**
     * Name or identifier of the digital-to-analog converter (DAC) chip.
     * The character set of this property is alphanumeric.
     */
    @JsonProperty("AdapterDACType")
    @Nullable
    String adapterDacType;

    /**
     * Memory size of the video adapter in bytes.
     * <p>
     * Example: 64000
     * </p>
     */
    @JsonProperty("AdapterRAM")
    @Nullable
    Long adapterRam;

    /**
     * Last modification date and time of the currently installed video driver.
     */
    @JsonProperty("DriverDate")
    @Nullable
    String driverDate;

    /**
     * Version number of the video driver.
     */
    @JsonProperty("DriverVersion")
    @Nullable
    String driverVersion;

    /**
     * Free-form string describing the video processor.
     * <p>Example {@code AMD Radeon HD 5450}</p>
     */
    @JsonProperty("VideoProcessor")
    @Nullable
    String videoProcessor;

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