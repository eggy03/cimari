/*
 * SPDX-License-Identifier: MIT
 * SPDX-FileCopyrightText: 2026 The ferrumx-windows contributors
 * SPDX-FileCopyrightText: 2026 Cimari contributors
 */
package io.github.eggy03.cimari.shell.query;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

@Tag("query_integrity")
class Cimv2EnumTest {

    private String generateExpectedQuery(String wmiClassName, String wmiProperties) {
        final String prefix = "Get-CimInstance -ClassName ";
        final String middle = " | Select-Object -Property ";
        final String suffix = " | ConvertTo-Json";

        return prefix + wmiClassName + middle + wmiProperties + suffix;
    }

    @Test
    void validateWin32BatteryQuery() {
        String wmiClassName = "Win32_Battery";
        String wmiProperties =
                "BatteryStatus, Caption, Chemistry, Description, DesignCapacity, DesignVoltage," +
                        " DeviceID, EstimatedChargeRemaining, EstimatedRunTime, Name, PowerManagementCapabilities," +
                        " PowerManagementSupported, Status";

        assertThat(generateExpectedQuery(wmiClassName, wmiProperties))
                .isEqualTo(Cimv2.WIN32_BATTERY.getQuery());
    }

    @Test
    void validateWin32DesktopMonitorQuery() {
        String wmiClassName = "Win32_DesktopMonitor";
        String wmiProperties =
                "DeviceID, MonitorManufacturer, MonitorType, Name, PNPDeviceID, PixelsPerXLogicalInch," +
                        " PixelsPerYLogicalInch, Status";

        assertThat(generateExpectedQuery(wmiClassName, wmiProperties))
                .isEqualTo(Cimv2.WIN32_DESKTOP_MONITOR.getQuery());
    }

    @Test
    void validateWin32VideoControllerQuery() {
        String wmiClassName = "Win32_VideoController";
        String wmiProperties =
                "AdapterDACType, AdapterRAM, CurrentBitsPerPixel, CurrentHorizontalResolution," +
                        " CurrentRefreshRate, CurrentVerticalResolution, DeviceID, DriverDate, DriverVersion, MaxRefreshRate," +
                        " MinRefreshRate, Name, PNPDeviceID, VideoProcessor";

        assertThat(generateExpectedQuery(wmiClassName, wmiProperties))
                .isEqualTo(Cimv2.WIN32_VIDEO_CONTROLLER.getQuery());
    }

    @Test
    void validateWin32CacheMemoryQuery() {
        String wmiClassName = "Win32_CacheMemory";
        String wmiProperties =
                "Associativity, Availability, CacheType, DeviceID, ErrorCorrectType, InstalledSize," +
                        " Level, Location, Purpose, Status, StatusInfo";

        assertThat(generateExpectedQuery(wmiClassName, wmiProperties))
                .isEqualTo(Cimv2.WIN32_CACHE_MEMORY.getQuery());
    }

    @Test
    void validateWin32BiosQuery() {
        String wmiClassName = "Win32_BIOS";
        String wmiProperties =
                "Caption, CurrentLanguage, Manufacturer, Name, PrimaryBIOS, ReleaseDate," +
                        " SMBIOSBIOSVersion, SMBIOSPresent, Status, Version";

        assertThat(generateExpectedQuery(wmiClassName, wmiProperties))
                .isEqualTo(Cimv2.WIN32_BIOS.getQuery());
    }

    @Test
    void validateWin32BaseboardQuery() {
        String wmiClassName = "Win32_Baseboard";
        String wmiProperties = "Manufacturer, Model, Product, SerialNumber, Version";

        assertThat(generateExpectedQuery(wmiClassName, wmiProperties))
                .isEqualTo(Cimv2.WIN32_BASEBOARD.getQuery());
    }

    @Test
    void validateWin32PortConnectorQuery() {
        String wmiClassName = "Win32_PortConnector";
        String wmiProperties = "ConnectorType, ExternalReferenceDesignator, InternalReferenceDesignator, PortType, Tag";

        assertThat(generateExpectedQuery(wmiClassName, wmiProperties))
                .isEqualTo(Cimv2.WIN32_PORT_CONNECTOR.getQuery());
    }

    @Test
    void validateWin32PhysicalMemoryQuery() {
        String wmiClassName = "Win32_PhysicalMemory";
        String wmiProperties =
                "BankLabel, Capacity, ConfiguredClockSpeed, DataWidth, DeviceLocator, FormFactor," +
                        " Manufacturer, Model, Name, OtherIdentifyingInfo, PartNumber, SerialNumber, Speed, Tag";

        assertThat(generateExpectedQuery(wmiClassName, wmiProperties))
                .isEqualTo(Cimv2.WIN32_PHYSICAL_MEMORY.getQuery());
    }

