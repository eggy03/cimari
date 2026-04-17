/*
 * SPDX-License-Identifier: MIT
 * SPDX-FileCopyrightText: 2025 The ferrumx-windows contributors
 * SPDX-FileCopyrightText: 2026 Cimari contributors
 */
package io.github.eggy03.cimari.entity.storage;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.github.eggy03.cimari.annotation.WmiClass;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;
import tools.jackson.databind.ObjectMapper;

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
 * @since 1.0.0
 */
@WmiClass(className = "Win32_LogicalDisk")
@NullMarked
public class Win32LogicalDisk {

    /**
     * Unique identifier of the logical disk from other devices on the system.
     * Appears as the drive letter assigned to the partition in the physical disk
     * Example: {@code "C:"}
     */
    @JsonProperty("DeviceID")
    @Nullable
    String deviceId;

    /**
     * Description of the logical disk object.
     */
    @JsonProperty("Description")
    @Nullable
    String description;

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
    Long driveType;

    /**
     * Type of media currently present in the logical drive.
     * Value corresponds to a member of the MEDIA_TYPE enumeration defined in {@code Winioctl.h.}
     * Visit the microsoft documentation stated at the class level to know about the possible values.
     */
    @JsonProperty("MediaType")
    @Nullable
    Long mediaType;

    /**
     * File system on the logical disk.
     * Example: {@code "NTFS"}, {@code "FAT32"}, {@code "ReFS"}
     */
    @JsonProperty("FileSystem")
    @Nullable
    String fileSystem;

    /**
     * Size of the disk drive in bytes.
     */
    @JsonProperty("Size")
    @Nullable
    BigInteger size;

    /**
     * Free space, in bytes, available on the logical disk.
     */
    @JsonProperty("FreeSpace")
    @Nullable
    BigInteger freeSpace;

    /**
     * Indicates if the logical volume exists as a single compressed entity (e.g., DoubleSpace).
     * If file-based compression is supported (e.g., NTFS), this value is {@code false}.
     */
    @JsonProperty("Compressed")
    @Nullable
    Boolean compressed;
    /**
     * Indicates whether the logical disk supports file-based compression (e.g., NTFS).
     */
    @JsonProperty("SupportsFileBasedCompression")
    @Nullable
    Boolean supportsFileBasedCompression;
    /**
     * Indicates whether this volume supports disk quotas.
     */
    @JsonProperty("SupportsDiskQuotas")
    @Nullable
    Boolean supportsDiskQuotas;
    /**
     * Volume name of the logical disk.
     * Example: {@code "Local Disk"}
     */
    @JsonProperty("VolumeName")
    @Nullable
    String volumeName;
    /**
     * Volume serial number of the logical disk.
     */
    @JsonProperty("VolumeSerialNumber")
    @Nullable
    String volumeSerialNumber;

    public @Nullable Boolean isCompressed() {
        return compressed;
    }

    public @Nullable Boolean supportsFileBasedCompression() {
        return supportsFileBasedCompression;
    }

    public @Nullable Boolean supportsDiskQuotas() {
        return supportsDiskQuotas;
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
