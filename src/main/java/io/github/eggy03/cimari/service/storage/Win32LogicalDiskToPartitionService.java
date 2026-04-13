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
import io.github.eggy03.cimari.terminal.TerminalResult;
import io.github.eggy03.cimari.terminal.TerminalService;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Unmodifiable;

import java.util.Collections;
import java.util.List;

/**
 * Service class for fetching the association between a {@link Win32DiskPartition}, and {@link Win32LogicalDisk} from the system.
 * <p>
 * This class executes the {@link Cimv2#WIN32_LOGICAL_DISK_TO_PARTITION} PowerShell command
 * and maps the resulting output into an unmodifiable {@link List} of {@link Win32LogicalDiskToPartition} objects.
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
public class Win32LogicalDiskToPartitionService implements CommonServiceInterface<Win32LogicalDiskToPartition> {

    /**
     * Retrieves an unmodifiable {@link List} of {@link Win32LogicalDiskToPartition} objects
     * <p>
     * Each invocation creates an isolated PowerShell process, which is
     * pre-maturely terminated if execution exceeds the specified timeout.
     * </p>
     *
     * @param timeout maximum time (in seconds) to wait for the PowerShell
     *                command to complete before terminating the process
     * @return an unmodifiable {@link List} of {@link Win32LogicalDiskToPartition} objects representing the association between
     * a {@link Win32DiskPartition} and a {@link Win32LogicalDisk}.
     * Returns a {@link Collections#emptyList()} if none are detected.
     * @since 1.0.0
     */
    @Override
    public @NotNull @Unmodifiable List<Win32LogicalDiskToPartition> get(long timeout) {
        TerminalResult result = new TerminalService().executeQuery(Cimv2.WIN32_LOGICAL_DISK_TO_PARTITION, timeout);
        return new Win32LogicalDiskToPartitionMapper().mapToList(result.getResult(), Win32LogicalDiskToPartition.class);
    }
}
