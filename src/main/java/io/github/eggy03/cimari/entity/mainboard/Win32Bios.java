/*
 * SPDX-License-Identifier: MIT
 * SPDX-FileCopyrightText: 2025 The ferrumx-windows contributors
 * SPDX-FileCopyrightText: 2026 Cimari contributors
 */
package io.github.eggy03.cimari.entity.mainboard;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.github.eggy03.cimari.annotation.WmiClass;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;
import tools.jackson.databind.ObjectMapper;

/**
 * Immutable representation of a BIOS entity on a Windows system.
 * <p>
 * Fields correspond to properties retrieved from the {@code Win32_BIOS} WMI class.
 * </p>
 *
 * <h2>Usage example</h2>
 * <pre>{@code
 * Win32Bios bios = Win32Bios.builder()
 *     .name("BIOS Name")
 *     .version("1.2.3")
 *     .build();
 *
 * // Create a modified copy
 * Win32Bios updated = bios.toBuilder()
 *     .version("1.2.4")
 *     .build();
 * }</pre>
 *
 *
 * @see <a href="https://learn.microsoft.com/en-us/windows/win32/cimwin32prov/win32-bios">Win32_BIOS</a>
 * @since 1.0.0
 */
@WmiClass(className = "Win32_BIOS")
@NullMarked
public class Win32Bios {

    /**
     * The BIOS name.
     */
    @JsonProperty("Name")
    @Nullable
    String name;

    /**
     * Short description of the BIOS.
     */
    @JsonProperty("Caption")
    @Nullable
    String caption;

    /**
     * Manufacturer of the BIOS.
     */
    @JsonProperty("Manufacturer")
    @Nullable
    String manufacturer;

    /**
     * BIOS release date in UTC format (YYYYMMDDHHMMSS.MMMMMM±OOO).
     */
    @JsonProperty("ReleaseDate")
    @Nullable
    String releaseDate;

    /**
     * If true, the SMBIOS is available on this computer system.
     */
    @JsonProperty("SMBIOSPresent")
    @Nullable
    Boolean smbiosPresent;
    /**
     * Current operational status of the BIOS.
     * <p>Possible OPERATIONAL values:</p>
     * <ul>
     *   <li>"OK"</li>
     *   <li>"Degraded"</li>
     *   <li>"Pred Fail"</li>
     * </ul>
     * <p>Possible NON-OPERATIONAL values:</p>
     * <ul>
     *   <li>"Unknown"</li>
     *   <li>"Error"</li>
     *   <li>"Starting"</li>
     *   <li>"Stopping"</li>
     *   <li>"Service"</li>
     * </ul>
     * <p>Possible OTHER values:</p>
     * <ul>
     *   <li>"Stressed"</li>
     *   <li>"NonRecover"</li>
     *   <li>"No Contact"</li>
     *   <li>"Lost Comm"</li>
     * </ul>
     */
    @JsonProperty("Status")
    @Nullable
    String status;
    /**
     * Version of the BIOS. This string is created by the BIOS manufacturer.
     */
    @JsonProperty("Version")
    @Nullable
    String version;
    /**
     * Name of the current BIOS language.
     */
    @JsonProperty("CurrentLanguage")
    @Nullable
    String currentLanguage;
    /**
     * BIOS version as reported by SMBIOS.
     */
    @JsonProperty("SMBIOSBIOSVersion")
    @Nullable
    String smbiosBiosVersion;
    /**
     * If TRUE, this is the primary BIOS of the computer system.
     */
    @JsonProperty("PrimaryBIOS")
    @Nullable
    Boolean primaryBios;

    public @Nullable Boolean isSMBIOSPresent() {
        return smbiosPresent;
    }

    public @Nullable Boolean isPrimaryBios() {
        return primaryBios;
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