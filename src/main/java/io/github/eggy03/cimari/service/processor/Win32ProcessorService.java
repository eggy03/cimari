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
import org.jspecify.annotations.NonNull;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

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

    private final TerminalService terminalService;
    private final Win32ProcessorMapper mapper;

    /**
     * Creates {@link Win32ProcessorService} with default configuration.
     *
     * @since 1.0.0
     */
    public Win32ProcessorService() {
        this(new TerminalService(), new Win32ProcessorMapper());
    }

    /**
     * Package Private constructor with injectable dependencies
     *
     * @param terminalService the {@link TerminalService} instance to use, must not be {@code null}
     * @param mapper          the mapper instance to use, must not be {@code null}
     * @since 1.0.0
     */
    Win32ProcessorService(TerminalService terminalService, Win32ProcessorMapper mapper) {
        this.terminalService = Objects.requireNonNull(terminalService, "terminalService cannot be null");
        this.mapper = Objects.requireNonNull(mapper, "mapper cannot be null");
    }

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
    public @NonNull List<Win32Processor> get(long timeout) {
        TerminalResult result = terminalService.executeQuery(Cimv2.WIN32_PROCESSOR, timeout);
        return mapper.mapToList(result.getResult(), Win32Processor.class);
    }
}
