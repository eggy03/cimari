/*
 * SPDX-License-Identifier: MIT
 * SPDX-FileCopyrightText: 2025 The ferrumx-windows contributors
 * SPDX-FileCopyrightText: 2026 Cimari contributors
 */
package io.github.eggy03.cimari.entity.memory;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.github.eggy03.cimari.annotation.ShallowImmutable;
import io.github.eggy03.cimari.annotation.WmiClass;
import lombok.Builder;
import lombok.Value;
import org.jetbrains.annotations.Nullable;
import tools.jackson.databind.ObjectMapper;

import java.math.BigInteger;

/**
 * Immutable representation of a RAM module on a Windows system.
 * <p>
 * Fields correspond to properties retrieved from the {@code Win32_PhysicalMemory} WMI class.
 * </p>
 *
 * <h2>Usage example</h2>
 * <pre>{@code
 * Win32PhysicalMemory ram = Win32PhysicalMemory.builder()
 *     .capacity(16L * 1024 * 1024 * 1024)
 *     .speed(3200)
 *     .build();
 *
 * // Create a modified copy
 * Win32PhysicalMemory upgraded = ram.toBuilder()
 *     .speed(3600)
 *     .build();
 * }</pre>
 *
 *
 * @see <a href="https://learn.microsoft.com/en-us/windows/win32/cimwin32prov/win32-physicalmemory">Win32_PhysicalMemory Documentation</a>
 * @since 1.0.0
 */

@Value
@Builder(toBuilder = true)
@ShallowImmutable
@WmiClass(className = "Win32_PhysicalMemory")
public class Win32PhysicalMemory {

    /**
     * Unique identifier for the physical memory device represented by an instance of this class.
     */
    @JsonProperty("Tag")
    @Nullable
    String tag;

    /**
     * Label for the Physical Memory.
     */
    @JsonProperty("Name")
    @Nullable
    String name;

    /**
     * Name of the organization responsible for producing the physical memory.
     */
    @JsonProperty("Manufacturer")
    @Nullable
    String manufacturer;

    /**
     * Model name for the physical element.
     */
    @JsonProperty("Model")
    @Nullable
    String model;

    /**
     * Additional data, beyond asset tag information, that can be used to identify a physical element.
     * For example, bar code data associated with an element that also has an asset tag.
     */
    @JsonProperty("OtherIdentifyingInfo")
    @Nullable
    String otherIdentifyingInfo;

    /**
     * Part number assigned by the organization responsible for producing or manufacturing the physical element.
     */
    @JsonProperty("PartNumber")
    @Nullable
    String partNumber;

    /**
     * Implementation form factor for the chip.
     * <ul>
     *     <li>0 - Unknown</li>
     *     <li>1 - Other</li>
     *     <li>2 - SIP</li>
     *     <li>3 - DIP</li>
     *     <li>4 - ZIP</li>
     *     <li>5 - SOJ</li>
     *     <li>6 - Proprietary</li>
     *     <li>7 - SIMM</li>
     *     <li>8 - DIMM</li>
     *     <li>9 - TSOP</li>
     *     <li>10 - PGA</li>
     *     <li>11 - RIMM</li>
     *     <li>12 - SODIMM</li>
     *     <li>13 - SRIMM</li>
     *     <li>14 - SMD</li>
     *     <li>15 - SSMP</li>
     *     <li>16 - QFP</li>
     *     <li>17 - TQFP</li>
     *     <li>18 - SOIC</li>
     *     <li>19 - LCC</li>
     *     <li>20 - PLCC</li>
     *     <li>21 - BGA</li>
     *     <li>22 - FPBGA</li>
     *     <li>23 - LGA</li>
     * </ul>
     */
    @JsonProperty("FormFactor")
    @Nullable
    Integer formFactor;

    /**
     * Physically labeled bank where the memory is located.
     */
    @JsonProperty("BankLabel")
    @Nullable
    String bankLabel;

    /**
     * Total capacity of the physical memory—in bytes.
     */
    @JsonProperty("Capacity")
    @Nullable
    BigInteger capacity;

    /**
     * Data width of the physical memory—in bits.
     * A data width of 0 (zero) and a total width of 8 (eight) indicates that the memory is used solely to provide error correction bits.
     */
    @JsonProperty("DataWidth")
    @Nullable
    Integer dataWidth;

    /**
     * Speed of the physical memory—in MHz.
     */
    @JsonProperty("Speed")
    @Nullable
    Long speed;

    /**
     * The configured clock speed of the memory device, in MHz, or 0, if the speed is unknown.
     */
    @JsonProperty("ConfiguredClockSpeed")
    @Nullable
    Long configuredClockSpeed;

    /**
     * Label of the socket or circuit board that holds the memory.
     */
    @JsonProperty("DeviceLocator")
    @Nullable
    String deviceLocator;

    /**
     * Manufacturer-allocated number to identify the physical element.
     */
    @JsonProperty("SerialNumber")
    @Nullable
    String serialNumber;

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
