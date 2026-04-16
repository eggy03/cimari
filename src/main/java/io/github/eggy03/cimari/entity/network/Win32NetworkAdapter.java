/*
 * SPDX-License-Identifier: MIT
 * SPDX-FileCopyrightText: 2025 The ferrumx-windows contributors
 * SPDX-FileCopyrightText: 2026 Cimari contributors
 */
package io.github.eggy03.cimari.entity.network;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.github.eggy03.cimari.annotation.WmiClass;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;
import tools.jackson.databind.ObjectMapper;

/**
 * Immutable legacy representation of a network adapter on a Windows system.
 * <p>
 * Fields correspond to properties retrieved from the {@code Win32_NetworkAdapter} WMI class.
 * </p>
 *
 * <h2>Usage example</h2>
 * <pre>{@code
 * Win32NetworkAdapter adapter = Win32NetworkAdapter.builder()
 *     .name("Ethernet 1")
 *     .macAddress("00:1A:2B:3C:4D:5E")
 *     .netEnabled(true)
 *     .build();
 *
 * // Create a modified copy
 * Win32NetworkAdapter updated = adapter.toBuilder()
 *     .netEnabled(false)
 *     .build();
 * }</pre>
 * <p>
 * {@link Win32NetworkAdapterConfiguration} contains related network configuration details.
 *
 *
 * @see <a href="https://learn.microsoft.com/en-us/windows/win32/cimwin32prov/win32-networkadapter">Win32_NetworkAdapter Documentation</a>
 * @since 1.0.0
 */
@WmiClass(className = "Win32_NetworkAdapter")
@NullMarked
public class Win32NetworkAdapter {

    /**
     * Unique identifier of the network adapter within the system.
     */
    @JsonProperty("DeviceID")
    @Nullable
    String deviceId;

    /**
     * Index number of the network adapter, stored in the system registry.
     */
    @JsonProperty("Index")
    @Nullable
    Integer index;

    /**
     * Friendly name of the network adapter.
     * <p>Example: {@code "Intel(R) Ethernet Connection"}</p>
     */
    @JsonProperty("Name")
    @Nullable
    String name;

    /**
     * Text description of the network adapter.
     */
    @JsonProperty("Description")
    @Nullable
    String description;

    /**
     * Windows Plug-and-Play device identifier for the network adapter.
     */
    @JsonProperty("PNPDeviceID")
    @Nullable
    String pnpDeviceId;

    /**
     * Media access control (MAC) address for this adapter.
     */
    @JsonProperty("MACAddress")
    @Nullable
    String macAddress;

    /**
     * Indicates whether the network adapter is installed in the system.
     */
    @JsonProperty("Installed")
    @Nullable
    Boolean installed;
    /**
     * Indicates whether the network adapter is currently enabled.
     */
    @JsonProperty("NetEnabled")
    @Nullable
    Boolean netEnabled;
    /**
     * Name of the network connection as displayed in the Network Connections Control Panel.
     */
    @JsonProperty("NetConnectionID")
    @Nullable
    String netConnectionId;
    /**
     * Indicates whether the adapter represents a physical or logical device.
     */
    @JsonProperty("PhysicalAdapter")
    @Nullable
    Boolean physicalAdapter;
    /**
     * Date and time the network adapter was last reset.
     */
    @JsonProperty("TimeOfLastReset")
    @Nullable
    String timeOfLastReset;

    public @Nullable Boolean isInstalled() {
        return installed;
    }

    public @Nullable Boolean isNetEnabled() {
        return netEnabled;
    }

    public @Nullable Boolean isPhysicalAdapter() {
        return physicalAdapter;
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