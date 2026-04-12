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
import io.github.eggy03.cimari.service.TerminalService;
import io.github.eggy03.cimari.service.storage.Win32DiskDriveService;
import io.github.eggy03.cimari.service.storage.Win32DiskDriveToDiskPartitionService;
import io.github.eggy03.cimari.service.storage.Win32DiskPartitionService;
import io.github.eggy03.cimari.service.storage.Win32LogicalDiskService;
import io.github.eggy03.cimari.service.storage.Win32LogicalDiskToPartitionService;
import io.github.eggy03.cimari.shell.script.ScriptEnum;
import io.github.eggy03.cimari.shell.script.ScriptUtility;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Unmodifiable;

import java.util.List;

/**
 * Service class for fetching physical disk and related logical disk information from the system.
 * <p>
 * This class executes the {@link ScriptEnum#WIN32_DISK_PARTITION_TO_LOGICAL} script
 * and maps the resulting JSON into an immutable list of {@link Win32DiskPartitionToLogicalDisk} objects.
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
@Slf4j
public class Win32DiskPartitionToLogicalDiskService implements CommonServiceInterface<Win32DiskPartitionToLogicalDisk> {

    /**
     * Retrieves an immutable list of physical disk and related logical disks connected to the system
     * using an isolated PowerShell process with a configurable timeout.
     * <p>
     * Each invocation creates an isolated PowerShell process, which is
     * pre-maturely terminated if execution exceeds the specified timeout.
     * </p>
     *
     * @param timeout the maximum time (in seconds) to wait for the PowerShell
     *                command to complete before terminating the process
     * @return an immutable list of {@link Win32DiskPartitionToLogicalDisk} objects representing connected physical disk and related logical disks.
     * Returns an empty list if no data is found.
     * @since 1.0.0
     */
    @Override
    public @NotNull @Unmodifiable List<Win32DiskPartitionToLogicalDisk> get(long timeout) {

        String command = ScriptUtility.loadScript(ScriptEnum.WIN32_DISK_PARTITION_TO_LOGICAL.getScriptPath());
        String response = TerminalService.executeCommand(command, timeout);
        log.trace("PowerShell response for the apache terminal session: \n{}", response);
        return new Win32DiskPartitionToLogicalDiskMapper().mapToList(response, Win32DiskPartitionToLogicalDisk.class);
    }
}
