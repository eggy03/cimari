/*
 * SPDX-License-Identifier: MIT
 * SPDX-FileCopyrightText: 2025 The ferrumx-windows contributors
 * SPDX-FileCopyrightText: 2026 Cimari contributors
 */
package io.github.eggy03.cimari.service.storage;

import io.github.eggy03.cimari.entity.storage.Win32DiskDrive;
import io.github.eggy03.cimari.mapping.storage.Win32DiskDriveMapper;
import io.github.eggy03.cimari.service.CommonServiceInterface;
import io.github.eggy03.cimari.shell.query.Cimv2;
import io.github.eggy03.cimari.terminal.TerminalResult;
import io.github.eggy03.cimari.terminal.TerminalService;
import org.jspecify.annotations.NonNull;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * Service class for fetching information about disk drives.
 * <p>
 * This class executes the {@link Cimv2#WIN32_DISK_DRIVE} PowerShell command
 * and maps the resulting output into an unmodifiable {@link List} of {@link Win32DiskDrive} objects.
 * </p>
 *
 * <h2>Usage examples</h2>
 * <pre>{@code
 * Win32DiskDriveService service = new Win32DiskDriveService();
 * List<Win32DiskDrive> drives = service.get(10);
 * }</pre>
 *
 * @since 1.0.0
 */
public class Win32DiskDriveService implements CommonServiceInterface<Win32DiskDrive> {

    private final TerminalService terminalService;
    private final Win32DiskDriveMapper mapper;

    /**
     * Creates {@link Win32DiskDriveService} with default configuration.
     *
     * @since 1.0.0
     */
    public Win32DiskDriveService() {
        this(new TerminalService(), new Win32DiskDriveMapper());
    }

    /**
     * Package Private constructor with injectable dependencies
     *
     * @param terminalService the {@link TerminalService} instance to use, must not be {@code null}
     * @param mapper          the mapper instance to use, must not be {@code null}
     * @since 1.0.0
     */
    Win32DiskDriveService(TerminalService terminalService, Win32DiskDriveMapper mapper) {
        this.terminalService = Objects.requireNonNull(terminalService, "terminalService cannot be null");
        this.mapper = Objects.requireNonNull(mapper, "mapper cannot be null");
    }

    /**
     * Retrieves an unmodifiable {@link List} of {@link Win32DiskDrive} objects
     * <p>
     * Each invocation creates an isolated PowerShell process, which is
     * pre-maturely terminated if execution exceeds the specified timeout.
     * </p>
     *
     * @param timeout maximum time (in seconds) to wait for the PowerShell
     *                command to complete before terminating the process
     * @return an unmodifiable {@link List} of {@link Win32DiskDrive} objects representing the disk drives.
     * Returns a {@link Collections#emptyList()} if no disk drives are detected.
     * @since 1.0.0
     */
    @Override
    public @NonNull List<Win32DiskDrive> get(long timeout) {
        TerminalResult result = terminalService.executeQuery(Cimv2.WIN32_DISK_DRIVE, timeout);
        return mapper.mapToList(result.getResult(), Win32DiskDrive.class);
    }
}
