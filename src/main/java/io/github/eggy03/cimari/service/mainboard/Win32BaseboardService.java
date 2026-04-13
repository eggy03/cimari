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
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Unmodifiable;

import java.util.Collections;
import java.util.List;

/**
 * Service class for fetching mainboard/motherboard information from the system.
 * <p>
 * This class executes the {@link Cimv2#WIN32_BASEBOARD} PowerShell command
 * and maps the resulting output into a {@link Win32Baseboard} object.
 * </p>
 *
 * <h2>Usage examples</h2>
 * <pre>{@code
 * Win32BaseboardService service = new Win32BaseboardService();
 * List<Win32Baseboard> mainboardList = service.get(10);
 * }</pre>
 *
 * @since 1.0.0
 */
public class Win32BaseboardService implements CommonServiceInterface<Win32Baseboard> {


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
     * @since 1.0.0
     */
    @Override
    public @NotNull @Unmodifiable List<Win32Baseboard> get(long timeout) {
        TerminalResult result = new TerminalService().executeQuery(Cimv2.WIN32_BASEBOARD, timeout);
        return new Win32BaseboardMapper().mapToList(result.getResult(), Win32Baseboard.class);
    }
}