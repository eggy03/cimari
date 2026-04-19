/*
 * SPDX-License-Identifier: MIT
 * SPDX-FileCopyrightText: 2025 The ferrumx-windows contributors
 * SPDX-FileCopyrightText: 2026 Cimari contributors
 */
package io.github.eggy03.cimari.service.system;

import io.github.eggy03.cimari.entity.system.Win32Environment;
import io.github.eggy03.cimari.mapping.system.Win32EnvironmentMapper;
import io.github.eggy03.cimari.service.CommonServiceInterface;
import io.github.eggy03.cimari.shell.query.Cimv2;
import io.github.eggy03.cimari.terminal.TerminalResult;
import io.github.eggy03.cimari.terminal.TerminalService;
import org.jspecify.annotations.NonNull;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * Service class for fetching information about environment variables in a Windows System.
 * <p>
 * This class executes the {@link Cimv2#WIN32_ENVIRONMENT} PowerShell command
 * and maps the resulting output into an unmodifiable {@link List} of {@link Win32Environment} objects.
 * </p>
 *
 * <h2>Usage examples</h2>
 * <pre>{@code
 * Win32EnvironmentService service = new Win32EnvironmentService();
 * List<Win32Environment> env = service.get(10);
 * }</pre>
 *
 * @since 1.0.0
 */
public class Win32EnvironmentService implements CommonServiceInterface<Win32Environment> {

    private final @NonNull TerminalService terminalService;
    private final @NonNull Win32EnvironmentMapper mapper;

    /**
     * Creates {@link Win32EnvironmentService} with default configuration.
     *
     * @since 1.0.0
     */
    public Win32EnvironmentService() {
        this(new TerminalService(), new Win32EnvironmentMapper());
    }

    /**
     * Package Private constructor with injectable dependencies
     *
     * @param terminalService the {@link TerminalService} instance to use, must not be {@code null}
     * @param mapper          the mapper instance to use, must not be {@code null}
     * @since 1.0.0
     */
    Win32EnvironmentService(TerminalService terminalService, Win32EnvironmentMapper mapper) {
        this.terminalService = Objects.requireNonNull(terminalService, "terminalService cannot be null");
        this.mapper = Objects.requireNonNull(mapper, "mapper cannot be null");
    }

    /**
     * Retrieves an unmodifiable {@link List} of {@link Win32Environment} objects
     * <p>
     * Each invocation creates an isolated PowerShell process, which is
     * pre-maturely terminated if execution exceeds the specified timeout.
     * </p>
     *
     * @param timeout maximum time (in seconds) to wait for the PowerShell
     *                command to complete before terminating the process
     * @return an unmodifiable {@link List} of {@link Win32Environment} objects representing the env variables.
     * Returns a {@link Collections#emptyList()} if no env variables are detected.
     * @since 1.0.0
     */
    @Override
    public @NonNull List<Win32Environment> get(long timeout) {
        TerminalResult result = terminalService.executeQuery(Cimv2.WIN32_ENVIRONMENT, timeout);
        return mapper.mapToList(result.getResult(), Win32Environment.class);
    }
}
