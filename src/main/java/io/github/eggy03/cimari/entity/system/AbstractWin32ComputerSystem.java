/*
 * SPDX-License-Identifier: MIT
 * SPDX-FileCopyrightText: 2025 The ferrumx-windows contributors
 * SPDX-FileCopyrightText: 2026 Cimari contributors
 */
package io.github.eggy03.cimari.entity.system;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.github.eggy03.cimari.annotation.ImmutableEntityStyle;
import io.github.eggy03.cimari.annotation.WmiClass;
import io.github.eggy03.cimari.entity.memory.Win32PhysicalMemory;
import org.immutables.value.Value;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;
import tools.jackson.databind.ObjectMapper;
import tools.jackson.databind.annotation.JsonDeserialize;
import tools.jackson.databind.annotation.JsonSerialize;

import java.math.BigInteger;
import java.util.List;

/**
 * Immutable representation of a computer system running Windows.
 * <p>
 * Fields correspond to properties retrieved from the {@code Win32_ComputerSystem} WMI class.
 * </p>
 *
 * @see <a href="https://learn.microsoft.com/en-us/windows/win32/cimwin32prov/win32-computersystem">Win32_ComputerSystem Documentation</a>
 * @since 1.0.0
 */
@WmiClass(className = "Win32_ComputerSystem")
@NullMarked
@Value.Immutable
@ImmutableEntityStyle
@JsonSerialize(as = Win32ComputerSystem.class)
@JsonDeserialize(as = Win32ComputerSystem.class)
public abstract class AbstractWin32ComputerSystem {

    // Password Status Properties

    /**
     * System hardware security settings for administrator password status.
     * Possible values:
     * <ul>
     *   <li>0 — Disabled</li>
     *   <li>1 — Enabled</li>
     *   <li>2 — Not Implemented</li>
     *   <li>3 — Unknown</li>
     * </ul>
     */
    @JsonProperty("AdminPasswordStatus")
    @Nullable
    public abstract Integer adminPasswordStatus();

    /**
     * System hardware security settings for keyboard password status.
     * <ul>
     *   <li>0 — Disabled</li>
     *   <li>1 — Enabled</li>
     *   <li>2 — Not Implemented</li>
     *   <li>3 — Unknown</li>
     * </ul>
     */
    @JsonProperty("KeyboardPasswordStatus")
    @Nullable
    public abstract Integer keyboardPasswordStatus();

    /**
     * System hardware security settings for power-on password status.
     * <ul>
     *   <li>0 — Disabled</li>
     *   <li>1 — Enabled</li>
     *   <li>2 — Not Implemented</li>
     *   <li>3 — Unknown</li>
     * </ul>
     */
    @JsonProperty("PowerOnPasswordStatus")
    @Nullable
    public abstract Integer powerOnPasswordStatus();


    // Boot

    /**
     * System start type. Possible values:
     * <ul>
     *   <li>"Normal boot" — Normal boot</li>
     *   <li>"Fail-safe boot" — Safe mode (no network)</li>
     *   <li>"Fail-safe with network boot" — Safe mode with networking</li>
     * </ul>
     */
    @JsonProperty("BootupState")
    @Nullable
    public abstract String bootupState();

    /**
     * Status and additional data fields that identify the boot status.
     * This value comes from the Boot Status member of the System Boot Information structure in the SMBIOS information.
     * <p>
     * Note: BootStatus is platform/firmware dependent and is not supported before Windows 10 and Windows Server 2016
     */
    @JsonProperty("BootStatus")
    @Nullable
    public abstract List<@Nullable Integer> bootStatus();

    /**
     * If true, the automatic reset boot option is enabled.
     */
    @JsonProperty("AutomaticResetBootOption")
    @Nullable
    public abstract Boolean automaticResetBootOption();
    /**
     * Current power state of the computer system. Possible values:
     * <ul>
     *   <li>0 — Unknown</li>
     *   <li>1 — Full Power</li>
     *   <li>2 — Power Save - Low Power Mode</li>
     *   <li>3 — Power Save - Standby</li>
     *   <li>4 — Power Save - Unknown</li>
     *   <li>5 — Power Cycle</li>
     *   <li>6 — Power Off</li>
     *   <li>7 — Power Save - Warning</li>
     *   <li>8 — Power Save - Hibernate</li>
     *   <li>9 — Power Save - Soft Off</li>
     * </ul>
     */
    @JsonProperty("PowerState")
    @Nullable
    public abstract Integer powerState();


