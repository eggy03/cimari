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
 * Immutable representation of a <b>Partition</b> in a <b>Physical</b> disk on a Windows system.
 * <p>
 * Fields correspond to properties retrieved from the {@code Win32_DiskPartition} WMI class.
 * </p>
 *
 * <p>See {@link Win32DiskDrive} for information about physical disks.</p>
 * <p>See {@link Win32LogicalDisk} for information about the logical volumes on a physical disk.</p>
 *
 * @see <a href="https://learn.microsoft.com/en-us/windows/win32/cimwin32prov/win32-diskpartition">Win32_DiskPartition Documentation</a>
 * @since 1.0.0
 */
@WmiClass(className = "Win32_DiskPartition")
@NullMarked
@Value.Immutable
@ImmutableEntityStyle
@JsonSerialize(as = Win32DiskPartition.class)
@JsonDeserialize(as = Win32DiskPartition.class)
public abstract class AbstractWin32DiskPartition {

    /**
     * Unique identifier of the disk drive and partition within the system.
     */
    @JsonProperty("DeviceID")
    @Nullable
    public abstract String deviceId();

    /**
     * Label by which the partition is known.
     */
    @JsonProperty("Name")
    @Nullable
    public abstract String name();

    /**
     * Description of the partition object.
     */
    @JsonProperty("Description")
    @Nullable
    public abstract String description();

    /**
     * Size in bytes of the blocks that form this partition.
     */
    @JsonProperty("BlockSize")
    @Nullable
    public abstract BigInteger blockSize();

    /**
     * Total number of consecutive blocks that form this partition.
     * The total size of the partition can be calculated by multiplying this value by {@link #blockSize}.
     */
    @JsonProperty("NumberOfBlocks")
    @Nullable
    public abstract BigInteger numberOfBlocks();

    /**
     * Indicates whether the computer can be booted from this partition.
     */
    @JsonProperty("Bootable")
    @Nullable
    public abstract Boolean bootable();
    /**
     * Indicates whether this is the primary partition on the disk.
     */
    @JsonProperty("PrimaryPartition")
    @Nullable
    public abstract Boolean primaryPartition();
    /**
     * Indicates whether this is the active (boot) partition used by the operating system when booting.
     */
    @JsonProperty("BootPartition")
    @Nullable
    public abstract Boolean bootPartition();
    /**
     * Index number of the physical disk that contains this partition.
     */
    @JsonProperty("DiskIndex")
    @Nullable
    public abstract Long diskIndex();
    /**
     * Total size of the partition in bytes.
     */
    @JsonProperty("Size")
    @Nullable
    public abstract BigInteger size();
    /**
     * Type of the partition
     * <p>Possible Values (Non-exhaustive, will be updated when new values are found):</p>
     * <ul>
     *     <li>Unused</li>
     *     <li>12-bit FAT</li>
     *     <li>Xenix Type 1</li>
     *     <li>Xenix Type 2</li>
     *     <li>16-bit FAT</li>
     *     <li>Extended Partition</li>
     *     <li>MS-DOS V4 Huge</li>
     *     <li>Installable File System</li>
     *     <li>PowerPC Reference Platform</li>
     *     <li>UNIX</li>
     *     <li>NTFS</li>
     *     <li>ReFS</li>
     *     <li>Win95 w/Extended Int 13</li>
     *     <li>Extended w/Extended Int 13</li>
     *     <li>Logical Disk Manager</li>
     *     <li>Unknown</li>
     * </ul>
     */
    @JsonProperty("Type")
    @Nullable
    public abstract String type();

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