/*
 * SPDX-License-Identifier: MIT
 * SPDX-FileCopyrightText: 2025 The ferrumx-windows contributors
 * SPDX-FileCopyrightText: 2026 Cimari contributors
 */
package io.github.eggy03.cimari.service.storage;

import io.github.eggy03.cimari.entity.storage.Win32DiskPartition;
import io.github.eggy03.cimari.mapping.storage.Win32DiskPartitionMapper;
import io.github.eggy03.cimari.service.CommonServiceInterface;
import io.github.eggy03.cimari.shell.query.Cimv2;
import io.github.eggy03.cimari.terminal.TerminalResult;
import io.github.eggy03.cimari.terminal.TerminalService;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Unmodifiable;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * Service class for fetching information about disk partitions.
 * <p>
 * This class executes the {@link Cimv2#WIN32_DISK_PARTITION} PowerShell command
 * and maps the resulting output into an unmodifiable {@link List} of {@link Win32DiskPartition} objects.
 * </p>
 *
 * <h2>Usage examples</h2>
 * <pre>{@code
 * Win32DiskPartitionService service = new Win32DiskPartitionService();
 * List<Win32DiskPartition> partitions = service.get(10);
 * }</pre>
 *
 * @since 1.0.0
 */
public class Win32DiskPartitionService implements CommonServiceInterface<Win32DiskPartition> {

    private final TerminalService terminalService;
    private final Win32DiskPartitionMapper mapper;

    /**
     * Creates {@link Win32DiskPartitionService} with default configuration.
     *
     * @since 1.0.0
     */
    public Win32DiskPartitionService() {
        this(new TerminalService(), new Win32DiskPartitionMapper());
    }

    /**
     * Package Private constructor with injectable dependencies
     *
     * @param terminalService the {@link TerminalService} instance to use, must not be {@code null}
     * @param mapper          the mapper instance to use, must not be {@code null}
     * @since 1.0.0
     */
    Win32DiskPartitionService(TerminalService terminalService, Win32DiskPartitionMapper mapper) {
        this.terminalService = Objects.requireNonNull(terminalService, "terminalService cannot be null");
        this.mapper = Objects.requireNonNull(mapper, "mapper cannot be null");
    }

    /**
     * Retrieves an unmodifiable {@link List} of {@link Win32DiskPartition} objects <p>
     * Each invocation creates an isolated PowerShell process, which is
     * pre-maturely terminated if execution exceeds the specified timeout.
     * </p>
     *
     * @param timeout maximum time (in seconds) to wait for the PowerShell
     *                command to complete before terminating the process
     * @return an unmodifiable {@link List} of {@link Win32DiskPartition} objects representing the disk partitions.
     * Returns a {@link Collections#emptyList()} if no partitions are detected.
     * @since 1.0.0
     */
    @Override
    public @NotNull @Unmodifiable List<Win32DiskPartition> get(long timeout) {
        TerminalResult result = terminalService.executeQuery(Cimv2.WIN32_DISK_PARTITION, timeout);
        return mapper.mapToList(result.getResult(), Win32DiskPartition.class);
    }

}