    // Power
    /**
     * State of the power supply or supplies when last booted.
     * <p>Possible Values:</p>
     * <ul>
     *   <li>1 — Other</li>
     *   <li>2 — Unknown</li>
     *   <li>3 — Safe</li>
     *   <li>4 — Warning</li>
     *   <li>5 — Critical</li>
     *   <li>6 — Non-recoverable</li>
     * </ul>
     */
    @JsonProperty("PowerSupplyState")
    @Nullable
    public abstract Integer powerSupplyState();
    /**
     * Array of specific power-related capabilities.
     * <p>Possible Values: </p>
     * <ul>
     *   <li>0 — Unknown</li>
     *   <li>1 — Not Supported</li>
     *   <li>2 — Disabled</li>
     *   <li>3 — Enabled</li>
     *   <li>4 — Power Saving Modes Entered Automatically</li>
     *   <li>5 — Power State Settable</li>
     *   <li>6 — Power Cycling Supported</li>
     *   <li>7 — Timed Power On Supported</li>
     * </ul>
     */
    @JsonProperty("PowerManagementCapabilities")
    @Nullable
    public abstract List<@Nullable Integer> powerManagementCapabilities();
    /**
     * If true, the device can be power-managed
     */
    @JsonProperty("PowerManagementSupported")
    @Nullable
    public abstract Boolean powerManagementSupported();
    /**
     * If enabled, indicates the unitary computer system can be reset using power and reset buttons.
     * Typical values:
     * <ul>
     *   <li>1 — Other</li>
     *   <li>2 — Unknown</li>
     *   <li>3 — Disabled</li>
     *   <li>4 — Enabled</li>
     *   <li>5 — Not Implemented</li>
     * </ul>
     */
    @JsonProperty("ResetCapability")
    @Nullable
    public abstract Integer resetCapability();
    /**
     * Number of automatic resets since the last reset.
     * A value of -1 indicates the count is unknown.
     */
    @JsonProperty("ResetCount")
    @Nullable
    public abstract Integer resetCount();


    // Reset
    /**
     * Number of consecutive times a system reset is attempted.
     * A value of -1 indicates the limit is unknown.
     */
    @JsonProperty("ResetLimit")
    @Nullable
    public abstract Integer resetLimit();
    /**
     * Hardware security setting for the front-panel reset button.
     * Typical values:
     * <ul>
     *   <li>0 — Disabled</li>
     *   <li>1 — Enabled</li>
     *   <li>2 — Not Implemented</li>
     *   <li>3 — Unknown</li>
     * </ul>
     */
    @JsonProperty("FrontPanelResetStatus")
    @Nullable
    public abstract Integer frontPanelResetStatus();
    /**
     * If true, automatic reset capability is available.
     */
    @JsonProperty("AutomaticResetCapability")
    @Nullable
    public abstract Boolean automaticResetCapability();
    /**
     * Key of a CIM_System instance. Name of the computer system.
     */
    @JsonProperty("Name")
    @Nullable
    public abstract String name();
    /**
     * Short one-line description of the object.
     */
    @JsonProperty("Caption")
    @Nullable
    public abstract String caption();
    /**
     * Longer description of the object.
     */
    @JsonProperty("Description")
    @Nullable
    public abstract String description();


