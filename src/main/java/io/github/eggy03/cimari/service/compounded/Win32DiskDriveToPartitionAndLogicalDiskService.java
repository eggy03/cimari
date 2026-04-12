/*
 * SPDX-License-Identifier: MIT
 * SPDX-FileCopyrightText: 2025 The ferrumx-windows contributors
 * SPDX-FileCopyrightText: 2026 Cimari contributors
 */
package io.github.eggy03.cimari.service.compounded;

import io.github.eggy03.cimari.entity.compounded.Win32DiskDriveToPartitionAndLogicalDisk;
import io.github.eggy03.cimari.mapping.compounded.Win32DiskDriveToPartitionAndLogicalDiskMapper;
import io.github.eggy03.cimari.service.CommonServiceInterface;
import io.github.eggy03.cimari.service.storage.Win32DiskDriveService;
import io.github.eggy03.cimari.service.storage.Win32DiskDriveToDiskPartitionService;
import io.github.eggy03.cimari.service.storage.Win32DiskPartitionService;
import io.github.eggy03.cimari.service.storage.Win32LogicalDiskService;
import io.github.eggy03.cimari.service.storage.Win32LogicalDiskToPartitionService;
import io.github.eggy03.cimari.shell.script.ScriptEnum;
import io.github.eggy03.cimari.shell.script.ScriptUtility;
import io.github.eggy03.cimari.utility.TerminalUtility;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Unmodifiable;

import java.util.List;

/**
 * Service class for fetching physical disk and related partition and logical disk data from the system.
 * <p>
 * This class executes the {@link ScriptEnum#WIN32_DISK_DRIVE_TO_PARTITION_AND_LOGICAL} PowerShell command
 * and maps the resulting JSON into an immutable list of {@link Win32DiskDriveToPartitionAndLogicalDisk} objects.
 * </p>
 *
 * <h2>Usage examples</h2>
 * <pre>{@code
 * Win32DiskDriveToPartitionAndLogicalDiskService service = new Win32DiskDriveToPartitionAndLogicalDiskService();
 * List<Win32DiskDriveToPartitionAndLogicalDisk> disks = service.get(10);
 * }</pre>
 *
 * @see Win32DiskPartitionToLogicalDiskService
 * @see Win32DiskDriveService
 * @see Win32DiskPartitionService
 * @see Win32LogicalDiskService
 * @see Win32DiskDriveToDiskPartitionService
 * @see Win32LogicalDiskToPartitionService
 * @since 1.0.0
 */
@Slf4j
public class Win32DiskDriveToPartitionAndLogicalDiskService implements CommonServiceInterface<Win32DiskDriveToPartitionAndLogicalDisk> {

    /**
     * Retrieves an immutable list of physical disks with related partition and logical disk data connected to the system
     * using an isolated PowerShell process with a configurable timeout.
     * <p>
     * Each invocation creates an isolated PowerShell process, which is
     * pre-maturely terminated if execution exceeds the specified timeout.
     * </p>
     *
     * @param timeout the maximum time (in seconds) to wait for the PowerShell command to complete before terminating
     *                the process
     * @return an immutable list of {@link Win32DiskDriveToPartitionAndLogicalDisk} objects representing connected physical disks
     * with their partitions and logical disks. Returns an empty list if no data is found.
     * @since 1.0.0
     */
    @Override
    public @NotNull @Unmodifiable List<Win32DiskDriveToPartitionAndLogicalDisk> get(long timeout) {

        String script = ScriptUtility.loadScript(ScriptEnum.WIN32_DISK_DRIVE_TO_PARTITION_AND_LOGICAL.getScriptPath());
        String response = TerminalUtility.executeCommand(script, timeout);
        log.trace("PowerShell response for the apache terminal session: \n{}", response);
        return new Win32DiskDriveToPartitionAndLogicalDiskMapper().mapToList(response, Win32DiskDriveToPartitionAndLogicalDisk.class);
    }
}
