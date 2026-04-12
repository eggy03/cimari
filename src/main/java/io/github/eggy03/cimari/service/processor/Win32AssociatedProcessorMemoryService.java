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
import io.github.eggy03.cimari.terminal.TerminalService;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Unmodifiable;

import java.util.List;

/**
 * Service class for fetching the association between a Processor, and it's Cache information from the system.
 * <p>
 * This class executes the {@link Cimv2#WIN32_ASSOCIATED_PROCESSOR_MEMORY} PowerShell command
 * and maps the resulting JSON into an immutable list of {@link Win32AssociatedProcessorMemory} objects.
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
@Slf4j
public class Win32AssociatedProcessorMemoryService implements CommonServiceInterface<Win32AssociatedProcessorMemory> {

    /**
     * Retrieves an immutable list of {@link Win32AssociatedProcessorMemory} entities
     * using an isolated PowerShell process with a configurable timeout.
     * <p>
     * Each invocation creates an isolated PowerShell process, which is
     * pre-maturely terminated if execution exceeds the specified timeout.
     * </p>
     *
     * @param timeout the maximum time (in seconds) to wait for the PowerShell
     *                command to complete before terminating the process
     * @return an immutable list of {@link Win32AssociatedProcessorMemory} objects representing the association between
     * a {@link Win32Processor} and it's {@link Win32CacheMemory}.
     * Returns an empty list if none are detected.
     * @since 1.0.0
     */
    @Override
    public @NotNull @Unmodifiable List<Win32AssociatedProcessorMemory> get(long timeout) {
        String command = Cimv2.WIN32_ASSOCIATED_PROCESSOR_MEMORY.getQuery();
        String response = TerminalService.executeCommand(command, timeout);
        log.trace("PowerShell response for the apache terminal session: \n{}", response);
        return new Win32AssociatedProcessorMemoryMapper().mapToList(response, Win32AssociatedProcessorMemory.class);
    }
}
