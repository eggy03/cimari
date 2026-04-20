/*
 * SPDX-License-Identifier: MIT
 * SPDX-FileCopyrightText: 2025 The ferrumx-windows contributors
 * SPDX-FileCopyrightText: 2026 Cimari contributors
 */
package io.github.eggy03.cimari.service.storage;

import io.github.eggy03.cimari.entity.storage.Win32LogicalDisk;
import io.github.eggy03.cimari.mapping.storage.Win32LogicalDiskMapper;
import io.github.eggy03.cimari.service.CommonServiceInterface;
import io.github.eggy03.cimari.shell.query.Cimv2;
import io.github.eggy03.cimari.terminal.TerminalResult;
import io.github.eggy03.cimari.terminal.TerminalService;
import org.jspecify.annotations.NonNull;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * Service class for fetching information about <b>logical</b> disks.
 * <p>
 * This class executes the {@link Cimv2#WIN32_LOGICAL_DISK} PowerShell command
 * and maps the resulting output into an unmodifiable {@link List} of {@link Win32LogicalDisk} objects.
 * </p>
 *
 * <h2>Usage examples</h2>
 * <pre>{@code
 * Win32LogicalDiskService service = new Win32LogicalDiskService();
 * List<Win32LogicalDisk> logicalDisks = service.get(10);
 * }</pre>
 *
 * @since 0.1.0
 */
public class Win32LogicalDiskService implements CommonServiceInterface<Win32LogicalDisk> {

    private final @NonNull TerminalService terminalService;
    private final @NonNull Win32LogicalDiskMapper mapper;

    /**
     * Creates {@link Win32LogicalDiskService} with default configuration.
     *
     * @since 0.1.0
     */
    public Win32LogicalDiskService() {
        this(new TerminalService(), new Win32LogicalDiskMapper());
    }

    /**
     * Package Private constructor with injectable dependencies
     *
     * @param terminalService the {@link TerminalService} instance to use, must not be {@code null}
     * @param mapper          the mapper instance to use, must not be {@code null}
     * @since 0.1.0
     */
    Win32LogicalDiskService(TerminalService terminalService, Win32LogicalDiskMapper mapper) {
        this.terminalService = Objects.requireNonNull(terminalService, "terminalService cannot be null");
        this.mapper = Objects.requireNonNull(mapper, "mapper cannot be null");
    }

    /**
     * Retrieves an unmodifiable {@link List} of {@link Win32LogicalDisk} objects
     * <p>
     * Each invocation creates an isolated PowerShell process, which is
     * pre-maturely terminated if execution exceeds the specified timeout.
     * </p>
     *
     * @param timeout maximum time (in seconds) to wait for the PowerShell
     *                command to complete before terminating the process
     * @return an unmodifiable {@link List} of {@link Win32LogicalDisk} objects representing the logical volumes.
     * Returns a {@link Collections#emptyList()} if no volumes are detected.
     * @since 0.1.0
     */
    @Override
    public @NonNull List<Win32LogicalDisk> get(long timeout) {
        TerminalResult result = terminalService.executeQuery(Cimv2.WIN32_LOGICAL_DISK, timeout);
        return mapper.mapToList(result.getResult(), Win32LogicalDisk.class);
    }
}
