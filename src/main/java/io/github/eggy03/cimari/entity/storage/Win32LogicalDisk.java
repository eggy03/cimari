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

/**
 * Immutable representation of a <b>Logical</b> disk volume on a Windows system.
 * <p>
 * Fields correspond to properties retrieved from the {@code Win32_LogicalDisk} WMI class.
 * </p>
 *
 * <p>See {@link Win32DiskDrive} for information about physical disks in the system.</p>
 * <p>See {@link Win32DiskPartition} for information about partitions in a physical disk.</p>
 *
 * @see <a href="https://learn.microsoft.com/en-us/windows/win32/cimwin32prov/win32-logicaldisk">Win32_LogicalDisk Documentation</a>
 * @since 0.1.0
 */
@WmiClass(className = "Win32_LogicalDisk")
@NullMarked
@Value.Immutable
@ImmutableEntityStyle
@JsonSerialize(as = ImmutableWin32LogicalDisk.class)
@JsonDeserialize(as = ImmutableWin32LogicalDisk.class)
public abstract class Win32LogicalDisk {

    /**
     * Unique identifier of the logical disk from other devices on the system.
     * Appears as the drive letter assigned to the partition in the physical disk
     * Example: {@code "C:"}
     */
    @JsonProperty("DeviceID")
    @Nullable
    public abstract String deviceId();

    /**
     * Description of the logical disk object.
     */
    @JsonProperty("Description")
    @Nullable
    public abstract String description();

    /**
     * Numeric value that corresponds to the type of disk drive this logical disk represents.
     * <ul>
     *     <li>0 – Unknown</li>
     *     <li>1 – No Root Directory</li>
     *     <li>2 – Removable Disk</li>
     *     <li>3 – Local Disk</li>
     *     <li>4 – Network Drive</li>
     *     <li>5 – Compact Disc</li>
     *     <li>6 – RAM Disk</li>
     * </ul>
     * <p>Data type: uint32</p>
     */
    @JsonProperty("DriveType")
    @Nullable
    public abstract Long driveType();

    /**
     * Type of media currently present in the logical drive.
     * Value corresponds to a member of the MEDIA_TYPE enumeration defined in {@code Winioctl.h.}
     * Visit the microsoft documentation stated at the class level to know about the possible values.
     */
    @JsonProperty("MediaType")
    @Nullable
    public abstract Long mediaType();

    /**
     * File system on the logical disk.
     * Example: {@code "NTFS"}, {@code "FAT32"}, {@code "ReFS"}
     */
    @JsonProperty("FileSystem")
    @Nullable
    public abstract String fileSystem();

    /**
     * Size of the disk drive in bytes.
     */
    @JsonProperty("Size")
    @Nullable
    public abstract BigInteger size();

    /**
     * Free space, in bytes, available on the logical disk.
     */
    @JsonProperty("FreeSpace")
    @Nullable
    public abstract BigInteger freeSpace();

    /**
     * Indicates if the logical volume exists as a single compressed entity (e.g., DoubleSpace).
     * If file-based compression is supported (e.g., NTFS), this value is {@code false}.
     */
    @JsonProperty("Compressed")
    @Nullable
    public abstract Boolean compressed();

    /**
     * Indicates whether the logical disk supports file-based compression (e.g., NTFS).
     */
    @JsonProperty("SupportsFileBasedCompression")
    @Nullable
    public abstract Boolean supportsFileBasedCompression();

    /**
     * Indicates whether this volume supports disk quotas.
     */
    @JsonProperty("SupportsDiskQuotas")
    @Nullable
    public abstract Boolean supportsDiskQuotas();

    /**
     * Volume name of the logical disk.
     * Example: {@code "Local Disk"}
     */
    @JsonProperty("VolumeName")
    @Nullable
    public abstract String volumeName();

    /**
     * Volume serial number of the logical disk.
     */
    @JsonProperty("VolumeSerialNumber")
    @Nullable
    public abstract String volumeSerialNumber();

    /**
     * Retrieves the entity in a JSON pretty-print formatted string
     *
     * @return the {@link String} value of the object in JSON pretty-print format
     */
    public String toJson() {
        return new ObjectMapper()
                .writerWithDefaultPrettyPrinter()
                .writeValueAsString(this);
    }

}
