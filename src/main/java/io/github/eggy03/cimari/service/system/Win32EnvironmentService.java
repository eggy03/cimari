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
import io.github.eggy03.cimari.utility.TerminalUtility;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Unmodifiable;

import java.util.List;

/**
 * Service class for fetching information about environment variables in a Windows System.
 * <p>
 * This class executes the {@link Cimv2#WIN32_ENVIRONMENT} PowerShell command
 * and maps the resulting JSON into an immutable list of {@link Win32Environment} objects.
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
@Slf4j
public class Win32EnvironmentService implements CommonServiceInterface<Win32Environment> {

    /**
     * Retrieves an immutable list of env variables
     * using an isolated PowerShell process with a configurable timeout.
     * <p>
     * Each invocation creates an isolated PowerShell process, which is
     * pre-maturely terminated if execution exceeds the specified timeout.
     * </p>
     *
     * @param timeout the maximum time (in seconds) to wait for the PowerShell
     *                command to complete before terminating the process
     * @return an immutable list of {@link Win32Environment} objects representing the env variables.
     * Returns an empty list if no env variables are detected.
     * @since 1.0.0
     */
    @Override
    public @NotNull @Unmodifiable List<Win32Environment> get(long timeout) {
        String command = Cimv2.WIN32_ENVIRONMENT.getQuery();
        String response = TerminalUtility.executeCommand(command, timeout);
        log.trace("PowerShell response for the apache terminal session: \n{}", response);
        return new Win32EnvironmentMapper().mapToList(response, Win32Environment.class);
    }
}