    // General identifying / owner info
    /**
     * Name of the computer manufacturer
     */
    @JsonProperty("Manufacturer")
    @Nullable
    public abstract String manufacturer();
    /**
     * Product name assigned by the manufacturer.
     */
    @JsonProperty("Model")
    @Nullable
    public abstract String model();
    /**
     * Name of the primary owner.
     */
    @JsonProperty("PrimaryOwnerName")
    @Nullable
    public abstract String primaryOwnerName();
    /**
     * Contact information for the primary owner.
     */
    @JsonProperty("PrimaryOwnerContact")
    @Nullable
    public abstract String primaryOwnerContact();
    /**
     * List of roles the system performs in the environment (editable).
     */
    @JsonProperty("Roles")
    @Nullable
    public abstract List<@Nullable String> roles();
    /**
     * Chassis or enclosure SKU number (from SMBIOS).
     */
    @JsonProperty("ChassisSKUNumber")
    @Nullable
    public abstract String chassisSKUNumber();
    /**
     * SKU/Product ID for the system configuration.
     */
    @JsonProperty("SystemSKUNumber")
    @Nullable
    public abstract String systemSKUNumber();
    /**
     * Family of the computer (SMBIOS Family field). May be unsupported on older OS versions.
     */
    @JsonProperty("SystemFamily")
    @Nullable
    public abstract String systemFamily();
    /**
     * System architecture description
     * <p>Possible Values: </p>
     * <ul>
     *   <li>"x64-based PC"</li>
     *   <li>"X86-based PC"</li>
     *   <li>"MIPS-based PC"</li>
     *   <li>"Alpha-based PC"</li>
     *   <li>"Power PC"</li>
     *   <li>"SH-x PC"</li>
     *   <li>"StrongARM PC"</li>
     *   <li>"64-bit Intel PC"</li>
     *   <li>"64-bit Alpha PC"</li>
     *   <li>"Unknown"</li>
     *   <li>"X86-Nec98 PC"</li>
     * </ul>
     */
    @JsonProperty("SystemType")
    @Nullable
    public abstract String systemType();
    /**
     * Currently logged-on user. In Terminal Services scenarios, this is the console user.
     */
    @JsonProperty("UserName")
    @Nullable
    public abstract String userName();
    /**
     * Name of the workgroup or domain (if PartOfDomain==false this is a workgroup name).
     */
    @JsonProperty("Workgroup")
    @Nullable
    public abstract String workgroup();
    /**
     * OEM-defined strings
     */
    @JsonProperty("OEMStringArray")
    @Nullable
    public abstract List<@Nullable String> oemStringArray();
    /**
     * Number of physical processors installed (enabled).
     */
    @JsonProperty("NumberOfProcessors")
    @Nullable
    public abstract Long numberOfProcessors();
    /**
     * Number of logical processors available (includes hyperthreading logical CPUs).
     */
    @JsonProperty("NumberOfLogicalProcessors")
    @Nullable
    public abstract Long numberOfLogicalProcessors();
    /**
     * Total size of physical memory in bytes.
     * Note: under some circumstances this may not be accurate (BIOS reservation). For accurate module-by-module capacity,
     * query the equivalent method(s) in {@link Win32PhysicalMemory}
     */
    @JsonProperty("TotalPhysicalMemory")
    @Nullable
    public abstract BigInteger totalPhysicalMemory();
    /**
     * If true, the system manages the page file automatically.
     */
    @JsonProperty("AutomaticManagedPagefile")
    @Nullable
    public abstract Boolean automaticManagedPagefile();
    /**
     * If true, an infrared (IR) port exists on the computer system.
     */
    @JsonProperty("InfraredSupported")
    @Nullable
    public abstract Boolean infraredSupported();
    /**
     * If true, network server mode is enabled (system behaves as a server).
     */
    @JsonProperty("NetworkServerModeEnabled")
    @Nullable
    public abstract Boolean networkServerModeEnabled();


    // Uncategorized
    /**
     * If true, a hypervisor is present on the system.
     * Note: not supported before Windows 8 / Windows Server 2012 on older OSes.
     */
    @JsonProperty("HypervisorPresent")
    @Nullable
    public abstract Boolean hypervisorPresent();
    /**
     * Thermal state of the system when last booted.
     * <p>Possible values:</p>
     * <ul>
     *   <li>1 — Other</li>
     *   <li>2 — Unknown</li>
     *   <li>3 — Safe</li>
     *   <li>4 — Warning</li>
     *   <li>5 — Critical</li>
     *   <li>6 — Non-recoverable</li>
     * </ul>
     */
    @JsonProperty("ThermalState")
    @Nullable
    public abstract Integer thermalState();
    /**
     * Amount of time the system is offset from UTC, in minutes.
     * Example: for UTC+5:30 (Asia/Kolkata) the value is 330.
     */
    @JsonProperty("CurrentTimeZone")
    @Nullable
    public abstract Integer currentTimeZone();
    /**
     * If True, the daylight savings mode is ON.
     */
    @JsonProperty("DaylightInEffect")
    @Nullable
    public abstract Boolean daylightInEffect();

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
