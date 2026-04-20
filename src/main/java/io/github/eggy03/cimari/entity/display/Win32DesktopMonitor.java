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
import tools.jackson.databind.annotation.JsonDeserialize;
import tools.jackson.databind.annotation.JsonSerialize;

/**
 * Immutable representation of a monitor device on a Windows system.
 * <p>
 * Fields correspond to properties retrieved from the {@code Win32_DesktopMonitor} WMI class.
 * </p>
 *
 * @see <a href="https://learn.microsoft.com/en-us/windows/win32/cimwin32prov/win32-desktopmonitor">Win32_DesktopMonitor Documentation</a>
 * @since 0.1.0
 */
@WmiClass(className = "Win32_DesktopMonitor")
@NullMarked
@Value.Immutable
@ImmutableEntityStyle
@JsonSerialize(as = ImmutableWin32DesktopMonitor.class)
@JsonDeserialize(as = ImmutableWin32DesktopMonitor.class)
public abstract class Win32DesktopMonitor {

    /**
     * Unique identifier of the desktop monitor on the system.
     * <p>
     * Example: {@code "DesktopMonitor1"}
     */
    @JsonProperty("DeviceID")
    @Nullable
    public abstract String deviceId();

    /**
     * Label by which the object is known.
     * <p>
     * Example: {@code "Default Monitor"}
     */
    @JsonProperty("Name")
    @Nullable
    public abstract String name();

    /**
     * Windows Plug and Play device identifier of the monitor.
     * <p>
     * Example: {@code "DISPLAY\\DELA0D1\\4&273ACF3E&0&UID1048858"}
     */
    @JsonProperty("PNPDeviceID")
    @Nullable
    public abstract String pnpDeviceId();

    /**
     * Current operational status of the monitor device.
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
     * Name of the manufacturer of the monitor.
     * <p>
     * Example: {@code "NEC"}
     */
    @JsonProperty("MonitorManufacturer")
    @Nullable
    public abstract String monitorManufacturer();

    /**
     * Type of monitor.
     * <p>
     * Example: {@code "NEC 5FGp"}
     */
    @JsonProperty("MonitorType")
    @Nullable
    public abstract String monitorType();

    /**
     * Resolution along the x-axis (horizontal direction) of the monitor.
     */
    @JsonProperty("PixelsPerXLogicalInch")
    @Nullable
    public abstract Integer pixelsPerXLogicalInch();

    /**
     * Resolution along the y-axis (vertical direction) of the monitor.
     */
    @JsonProperty("PixelsPerYLogicalInch")
    @Nullable
    public abstract Integer pixelsPerYLogicalInch();

    /**
     * Retrieves the entity in a JSON pretty-print formatted string
     *
     * @return the {@link String} value of the object in JSON pretty-print format
     */
    public String toJson() {
        return new ObjectMapper()
                .writerWithDefaultPrettyPrinter()
                .writeValueAsString(this);
    }
}