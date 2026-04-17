/*
 * SPDX-License-Identifier: MIT
 * SPDX-FileCopyrightText: 2025 The ferrumx-windows contributors
 * SPDX-FileCopyrightText: 2026 Cimari contributors
 */
package io.github.eggy03.cimari.entity.compounded;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.github.eggy03.cimari.entity.network.Win32NetworkAdapter;
import io.github.eggy03.cimari.entity.network.Win32NetworkAdapterConfiguration;
import io.github.eggy03.cimari.entity.network.Win32NetworkAdapterSetting;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;
import tools.jackson.databind.ObjectMapper;

import java.util.List;

/**
 * Immutable representation of a {@link Win32NetworkAdapter} and its associated
 * {@code 1:N} relationship with {@link Win32NetworkAdapterConfiguration} in a Windows system.
 * <p>
 * Each instance represents a single network adapter identified by {@link #deviceId},
 * and maintains a one-to-many mapping with its corresponding network configuration objects.
 * </p>
 *
 * <p>
 * This class is purely a convenience class designed to eliminate the need for using
 * {@link Win32NetworkAdapterSetting} when fetching a relation between {@link Win32NetworkAdapter}
 * and {@link Win32NetworkAdapterConfiguration} as it directly stores all instances of configuration
 * for a particular adapter
 * </p>
 *
 * @see Win32NetworkAdapter
 * @see Win32NetworkAdapterConfiguration
 * @see Win32NetworkAdapterSetting
 * @since 1.0.0
 */
@NullMarked
public class Win32NetworkAdapterToConfiguration {

    /**
     * The unique identifier for the {@link Win32NetworkAdapter} instance.
     * <p>
     * Typically corresponds to the adapter’s {@code DeviceID} as defined by Windows Management Instrumentation (WMI).
     * </p>
     */
    @JsonProperty("DeviceID")
    @Nullable
    String deviceId;

    /**
     * The {@link Win32NetworkAdapter} entity associated with the {@link #deviceId}.
     * <p>
     * Represents the physical or virtual network adapter that owns one or more configuration objects.
     * </p>
     */
    @JsonProperty("Adapter")
    @Nullable
    Win32NetworkAdapter adapter;

    /**
     * A list of {@link Win32NetworkAdapterConfiguration} entities associated with the {@link #deviceId}.
     * <p>
     * Represents one or more configuration settings applied to the {@link #adapter},
     * </p>
     */
    @JsonProperty("Configurations")
    @Nullable
    List<@Nullable Win32NetworkAdapterConfiguration> configurationList;

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
