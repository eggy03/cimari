/*
 * SPDX-License-Identifier: MIT
 * SPDX-FileCopyrightText: 2025 The ferrumx-windows contributors
 * SPDX-FileCopyrightText: 2026 Cimari contributors
 */
package io.github.eggy03.cimari.service.processor;

import io.github.eggy03.cimari.entity.processor.Win32CacheMemory;
import io.github.eggy03.cimari.mapping.processor.Win32CacheMemoryMapper;
import io.github.eggy03.cimari.service.CommonServiceInterface;
import io.github.eggy03.cimari.shell.query.Cimv2;
import io.github.eggy03.cimari.terminal.TerminalResult;
import io.github.eggy03.cimari.terminal.TerminalService;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Unmodifiable;

import java.util.Collections;
import java.util.List;

/**
 * Service class for fetching processor cache information from the system.
 * <p>
 * This class executes the {@link Cimv2#WIN32_CACHE_MEMORY} PowerShell command
 * and maps the resulting output into an unmodifiable {@link List} of {@link Win32CacheMemory} objects.
 * </p>
 *
 * <h2>Usage examples</h2>
 * <pre>{@code
 * Win32CacheMemoryService service = new Win32CacheMemoryService();
 * List<Win32CacheMemory> caches = service.get(10);
 * }</pre>
 *
 * @since 1.0.0
 */
public class Win32CacheMemoryService implements CommonServiceInterface<Win32CacheMemory> {

    /**
     * Retrieves an unmodifiable {@link List} of {@link Win32CacheMemory} objects
     * <p>
     * Each invocation creates an isolated PowerShell process, which is
     * pre-maturely terminated if execution exceeds the specified timeout.
     * </p>
     *
     * @param timeout maximum time (in seconds) to wait for the PowerShell
     *                command to complete before terminating the process
     * @return an unmodifiable {@link List} of {@link Win32CacheMemory} objects representing the CPU caches.
     * Returns a {@link Collections#emptyList()} if none are detected.
     * @since 1.0.0
     */
    @Override
    public @NotNull @Unmodifiable List<Win32CacheMemory> get(long timeout) {
        TerminalResult result = new TerminalService().executeQuery(Cimv2.WIN32_CACHE_MEMORY, timeout);
        return new Win32CacheMemoryMapper().mapToList(result.getResult(), Win32CacheMemory.class);
    }
}
