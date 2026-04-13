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
import io.github.eggy03.cimari.terminal.TerminalResult;
import io.github.eggy03.cimari.terminal.TerminalService;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Unmodifiable;

import java.util.Collections;
import java.util.List;

/**
 * Service class for fetching CPU information from the system.
 * <p>
 * This class executes the {@link Cimv2#WIN32_PROCESSOR} PowerShell command
 * and maps the resulting output into {@link Win32Processor} objects.
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
public class Win32ProcessorService implements CommonServiceInterface<Win32Processor> {

    /**
     * Retrieves an unmodifiable {@link List} {@link Win32Processor} objects
     * <p>
     * Each invocation creates an isolated PowerShell process, which is
     * pre-maturely terminated if execution exceeds the specified timeout.
     * </p>
     *
     * @param timeout maximum time (in seconds) to wait for the PowerShell
     *                command to complete before terminating the process
     * @return an unmodifiable {@link List} of {@link Win32Processor} objects representing the CPU(s).
     * Returns a {@link Collections#emptyList()} if no processors are detected.
     * @since 1.0.0
     */
    @Override
    public @NotNull @Unmodifiable List<Win32Processor> get(long timeout) {
        TerminalResult result = new TerminalService().executeQuery(Cimv2.WIN32_PROCESSOR, timeout);
        return new Win32ProcessorMapper().mapToList(result.getResult(), Win32Processor.class);
    }
}
