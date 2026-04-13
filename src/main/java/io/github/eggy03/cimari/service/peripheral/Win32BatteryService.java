/*
 * SPDX-License-Identifier: MIT
 * SPDX-FileCopyrightText: 2025 The ferrumx-windows contributors
 * SPDX-FileCopyrightText: 2026 Cimari contributors
 */
package io.github.eggy03.cimari.service.peripheral;

import io.github.eggy03.cimari.entity.peripheral.Win32Battery;
import io.github.eggy03.cimari.mapping.peripheral.Win32BatteryMapper;
import io.github.eggy03.cimari.service.CommonServiceInterface;
import io.github.eggy03.cimari.shell.query.Cimv2;
import io.github.eggy03.cimari.terminal.TerminalResult;
import io.github.eggy03.cimari.terminal.TerminalService;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Unmodifiable;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * Service class for fetching battery information from the system.
 * <p>
 * This class executes the {@link Cimv2#WIN32_BATTERY} PowerShell command
 * and maps the resulting output into an unmodifiable {@link List} of {@link Win32Battery} objects.
 * </p>
 *
 * <h2>Usage examples</h2>
 * <pre>{@code
 * Win32BatteryService service = new Win32BatteryService();
 * List<Win32Battery> batteries = service.get(10);
 * }</pre>
 *
 * @since 1.0.0
 */
public class Win32BatteryService implements CommonServiceInterface<Win32Battery> {

    private final TerminalService terminalService;

    /**
     * Creates a {@link Win32BatteryService} object.
     */
    public Win32BatteryService() {
        this(new TerminalService());
    }

    /**
     * Creates a {@link  Win32BatteryService} with the provided {@link TerminalService}.
     * <p>
     * This constructor is package private and is primarily intended for testing
     * </p>
     *
     * @param terminalService the {@link TerminalService} to use, must not be {@code null}
     * @throws NullPointerException if {@code terminalService} is {@code null}
     */
    Win32BatteryService(TerminalService terminalService) {
        this.terminalService = Objects.requireNonNull(terminalService, "terminalService cannot be null");
    }

    /**
     * Retrieves an unmodifiable {@link List} of {@link Win32Battery} objects
     * <p>
     * Each invocation creates an isolated PowerShell process, which is
     * pre-maturely terminated if execution exceeds the specified timeout.
     * </p>
     *
     * @param timeout maximum time (in seconds) to wait for the PowerShell
     *                command to complete before terminating the process
     * @return an unmodifiable {@link List} of {@link Win32Battery} objects representing the system's batteries.
     * If no batteries are present, returns a {@link Collections#emptyList()}.
     * @since 1.0.0
     */
    @Override
    public @NotNull @Unmodifiable List<Win32Battery> get(long timeout) {
        TerminalResult result = terminalService.executeQuery(Cimv2.WIN32_BATTERY, timeout);
        return new Win32BatteryMapper().mapToList(result.getResult(), Win32Battery.class);
    }
}