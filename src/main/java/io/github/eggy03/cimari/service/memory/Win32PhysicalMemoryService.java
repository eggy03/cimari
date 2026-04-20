/*
 * SPDX-License-Identifier: MIT
 * SPDX-FileCopyrightText: 2025 The ferrumx-windows contributors
 * SPDX-FileCopyrightText: 2026 Cimari contributors
 */
package io.github.eggy03.cimari.service.memory;

import io.github.eggy03.cimari.entity.memory.Win32PhysicalMemory;
import io.github.eggy03.cimari.mapping.memory.Win32PhysicalMemoryMapper;
import io.github.eggy03.cimari.service.CommonServiceInterface;
import io.github.eggy03.cimari.shell.query.Cimv2;
import io.github.eggy03.cimari.terminal.TerminalResult;
import io.github.eggy03.cimari.terminal.TerminalService;
import org.jspecify.annotations.NonNull;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * Service class for fetching information about physical memory modules (RAM) in the system.
 * <p>
 * This class executes the {@link Cimv2#WIN32_PHYSICAL_MEMORY} PowerShell command
 * and maps the resulting output into an unmodifiable {@link List} of {@link Win32PhysicalMemory} objects.
 * </p>
 *
 * <h2>Usage examples</h2>
 * <pre>{@code
 * Win32PhysicalMemoryService service = new Win32PhysicalMemoryService();
 * List<Win32PhysicalMemory> memories = service.get(10);
 * }</pre>
 *
 * @since 0.1.0
 */
public class Win32PhysicalMemoryService implements CommonServiceInterface<Win32PhysicalMemory> {

    private final @NonNull TerminalService terminalService;
    private final @NonNull Win32PhysicalMemoryMapper mapper;

    /**
     * Creates {@link Win32PhysicalMemoryService} with default configuration.
     *
     * @since 0.1.0
     */
    public Win32PhysicalMemoryService() {
        this(new TerminalService(), new Win32PhysicalMemoryMapper());
    }

    /**
     * Package Private constructor with injectable dependencies
     *
     * @param terminalService the {@link TerminalService} instance to use, must not be {@code null}
     * @param mapper          the mapper instance to use, must not be {@code null}
     * @since 0.1.0
     */
    Win32PhysicalMemoryService(TerminalService terminalService, Win32PhysicalMemoryMapper mapper) {
        this.terminalService = Objects.requireNonNull(terminalService, "terminalService cannot be null");
        this.mapper = Objects.requireNonNull(mapper, "mapper cannot be null");
    }

    /**
     * Retrieves an unmodifiable {@link List} of {@link Win32PhysicalMemory} objects
     * <p>
     * Each invocation creates an isolated PowerShell process, which is
     * pre-maturely terminated if execution exceeds the specified timeout.
     * </p>
     *
     * @param timeout maximum time (in seconds) to wait for the PowerShell
     *                command to complete before terminating the process
     * @return an unmodifiable {@link List} of {@link Win32PhysicalMemory} objects representing the system's RAM.
     * Returns a {@link Collections#emptyList()} if no memory modules are detected.
     * @since 0.1.0
     */
    @Override
    public @NonNull List<Win32PhysicalMemory> get(long timeout) {
        TerminalResult result = terminalService.executeQuery(Cimv2.WIN32_PHYSICAL_MEMORY, timeout);
        return mapper.mapToList(result.getResult(), Win32PhysicalMemory.class);
    }

}