/*
 * SPDX-License-Identifier: MIT
 * SPDX-FileCopyrightText: 2025 The ferrumx-windows contributors
 * SPDX-FileCopyrightText: 2026 Cimari contributors
 */
package io.github.eggy03.cimari.service.storage;

import io.github.eggy03.cimari.entity.storage.Win32DiskPartition;
import io.github.eggy03.cimari.entity.storage.Win32LogicalDisk;
import io.github.eggy03.cimari.entity.storage.Win32LogicalDiskToPartition;
import io.github.eggy03.cimari.mapping.storage.Win32LogicalDiskToPartitionMapper;
import io.github.eggy03.cimari.service.CommonServiceInterface;
import io.github.eggy03.cimari.shell.query.Cimv2;
import io.github.eggy03.cimari.utility.TerminalService;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Unmodifiable;

import java.util.List;

/**
 * Service class for fetching the association between a {@link Win32DiskPartition}, and {@link Win32LogicalDisk} from the system.
 * <p>
 * This class executes the {@link Cimv2#WIN32_LOGICAL_DISK_TO_PARTITION} PowerShell command
 * and maps the resulting JSON into an immutable list of {@link Win32LogicalDiskToPartition} objects.
 * </p>
 *
 * <h2>Usage examples</h2>
 * <pre>{@code
 * Win32LogicalDiskToPartitionService service = new Win32LogicalDiskToPartitionService();
 * List<Win32LogicalDiskToPartition> ldt = ddtService.get(10);
 * }</pre>
 *
 * @since 1.0.0
 */
@Slf4j
public class Win32LogicalDiskToPartitionService implements CommonServiceInterface<Win32LogicalDiskToPartition> {

    /**
     * Retrieves an immutable list of {@link Win32LogicalDiskToPartition} entities
     * using an isolated PowerShell process with a configurable timeout.
     * <p>
     * Each invocation creates an isolated PowerShell process, which is
     * pre-maturely terminated if execution exceeds the specified timeout.
     * </p>
     *
     * @param timeout the maximum time (in seconds) to wait for the PowerShell
     *                command to complete before terminating the process
     * @return an immutable list of {@link Win32LogicalDiskToPartition} objects representing the association between
     * a {@link Win32DiskPartition} and a {@link Win32LogicalDisk}.
     * Returns an empty list if none are detected.
     * @since 1.0.0
     */
    @Override
    public @NotNull @Unmodifiable List<Win32LogicalDiskToPartition> get(long timeout) {
        String command = Cimv2.WIN32_LOGICAL_DISK_TO_PARTITION.getQuery();
        String response = TerminalService.executeCommand(command, timeout);
        log.trace("PowerShell response for the apache terminal session: \n{}", response);
        return new Win32LogicalDiskToPartitionMapper().mapToList(response, Win32LogicalDiskToPartition.class);
    }
}
