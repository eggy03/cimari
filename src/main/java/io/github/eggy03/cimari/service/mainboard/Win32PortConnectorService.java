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
import io.github.eggy03.cimari.terminal.TerminalResult;
import io.github.eggy03.cimari.terminal.TerminalService;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Unmodifiable;

import java.util.Collections;
import java.util.List;

/**
 * Service class for fetching mainboard port information from the system.
 * <p>
 * This class executes the {@link Cimv2#WIN32_PORT_CONNECTOR} PowerShell command
 * and maps the resulting output into an unmodifiable {@link List} of {@link Win32PortConnector} objects.
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
public class Win32PortConnectorService implements CommonServiceInterface<Win32PortConnector> {

    /**
     * Retrieves an unmodifiable {@link List} of {@link Win32PortConnector} objects
     * <p>
     * Each invocation creates an isolated PowerShell process, which is
     * pre-maturely terminated if execution exceeds the specified timeout.
     * </p>
     *
     * @param timeout maximum time (in seconds) to wait for the PowerShell
     *                command to complete before terminating the process
     * @return an unmodifiable {@link List} of {@link Win32PortConnector} objects representing the system's mainboard ports.
     * Returns a {@link Collections#emptyList()} if no ports are detected.
     * @since 1.0.0
     */
    @Override
    public @NotNull @Unmodifiable List<Win32PortConnector> get(long timeout) {
        TerminalResult result = new TerminalService().executeQuery(Cimv2.WIN32_PORT_CONNECTOR, timeout);
        return new Win32PortConnectorMapper().mapToList(result.getResult(), Win32PortConnector.class);
    }
}
