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
import io.github.eggy03.cimari.utility.TerminalUtility;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Unmodifiable;

import java.util.List;

/**
 * Service class for fetching mainboard/motherboard information from the system.
 * <p>
 * This class executes the {@link Cimv2#WIN32_BASEBOARD} PowerShell command
 * and maps the resulting JSON into a {@link Win32Baseboard} object.
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
@Slf4j
public class Win32BaseboardService implements CommonServiceInterface<Win32Baseboard> {


    /**
     * Retrieves an immutable list of motherboard entries present in the system
     * using an isolated PowerShell process with a configurable timeout.
     * <p>
     * Each invocation creates an isolated PowerShell process, which is
     * pre-maturely terminated if execution exceeds the specified timeout.
     * </p>
     *
     * @param timeout the maximum time (in seconds) to wait for the PowerShell
     *                command to complete before terminating the process
     * @return an immutable list of {@link Win32Baseboard} objects representing the system motherboards.
     * Returns an empty list if no motherboard entries are detected.
     * @since 1.0.0
     */
    @Override
    public @NotNull @Unmodifiable List<Win32Baseboard> get(long timeout) {
        String command = Cimv2.WIN32_BASEBOARD.getQuery();
        String response = TerminalUtility.executeCommand(command, timeout);
        log.trace("PowerShell response for the apache terminal session: \n{}", response);
        return new Win32BaseboardMapper().mapToList(response, Win32Baseboard.class);
    }
}