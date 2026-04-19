/*
 * SPDX-License-Identifier: MIT
 * SPDX-FileCopyrightText: 2026 The ferrumx-windows contributors
 * SPDX-FileCopyrightText: 2026 Cimari contributors
 */
package io.github.eggy03.cimari.shell.script;

import io.github.eggy03.cimari.entity.compounded.HardwareId;
import io.github.eggy03.cimari.entity.compounded.MsftNetAdapterToIpAndDnsAndProfile;
import io.github.eggy03.cimari.entity.compounded.Win32DiskDriveToPartitionAndLogicalDisk;
import io.github.eggy03.cimari.entity.compounded.Win32DiskPartitionToLogicalDisk;
import io.github.eggy03.cimari.entity.compounded.Win32NetworkAdapterToConfiguration;
import io.github.eggy03.cimari.entity.compounded.Win32ProcessorToCacheMemory;
import org.jspecify.annotations.NonNull;

import java.util.Objects;

import static io.github.eggy03.cimari.shell.script.ScriptUtility.loadScript;

/**
 * Enum representing the location for some predefined PowerShell scripts
 *
 * @since 1.0.0
 */
public enum ScriptEnum {

    /**
     * Script that returns a JSON which can be deserialized into {@link MsftNetAdapterToIpAndDnsAndProfile}
     *
     * @since 1.0.0
     */
    MSFT_NET_ADAPTER_TO_IP_AND_DNS_AND_PROFILE(loadScript("/MsftNetAdapterToIpAndDnsAndProfile.ps1")),

    /**
     * Script that returns a JSON which can be deserialized into {@link Win32NetworkAdapterToConfiguration}
     *
     * @since 1.0.0
     */
    WIN32_NETWORK_ADAPTER_TO_CONFIGURATION(loadScript("/Win32NetworkAdapterToConfiguration.ps1")),

    /**
     * Script that returns a JSON which can be deserialized into {@link Win32DiskDriveToPartitionAndLogicalDisk}
     *
     * @since 1.0.0
     */
    WIN32_DISK_DRIVE_TO_PARTITION_AND_LOGICAL_DISK(loadScript("/Win32DiskDriveToPartitionAndLogicalDisk.ps1")),

    /**
     * Script that returns a JSON which can be deserialized into {@link Win32DiskPartitionToLogicalDisk}
     *
     * @since 1.0.0
     */
    WIN32_DISK_PARTITION_TO_LOGICAL_DISK(loadScript("/Win32DiskPartitionToLogicalDisk.ps1")),

    /**
     * Script that returns a JSON which can be deserialized into {@link Win32ProcessorToCacheMemory}
     *
     * @since 1.0.0
     */
    WIN32_PROCESSOR_TO_CACHE_MEMORY(loadScript("/Win32ProcessorToCacheMemory.ps1")),

    /**
     * Script that returns a JSON which can be deserialized into {@link HardwareId}
     *
     * @since 1.0.0
     */
    HWID(loadScript("/HardwareID.ps1"));

    private final @NonNull String script;

    ScriptEnum(@NonNull String script) {
        this.script = Objects.requireNonNull(script, "script cannot be null");
    }

    public @NonNull String getScript() {
        return this.script;
    }
}
