/*
 * SPDX-License-Identifier: MIT
 * SPDX-FileCopyrightText: 2025 The ferrumx-windows contributors
 * SPDX-FileCopyrightText: 2026 Cimari contributors
 */
package io.github.eggy03.cimari.entity.peripheral;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.github.eggy03.cimari.annotation.ShallowImmutable;
import io.github.eggy03.cimari.annotation.WmiClass;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.Value;

import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;
import tools.jackson.databind.ObjectMapper;

import java.util.List;

/**
 * Immutable representation of a Printing device on a Windows system.
 * <p>
 * Fields correspond to properties retrieved from the {@code Win32_Printer} WMI class.
 * </p>
 *
 * <h2>Usage example</h2>
 * <pre>{@code
 * // Build a new instance
 * Win32Printer printer = Win32Printer.builder()
 *     .deviceId("PR1")
 *     .name("Primary Printer")
 *     .isShared(true)
 *     .shareName("Shared Primary Printer")
 *     .build();
 *
 * // Modify using toBuilder()
 * Win32Printer updated = printer.toBuilder()
 *     .isShared(false)
 *     .shareName(null)
 *     .build();
 * }</pre>
 *
 *
 * @see <a href="https://learn.microsoft.com/en-us/windows/win32/cimwin32prov/win32-printer">Win32_Printer Documentation</a>
 * @since 1.0.0
 */
@Value
@Builder(toBuilder = true)
@ShallowImmutable
@WmiClass(className = "Win32_Printer")
@NullMarked
public class Win32Printer {

    /**
     * System-assigned unique identifier of the printer.
     */
    @JsonProperty("DeviceID")
    @Nullable
    String deviceId;

    /**
     * Name of the printer as recognized by the system.
     */
    @JsonProperty("Name")
    @Nullable
    String name;

    /**
     * Windows Plug and Play device identifier
     */
    @JsonProperty("PNPDeviceID")
    @Nullable
    String pnpDeviceId;

    /**
     * List of capability codes supported by the printer.
     * <ul>
     *   <li>1 - Other</li>
     *   <li>2 - Color printing</li>
     *   <li>3 - Duplex printing</li>
     *   <li>4 - Copies</li>
     *   <li>5 - Collation</li>
     *   <li>6 - Stapling</li>
     *   <li>7 - Transparency printing</li>
     *   <li>8 - Punch</li>
     *   <li>9 - Cover</li>
     *   <li>10 - Bind</li>
     *   <li>11 - Black-and-white printing</li>
     *   <li>12 - One-sided</li>
     *   <li>13 - Two-sided long edge</li>
     *   <li>14 - Two-sided short edge</li>
     *   <li>15 - Portrait</li>
     *   <li>16 - Landscape</li>
     *   <li>17 - Reverse Portrait</li>
     *   <li>18 - Reverse Landscape</li>
     *   <li>19 - Quality High</li>
     *   <li>20 - Quality Normal</li>
     *   <li>21 - Quality Low</li>
     * </ul>
     */
    @JsonProperty("Capabilities")
    @Nullable
    List<@Nullable Integer> capabilities;

    /**
     * Descriptive text corresponding to {@link #capabilities}.
     */
    @JsonProperty("CapabilityDescriptions")
    @Nullable
    List<@Nullable String> capabilityDescriptions;

    /**
     * Printer’s horizontal resolution in DPI (dots per inch).
     */
    @JsonProperty("HorizontalResolution")
    @Nullable
    Long horizontalResolution;

    /**
     * Printer’s vertical resolution in DPI (dots per inch).
     */
    @JsonProperty("VerticalResolution")
    @Nullable
    Long verticalResolution;

    /**
     * Numeric codes of paper sizes supported by the printer.
     * Refer to the documentation link attached at the class level for the exhaustive list of available sizes
     */
    @JsonProperty("PaperSizesSupported")
    @Nullable
    List<@Nullable Integer> paperSizesSupported;

    /**
     * Names of paper types or forms supported by the printer.
     */
    @JsonProperty("PrinterPaperNames")
    @Nullable
    List<@Nullable String> printerPaperNames;

    /**
     * Current operational state of the printer.
     * <ul>
     *   <li>1 - Other</li>
     *   <li>2 - Unknown</li>
     *   <li>3 - Idle</li>
     *   <li>4 - Printing</li>
     *   <li>5 - Warm-up</li>
     *   <li>6 - Stopped printing</li>
     *   <li>7 - Offline</li>
     * </ul>
     */
    @JsonProperty("PrinterStatus")
    @Nullable
    Integer printerStatus;

    /**
     * Data type of print jobs
     * Example: RAW or EMF
     */
    @JsonProperty("PrintJobDataType")
    @Nullable
    String printJobDataType;

    /**
     * Print processor used to process print jobs
     * Example: WinPrint
     */
    @JsonProperty("PrintProcessor")
    @Nullable
    String printProcessor;

    /**
     * Name of the printer driver installed.
     */
    @JsonProperty("DriverName")
    @Nullable
    String driverName;

    /**
     * Indicates whether the printer is shared on the network.
     */
    @Getter(AccessLevel.NONE)
    @JsonProperty("Shared")
    @Nullable
    Boolean shared;
    /**
     * Share name of the printer if it is shared.
     */
    @JsonProperty("ShareName")
    @Nullable
    String shareName;
    /**
     * Indicates whether spooling is enabled for the printer.
     */
    @Getter(AccessLevel.NONE)
    @JsonProperty("SpoolEnabled")
    @Nullable
    Boolean spoolEnabled;
    /**
     * Specifies whether the printer is hidden from standard user interfaces.
     */
    @Getter(AccessLevel.NONE)
    @JsonProperty("Hidden")
    @Nullable
    Boolean hidden;

    public @Nullable Boolean isShared() {
        return shared;
    }

    public @Nullable Boolean hasSpoolEnabled() {
        return spoolEnabled;
    }

    public @Nullable Boolean isHidden() {
        return hidden;
    }

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