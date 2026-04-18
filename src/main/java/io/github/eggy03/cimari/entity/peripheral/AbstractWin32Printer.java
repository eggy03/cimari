/*
 * SPDX-License-Identifier: MIT
 * SPDX-FileCopyrightText: 2025 The ferrumx-windows contributors
 * SPDX-FileCopyrightText: 2026 Cimari contributors
 */
package io.github.eggy03.cimari.entity.peripheral;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.github.eggy03.cimari.annotation.ImmutableEntityStyle;
import io.github.eggy03.cimari.annotation.WmiClass;
import org.immutables.value.Value;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;
import tools.jackson.databind.ObjectMapper;
import tools.jackson.databind.annotation.JsonDeserialize;
import tools.jackson.databind.annotation.JsonSerialize;

import java.util.List;

/**
 * Immutable representation of a Printing device on a Windows system.
 * <p>
 * Fields correspond to properties retrieved from the {@code Win32_Printer} WMI class.
 * </p>
 *
 * @see <a href="https://learn.microsoft.com/en-us/windows/win32/cimwin32prov/win32-printer">Win32_Printer Documentation</a>
 * @since 1.0.0
 */
@WmiClass(className = "Win32_Printer")
@NullMarked
@Value.Immutable
@ImmutableEntityStyle
@JsonSerialize(as = Win32Printer.class)
@JsonDeserialize(as = Win32Printer.class)
public abstract class AbstractWin32Printer {

    /**
     * System-assigned unique identifier of the printer.
     */
    @JsonProperty("DeviceID")
    @Nullable
    public abstract String deviceId();

    /**
     * Name of the printer as recognized by the system.
     */
    @JsonProperty("Name")
    @Nullable
    public abstract String name();

    /**
     * Windows Plug and Play device identifier
     */
    @JsonProperty("PNPDeviceID")
    @Nullable
    public abstract String pnpDeviceId();

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
    public abstract List<@Nullable Integer> capabilities();

    /**
     * Descriptive text corresponding to {@link #capabilities}.
     */
    @JsonProperty("CapabilityDescriptions")
    @Nullable
    public abstract List<@Nullable String> capabilityDescriptions();

    /**
     * Printer’s horizontal resolution in DPI (dots per inch).
     */
    @JsonProperty("HorizontalResolution")
    @Nullable
    public abstract Long horizontalResolution();

    /**
     * Printer’s vertical resolution in DPI (dots per inch).
     */
    @JsonProperty("VerticalResolution")
    @Nullable
    public abstract Long verticalResolution();

    /**
     * Numeric codes of paper sizes supported by the printer.
     * Refer to the documentation link attached at the class level for the exhaustive list of available sizes
     */
    @JsonProperty("PaperSizesSupported")
    @Nullable
    public abstract List<@Nullable Integer> paperSizesSupported();

    /**
     * Names of paper types or forms supported by the printer.
     */
    @JsonProperty("PrinterPaperNames")
    @Nullable
    public abstract List<@Nullable String> printerPaperNames();

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
    public abstract Integer printerStatus();

    /**
     * Data type of print jobs
     * Example: RAW or EMF
     */
    @JsonProperty("PrintJobDataType")
    @Nullable
    public abstract String printJobDataType();

    /**
     * Print processor used to process print jobs
     * Example: WinPrint
     */
    @JsonProperty("PrintProcessor")
    @Nullable
    public abstract String printProcessor();

    /**
     * Name of the printer driver installed.
     */
    @JsonProperty("DriverName")
    @Nullable
    public abstract String driverName();

    /**
     * Indicates whether the printer is shared on the network.
     */
    @JsonProperty("Shared")
    @Nullable
    public abstract Boolean shared();
    /**
     * Share name of the printer if it is shared.
     */
    @JsonProperty("ShareName")
    @Nullable
    public abstract String shareName();
    /**
     * Indicates whether spooling is enabled for the printer.
     */
    @JsonProperty("SpoolEnabled")
    @Nullable
    public abstract Boolean spoolEnabled();
    /**
     * Specifies whether the printer is hidden from standard user interfaces.
     */
    @JsonProperty("Hidden")
    @Nullable
    public abstract Boolean hidden();

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