    @Test
    void validateWin32NetworkAdapterQuery() {
        String wmiClassName = "Win32_NetworkAdapter";
        String wmiProperties =
                "Description, DeviceID, Index, Installed, MACAddress, Name, NetConnectionID, NetEnabled," +
                        " PNPDeviceID, PhysicalAdapter, TimeOfLastReset";

        assertThat(generateExpectedQuery(wmiClassName, wmiProperties))
                .isEqualTo(Cimv2.WIN32_NETWORK_ADAPTER.getQuery());
    }

    @Test
    void validateWin32NetworkAdapterConfigurationQuery() {
        String wmiClassName = "Win32_NetworkAdapterConfiguration";
        String wmiProperties =
                "Caption, DHCPEnabled, DHCPLeaseExpires, DHCPLeaseObtained, DHCPServer, DNSHostName," +
                        " DNSServerSearchOrder, DefaultIPGateway, Description, IPAddress, IPEnabled, IPSubnet, Index," +
                        " SettingID";

        assertThat(generateExpectedQuery(wmiClassName, wmiProperties))
                .isEqualTo(Cimv2.WIN32_NETWORK_ADAPTER_CONFIGURATION.getQuery());
    }

    @Test
    void validateWin32OperatingSystemQuery() {
        String wmiClassName = "Win32_OperatingSystem";
        String wmiProperties =
                "BootDevice, BuildNumber, BuildType, CSName, Caption, Distributed, InstallDate," +
                        " LastBootUpTime, LocalDateTime, MUILanguages, Manufacturer, Name, NumberOfUsers, OSArchitecture," +
                        " PortableOperatingSystem, Primary, RegisteredUser, SerialNumber, ServicePackMajorVersion," +
                        " ServicePackMinorVersion, SystemDirectory, SystemDrive, Version, WindowsDirectory";

        assertThat(generateExpectedQuery(wmiClassName, wmiProperties))
                .isEqualTo(Cimv2.WIN32_OPERATING_SYSTEM.getQuery());
    }

    @Test
    void validateWin32DiskDriveQuery() {
        String wmiClassName = "Win32_DiskDrive";
        String wmiProperties =
                "Capabilities, CapabilityDescriptions, Caption, DeviceID, FirmwareRevision," +
                        " InterfaceType, Model, PNPDeviceID, Partitions, SerialNumber, Size, Status";

        assertThat(generateExpectedQuery(wmiClassName, wmiProperties))
                .isEqualTo(Cimv2.WIN32_DISK_DRIVE.getQuery());
    }

    @Test
    void validateWin32DiskPartitionQuery() {
        String wmiClassName = "Win32_DiskPartition";
        String wmiProperties =
                "BlockSize, BootPartition, Bootable, Description, DeviceID, DiskIndex, Name," +
                        " NumberOfBlocks, PrimaryPartition, Size, Type";

        assertThat(generateExpectedQuery(wmiClassName, wmiProperties))
                .isEqualTo(Cimv2.WIN32_DISK_PARTITION.getQuery());
    }

    @Test
    void validateWin32LogicalDiskQuery() {
        String wmiClassName = "Win32_LogicalDisk";
        String wmiProperties =
                "Compressed, Description, DeviceID, DriveType, FileSystem, FreeSpace, MediaType, Size," +
                        " SupportsDiskQuotas, SupportsFileBasedCompression, VolumeName, VolumeSerialNumber";

        assertThat(generateExpectedQuery(wmiClassName, wmiProperties))
                .isEqualTo(Cimv2.WIN32_LOGICAL_DISK.getQuery());
    }

    @Test
    void validateWin32ComputerSystemQuery() {
        String wmiClassName = "Win32_ComputerSystem";
        String wmiProperties =
                "AdminPasswordStatus, AutomaticManagedPagefile, AutomaticResetBootOption," +
                        " AutomaticResetCapability, BootStatus, BootupState, Caption, ChassisSKUNumber, CurrentTimeZone," +
                        " DaylightInEffect, Description, FrontPanelResetStatus, HypervisorPresent, InfraredSupported," +
                        " KeyboardPasswordStatus, Manufacturer, Model, Name, NetworkServerModeEnabled, NumberOfLogicalProcessors," +
                        " NumberOfProcessors, OEMStringArray, PowerManagementCapabilities, PowerManagementSupported," +
                        " PowerOnPasswordStatus, PowerState, PowerSupplyState, PrimaryOwnerContact, PrimaryOwnerName," +
                        " ResetCapability, ResetCount, ResetLimit, Roles, SystemFamily, SystemSKUNumber, SystemType, ThermalState," +
                        " TotalPhysicalMemory, UserName, Workgroup";

        assertThat(generateExpectedQuery(wmiClassName, wmiProperties))
                .isEqualTo(Cimv2.WIN32_COMPUTER_SYSTEM.getQuery());
    }

    @Test
    void validateWin32EnvironmentQuery() {
        String wmiClassName = "Win32_Environment";
        String wmiProperties = "Name, SystemVariable, VariableValue";

        assertThat(generateExpectedQuery(wmiClassName, wmiProperties))
                .isEqualTo(Cimv2.WIN32_ENVIRONMENT.getQuery());
    }

