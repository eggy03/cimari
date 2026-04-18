/*
 * SPDX-License-Identifier: MIT
 * SPDX-FileCopyrightText: 2025 The ferrumx-windows contributors
 * SPDX-FileCopyrightText: 2026 Cimari contributors
 */
package io.github.eggy03.cimari.entity.system;

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
 * Immutable representation of a Plug and Play (PnP) device on Windows systems.
 * <p>
 * Fields correspond to properties retrieved from the WMI {@code Win32_PnPEntity} class.
 * Instances of this class represent entries as they would appear in the Windows Device Manager.
 * </p>
 *
 * @see <a href="https://learn.microsoft.com/en-us/windows/win32/cimwin32prov/win32-pnpentity">Win32_PnPEntity Documentation</a>
 * @since 1.0.0
 */
@WmiClass(className = "Win32_PnPEntity")
@NullMarked
@Value.Immutable
@ImmutableEntityStyle
@JsonSerialize(as = Win32PnPEntity.class)
@JsonDeserialize(as = Win32PnPEntity.class)
public abstract class AbstractWin32PnPEntity {

    /**
     * Identifier of the Plug and Play device.
     */
    @JsonProperty("DeviceID")
    @Nullable
    public abstract String deviceId();

    /**
     * Windows Plug and Play device identifier of the logical device.
     */
    @JsonProperty("PNPDeviceID")
    @Nullable
    public abstract String pnpDeviceId();

    /**
     * A vendor-defined list of hardware identification strings used by Windows Setup
     * to match the device to an INF file.
     */
    @JsonProperty("HardwareID")
    @Nullable
    public abstract List<@Nullable String> hardwareId();

    /**
     * A vendor-defined list of compatible identification strings that Windows Setup
     * uses as fallback identifiers when no matching hardware ID is found.
     */
    @JsonProperty("CompatibleID")
    @Nullable
    public abstract List<@Nullable String> compatibleId();

    /**
     * The name by which the device is known.
     */
    @JsonProperty("Name")
    @Nullable
    public abstract String name();

    /**
     * A human-readable description of the device.
     */
    @JsonProperty("Description")
    @Nullable
    public abstract String description();

    /**
     * Name of the manufacturer of the Plug and Play device.
     */
    @JsonProperty("Manufacturer")
    @Nullable
    public abstract String manufacturer();

    /**
     * Indicates whether this Plug and Play device is currently present in the system.
     * Note: This property is not supported on Windows Server 2012 R2,
     * Windows 8.1, Windows Server 2012, Windows 8, Windows Server 2008 R2, Windows 7,
     * Windows Server 2008 and Windows Vista.
     */
    @JsonProperty("Present")
    @Nullable
    public abstract Boolean present();
    /**
     * Current operational status of the PnP Device.
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
