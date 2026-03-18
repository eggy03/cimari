/*
 * © 2024–2025 The ferrumx-windows contributors
 * Licensed under the MIT License.
 * See the LICENSE file in the project root for more information.
 */
package io.github.eggy03.ferrumx.windows.shell.script;

import io.github.eggy03.ferrumx.windows.entity.compounded.HardwareId;
import io.github.eggy03.ferrumx.windows.entity.compounded.MsftNetAdapterToIpAndDnsAndProfile;
import io.github.eggy03.ferrumx.windows.entity.compounded.Win32DiskDriveToPartitionAndLogicalDisk;
import io.github.eggy03.ferrumx.windows.entity.compounded.Win32DiskPartitionToLogicalDisk;
import io.github.eggy03.ferrumx.windows.entity.compounded.Win32NetworkAdapterToConfiguration;
import io.github.eggy03.ferrumx.windows.entity.compounded.Win32ProcessorToCacheMemory;
import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

/**
 * Enum representing the location for some predefined PowerShell scripts
 *
 * @author Sayan Bhattacharjee (Egg-03/Eggy)
 * @since 4.1.0
 */
@RequiredArgsConstructor
@Getter
public enum ScriptEnum {

    /**
     * Script that returns a JSON which can be deserialized into {@link MsftNetAdapterToIpAndDnsAndProfile}
     */
    MSFT_NET_ADAPTER_TO_IP_AND_DNS_AND_PROFILE("/MsftNetAdapterToIpAndDnsAndProfile.ps1"),

    /**
     * Script that returns a JSON which can be deserialized into {@link Win32NetworkAdapterToConfiguration}
     */
    WIN32_NETWORK_ADAPTER_TO_CONFIGURATION("/Win32NetworkAdapterToConfiguration.ps1"),

    /**
     * Script that returns a JSON which can be deserialized into {@link Win32DiskDriveToPartitionAndLogicalDisk}
     */
    WIN32_DISK_DRIVE_TO_PARTITION_AND_LOGICAL("/Win32DiskDriveToPartitionAndLogicalDisk.ps1"),

    /**
     * Script that returns a JSON which can be deserialized into {@link Win32DiskPartitionToLogicalDisk}
     */
    WIN32_DISK_PARTITION_TO_LOGICAL("/Win32DiskPartitionToLogicalDisk.ps1"),

    /**
     * Script that returns a JSON which can be deserialized into {@link Win32ProcessorToCacheMemory}
     */
    WIN32_PROCESSOR_TO_CACHE_MEMORY("/Win32ProcessorToCacheMemory.ps1"),

    /**
     * Script that returns a JSON which can be deserialized into {@link HardwareId}
     */
    HWID("/HardwareID.ps1");

    @NonNull
    private final String scriptPath;
}
