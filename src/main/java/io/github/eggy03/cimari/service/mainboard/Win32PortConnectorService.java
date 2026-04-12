/*
 * SPDX-License-Identifier: MIT
 * SPDX-FileCopyrightText: 2025 The ferrumx-windows contributors
 * SPDX-FileCopyrightText: 2026 Cimari contributors
 */
package io.github.eggy03.cimari.service.mainboard;

import io.github.eggy03.cimari.entity.mainboard.Win32PortConnector;
import io.github.eggy03.cimari.mapping.mainboard.Win32PortConnectorMapper;
import io.github.eggy03.cimari.service.CommonServiceInterface;
import io.github.eggy03.cimari.shell.query.Cimv2;
import io.github.eggy03.cimari.utility.TerminalService;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Unmodifiable;

import java.util.List;

/**
 * Service class for fetching mainboard port information from the system.
 * <p>
 * This class executes the {@link Cimv2#WIN32_PORT_CONNECTOR} PowerShell command
 * and maps the resulting JSON into an immutable list of {@link Win32PortConnector} objects.
 * </p>
 *
 * <h2>Usage examples</h2>
 * <pre>{@code
 * Win32PortConnectorService service = new Win32PortConnectorService();
 * List<Win32PortConnector> ports = service.get(10);
 * }</pre>
 *
 * @since 1.0.0
 */
@Slf4j
public class Win32PortConnectorService implements CommonServiceInterface<Win32PortConnector> {

    /**
     * Retrieves an immutable list of mainboard ports present in the system
     * using an isolated PowerShell process with a configurable timeout.
     * <p>
     * Each invocation creates an isolated PowerShell process, which is
     * pre-maturely terminated if execution exceeds the specified timeout.
     * </p>
     *
     * @param timeout the maximum time (in seconds) to wait for the PowerShell
     *                command to complete before terminating the process
     * @return an immutable list of {@link Win32PortConnector} objects representing the system's mainboard ports.
     * Returns an empty list if no ports are detected.
     * @since 1.0.0
     */
    @Override
    public @NotNull @Unmodifiable List<Win32PortConnector> get(long timeout) {
        String command = Cimv2.WIN32_PORT_CONNECTOR.getQuery();
        String response = TerminalService.executeCommand(command, timeout);
        log.trace("PowerShell response for the apache terminal session: \n{}", response);
        return new Win32PortConnectorMapper().mapToList(response, Win32PortConnector.class);
    }
}
