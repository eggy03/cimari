/*
 * SPDX-License-Identifier: MIT
 * SPDX-FileCopyrightText: 2025 The ferrumx-windows contributors
 * SPDX-FileCopyrightText: 2026 Cimari contributors
 */
package io.github.eggy03.cimari.entity.display;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.github.eggy03.cimari.annotation.ImmutableEntityStyle;
import io.github.eggy03.cimari.annotation.WmiClass;
import org.immutables.value.Value;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;
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
 *
 * @see <a href="https://learn.microsoft.com/en-us/windows/win32/cimwin32prov/win32-videocontroller">Win32_VideoController Documentation</a>
 * @since 1.0.0
 */
@WmiClass(className = "Win32_VideoController")
@NullMarked
@Value.Immutable
@ImmutableEntityStyle
public abstract class AbstractWin32VideoController {

    /**
     * Identifier (unique to the computer system) for this video controller.
     */
    @JsonProperty("DeviceID")
    @Nullable
    public abstract String deviceId();

    /**
     * Label by which the video controller is known.
     */
    @JsonProperty("Name")
    @Nullable
    public abstract String name();

    /**
     * Windows Plug and Play device identifier of the video controller.
     * <p>
     * Example: "*PNP030b"
     * </p>
     */
    @JsonProperty("PNPDeviceID")
    @Nullable
    public abstract String pnpDeviceId();

    /**
     * Number of bits used to display each pixel.
     */
    @JsonProperty("CurrentBitsPerPixel")
    @Nullable
    public abstract Integer currentBitsPerPixel();

    /**
     * Current number of horizontal pixels.
     */
    @JsonProperty("CurrentHorizontalResolution")
    @Nullable
    public abstract Integer currentHorizontalResolution();

    /**
     * Current number of vertical pixels.
     */
    @JsonProperty("CurrentVerticalResolution")
    @Nullable
    public abstract Integer currentVerticalResolution();

    /**
     * Frequency at which the video controller refreshes the image for the monitor.
     */
    @JsonProperty("CurrentRefreshRate")
    @Nullable
    public abstract Integer currentRefreshRate();

    /**
     * Maximum refresh rate of the video controller in hertz.
     */
    @JsonProperty("MaxRefreshRate")
    @Nullable
    public abstract Integer maxRefreshRate();

    /**
     * Minimum refresh rate of the video controller in hertz.
     */
    @JsonProperty("MinRefreshRate")
    @Nullable
    public abstract Integer minRefreshRate();

    /**
     * Name or identifier of the digital-to-analog converter (DAC) chip.
     * The character set of this property is alphanumeric.
     */
    @JsonProperty("AdapterDACType")
    @Nullable
    public abstract String adapterDacType();

    /**
     * Memory size of the video adapter in bytes.
     * <p>
     * Example: 64000
     * </p>
     */
    @JsonProperty("AdapterRAM")
    @Nullable
    public abstract Long adapterRam();

    /**
     * Last modification date and time of the currently installed video driver.
     */
    @JsonProperty("DriverDate")
    @Nullable
    public abstract String driverDate();

    /**
     * Version number of the video driver.
     */
    @JsonProperty("DriverVersion")
    @Nullable
    public abstract String driverVersion();

    /**
     * Free-form string describing the video processor.
     * <p>Example {@code AMD Radeon HD 5450}</p>
     */
    @JsonProperty("VideoProcessor")
    @Nullable
    public abstract String videoProcessor();

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