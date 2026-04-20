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
import org.jspecify.annotations.NonNull;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

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
 * @since 0.1.0
 */
public class Win32LogicalDiskToPartitionService implements CommonServiceInterface<Win32LogicalDiskToPartition> {

    private final @NonNull TerminalService terminalService;
    private final @NonNull Win32LogicalDiskToPartitionMapper mapper;

    /**
     * Creates {@link Win32LogicalDiskToPartitionService} with default configuration.
     *
     * @since 0.1.0
     */
    public Win32LogicalDiskToPartitionService() {
        this(new TerminalService(), new Win32LogicalDiskToPartitionMapper());
    }

    /**
     * Package Private constructor with injectable dependencies
     *
     * @param terminalService the {@link TerminalService} instance to use, must not be {@code null}
     * @param mapper          the mapper instance to use, must not be {@code null}
     * @since 0.1.0
     */
    Win32LogicalDiskToPartitionService(TerminalService terminalService, Win32LogicalDiskToPartitionMapper mapper) {
        this.terminalService = Objects.requireNonNull(terminalService, "terminalService cannot be null");
        this.mapper = Objects.requireNonNull(mapper, "mapper cannot be null");
    }

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
     * @since 0.1.0
     */
    @Override
    public @NonNull List<Win32LogicalDiskToPartition> get(long timeout) {
        TerminalResult result = terminalService.executeQuery(Cimv2.WIN32_LOGICAL_DISK_TO_PARTITION, timeout);
        return mapper.mapToList(result.getResult(), Win32LogicalDiskToPartition.class);
    }
}
