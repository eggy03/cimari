/*
 * SPDX-License-Identifier: MIT
 * SPDX-FileCopyrightText: 2025 The ferrumx-windows contributors
 * SPDX-FileCopyrightText: 2026 Cimari contributors
 */
package io.github.eggy03.cimari.entity.mainboard;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.github.eggy03.cimari.annotation.ImmutableEntityStyle;
import io.github.eggy03.cimari.annotation.WmiClass;
import org.immutables.value.Value;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;
import tools.jackson.databind.ObjectMapper;
import tools.jackson.databind.annotation.JsonDeserialize;
import tools.jackson.databind.annotation.JsonSerialize;

/**
 * Immutable representation of a BIOS entity on a Windows system.
 * <p>
 * Fields correspond to properties retrieved from the {@code Win32_BIOS} WMI class.
 * </p>
 *
 * @see <a href="https://learn.microsoft.com/en-us/windows/win32/cimwin32prov/win32-bios">Win32_BIOS</a>
 * @since 1.0.0
 */
@WmiClass(className = "Win32_BIOS")
@NullMarked
@Value.Immutable
@ImmutableEntityStyle
@JsonSerialize(as = ImmutableWin32Bios.class)
@JsonDeserialize(as = ImmutableWin32Bios.class)
public abstract class Win32Bios {

    /**
     * The BIOS name.
     */
    @JsonProperty("Name")
    @Nullable
    public abstract String name();

    /**
     * Short description of the BIOS.
     */
    @JsonProperty("Caption")
    @Nullable
    public abstract String caption();

    /**
     * Manufacturer of the BIOS.
     */
    @JsonProperty("Manufacturer")
    @Nullable
    public abstract String manufacturer();

    /**
     * BIOS release date in UTC format (YYYYMMDDHHMMSS.MMMMMM±OOO).
     */
    @JsonProperty("ReleaseDate")
    @Nullable
    public abstract String releaseDate();

    /**
     * If true, the SMBIOS is available on this computer system.
     */
    @JsonProperty("SMBIOSPresent")
    @Nullable
    public abstract Boolean smbiosPresent();

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
    public abstract String status();

    /**
     * Version of the BIOS. This string is created by the BIOS manufacturer.
     */
    @JsonProperty("Version")
    @Nullable
    public abstract String version();

    /**
     * Name of the current BIOS language.
     */
    @JsonProperty("CurrentLanguage")
    @Nullable
    public abstract String currentLanguage();

    /**
     * BIOS version as reported by SMBIOS.
     */
    @JsonProperty("SMBIOSBIOSVersion")
    @Nullable
    public abstract String smbiosBiosVersion();

    /**
     * If TRUE, this is the primary BIOS of the computer system.
     */
    @JsonProperty("PrimaryBIOS")
    @Nullable
    public abstract Boolean primaryBios();

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