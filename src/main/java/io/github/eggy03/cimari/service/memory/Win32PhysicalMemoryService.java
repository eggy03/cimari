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
import io.github.eggy03.cimari.utility.TerminalService;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Unmodifiable;

import java.util.List;

/**
 * Service class for fetching information about physical memory modules (RAM) in the system.
 * <p>
 * This class executes the {@link Cimv2#WIN32_PHYSICAL_MEMORY} PowerShell command
 * and maps the resulting JSON into an immutable list of {@link Win32PhysicalMemory} objects.
 * </p>
 *
 * <h2>Usage examples</h2>
 * <pre>{@code
 * Win32PhysicalMemoryService service = new Win32PhysicalMemoryService();
 * List<Win32PhysicalMemory> memories = service.get(10);
 * }</pre>
 *
 * @since 1.0.0
 */
@Slf4j
public class Win32PhysicalMemoryService implements CommonServiceInterface<Win32PhysicalMemory> {

    /**
     * Retrieves an immutable list of physical memory modules connected to the system
     * using an isolated PowerShell process with a configurable timeout.
     * <p>
     * Each invocation creates an isolated PowerShell process, which is
     * pre-maturely terminated if execution exceeds the specified timeout.
     * </p>
     *
     * @param timeout the maximum time (in seconds) to wait for the PowerShell
     *                command to complete before terminating the process
     * @return an immutable list of {@link Win32PhysicalMemory} objects representing the system's RAM.
     * Returns an empty list if no memory modules are detected.
     * @since 1.0.0
     */
    @Override
    public @NotNull @Unmodifiable List<Win32PhysicalMemory> get(long timeout) {
        String command = Cimv2.WIN32_PHYSICAL_MEMORY.getQuery();
        String response = TerminalService.executeCommand(command, timeout);
        log.trace("PowerShell response for the apache terminal session: \n{}", response);
        return new Win32PhysicalMemoryMapper().mapToList(response, Win32PhysicalMemory.class);
    }

}