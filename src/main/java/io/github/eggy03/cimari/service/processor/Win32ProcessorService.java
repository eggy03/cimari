/*
 * SPDX-License-Identifier: MIT
 * SPDX-FileCopyrightText: 2025 The ferrumx-windows contributors
 * SPDX-FileCopyrightText: 2026 Cimari contributors
 */
package io.github.eggy03.cimari.service.processor;

import io.github.eggy03.cimari.entity.processor.Win32Processor;
import io.github.eggy03.cimari.mapping.processor.Win32ProcessorMapper;
import io.github.eggy03.cimari.service.CommonServiceInterface;
import io.github.eggy03.cimari.shell.query.Cimv2;
import io.github.eggy03.cimari.utility.TerminalUtility;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Unmodifiable;

import java.util.List;

/**
 * Service class for fetching CPU information from the system.
 * <p>
 * This class executes the {@link Cimv2#WIN32_PROCESSOR} PowerShell command
 * and maps the resulting JSON into {@link Win32Processor} objects.
 * </p>
 *
 * <h2>Usage examples</h2>
 * <pre>{@code
 * Win32ProcessorServiceservice = new Win32ProcessorService();
 * List<Win32Processor> processors = service.get(10);
 * }</pre>
 *
 * @since 1.0.0
 */
@Slf4j
public class Win32ProcessorService implements CommonServiceInterface<Win32Processor> {

    /**
     * Retrieves an immutable list of processor entries
     * using an isolated PowerShell process with a configurable timeout.
     * <p>
     * Each invocation creates an isolated PowerShell process, which is
     * pre-maturely terminated if execution exceeds the specified timeout.
     * </p>
     *
     * @param timeout the maximum time (in seconds) to wait for the PowerShell
     *                command to complete before terminating the process
     * @return an immutable list of {@link Win32Processor} objects representing the CPU(s).
     * Returns an empty list if no processors are detected.
     * @since 1.0.0
     */
    @Override
    public @NotNull @Unmodifiable List<Win32Processor> get(long timeout) {
        String command = Cimv2.WIN32_PROCESSOR.getQuery();
        String response = TerminalUtility.executeCommand(command, timeout);
        log.trace("PowerShell response for the apache terminal session: \n{}", response);
        return new Win32ProcessorMapper().mapToList(response, Win32Processor.class);
    }
}
