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
import io.github.eggy03.cimari.utility.TerminalUtility;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Unmodifiable;

import java.util.List;

/**
 * Service class for fetching operating system information from the system.
 * <p>
 * This class executes the {@link Cimv2#WIN32_OPERATING_SYSTEM} PowerShell command
 * and maps the resulting JSON into an immutable list of {@link Win32OperatingSystem} objects.
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
@Slf4j
public class Win32OperatingSystemService implements CommonServiceInterface<Win32OperatingSystem> {

    /**
     * Retrieves an immutable list of operating systems installed in the system
     * using an isolated PowerShell process with a configurable timeout.
     * <p>
     * Each invocation creates an isolated PowerShell process, which is
     * pre-maturely terminated if execution exceeds the specified timeout.
     * </p>
     *
     * @param timeout the maximum time (in seconds) to wait for the PowerShell
     *                command to complete before terminating the process
     * @return an immutable list of {@link Win32OperatingSystem} objects representing the system's operating systems.
     * Returns an empty list if none are detected.
     * @since 1.0.0
     */
    @Override
    public @NotNull @Unmodifiable List<Win32OperatingSystem> get(long timeout) {
        String command = Cimv2.WIN32_OPERATING_SYSTEM.getQuery();
        String response = TerminalUtility.executeCommand(command, timeout);
        log.trace("PowerShell response for the apache terminal session: \n{}", response);
        return new Win32OperatingSystemMapper().mapToList(response, Win32OperatingSystem.class);
    }
}
