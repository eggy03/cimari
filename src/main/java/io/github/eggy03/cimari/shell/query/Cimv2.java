/*
 * SPDX-License-Identifier: MIT
 * SPDX-FileCopyrightText: 2026 The ferrumx-windows contributors
 * SPDX-FileCopyrightText: 2026 Cimari contributors
 */
package io.github.eggy03.cimari.shell.query;

import com.profesorfalken.jpowershell.PowerShell;
import io.github.eggy03.cimari.entity.display.Win32DesktopMonitor;
import io.github.eggy03.cimari.entity.display.Win32VideoController;
import io.github.eggy03.cimari.entity.mainboard.Win32Baseboard;
import io.github.eggy03.cimari.entity.mainboard.Win32Bios;
import io.github.eggy03.cimari.entity.mainboard.Win32PortConnector;
import io.github.eggy03.cimari.entity.memory.Win32PhysicalMemory;
import io.github.eggy03.cimari.entity.network.Win32NetworkAdapter;
import io.github.eggy03.cimari.entity.network.Win32NetworkAdapterConfiguration;
import io.github.eggy03.cimari.entity.peripheral.Win32Battery;
import io.github.eggy03.cimari.entity.peripheral.Win32Printer;
import io.github.eggy03.cimari.entity.peripheral.Win32SoundDevice;
import io.github.eggy03.cimari.entity.processor.Win32CacheMemory;
import io.github.eggy03.cimari.entity.processor.Win32Processor;
import io.github.eggy03.cimari.entity.storage.Win32DiskDrive;
import io.github.eggy03.cimari.entity.storage.Win32DiskPartition;
import io.github.eggy03.cimari.entity.storage.Win32LogicalDisk;
import io.github.eggy03.cimari.entity.system.Win32ComputerSystem;
import io.github.eggy03.cimari.entity.system.Win32Environment;
import io.github.eggy03.cimari.entity.system.Win32OperatingSystem;
import io.github.eggy03.cimari.entity.system.Win32PnPEntity;
import io.github.eggy03.cimari.entity.system.Win32Process;
import io.github.eggy03.cimari.entity.user.Win32UserAccount;
import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;

/**
 * Enum representing the predefined WMI (CIM) queries for the classes available in the {@code root/cimv2} namespace.
 * <p>
 * Each constant holds a PowerShell query that queries a specific class in the namespace
 * and returns the result in JSON format. These queries are typically executed
 * using {@link PowerShell} and mapped to
 * corresponding Java objects.
 * </p>
 *
 *
 * @since 4.1.0
 */
@RequiredArgsConstructor
@Getter
public enum Cimv2 {

    /**
     * Query to fetch the properties of {@code Win32_Battery} class
     *
     * @since 4.1.0
     */
    WIN32_BATTERY(generateQuery(Win32Battery.class)),

    /**
     * Query to fetch the properties of {@code Win32_DesktopMonitor} class
     *
     * @since 4.1.0
     */
    WIN32_DESKTOP_MONITOR(generateQuery(Win32DesktopMonitor.class)),

    /**
     * Query to fetch the properties of {@code Win32_VideoController} class
     *
     * @since 4.1.0
     */
    WIN32_VIDEO_CONTROLLER(generateQuery(Win32VideoController.class)),

    /**
     * Query to fetch the properties of {@code Win32_Processor} class
     *
     * @since 4.1.0
     */
    WIN32_PROCESSOR(generateQuery(Win32Processor.class)),

    /**
     * Query to fetch the properties of {@code Win32_CacheMemory} class
     *
     * @since 4.1.0
     */
    WIN32_CACHE_MEMORY(generateQuery(Win32CacheMemory.class)),

    /**
     * Query to fetch the properties of {@code Win32_AssociatedProcessorMemory} class in a custom object
     *
     * @since 4.1.0
     */
    WIN32_ASSOCIATED_PROCESSOR_MEMORY(
            "Get-CimInstance Win32_AssociatedProcessorMemory " +
                    "| ForEach-Object { [PSCustomObject]@{ CacheMemoryDeviceID = $_.Antecedent.DeviceID; ProcessorDeviceID = $_.Dependent.DeviceID } } " +
                    "| ConvertTo-Json"
    ),

    /**
     * Query to fetch the properties of {@code Win32_BIOS} class
     *
     * @since 4.1.0
     */
    WIN32_BIOS(generateQuery(Win32Bios.class)),

    /**
     * Query to fetch the properties of {@code Win32_Baseboard} class
     *
     * @since 4.1.0
     */
    WIN32_BASEBOARD(generateQuery(Win32Baseboard.class)),

    /**
     * Query to fetch the properties of {@code Win32_PortConnector} class
     *
     * @since 4.1.0
     */
    WIN32_PORT_CONNECTOR(generateQuery(Win32PortConnector.class)),

    /**
     * Query to fetch the properties of {@code Win32_PhysicalMemory} class
     *
     * @since 4.1.0
     */
    WIN32_PHYSICAL_MEMORY(generateQuery(Win32PhysicalMemory.class)),

