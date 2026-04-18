/*
 * SPDX-License-Identifier: MIT
 * SPDX-FileCopyrightText: 2025 The ferrumx-windows contributors
 * SPDX-FileCopyrightText: 2026 Cimari contributors
 */
package io.github.eggy03.cimari.entity.processor;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.github.eggy03.cimari.annotation.ImmutableEntityStyle;
import io.github.eggy03.cimari.annotation.WmiClass;
import org.immutables.value.Value;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;
import tools.jackson.databind.ObjectMapper;
import tools.jackson.databind.annotation.JsonDeserialize;
import tools.jackson.databind.annotation.JsonSerialize;

/**
 * Immutable representation of a CPU device on a Windows system.
 * <p>
 * Fields correspond to properties retrieved from the {@code Win32_Processor} WMI class.
 * </p>
 *
 * See {@link Win32CacheMemory} for related cache information.
 *
 * @see <a href="https://learn.microsoft.com/en-us/windows/win32/cimwin32prov/win32-processor">Win32_Processor Documentation</a>
 * @since 1.0.0
 */
@WmiClass(className = "Win32_Processor")
@NullMarked
@Value.Immutable
@ImmutableEntityStyle
@JsonSerialize(as = ImmutableWin32Processor.class)
@JsonDeserialize(as = ImmutableWin32Processor.class)
public abstract class Win32Processor {

    /**
     * Unique identifier of the processor on the system.
     */
    @JsonProperty("DeviceID")
    @Nullable
    public abstract String deviceId();

    /**
     * Processor name: Typically includes manufacturer, brand, and model information.
     */
    @JsonProperty("Name")
    @Nullable
    public abstract String name();

    /**
     * Number of physical cores on the processor.
     */
    @JsonProperty("NumberOfCores")
    @Nullable
    public abstract Integer numberOfCores();

    /**
     * Number of enabled processor cores.
     */
    @JsonProperty("NumberOfEnabledCore")
    @Nullable
    public abstract Integer numberOfEnabledCores();

    /**
     * Number of hardware threads across all cores.
     */
    @JsonProperty("ThreadCount")
    @Nullable
    public abstract Integer threadCount();

    /**
     * Number of logical processors on the system.
     */
    @JsonProperty("NumberOfLogicalProcessors")
    @Nullable
    public abstract Integer numberOfLogicalProcessors();

    /**
     * Name of the processor manufacturer.
     */
    @JsonProperty("Manufacturer")
    @Nullable
    public abstract String manufacturer();

    /**
     * Width of the processor address bus in bits.
     * For a 32-bit CPU the value is 32 and for a 64-bit CPU, the value is 64
     */
    @JsonProperty("AddressWidth")
    @Nullable
    public abstract Integer addressWidth();

    /**
     * Size of the Level 2 cache in kilobytes.
     */
    @JsonProperty("L2CacheSize")
    @Nullable
    public abstract Integer l2CacheSize();

    /**
     * Size of the Level 3 cache in kilobytes.
     */
    @JsonProperty("L3CacheSize")
    @Nullable
    public abstract Integer l3CacheSize();

    /**
     * Maximum speed of the processor in megahertz under normal operating conditions.
     */
    @JsonProperty("MaxClockSpeed")
    @Nullable
    public abstract Integer maxClockSpeed();

    /**
     * External clock frequency of the processor in megahertz.
     */
    @JsonProperty("ExtClock")
    @Nullable
    public abstract Integer extClock();

    /**
     * Type of socket or slot used by the processor.
     */
    @JsonProperty("SocketDesignation")
    @Nullable
    public abstract String socketDesignation();

    /**
     * Version of the processor as reported by the manufacturer.
     */
    @JsonProperty("Version")
    @Nullable
    public abstract String version();

    /**
     * Short textual description of the processor.
     */
    @JsonProperty("Caption")
    @Nullable
    public abstract String caption();

    /**
     * Processor family type. Indicates the manufacturer and generation of the processor.
     */
    @JsonProperty("Family")
    @Nullable
    public abstract Integer family();

    /**
     * Stepping information for the processor revision.
     */
    @JsonProperty("Stepping")
    @Nullable
    public abstract String stepping();

    /**
     * Indicates whether virtualization technology is enabled in firmware.
     */
    @JsonProperty("VirtualizationFirmwareEnabled")
    @Nullable
    public abstract Boolean virtualizationFirmwareEnabled();
    /**
     * Processor identifier string, which may include family, model, and stepping information.
     */
    @JsonProperty("ProcessorId")
    @Nullable
    public abstract String processorId();
    /**
     * Processor architecture used by the platform.
     * <p>Possible Values:</p>
     * <ul>
     *     <li>x86 (0)</li>
     *     <li>MIPS (1)</li>
     *     <li>Alpha (2)</li>
     *     <li>PowerPC (3)</li>
     *     <li>ARM (5)</li>
     *     <li>ia64 (6)</li>
     *     <li>x64 (9)</li>
     *     <li>ARM64 (12)</li>
     * </ul>
     */
    @JsonProperty("Architecture")
    @Nullable
    public abstract Integer architecture();

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