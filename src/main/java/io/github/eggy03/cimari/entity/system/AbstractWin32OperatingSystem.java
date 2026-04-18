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

import java.util.List;

/**
 * Immutable representation of the Windows Operating System.
 * <p>
 * Fields correspond to properties retrieved from the {@code Win32_OperatingSystem} WMI class.
 * </p>
 *
 * @see <a href="https://learn.microsoft.com/en-us/windows/win32/cimwin32prov/win32-operatingsystem">Win32_OperatingSystem Documentation</a>
 * @since 1.0.0
 */
@WmiClass(className = "Win32_OperatingSystem")
@NullMarked
@Value.Immutable
@ImmutableEntityStyle
public abstract class AbstractWin32OperatingSystem {

    /**
     * Name of the operating system.
     */
    @JsonProperty("Name")
    @Nullable
    public abstract String name();

    /**
     * Short textual description (friendly name) of the operating system.
     */
    @JsonProperty("Caption")
    @Nullable
    public abstract String caption();

    /**
     * Date and time when the operating system was installed.
     */
    @JsonProperty("InstallDate")
    @Nullable
    public abstract String installDate();

    /**
     * Name of the computer system running the operating system.
     */
    @JsonProperty("CSName")
    @Nullable
    public abstract String csName();

    /**
     * Date and time when the computer was last booted.
     */
    @JsonProperty("LastBootUpTime")
    @Nullable
    public abstract String lastBootUpTime();

    /**
     * Current local date and time of the operating system.
     */
    @JsonProperty("LocalDateTime")
    @Nullable
    public abstract String localDateTime();

    /**
     * Indicates whether this operating system is part of a distributed system.
     */
    @JsonProperty("Distributed")
    @Nullable
    public abstract Boolean distributed();
    /**
     * Number of user sessions currently active.
     */
    @JsonProperty("NumberOfUsers")
    @Nullable
    public abstract Integer numberOfUsers();
    /**
     * Version number of the operating system (for example, "10.0.22621").
     */
    @JsonProperty("Version")
    @Nullable
    public abstract String version();
    /**
     * Path to the boot device that the operating system uses to start the computer.
     */
    @JsonProperty("BootDevice")
    @Nullable
    public abstract String bootDevice();
    /**
     * Internal build number of the operating system.
     */
    @JsonProperty("BuildNumber")
    @Nullable
    public abstract String buildNumber();
    /**
     * Type of build (e.g., "Multiprocessor Free" or "Checked").
     */
    @JsonProperty("BuildType")
    @Nullable
    public abstract String buildType();
    /**
     * Manufacturer of the operating system (typically "Microsoft Corporation").
     */
    @JsonProperty("Manufacturer")
    @Nullable
    public abstract String manufacturer();
    /**
     * Architecture of the operating system, such as "32-bit" or "64-bit".
     */
    @JsonProperty("OSArchitecture")
    @Nullable
    public abstract String osArchitecture();
    /**
     * List of installed user interface languages (MUI language codes).
     */
    @JsonProperty("MUILanguages")
    @Nullable
    public abstract List<@Nullable String> muiLanguages();
    /**
     * Indicates whether the operating system is installed on a portable device.
     * <ul>
     *   <li><b>true</b> — The operating system is installed on a portable device (e.g., Windows To Go).</li>
     *   <li><b>false</b> — The operating system is installed on a fixed computer.</li>
     * </ul>
     */
    @JsonProperty("PortableOperatingSystem")
    @Nullable
    public abstract Boolean portableOperatingSystem();
    /**
     * Indicates whether this is the primary operating system on the computer.
     */
    @JsonProperty("Primary")
    @Nullable
    public abstract Boolean primary();
    /**
     * Name of the registered user of the operating system.
     */
    @JsonProperty("RegisteredUser")
    @Nullable
    public abstract String registeredUser();
    /**
     * Operating system serial number or product key identifier.
     */
    @JsonProperty("SerialNumber")
    @Nullable
    public abstract String serialNumber();
    /**
     * Major version number of the most recent service pack installed.
     */
    @JsonProperty("ServicePackMajorVersion")
    @Nullable
    public abstract Integer servicePackMajorVersion();
    /**
     * Minor version number of the most recent service pack installed.
     */
    @JsonProperty("ServicePackMinorVersion")
    @Nullable
    public abstract Integer servicePackMinorVersion();
    /**
     * Full path to the system directory (typically "C:\WINDOWS\system32").
     */
    @JsonProperty("SystemDirectory")
    @Nullable
    public abstract String systemDirectory();
    /**
     * Drive letter where the operating system is installed (e.g., "C:").
     */
    @JsonProperty("SystemDrive")
    @Nullable
    public abstract String systemDrive();
    /**
     * Full path to the Windows installation directory (typically "C:\WINDOWS").
     */
    @JsonProperty("WindowsDirectory")
    @Nullable
    public abstract String windowsDirectory();

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