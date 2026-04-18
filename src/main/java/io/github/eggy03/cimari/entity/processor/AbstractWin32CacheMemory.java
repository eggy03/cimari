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
 * Immutable representation of a processor cache (e.g., L1, L2, L3) on a Windows system.
 * <p>
 * Fields correspond to properties retrieved from the {@code Win32_CacheMemory} WMI class.
 * </p>
 *
 * See {@link Win32Processor} for related CPU information.
 *
 * @see <a href="https://learn.microsoft.com/en-us/windows/win32/cimwin32prov/win32-cachememory">Win32_CacheMemory Documentation</a>
 * @since 1.0.0
 */
@WmiClass(className = "Win32_CacheMemory")
@NullMarked
@Value.Immutable
@ImmutableEntityStyle
@JsonSerialize(as = Win32CacheMemory.class)
@JsonDeserialize(as = Win32CacheMemory.class)
public abstract class AbstractWin32CacheMemory {

    /**
     * Unique identifier of the cache instance.
     * <p>
     * Example: {@code "Cache Memory 1"}
     * </p>
     */
    @JsonProperty("DeviceID")
    @Nullable
    public abstract String deviceId();

    /**
     * Free-form description of the cache purpose or level designation.
     * <p>
     * Example: {@code "L2 Cache"}
     * </p>
     */
    @JsonProperty("Purpose")
    @Nullable
    public abstract String purpose();

    /**
     * Type of cache.
     * <p>
     * Possible values:
     * <ul>
     *   <li>1 – Other</li>
     *   <li>2 – Unknown</li>
     *   <li>3 – Instruction</li>
     *   <li>4 – Data</li>
     *   <li>5 – Unified</li>
     * </ul>
     */
    @JsonProperty("CacheType")
    @Nullable
    public abstract Integer cacheType();

    /**
     * Cache hierarchy level.
     * <p>
     * Possible values:
     * <ul>
     *   <li>1 – Other</li>
     *   <li>2 – Unknown</li>
     *   <li>3 – Primary (L1)</li>
     *   <li>4 – Secondary (L2)</li>
     *   <li>5 – Tertiary (L3)</li>
     *   <li>6 – Not Applicable</li>
     * </ul>
     */
    @JsonProperty("Level")
    @Nullable
    public abstract Integer level();

    /**
     * Installed cache size in kilobytes.
     * <p>
     * Example: {@code 512} for 512 KB.
     * </p>
     */
    @JsonProperty("InstalledSize")
    @Nullable
    public abstract Long installedSize();

    /**
     * Cache associativity.
     * <p>
     * Possible values:
     * <ul>
     *   <li>1 – Other</li>
     *   <li>2 – Unknown</li>
     *   <li>3 – Direct Mapped</li>
     *   <li>4 – 2-way Set-Associative</li>
     *   <li>5 – 4-way Set-Associative</li>
     *   <li>6 – Fully Associative</li>
     *   <li>7 – 8-way Set-Associative</li>
     *   <li>8 – 16-way Set-Associative</li>
     * </ul>
     */
    @JsonProperty("Associativity")
    @Nullable
    public abstract Integer associativity();

    /**
     * Physical cache location relative to the processor.
     * <p>
     * Possible values:
     * <ul>
     *   <li>0 – Internal</li>
     *   <li>1 – External</li>
     *   <li>2 – Reserved</li>
     *   <li>3 – Unknown</li>
     * </ul>
     */
    @JsonProperty("Location")
    @Nullable
    public abstract Integer location();

    /**
     * Error-correction method used by the cache.
     * <p>
     * Possible values:
     * <ul>
     *   <li>0 – Reserved</li>
     *   <li>1 – Other</li>
     *   <li>2 – Unknown</li>
     *   <li>3 – None</li>
     *   <li>4 – Parity</li>
     *   <li>5 – Single-bit ECC</li>
     *   <li>6 – Multi-bit ECC</li>
     * </ul>
     */
    @JsonProperty("ErrorCorrectType")
    @Nullable
    public abstract Integer errorCorrectType();

    /**
     * Current availability and operational state.
     * <p>
     * Possible Values:
     * <ul>
     *   <li>1 – Other</li>
     *   <li>2 – Unknown</li>
     *   <li>3 – Running/Full Power</li>
     *   <li>4 – Warning</li>
     *   <li>5 – In Test</li>
     *   <li>6 – Not Applicable</li>
     *   <li>7 – Power Off</li>
     *   <li>8 – Offline</li>
     *   <li>9 – Off-duty</li>
     *   <li>10 – Degraded</li>
     *   <li>11 – Not Installed</li>
     *   <li>12 – Install Error</li>
     *   <li>13 – Power Save - Unknown</li>
     *   <li>14 – Power Save - Low Power Mode</li>
     *   <li>15 – Power Save - Standby</li>
     *   <li>16 – Power Cycle</li>
     *   <li>17 – Power Save - Warning</li>
     *   <li>18 – Paused</li>
     *   <li>19 – Not Ready</li>
     *   <li>20 – Not Configured</li>
     *   <li>21 – Quiesced</li>
     * </ul>
     */
    @JsonProperty("Availability")
    @Nullable
    public abstract Integer availability();

    /**
     * Current operational status of the cache device.
     * <p>Possible OPERATIONAL values:</p>
     * <ul>
     *   <li>"OK"</li>
     *   <li>"Degraded"</li>
     *   <li>"Pred Fail"</li>
     * </ul>
     * <p>Possible NON-OPERATIONAL values:</p>
     * <ul>
     *   <li>"Unknown"</li>
     *   <li>"Error"</li>
     *   <li>"Starting"</li>
     *   <li>"Stopping"</li>
     *   <li>"Service"</li>
     * </ul>
     * <p>Possible OTHER values:</p>
     * <ul>
     *   <li>"Stressed"</li>
     *   <li>"NonRecover"</li>
     *   <li>"No Contact"</li>
     *   <li>"Lost Comm"</li>
     * </ul>
     */
    @JsonProperty("Status")
    @Nullable
    public abstract String status();

    /**
     * Logical state of the device.
     * <p>
     * Possible values:
     * <ul>
     *   <li>1 – Other</li>
     *   <li>2 – Unknown</li>
     *   <li>3 – Enabled</li>
     *   <li>4 – Disabled</li>
     *   <li>5 – Not Applicable</li>
     * </ul>
     */
    @JsonProperty("StatusInfo")
    @Nullable
    public abstract Integer statusInfo();

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