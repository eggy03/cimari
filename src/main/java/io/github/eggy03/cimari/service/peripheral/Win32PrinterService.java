/*
 * SPDX-License-Identifier: MIT
 * SPDX-FileCopyrightText: 2025 The ferrumx-windows contributors
 * SPDX-FileCopyrightText: 2026 Cimari contributors
 */
package io.github.eggy03.cimari.service.peripheral;

import io.github.eggy03.cimari.entity.peripheral.Win32Printer;
import io.github.eggy03.cimari.mapping.peripheral.Win32PrinterMapper;
import io.github.eggy03.cimari.service.CommonServiceInterface;
import io.github.eggy03.cimari.shell.query.Cimv2;
import io.github.eggy03.cimari.terminal.TerminalResult;
import io.github.eggy03.cimari.terminal.TerminalService;
import org.jspecify.annotations.NonNull;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * Service class for fetching printer information from the system.
 * <p>
 * This class executes the {@link Cimv2#WIN32_PRINTER} PowerShell command
 * and maps the resulting output into an unmodifiable {@link List} of {@link Win32Printer} objects.
 * </p>
 *
 * <h2>Usage examples</h2>
 * <pre>{@code
 * Win32PrinterService service = new Win32PrinterService();
 * List<Win32Printer> printers = service.get(10);
 * }</pre>
 *
 * @since 1.0.0
 */
public class Win32PrinterService implements CommonServiceInterface<Win32Printer> {

    private final TerminalService terminalService;
    private final Win32PrinterMapper mapper;

    /**
     * Creates {@link Win32PrinterService} with default configuration.
     *
     * @since 1.0.0
     */
    public Win32PrinterService() {
        this(new TerminalService(), new Win32PrinterMapper());
    }

    /**
     * Package Private constructor with injectable dependencies
     *
     * @param terminalService the {@link TerminalService} instance to use, must not be {@code null}
     * @param mapper          the mapper instance to use, must not be {@code null}
     * @since 1.0.0
     */
    Win32PrinterService(TerminalService terminalService, Win32PrinterMapper mapper) {
        this.terminalService = Objects.requireNonNull(terminalService, "terminalService cannot be null");
        this.mapper = Objects.requireNonNull(mapper, "mapper cannot be null");
    }

    /**
     * Retrieves an unmodifiable {@link List} of {@link Win32Printer} objects
     * <p>
     * Each invocation creates an isolated PowerShell process, which is
     * pre-maturely terminated if execution exceeds the specified timeout.
     * </p>
     *
     * @param timeout maximum time (in seconds) to wait for the PowerShell
     *                command to complete before terminating the process
     * @return an unmodifiable {@link List} of {@link Win32Printer} objects representing the system's printers.
     * If no printers are present, returns a {@link Collections#emptyList()}.
     * @since 1.0.0
     */
    @Override
    public @NonNull List<Win32Printer> get(long timeout) {
        TerminalResult result = terminalService.executeQuery(Cimv2.WIN32_PRINTER, timeout);
        return mapper.mapToList(result.getResult(), Win32Printer.class);
    }
}
