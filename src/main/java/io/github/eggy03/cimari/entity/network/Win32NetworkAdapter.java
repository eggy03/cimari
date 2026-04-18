/*
 * SPDX-License-Identifier: MIT
 * SPDX-FileCopyrightText: 2025 The ferrumx-windows contributors
 * SPDX-FileCopyrightText: 2026 Cimari contributors
 */
package io.github.eggy03.cimari.entity.network;

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
 * Immutable legacy representation of a network adapter on a Windows system.
 * <p>
 * Fields correspond to properties retrieved from the {@code Win32_NetworkAdapter} WMI class.
 * </p>
 * <p>
 * {@link Win32NetworkAdapterConfiguration} contains related network configuration details.
 *
 * @see <a href="https://learn.microsoft.com/en-us/windows/win32/cimwin32prov/win32-networkadapter">Win32_NetworkAdapter Documentation</a>
 * @since 1.0.0
 */
@WmiClass(className = "Win32_NetworkAdapter")
@NullMarked
@Value.Immutable
@ImmutableEntityStyle
@JsonSerialize(as = ImmutableWin32NetworkAdapter.class)
@JsonDeserialize(as = ImmutableWin32NetworkAdapter.class)
public abstract class Win32NetworkAdapter {

    /**
     * Unique identifier of the network adapter within the system.
     */
    @JsonProperty("DeviceID")
    @Nullable
    public abstract String deviceId();

    /**
     * Index number of the network adapter, stored in the system registry.
     */
    @JsonProperty("Index")
    @Nullable
    public abstract Integer index();

    /**
     * Friendly name of the network adapter.
     * <p>Example: {@code "Intel(R) Ethernet Connection"}</p>
     */
    @JsonProperty("Name")
    @Nullable
    public abstract String name();

    /**
     * Text description of the network adapter.
     */
    @JsonProperty("Description")
    @Nullable
    public abstract String description();

    /**
     * Windows Plug-and-Play device identifier for the network adapter.
     */
    @JsonProperty("PNPDeviceID")
    @Nullable
    public abstract String pnpDeviceId();

    /**
     * Media access control (MAC) address for this adapter.
     */
    @JsonProperty("MACAddress")
    @Nullable
    public abstract String macAddress();

    /**
     * Indicates whether the network adapter is installed in the system.
     */
    @JsonProperty("Installed")
    @Nullable
    public abstract Boolean installed();

    /**
     * Indicates whether the network adapter is currently enabled.
     */
    @JsonProperty("NetEnabled")
    @Nullable
    public abstract Boolean netEnabled();

    /**
     * Name of the network connection as displayed in the Network Connections Control Panel.
     */
    @JsonProperty("NetConnectionID")
    @Nullable
    public abstract String netConnectionId();

    /**
     * Indicates whether the adapter represents a physical or logical device.
     */
    @JsonProperty("PhysicalAdapter")
    @Nullable
    public abstract Boolean physicalAdapter();

    /**
     * Date and time the network adapter was last reset.
     */
    @JsonProperty("TimeOfLastReset")
    @Nullable
    public abstract String timeOfLastReset();

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