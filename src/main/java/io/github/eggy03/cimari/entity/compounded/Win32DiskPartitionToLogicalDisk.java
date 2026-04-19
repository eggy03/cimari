/*
 * SPDX-License-Identifier: MIT
 * SPDX-FileCopyrightText: 2025 The ferrumx-windows contributors
 * SPDX-FileCopyrightText: 2026 Cimari contributors
 */
package io.github.eggy03.cimari.entity.compounded;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.github.eggy03.cimari.annotation.ImmutableEntityStyle;
import io.github.eggy03.cimari.entity.storage.Win32DiskDrive;
import io.github.eggy03.cimari.entity.storage.Win32DiskPartition;
import io.github.eggy03.cimari.entity.storage.Win32LogicalDisk;
import io.github.eggy03.cimari.entity.storage.Win32LogicalDiskToPartition;
import org.immutables.value.Value;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;
import tools.jackson.databind.ObjectMapper;
import tools.jackson.databind.annotation.JsonDeserialize;
import tools.jackson.databind.annotation.JsonSerialize;

import java.util.List;

/**
 * Immutable representation of a {@link Win32DiskPartition} and its
 * {@code 1:N} relationship with {@link Win32LogicalDisk} in a Windows system.
 * <p>
 * Each instance represents a single disk partition identified by {@link #partitionId},
 * and maintains a one-to-many mapping with its corresponding logical disks.
 * </p>
 *
 * <p>
 * This class is purely a convenience class designed to eliminate the need for using
 * {@link Win32LogicalDiskToPartition} when
 * fetching a relation between {@link Win32DiskPartition} and {@link Win32LogicalDisk}
 * </p>
 * <p>
 * A class for representing{@code 1:N} mappings of
 * {@link Win32DiskDrive} with {@link Win32DiskPartition} and {@link Win32LogicalDisk}
 * is also available by the name of {@link Win32DiskDriveToPartitionAndLogicalDisk}.
 * </p>
 *
 * @see Win32DiskPartition
 * @see Win32LogicalDisk
 * @see Win32LogicalDiskToPartition
 * @since 1.0.0
 */
@NullMarked
@Value.Immutable
@ImmutableEntityStyle
@JsonSerialize(as = ImmutableWin32DiskPartitionToLogicalDisk.class)
@JsonDeserialize(as = ImmutableWin32DiskPartitionToLogicalDisk.class)
public abstract class Win32DiskPartitionToLogicalDisk {

    /**
     * The unique identifier for the {@link Win32DiskPartition} instance.
     * <p>
     * Typically represented as a string such as {@code "Disk #0, Partition #1"}.
     * </p>
     */
    @JsonProperty("PartitionID")
    @Nullable
    public abstract String partitionId();

    /**
     * The {@link Win32DiskPartition} entity associated with the {@link #partitionId}.
     * <p>
     * Represents the physical partition on a disk drive that may contain one or more logical disks.
     * </p>
     */
    @JsonProperty("Partition")
    @Nullable
    public abstract Win32DiskPartition diskPartition();

    /**
     * A list of {@link Win32LogicalDisk} entities associated with the {@link #partitionId}.
     * <p>
     * Represents all logical disks or drive letters mapped to the specified {@link #diskPartition}.
     * </p>
     */
    @JsonProperty("LogicalDisks")
    @Nullable
    public abstract List<@Nullable Win32LogicalDisk> logicalDiskList();

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
