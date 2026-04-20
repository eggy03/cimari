/*
 * SPDX-License-Identifier: MIT
 * SPDX-FileCopyrightText: 2025 The ferrumx-windows contributors
 * SPDX-FileCopyrightText: 2026 Cimari contributors
 */
package io.github.eggy03.cimari.service.mainboard;

import io.github.eggy03.cimari.entity.mainboard.Win32Baseboard;
import io.github.eggy03.cimari.mapping.mainboard.Win32BaseboardMapper;
import io.github.eggy03.cimari.service.CommonServiceInterface;
import io.github.eggy03.cimari.shell.query.Cimv2;
import io.github.eggy03.cimari.terminal.TerminalResult;
import io.github.eggy03.cimari.terminal.TerminalService;
import org.jspecify.annotations.NonNull;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * Service class for fetching mainboard/motherboard information from the system.
 * <p>
 * This class executes the {@link Cimv2#WIN32_BASEBOARD} PowerShell command
 * and maps the resulting output into a {@link Win32Baseboard} with default configuration.
 * </p>
 *
 * <h2>Usage examples</h2>
 * <pre>{@code
 * Win32BaseboardService service = new Win32BaseboardService();
 * List<Win32Baseboard> mainboardList = service.get(10);
 * }</pre>
 * @since 0.1.0
 */
public class Win32BaseboardService implements CommonServiceInterface<Win32Baseboard> {

    private final @NonNull TerminalService terminalService;
    private final @NonNull Win32BaseboardMapper mapper;

    /**
     * Creates {@link Win32BaseboardService} with default configuration.
     *
     * @since 0.1.0
     */
    public Win32BaseboardService() {
        this(new TerminalService(), new Win32BaseboardMapper());
    }

    /**
     * Package Private constructor with injectable dependencies
     *
     * @param terminalService the {@link TerminalService} instance to use, must not be {@code null}
     * @param mapper          the mapper instance to use, must not be {@code null}
     * @since 0.1.0
     */
    Win32BaseboardService(TerminalService terminalService, Win32BaseboardMapper mapper) {
        this.terminalService = Objects.requireNonNull(terminalService, "terminalService cannot be null");
        this.mapper = Objects.requireNonNull(mapper, "mapper cannot be null");
    }

    /**
     * Retrieves an unmodifiable {@link List} of {@link Win32Baseboard} objects
     * <p>
     * Each invocation creates an isolated PowerShell process, which is
     * pre-maturely terminated if execution exceeds the specified timeout.
     * </p>
     *
     * @param timeout maximum time (in seconds) to wait for the PowerShell
     *                command to complete before terminating the process
     * @return an unmodifiable {@link List} of {@link Win32Baseboard} objects representing the system motherboards.
     * Returns a {@link Collections#emptyList()} if no motherboard entries are detected.
     * @since 0.1.0
     */
    @Override
    public @NonNull List<Win32Baseboard> get(long timeout) {
        TerminalResult result = terminalService.executeQuery(Cimv2.WIN32_BASEBOARD, timeout);
        return mapper.mapToList(result.getResult(), Win32Baseboard.class);
    }
}