    @Test
    void validateWin32PrinterQuery() {
        String wmiClassName = "Win32_Printer";
        String wmiProperties = "Capabilities, CapabilityDescriptions, DeviceID, DriverName, Hidden," +
                " HorizontalResolution, Name, PNPDeviceID, PaperSizesSupported, PrintJobDataType, PrintProcessor," +
                " PrinterPaperNames, PrinterStatus, ShareName, Shared, SpoolEnabled, VerticalResolution";

        assertThat(generateExpectedQuery(wmiClassName, wmiProperties))
                .isEqualTo(Cimv2.WIN32_PRINTER.getQuery());
    }

    @Test
    void validateWin32UserAccountQuery() {
        String wmiClassName = "Win32_UserAccount";
        String wmiProperties =
                "AccountType, Caption, Description, Disabled, Domain, LocalAccount, Lockout, Name," +
                        " PasswordChangeable, PasswordExpires, PasswordRequired, SID, SIDType, Status";

        assertThat(generateExpectedQuery(wmiClassName, wmiProperties))
                .isEqualTo(Cimv2.WIN32_USER_ACCOUNT.getQuery());
    }

    @Test
    void validateWin32ProcessQuery() {
        String wmiClassName = "Win32_Process";
        String wmiProperties =
                "Caption, CreationDate, Description, ExecutablePath, ExecutionState, Handle, HandleCount," +
                        " KernelModeTime, Name, PageFileUsage, PeakPageFileUsage, PeakVirtualSize, PeakWorkingSetSize, Priority," +
                        " PrivatePageCount, ProcessId, SessionId, TerminationDate, ThreadCount, UserModeTime, VirtualSize," +
                        " WorkingSetSize";

        assertThat(generateExpectedQuery(wmiClassName, wmiProperties))
                .isEqualTo(Cimv2.WIN32_PROCESS.getQuery());
    }

    @Test
    void validateWin32SoundDeviceQuery() {
        String wmiClassName = "Win32_SoundDevice";
        String wmiProperties = "DeviceID, Manufacturer, Name, PNPDeviceID, Status, StatusInfo";

        assertThat(generateExpectedQuery(wmiClassName, wmiProperties))
                .isEqualTo(Cimv2.WIN32_SOUND_DEVICE.getQuery());
    }

    @Test
    void validateWin32PnPEntityQuery() {
        String wmiClassName = "Win32_PnPEntity";
        String wmiProperties =
                "CompatibleID, Description, DeviceID, HardwareID, Manufacturer, Name, PNPDeviceID," +
                        " Present, Status";

        assertThat(generateExpectedQuery(wmiClassName, wmiProperties))
                .isEqualTo(Cimv2.WIN32_PNP_ENTITY.getQuery());
    }

    // special cases
    @Test
    void validateWin32AssociatedProcessorMemoryQuery() {
        assertThat(Cimv2.WIN32_ASSOCIATED_PROCESSOR_MEMORY.getQuery())
                .isEqualTo("Get-CimInstance Win32_AssociatedProcessorMemory " +
                        "| ForEach-Object { [PSCustomObject]@{ CacheMemoryDeviceID = $_.Antecedent.DeviceID; ProcessorDeviceID = $_.Dependent.DeviceID } } " +
                        "| ConvertTo-Json");
    }

    @Test
    void validateWin32NetworkAdapterSettingQuery() {
        assertThat(Cimv2.WIN32_NETWORK_ADAPTER_SETTING.getQuery())
                .isEqualTo("Get-CimInstance Win32_NetworkAdapterSetting " +
                        "| ForEach-Object { [PSCustomObject]@{ NetworkAdapterDeviceID = $_.Element.DeviceID; NetworkAdapterConfigurationIndex = $_.Setting.Index } } " +
                        "| ConvertTo-Json");
    }

    @Test
    void validateWin32DiskDriveToDiskPartitionQuery() {
        assertThat(Cimv2.WIN32_DISK_DRIVE_TO_DISK_PARTITION.getQuery())
                .isEqualTo("Get-CimInstance Win32_DiskDriveToDiskPartition " +
                        "| ForEach-Object { [PSCustomObject]@{ DiskDriveDeviceID = $_.Antecedent.DeviceID; DiskPartitionDeviceID = $_.Dependent.DeviceID } } " +
                        "| ConvertTo-Json");
    }

    @Test
    void validateWin32LogicalDiskToPartitionQuery() {
        assertThat(Cimv2.WIN32_LOGICAL_DISK_TO_PARTITION.getQuery())
                .isEqualTo("Get-CimInstance Win32_LogicalDiskToPartition " +
                        "| ForEach-Object { [PSCustomObject]@{ DiskPartitionDeviceID = $_.Antecedent.DeviceID; LogicalDiskDeviceID = $_.Dependent.DeviceID } } " +
                        "| ConvertTo-Json");
    }
}
