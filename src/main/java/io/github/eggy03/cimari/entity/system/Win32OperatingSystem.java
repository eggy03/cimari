/*
 * SPDX-License-Identifier: MIT
 * SPDX-FileCopyrightText: 2025 The ferrumx-windows contributors
 * SPDX-FileCopyrightText: 2026 Cimari contributors
 */
package io.github.eggy03.cimari.entity.system;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.github.eggy03.cimari.annotation.ShallowImmutable;
import io.github.eggy03.cimari.annotation.WmiClass;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.Value;
import org.jetbrains.annotations.Nullable;
import tools.jackson.databind.ObjectMapper;

import java.util.List;

/**
 * Immutable representation of the Windows Operating System.
 * <p>
 * Fields correspond to properties retrieved from the {@code Win32_OperatingSystem} WMI class.
 * </p>
 *
 * <h2>Usage examples</h2>
 * <pre>{@code
 * // Build a new OperatingSystem instance
 * Win32OperatingSystem os = Win32OperatingSystem.builder()
 *     .name("Windows 11 Pro")
 *     .version("22H2")
 *     .numberOfUsers(1)
 *     .osArchitecture("64-bit")
 *     .build();
 *
 * // Create a modified copy using the builder
 * Win32OperatingSystem updated = os.toBuilder()
 *     .numberOfUsers(5)
 *     .build();
 *
 * }</pre>
 *
 *
 * @see <a href="https://learn.microsoft.com/en-us/windows/win32/cimwin32prov/win32-operatingsystem">Win32_OperatingSystem Documentation</a>
 * @since 1.0.0
 */

@Value
@Builder(toBuilder = true)
@ShallowImmutable
@WmiClass(className = "Win32_OperatingSystem")
public class Win32OperatingSystem {

    /**
     * Name of the operating system.
     */
    @JsonProperty("Name")
    @Nullable
    String name;

    /**
     * Short textual description (friendly name) of the operating system.
     */
    @JsonProperty("Caption")
    @Nullable
    String caption;

    /**
     * Date and time when the operating system was installed.
     */
    @JsonProperty("InstallDate")
    @Nullable
    String installDate;

    /**
     * Name of the computer system running the operating system.
     */
    @JsonProperty("CSName")
    @Nullable
    String csName;

    /**
     * Date and time when the computer was last booted.
     */
    @JsonProperty("LastBootUpTime")
    @Nullable
    String lastBootUpTime;

    /**
     * Current local date and time of the operating system.
     */
    @JsonProperty("LocalDateTime")
    @Nullable
    String localDateTime;

    /**
     * Indicates whether this operating system is part of a distributed system.
     */
    @Getter(AccessLevel.NONE)
    @JsonProperty("Distributed")
    @Nullable
    Boolean distributed;
    /**
     * Number of user sessions currently active.
     */
    @JsonProperty("NumberOfUsers")
    @Nullable
    Integer numberOfUsers;
    /**
     * Version number of the operating system (for example, "10.0.22621").
     */
    @JsonProperty("Version")
    @Nullable
    String version;
    /**
     * Path to the boot device that the operating system uses to start the computer.
     */
    @JsonProperty("BootDevice")
    @Nullable
    String bootDevice;
    /**
     * Internal build number of the operating system.
     */
    @JsonProperty("BuildNumber")
    @Nullable
    String buildNumber;
    /**
     * Type of build (e.g., "Multiprocessor Free" or "Checked").
     */
    @JsonProperty("BuildType")
    @Nullable
    String buildType;
    /**
     * Manufacturer of the operating system (typically "Microsoft Corporation").
     */
    @JsonProperty("Manufacturer")
    @Nullable
    String manufacturer;
    /**
     * Architecture of the operating system, such as "32-bit" or "64-bit".
     */
    @JsonProperty("OSArchitecture")
    @Nullable
    String osArchitecture;
    /**
     * List of installed user interface languages (MUI language codes).
     */
    @JsonProperty("MUILanguages")
    @Nullable
    List<String> muiLanguages;
    /**
     * Indicates whether the operating system is installed on a portable device.
     * <ul>
     *   <li><b>true</b> — The operating system is installed on a portable device (e.g., Windows To Go).</li>
     *   <li><b>false</b> — The operating system is installed on a fixed computer.</li>
     * </ul>
     */
    @Getter(AccessLevel.NONE)
    @JsonProperty("PortableOperatingSystem")
    @Nullable
    Boolean portableOperatingSystem;
    /**
     * Indicates whether this is the primary operating system on the computer.
     */
    @Getter(AccessLevel.NONE)
    @JsonProperty("Primary")
    @Nullable
    Boolean primary;
    /**
     * Name of the registered user of the operating system.
     */
    @JsonProperty("RegisteredUser")
    @Nullable
    String registeredUser;
    /**
     * Operating system serial number or product key identifier.
     */
    @JsonProperty("SerialNumber")
    @Nullable
    String serialNumber;
    /**
     * Major version number of the most recent service pack installed.
     */
    @JsonProperty("ServicePackMajorVersion")
    @Nullable
    Integer servicePackMajorVersion;
    /**
     * Minor version number of the most recent service pack installed.
     */
    @JsonProperty("ServicePackMinorVersion")
    @Nullable
    Integer servicePackMinorVersion;
    /**
     * Full path to the system directory (typically "C:\WINDOWS\system32").
     */
    @JsonProperty("SystemDirectory")
    @Nullable
    String systemDirectory;
    /**
     * Drive letter where the operating system is installed (e.g., "C:").
     */
    @JsonProperty("SystemDrive")
    @Nullable
    String systemDrive;
    /**
     * Full path to the Windows installation directory (typically "C:\WINDOWS").
     */
    @JsonProperty("WindowsDirectory")
    @Nullable
    String windowsDirectory;

    public @Nullable Boolean isDistributed() {
        return distributed;
    }

    public @Nullable Boolean isPortable() {
        return portableOperatingSystem;
    }

    public @Nullable Boolean isPrimary() {
        return primary;
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