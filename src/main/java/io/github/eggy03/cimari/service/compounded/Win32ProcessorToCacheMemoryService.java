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
import io.github.eggy03.cimari.shell.script.ScriptUtility;
import io.github.eggy03.cimari.terminal.TerminalService;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Unmodifiable;

import java.util.List;

/**
 * Service class for fetching processor and related cache information from the system.
 * <p>
 * This class executes the {@link ScriptEnum#WIN32_PROCESSOR_TO_CACHE_MEMORY} script
 * and maps the resulting JSON into an immutable list of {@link Win32ProcessorToCacheMemory} objects.
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
@Slf4j
public class Win32ProcessorToCacheMemoryService implements CommonServiceInterface<Win32ProcessorToCacheMemory> {

    /**
     * Retrieves an immutable list of processors and related cache information
     * using an isolated PowerShell process with a configurable timeout.
     * <p>
     * Each invocation creates an isolated PowerShell process, which is
     * pre-maturely terminated if execution exceeds the specified timeout.
     * </p>
     *
     * @param timeout the maximum time (in seconds) to wait for the PowerShell
     *                command to complete before terminating the process
     * @return an immutable list of {@link Win32ProcessorToCacheMemory} objects
     * Returns an empty list if no processors and related cache information are detected.
     * @since 1.0.0
     */
    @Override
    public @NotNull @Unmodifiable List<Win32ProcessorToCacheMemory> get(long timeout) {

        String command = ScriptUtility.loadScript(ScriptEnum.WIN32_PROCESSOR_TO_CACHE_MEMORY.getScriptPath());
        String response = TerminalService.executeCommand(command, timeout);
        log.trace("PowerShell response for the apache terminal session: \n{}", response);
        return new Win32ProcessorToCacheMemoryMapper().mapToList(response, Win32ProcessorToCacheMemory.class);
    }
}
