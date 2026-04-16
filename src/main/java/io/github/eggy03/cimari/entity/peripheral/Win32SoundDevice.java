/*
 * SPDX-License-Identifier: MIT
 * SPDX-FileCopyrightText: 2025 The ferrumx-windows contributors
 * SPDX-FileCopyrightText: 2026 Cimari contributors
 */
package io.github.eggy03.cimari.entity.peripheral;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.github.eggy03.cimari.annotation.ShallowImmutable;
import io.github.eggy03.cimari.annotation.WmiClass;
import lombok.Builder;
import lombok.Value;

import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;
import tools.jackson.databind.ObjectMapper;

/**
 * Immutable representation of a Sound device on a Windows system.
 * <p>
 * Fields correspond to properties retrieved from the {@code Win32_SoundDevice} WMI class.
 * </p>
 *
 * <h2>Usage example</h2>
 * <pre>{@code
 * // Build a new instance
 * Win32SoundDevice device = Win32SoundDevice.builder()
 *     .deviceId("AUDIO\\0001")
 *     .name("Realtek High Definition Audio")
 *     .pnpDeviceId("HDAUDIO\\FUNC_01&VEN_10EC&DEV_0256&SUBSYS_10431A00&REV_1000")
 *     .manufacturer("Realtek Semiconductor Corp.")
 *     .status("OK")
 *     .statusInfo(3)
 *     .build();
 *
 * // Modify using toBuilder()
 * Win32SoundDevice updated = device.toBuilder()
 *     .status("Degraded")
 *     .statusInfo(4)
 *     .build();
 * }</pre>
 *
 *
 * @see <a href="https://learn.microsoft.com/en-us/windows/win32/cimwin32prov/win32-sounddevice">Win32_SoundDevice Documentation</a>
 * @since 1.0.0
 */
@Value
@Builder(toBuilder = true)
@ShallowImmutable
@WmiClass(className = "Win32_SoundDevice")
@NullMarked
public class Win32SoundDevice {

    /**
     * Unique identifier of the sound device.
     */
    @JsonProperty("DeviceID")
    @Nullable
    String deviceId;

    /**
     * Windows Plug and Play device identifier.
     */
    @JsonProperty("PNPDeviceID")
    @Nullable
    String pnpDeviceId;

    /**
     * Friendly name of the sound device as recognized by the system.
     */
    @JsonProperty("Name")
    @Nullable
    String name;

    /**
     * Manufacturer of the sound device.
     */
    @JsonProperty("Manufacturer")
    @Nullable
    String manufacturer;

    /**
     * Current operational status of the sound device.
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
    String status;

    /**
     * Numeric state of the logical device.
     * <ul>
     *   <li>1 - Other</li>
     *   <li>2 - Unknown</li>
     *   <li>3 - Enabled</li>
     *   <li>4 - Disabled</li>
     *   <li>5 - Not Applicable</li>
     * </ul>
     */
    @JsonProperty("StatusInfo")
    @Nullable
    Integer statusInfo;

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
