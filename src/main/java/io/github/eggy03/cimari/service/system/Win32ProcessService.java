/*
 * SPDX-License-Identifier: MIT
 * SPDX-FileCopyrightText: 2025 The ferrumx-windows contributors
 * SPDX-FileCopyrightText: 2026 Cimari contributors
 */
package io.github.eggy03.cimari.service.system;

import io.github.eggy03.cimari.entity.system.Win32Process;
import io.github.eggy03.cimari.mapping.system.Win32ProcessMapper;
import io.github.eggy03.cimari.service.CommonServiceInterface;
import io.github.eggy03.cimari.shell.query.Cimv2;
import io.github.eggy03.cimari.terminal.TerminalResult;
import io.github.eggy03.cimari.terminal.TerminalService;
import org.jspecify.annotations.NonNull;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * Service class for fetching process information from the system.
 * <p>
 * This class executes the {@link Cimv2#WIN32_PROCESS} PowerShell command
 * and maps the resulting output into an unmodifiable {@link List} of {@link Win32Process} objects.
 * </p>
 *
 * <h2>Usage examples</h2>
 * <pre>{@code
 * Win32ProcessService service = new Win32ProcessService();
 * List<Win32Process> processList = service.get(10);
 * }</pre>
 *
 * @since 0.1.0
 */
public class Win32ProcessService implements CommonServiceInterface<Win32Process> {

    private final @NonNull TerminalService terminalService;
    private final @NonNull Win32ProcessMapper mapper;

    /**
     * Creates {@link Win32ProcessService} with default configuration.
     *
     * @since 0.1.0
     */
    public Win32ProcessService() {
        this(new TerminalService(), new Win32ProcessMapper());
    }

    /**
     * Package Private constructor with injectable dependencies
     *
     * @param terminalService the {@link TerminalService} instance to use, must not be {@code null}
     * @param mapper          the mapper instance to use, must not be {@code null}
     * @since 0.1.0
     */
    Win32ProcessService(TerminalService terminalService, Win32ProcessMapper mapper) {
        this.terminalService = Objects.requireNonNull(terminalService, "terminalService cannot be null");
        this.mapper = Objects.requireNonNull(mapper, "mapper cannot be null");
    }

    /**
     * Retrieves an unmodifiable {@link List} of {@link Win32Process}
     * <p>
     * Each invocation creates an isolated PowerShell process, which is
     * pre-maturely terminated if execution exceeds the specified timeout.
     * </p>
     *
     * @param timeout maximum time (in seconds) to wait for the PowerShell
     *                command to complete before terminating the process
     * @return an unmodifiable {@link List} of {@link Win32Process} objects representing the system's processes.
     * Returns a {@link Collections#emptyList()} if none are detected.
     * @since 0.1.0
     */
    @Override
    public @NonNull List<Win32Process> get(long timeout) {
        TerminalResult result = terminalService.executeQuery(Cimv2.WIN32_PROCESS, timeout);
        return mapper.mapToList(result.getResult(), Win32Process.class);
    }
}
