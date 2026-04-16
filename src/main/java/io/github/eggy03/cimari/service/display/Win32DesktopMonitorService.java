/*
 * SPDX-License-Identifier: MIT
 * SPDX-FileCopyrightText: 2025 The ferrumx-windows contributors
 * SPDX-FileCopyrightText: 2026 Cimari contributors
 */
package io.github.eggy03.cimari.service.display;

import io.github.eggy03.cimari.entity.display.Win32DesktopMonitor;
import io.github.eggy03.cimari.mapping.display.Win32DesktopMonitorMapper;
import io.github.eggy03.cimari.service.CommonServiceInterface;
import io.github.eggy03.cimari.shell.query.Cimv2;
import io.github.eggy03.cimari.terminal.TerminalResult;
import io.github.eggy03.cimari.terminal.TerminalService;
import org.jspecify.annotations.NonNull;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * Service class for fetching monitor information from the system.
 * <p>
 * This class executes the {@link Cimv2#WIN32_DESKTOP_MONITOR} PowerShell command
 * and maps the resulting output into an unmodifiable {@link List} of {@link Win32DesktopMonitor} objects.
 * </p>
 *
 * <h2>Usage examples</h2>
 * <pre>{@code
 * Win32DesktopMonitorService service = new Win32DesktopMonitorService();
 * List<Win32DesktopMonitor> monitors = service.get(10);
 * }</pre>
 *
 * @since 1.0.0
 */
public class Win32DesktopMonitorService implements CommonServiceInterface<Win32DesktopMonitor> {

    private final TerminalService terminalService;
    private final Win32DesktopMonitorMapper mapper;

    /**
     * Creates {@link Win32DesktopMonitorService} with default configuration.
     *
     * @since 1.0.0
     */
    public Win32DesktopMonitorService() {
        this(new TerminalService(), new Win32DesktopMonitorMapper());
    }

    /**
     * Package Private constructor with injectable dependencies
     *
     * @param terminalService the {@link TerminalService} instance to use, must not be {@code null}
     * @param mapper          the mapper instance to use, must not be {@code null}
     * @since 1.0.0
     */
    Win32DesktopMonitorService(TerminalService terminalService, Win32DesktopMonitorMapper mapper) {
        this.terminalService = Objects.requireNonNull(terminalService, "terminalService cannot be null");
        this.mapper = Objects.requireNonNull(mapper, "mapper cannot be null");
    }

    /**
     * Retrieves an unmodifiable {@link List} of {@link Win32DesktopMonitor} objects
     * <p>
     * Each invocation creates an isolated PowerShell process, which is
     * pre-maturely terminated if execution exceeds the specified timeout.
     * </p>
     *
     * @param timeout maximum time (in seconds) to wait for the PowerShell
     *                command to complete before terminating the process
     * @return an unmodifiable {@link List} of {@link Win32DesktopMonitor} objects representing connected monitors.
     * Returns a {@link Collections#emptyList()} if no monitors are detected.
     * @since 1.0.0
     */
    @Override
    public @NonNull List<Win32DesktopMonitor> get(long timeout) {
        TerminalResult result = terminalService.executeQuery(Cimv2.WIN32_DESKTOP_MONITOR, timeout);
        return mapper.mapToList(result.getResult(), Win32DesktopMonitor.class);
    }
}
