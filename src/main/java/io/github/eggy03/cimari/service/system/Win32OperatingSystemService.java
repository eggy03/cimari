/*
 * SPDX-License-Identifier: MIT
 * SPDX-FileCopyrightText: 2025 The ferrumx-windows contributors
 * SPDX-FileCopyrightText: 2026 Cimari contributors
 */
package io.github.eggy03.cimari.service.system;

import io.github.eggy03.cimari.entity.system.Win32OperatingSystem;
import io.github.eggy03.cimari.mapping.system.Win32OperatingSystemMapper;
import io.github.eggy03.cimari.service.CommonServiceInterface;
import io.github.eggy03.cimari.shell.query.Cimv2;
import io.github.eggy03.cimari.terminal.TerminalResult;
import io.github.eggy03.cimari.terminal.TerminalService;
import org.jspecify.annotations.NonNull;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * Service class for fetching operating system information from the system.
 * <p>
 * This class executes the {@link Cimv2#WIN32_OPERATING_SYSTEM} PowerShell command
 * and maps the resulting output into an unmodifiable {@link List} of {@link Win32OperatingSystem} objects.
 * </p>
 *
 * <h2>Usage examples</h2>
 * <pre>{@code
 * Win32OperatingSystemService service = new Win32OperatingSystemService();
 * List<Win32OperatingSystem> operatingSystems = service.get(10);
 * }</pre>
 *
 * @since 1.0.0
 */
public class Win32OperatingSystemService implements CommonServiceInterface<Win32OperatingSystem> {

    private final @NonNull TerminalService terminalService;
    private final @NonNull Win32OperatingSystemMapper mapper;

    /**
     * Creates {@link Win32OperatingSystemService} with default configuration.
     *
     * @since 1.0.0
     */
    public Win32OperatingSystemService() {
        this(new TerminalService(), new Win32OperatingSystemMapper());
    }

    /**
     * Package Private constructor with injectable dependencies
     *
     * @param terminalService the {@link TerminalService} instance to use, must not be {@code null}
     * @param mapper          the mapper instance to use, must not be {@code null}
     * @since 1.0.0
     */
    Win32OperatingSystemService(TerminalService terminalService, Win32OperatingSystemMapper mapper) {
        this.terminalService = Objects.requireNonNull(terminalService, "terminalService cannot be null");
        this.mapper = Objects.requireNonNull(mapper, "mapper cannot be null");
    }

    /**
     * Retrieves an unmodifiable {@link List} of {@link Win32OperatingSystem}
     * <p>
     * Each invocation creates an isolated PowerShell process, which is
     * pre-maturely terminated if execution exceeds the specified timeout.
     * </p>
     *
     * @param timeout maximum time (in seconds) to wait for the PowerShell
     *                command to complete before terminating the process
     * @return an unmodifiable {@link List} of {@link Win32OperatingSystem} objects representing the system's operating systems.
     * Returns a {@link Collections#emptyList()} if none are detected.
     * @since 1.0.0
     */
    @Override
    public @NonNull List<Win32OperatingSystem> get(long timeout) {
        TerminalResult result = terminalService.executeQuery(Cimv2.WIN32_OPERATING_SYSTEM, timeout);
        return mapper.mapToList(result.getResult(), Win32OperatingSystem.class);
    }
}
