/*
 * SPDX-License-Identifier: MIT
 * SPDX-FileCopyrightText: 2025 The ferrumx-windows contributors
 * SPDX-FileCopyrightText: 2026 Cimari contributors
 */
package io.github.eggy03.cimari.service.storage;

import io.github.eggy03.cimari.entity.storage.Win32DiskDrive;
import io.github.eggy03.cimari.entity.storage.Win32DiskDriveToDiskPartition;
import io.github.eggy03.cimari.entity.storage.Win32DiskPartition;
import io.github.eggy03.cimari.mapping.storage.Win32DiskDriveToDiskPartitionMapper;
import io.github.eggy03.cimari.service.CommonServiceInterface;
import io.github.eggy03.cimari.shell.query.Cimv2;
import io.github.eggy03.cimari.terminal.TerminalResult;
import io.github.eggy03.cimari.terminal.TerminalService;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Unmodifiable;

import java.util.Collections;
import java.util.List;

/**
 * Service class for fetching the association between a {@link Win32DiskDrive}, and {@link Win32DiskPartition} from the system.
 * <p>
 * This class executes the {@link Cimv2#WIN32_DISK_DRIVE_TO_DISK_PARTITION} PowerShell command
 * and maps the resulting output into an unmodifiable {@link List} of {@link Win32DiskDriveToDiskPartition} objects.
 * </p>
 *
 * <h2>Usage examples</h2>
 * <pre>{@code
 * Win32DiskDriveToDiskPartitionServiceservice = new Win32DiskDriveToDiskPartitionService();
 * List<Win32DiskDriveToDiskPartition> ddt = service.get(10);
 * }</pre>
 *
 * @since 1.0.0
 */
public class Win32DiskDriveToDiskPartitionService implements CommonServiceInterface<Win32DiskDriveToDiskPartition> {

    /**
     * Retrieves an unmodifiable {@link List} of {@link Win32DiskDriveToDiskPartition} objects
     * <p>
     * Each invocation creates an isolated PowerShell process, which is
     * pre-maturely terminated if execution exceeds the specified timeout.
     * </p>
     *
     * @param timeout maximum time (in seconds) to wait for the PowerShell
     *                command to complete before terminating the process
     * @return an unmodifiable {@link List} of {@link Win32DiskDriveToDiskPartition} objects representing the association between
     * a {@link Win32DiskDrive} and it's {@link Win32DiskPartition}.
     * Returns a {@link Collections#emptyList()} if none are detected.
     * @since 1.0.0
     */
    @Override
    public @NotNull @Unmodifiable List<Win32DiskDriveToDiskPartition> get(long timeout) {
        TerminalResult result = new TerminalService().executeQuery(Cimv2.WIN32_DISK_DRIVE_TO_DISK_PARTITION, timeout);
        return new Win32DiskDriveToDiskPartitionMapper().mapToList(result.getResult(), Win32DiskDriveToDiskPartition.class);
    }
}
