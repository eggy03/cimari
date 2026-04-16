/*
 * SPDX-License-Identifier: MIT
 * SPDX-FileCopyrightText: 2025 The ferrumx-windows contributors
 * SPDX-FileCopyrightText: 2026 Cimari contributors
 */
package io.github.eggy03.cimari.service.compounded;

import io.github.eggy03.cimari.entity.compounded.Win32DiskDriveToPartitionAndLogicalDisk;
import io.github.eggy03.cimari.entity.compounded.Win32DiskPartitionToLogicalDisk;
import io.github.eggy03.cimari.mapping.compounded.Win32DiskPartitionToLogicalDiskMapper;
import io.github.eggy03.cimari.service.CommonServiceInterface;
import io.github.eggy03.cimari.service.storage.Win32DiskDriveService;
import io.github.eggy03.cimari.service.storage.Win32DiskDriveToDiskPartitionService;
import io.github.eggy03.cimari.service.storage.Win32DiskPartitionService;
import io.github.eggy03.cimari.service.storage.Win32LogicalDiskService;
import io.github.eggy03.cimari.service.storage.Win32LogicalDiskToPartitionService;
import io.github.eggy03.cimari.shell.script.ScriptEnum;
import io.github.eggy03.cimari.terminal.TerminalResult;
import io.github.eggy03.cimari.terminal.TerminalService;
import org.jspecify.annotations.NonNull;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * Service class for fetching physical disk and related logical disk information from the system.
 * <p>
 * This class executes the {@link ScriptEnum#WIN32_DISK_PARTITION_TO_LOGICAL_DISK} script
 * and maps the resulting output into an unmodifiable {@link List} of {@link Win32DiskPartitionToLogicalDisk} objects.
 * </p>
 *
 * <h2>Usage examples</h2>
 * <pre>{@code
 * Win32DiskPartitionToLogicalDiskService service = new Win32DiskPartitionToLogicalDiskService();
 * List<Win32DiskPartitionToLogicalDisk> disks = service.get(10);
 * }</pre>
 *
 * @see Win32DiskDriveToPartitionAndLogicalDisk
 * @see Win32DiskDriveService
 * @see Win32DiskPartitionService
 * @see Win32LogicalDiskService
 * @see Win32DiskDriveToDiskPartitionService
 * @see Win32LogicalDiskToPartitionService
 * @since 1.0.0
 */
public class Win32DiskPartitionToLogicalDiskService implements CommonServiceInterface<Win32DiskPartitionToLogicalDisk> {

    private final TerminalService terminalService;
    private final Win32DiskPartitionToLogicalDiskMapper mapper;

    /**
     * Creates {@link Win32DiskPartitionToLogicalDiskService} with default configuration.
     *
     * @since 1.0.0
     */
    public Win32DiskPartitionToLogicalDiskService() {
        this(new TerminalService(), new Win32DiskPartitionToLogicalDiskMapper());
    }

    /**
     * Package Private constructor with injectable dependencies
     *
     * @param terminalService the {@link TerminalService} instance to use, must not be {@code null}
     * @param mapper          the mapper instance to use, must not be {@code null}
     * @since 1.0.0
     */
    Win32DiskPartitionToLogicalDiskService(TerminalService terminalService, Win32DiskPartitionToLogicalDiskMapper mapper) {
        this.terminalService = Objects.requireNonNull(terminalService, "terminalService cannot be null");
        this.mapper = Objects.requireNonNull(mapper, "mapper cannot be null");
    }

    /**
     * Retrieves an unmodifiable {@link List} of {@link Win32DiskPartitionToLogicalDisk} objects
     * <p>
     * Each invocation creates an isolated PowerShell process, which is
     * pre-maturely terminated if execution exceeds the specified timeout.
     * </p>
     *
     * @param timeout maximum time (in seconds) to wait for the PowerShell
     *                command to complete before terminating the process
     * @return an unmodifiable {@link List} of {@link Win32DiskPartitionToLogicalDisk} objects representing connected physical disk and related logical disks.
     * Returns a {@link Collections#emptyList()} if no data is found.
     * @since 1.0.0
     */
    @Override
    public @NonNull List<Win32DiskPartitionToLogicalDisk> get(long timeout) {
        TerminalResult result = terminalService.executeScript(ScriptEnum.WIN32_DISK_PARTITION_TO_LOGICAL_DISK, timeout);
        return mapper.mapToList(result.getResult(), Win32DiskPartitionToLogicalDisk.class);
    }
}
