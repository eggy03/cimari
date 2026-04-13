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
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Unmodifiable;

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

    /**
     * Creates a {@link Win32DiskPartitionToLogicalDiskService} object.
     */
    public Win32DiskPartitionToLogicalDiskService() {
        this(new TerminalService());
    }

    /**
     * Creates a {@link  Win32DiskPartitionToLogicalDiskService} with the provided {@link TerminalService}.
     * <p>
     * This constructor is package private and is primarily intended for testing
     * </p>
     *
     * @param terminalService the {@link TerminalService} to use, must not be {@code null}
     * @throws NullPointerException if {@code terminalService} is {@code null}
     */
    Win32DiskPartitionToLogicalDiskService(TerminalService terminalService) {
        this.terminalService = Objects.requireNonNull(terminalService, "terminalService cannot be null");
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
    public @NotNull @Unmodifiable List<Win32DiskPartitionToLogicalDisk> get(long timeout) {
        TerminalResult result = terminalService.executeScript(ScriptEnum.WIN32_DISK_PARTITION_TO_LOGICAL_DISK, timeout);
        return new Win32DiskPartitionToLogicalDiskMapper().mapToList(result.getResult(), Win32DiskPartitionToLogicalDisk.class);
    }
}
