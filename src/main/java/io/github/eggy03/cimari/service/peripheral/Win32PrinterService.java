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
import io.github.eggy03.cimari.utility.TerminalService;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Unmodifiable;

import java.util.List;

/**
 * Service class for fetching printer information from the system.
 * <p>
 * This class executes the {@link Cimv2#WIN32_PRINTER} PowerShell command
 * and maps the resulting JSON into an immutable list of {@link Win32Printer} objects.
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
@Slf4j
public class Win32PrinterService implements CommonServiceInterface<Win32Printer> {

    /**
     * Retrieves an immutable list of printers connected to the system
     * using an isolated PowerShell process with a configurable timeout.
     * <p>
     * Each invocation creates an isolated PowerShell process, which is
     * pre-maturely terminated if execution exceeds the specified timeout.
     * </p>
     *
     * @param timeout the maximum time (in seconds) to wait for the PowerShell
     *                command to complete before terminating the process
     * @return an immutable list of {@link Win32Printer} objects representing the system's printers.
     * If no printers are present, returns an empty list.
     * @since 1.0.0
     */
    @Override
    public @NotNull @Unmodifiable List<Win32Printer> get(long timeout) {
        String command = Cimv2.WIN32_PRINTER.getQuery();
        String response = TerminalService.executeCommand(command, timeout);
        log.trace("PowerShell response for the apache terminal session: \n{}", response);
        return new Win32PrinterMapper().mapToList(response, Win32Printer.class);
    }
}
