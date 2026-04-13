/*
 * SPDX-License-Identifier: MIT
 * SPDX-FileCopyrightText: 2025 The ferrumx-windows contributors
 * SPDX-FileCopyrightText: 2026 Cimari contributors
 */
package io.github.eggy03.cimari.service.compounded;

import io.github.eggy03.cimari.entity.compounded.Win32ProcessorToCacheMemory;
import io.github.eggy03.cimari.mapping.compounded.Win32ProcessorToCacheMemoryMapper;
import io.github.eggy03.cimari.service.CommonServiceInterface;
import io.github.eggy03.cimari.service.processor.Win32AssociatedProcessorMemoryService;
import io.github.eggy03.cimari.service.processor.Win32CacheMemoryService;
import io.github.eggy03.cimari.service.processor.Win32ProcessorService;
import io.github.eggy03.cimari.shell.script.ScriptEnum;
import io.github.eggy03.cimari.terminal.TerminalResult;
import io.github.eggy03.cimari.terminal.TerminalService;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Unmodifiable;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * Service class for fetching processor and related cache information from the system.
 * <p>
 * This class executes the {@link ScriptEnum#WIN32_PROCESSOR_TO_CACHE_MEMORY} script
 * and maps the resulting output into an unmodifiable {@link List} of {@link Win32ProcessorToCacheMemory} objects.
 * </p>
 *
 * <h2>Usage examples</h2>
 * <pre>{@code
 * Win32ProcessorToCacheMemoryService service = new Win32ProcessorToCacheMemoryService();
 * List<Win32ProcessorToCacheMemory> procAndCacheList = service.get(10);
 * }</pre>
 *
 * @see Win32AssociatedProcessorMemoryService
 * @see Win32ProcessorService
 * @see Win32CacheMemoryService
 * @since 1.0.0
 */
public class Win32ProcessorToCacheMemoryService implements CommonServiceInterface<Win32ProcessorToCacheMemory> {

    private final TerminalService terminalService;

    /**
     * Creates a {@link Win32ProcessorToCacheMemoryService} object.
     */
    public Win32ProcessorToCacheMemoryService() {
        this(new TerminalService());
    }

    /**
     * Creates a {@link  Win32ProcessorToCacheMemoryService} with the provided {@link TerminalService}.
     * <p>
     * This constructor is package private and is primarily intended for testing
     * </p>
     *
     * @param terminalService the {@link TerminalService} to use, must not be {@code null}
     * @throws NullPointerException if {@code terminalService} is {@code null}
     */
    Win32ProcessorToCacheMemoryService(TerminalService terminalService) {
        this.terminalService = Objects.requireNonNull(terminalService, "terminalService cannot be null");
    }

    /**
     * Retrieves an unmodifiable {@link List} of {@link Win32ProcessorToCacheMemory} objects
     * <p>
     * Each invocation creates an isolated PowerShell process, which is
     * pre-maturely terminated if execution exceeds the specified timeout.
     * </p>
     *
     * @param timeout maximum time (in seconds) to wait for the PowerShell
     *                command to complete before terminating the process
     * @return an unmodifiable {@link List} of {@link Win32ProcessorToCacheMemory} objects
     * Returns a {@link Collections#emptyList()} if no processors and related cache information are detected.
     * @since 1.0.0
     */
    @Override
    public @NotNull @Unmodifiable List<Win32ProcessorToCacheMemory> get(long timeout) {
        TerminalResult result = terminalService.executeScript(ScriptEnum.WIN32_PROCESSOR_TO_CACHE_MEMORY, timeout);
        return new Win32ProcessorToCacheMemoryMapper().mapToList(result.getResult(), Win32ProcessorToCacheMemory.class);
    }
}
