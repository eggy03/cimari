/*
 * SPDX-License-Identifier: MIT
 * SPDX-FileCopyrightText: 2025 The ferrumx-windows contributors
 * SPDX-FileCopyrightText: 2026 Cimari contributors
 */
package io.github.eggy03.cimari.service.processor;

import io.github.eggy03.cimari.entity.processor.Win32AssociatedProcessorMemory;
import io.github.eggy03.cimari.entity.processor.Win32CacheMemory;
import io.github.eggy03.cimari.entity.processor.Win32Processor;
import io.github.eggy03.cimari.mapping.processor.Win32AssociatedProcessorMemoryMapper;
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
 * Service class for fetching the association between a Processor, and it's Cache information from the system.
 * <p>
 * This class executes the {@link Cimv2#WIN32_ASSOCIATED_PROCESSOR_MEMORY} PowerShell command
 * and maps the resulting output into an unmodifiable {@link List} of {@link Win32AssociatedProcessorMemory} objects.
 * </p>
 *
 * <h2>Usage examples</h2>
 * <pre>{@code
 * Win32AssociatedProcessorMemoryService service = new Win32AssociatedProcessorMemoryService();
 * List<Win32AssociatedProcessorMemory> apm = service.get(10);
 * }</pre>
 *
 * @since 1.0.0
 */
public class Win32AssociatedProcessorMemoryService implements CommonServiceInterface<Win32AssociatedProcessorMemory> {

    private final TerminalService terminalService;

    /**
     * Creates a {@link Win32AssociatedProcessorMemoryService} object.
     */
    public Win32AssociatedProcessorMemoryService() {
        this(new TerminalService());
    }

    /**
     * Creates a {@link  Win32AssociatedProcessorMemoryService} with the provided {@link TerminalService}.
     * <p>
     * This constructor is package private and is primarily intended for testing
     * </p>
     *
     * @param terminalService the {@link TerminalService} to use, must not be {@code null}
     * @throws NullPointerException if {@code terminalService} is {@code null}
     */
    Win32AssociatedProcessorMemoryService(TerminalService terminalService) {
        this.terminalService = Objects.requireNonNull(terminalService, "terminalService cannot be null");
    }

    /**
     * Retrieves an unmodifiable {@link List} of {@link Win32AssociatedProcessorMemory} objects
     * <p>
     * Each invocation creates an isolated PowerShell process, which is
     * pre-maturely terminated if execution exceeds the specified timeout.
     * </p>
     *
     * @param timeout maximum time (in seconds) to wait for the PowerShell
     *                command to complete before terminating the process
     * @return an unmodifiable {@link List} of {@link Win32AssociatedProcessorMemory} objects representing the association between
     * a {@link Win32Processor} and it's {@link Win32CacheMemory}.
     * Returns a {@link Collections#emptyList()} if none are detected.
     * @since 1.0.0
     */
    @Override
    public @NotNull @Unmodifiable List<Win32AssociatedProcessorMemory> get(long timeout) {
        TerminalResult result = terminalService.executeQuery(Cimv2.WIN32_ASSOCIATED_PROCESSOR_MEMORY, timeout);
        return new Win32AssociatedProcessorMemoryMapper().mapToList(result.getResult(), Win32AssociatedProcessorMemory.class);
    }
}
