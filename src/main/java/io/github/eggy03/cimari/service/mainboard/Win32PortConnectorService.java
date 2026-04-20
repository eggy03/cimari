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
import org.jspecify.annotations.NonNull;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

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
 * @since 0.1.0
 */
public class Win32PortConnectorService implements CommonServiceInterface<Win32PortConnector> {

    private final @NonNull TerminalService terminalService;
    private final @NonNull Win32PortConnectorMapper mapper;

    /**
     * Creates {@link Win32PortConnectorService} with default configuration.
     *
     * @since 0.1.0
     */
    public Win32PortConnectorService() {
        this(new TerminalService(), new Win32PortConnectorMapper());
    }

    /**
     * Package Private constructor with injectable dependencies
     *
     * @param terminalService the {@link TerminalService} instance to use, must not be {@code null}
     * @param mapper          the mapper instance to use, must not be {@code null}
     * @since 0.1.0
     */
    Win32PortConnectorService(TerminalService terminalService, Win32PortConnectorMapper mapper) {
        this.terminalService = Objects.requireNonNull(terminalService, "terminalService cannot be null");
        this.mapper = Objects.requireNonNull(mapper, "mapper cannot be null");
    }

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
     * @since 0.1.0
     */
    @Override
    public @NonNull List<Win32PortConnector> get(long timeout) {
        TerminalResult result = terminalService.executeQuery(Cimv2.WIN32_PORT_CONNECTOR, timeout);
        return mapper.mapToList(result.getResult(), Win32PortConnector.class);
    }
}
