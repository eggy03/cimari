/*
 * SPDX-License-Identifier: MIT
 * SPDX-FileCopyrightText: 2025 The ferrumx-windows contributors
 * SPDX-FileCopyrightText: 2026 Cimari contributors
 */
package io.github.eggy03.cimari.entity.storage;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.github.eggy03.cimari.annotation.ImmutableEntityStyle;
import io.github.eggy03.cimari.annotation.WmiClass;
import org.immutables.value.Value;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;
import tools.jackson.databind.ObjectMapper;
import tools.jackson.databind.annotation.JsonDeserialize;
import tools.jackson.databind.annotation.JsonSerialize;

import java.math.BigInteger;
import java.util.List;

/**
 * Immutable representation of a <b>Physical</b> disk on a Windows system.
 * <p>
 * Fields correspond to properties retrieved from the {@code Win32_DiskDrive} WMI class.
 * </p>
 *
 * <p>See {@link Win32DiskPartition} for information about partitions on this disk.</p>
 * <p>See {@link Win32LogicalDisk} for information about the logical volumes on this disk.</p>
 *
 * @see <a href="https://learn.microsoft.com/en-us/windows/win32/cimwin32prov/win32-diskdrive">Win32_DiskDrive Documentation</a>
 * @since 1.0.0
 */
@WmiClass(className = "Win32_DiskDrive")
@NullMarked
@Value.Immutable
@ImmutableEntityStyle
@JsonSerialize(as = Win32DiskDrive.class)
@JsonDeserialize(as = Win32DiskDrive.class)
public abstract class AbstractWin32DiskDrive {

    /**
     * Unique identifier of the disk drive with other devices on the system.
     */
    @JsonProperty("DeviceID")
    @Nullable
    public abstract String deviceId();

    /**
     * Short description of the object.
     */
    @JsonProperty("Caption")
    @Nullable
    public abstract String caption();

    /**
     * Manufacturer’s model number of the disk drive.
     */
    @JsonProperty("Model")
    @Nullable
    public abstract String model();

    /**
     * Size of the disk drive, calculated by multiplying the total number of cylinders,
     * tracks in each cylinder, sectors in each track, and bytes in each sector.
     */
    @JsonProperty("Size")
    @Nullable
    public abstract BigInteger size();

    /**
     * Revision of the disk drive firmware assigned by the manufacturer.
     */
    @JsonProperty("FirmwareRevision")
    @Nullable
    public abstract String firmwareRevision();

    /**
     * Number allocated by the manufacturer to identify the physical media.
     */
    @JsonProperty("SerialNumber")
    @Nullable
    public abstract String serialNumber();

    /**
     * Number of partitions on this physical disk drive recognized by the operating system.
     */
    @JsonProperty("Partitions")
    @Nullable
    public abstract Long partitions();

    /**
     * Current operational status of the physical disk.
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
     * Interface type of the physical disk drive (e.g., SCSI, IDE, USB, NVMe).
     */
    @JsonProperty("InterfaceType")
    @Nullable
    public abstract String interfaceType();

    /**
     * Windows Plug and Play device identifier of the logical device.
     */
    @JsonProperty("PNPDeviceID")
    @Nullable
    public abstract String pnpDeviceId();

    /**
     * Array of capabilities of the media access device.
     * <p>Possible Values: </p>
     * <ul>
     *     <li>1 - Other</li>
     *     <li>2 - Sequential Access</li>
     *     <li>3 - Random Access</li>
     *     <li>4 - Supports Writing</li>
     *     <li>5 - Encryption</li>
     *     <li>6 - Compression</li>
     *     <li>7 - Supports Removable Media</li>
     *     <li>8 - Manual Cleaning</li>
     *     <li>9 - Automatic Cleaning</li>
     *     <li>10 - S.M.A.R.T Notification</li>
     *     <li>11 - Supports Dual Sided Media</li>
     *     <li>12 - Pre-dismount Eject Not Required</li>
     * </ul>
     */
    @JsonProperty("Capabilities")
    @Nullable
    public abstract List<@Nullable Integer> capabilities();

    /**
     * List of more detailed explanations for any of the access device features indicated in the {@link #capabilities} array.
     * Note, each entry of this array is related to the entry in the {@link #capabilities} array that is located at the same index.
     */
    @JsonProperty("CapabilityDescriptions")
    @Nullable
    public abstract List<@Nullable String> capabilityDescriptions();

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