    /**
     * Query to fetch the properties of {@code Win32_NetworkAdapter} class
     *
     * @since 4.1.0
     */
    WIN32_NETWORK_ADAPTER(generateQuery(Win32NetworkAdapter.class)),

    /**
     * Query to fetch the properties of {@code Win32_NetworkAdapterConfiguration} class
     *
     * @since 4.1.0
     */
    WIN32_NETWORK_ADAPTER_CONFIGURATION(generateQuery(Win32NetworkAdapterConfiguration.class)),

    /**
     * Query to fetch the properties of {@code Win32_NetworkAdapterSetting} in a custom object
     *
     * @since 4.1.0
     */
    WIN32_NETWORK_ADAPTER_SETTING(
            "Get-CimInstance Win32_NetworkAdapterSetting " +
                    "| ForEach-Object { [PSCustomObject]@{ NetworkAdapterDeviceID = $_.Element.DeviceID; NetworkAdapterConfigurationIndex = $_.Setting.Index } } " +
                    "| ConvertTo-Json"
    ),

    /**
     * Query to fetch the properties of {@code Win32_OperatingSystem} class
     *
     * @since 4.1.0
     */
    WIN32_OPERATING_SYSTEM(generateQuery(Win32OperatingSystem.class)),

    /**
     * Query to fetch the properties of {@code Win32_DiskDrive} class
     *
     * @since 4.1.0
     */
    WIN32_DISK_DRIVE(generateQuery(Win32DiskDrive.class)),

    /**
     * Query to fetch the properties of {@code Win32_DiskPartition} class
     *
     * @since 4.1.0
     */
    WIN32_DISK_PARTITION(generateQuery(Win32DiskPartition.class)),

    /**
     * Query to fetch the properties of {@code Win32_LogicalDisk} class
     *
     * @since 4.1.0
     */
    WIN32_LOGICAL_DISK(generateQuery(Win32LogicalDisk.class)),

    /**
     * Query to fetch the properties of {@code Win32_DiskDriveToDiskPartition} class in a custom object
     *
     * @since 4.1.0
     */
    WIN32_DISK_DRIVE_TO_DISK_PARTITION(
            "Get-CimInstance Win32_DiskDriveToDiskPartition " +
                    "| ForEach-Object { [PSCustomObject]@{ DiskDriveDeviceID = $_.Antecedent.DeviceID; DiskPartitionDeviceID = $_.Dependent.DeviceID } } " +
                    "| ConvertTo-Json"
    ),

    /**
     * Query to fetch the properties of {@code Win32_LogicalDiskToPartition} class in a custom object
     *
     * @since 4.1.0
     */
    WIN32_LOGICAL_DISK_TO_PARTITION(
            "Get-CimInstance Win32_LogicalDiskToPartition " +
                    "| ForEach-Object { [PSCustomObject]@{ DiskPartitionDeviceID = $_.Antecedent.DeviceID; LogicalDiskDeviceID = $_.Dependent.DeviceID } } " +
                    "| ConvertTo-Json"
    ),

    /**
     * Query to fetch the properties of {@code Win32_ComputerSystem} class
     *
     * @since 4.1.0
     */
    WIN32_COMPUTER_SYSTEM(generateQuery(Win32ComputerSystem.class)),

    /**
     * Query to fetch the properties of {@code Win32_Environment} class
     *
     * @since 4.1.0
     */
    WIN32_ENVIRONMENT(generateQuery(Win32Environment.class)),

    /**
     * Query to fetch the properties of {@code Win32_Printer} class
     *
     * @since 4.1.0
     */
    WIN32_PRINTER(generateQuery(Win32Printer.class)),

    /**
     * Query to fetch the properties of {@code Win32_UserAccount} class
     *
     * @since 4.1.0
     */
    WIN32_USER_ACCOUNT(generateQuery(Win32UserAccount.class)),

    /**
     * Query to fetch some select properties of {@code Win32_Process} class
     *
     * @since 4.1.0
     */
    WIN32_PROCESS(generateQuery(Win32Process.class)),

    /**
     * Query to fetch the properties of {@code Win32_SoundDevice} class
     *
     * @since 4.1.0
     */
    WIN32_SOUND_DEVICE(generateQuery(Win32SoundDevice.class)),

    /**
     * Query to fetch the properties of {@code Win32_PnPEntity} class
     */
    WIN32_PNP_ENTITY(generateQuery(Win32PnPEntity.class));

    @NonNull
    private final String query;


    @NotNull
    private static <T> String generateQuery(@NonNull Class<T> wmiClass) {
        return "Get-CimInstance -ClassName " + QueryUtility.getClassNameFromWmiClassAnnotation(wmiClass) +
                " | Select-Object -Property " + QueryUtility.getPropertiesFromSerializedNameAnnotation(wmiClass) +
                " | ConvertTo-Json";

    }
}
