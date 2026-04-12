/*
 * SPDX-License-Identifier: MIT
 * SPDX-FileCopyrightText: 2025 The ferrumx-windows contributors
 * SPDX-FileCopyrightText: 2026 Cimari contributors
 */
package io.github.eggy03.cimari.service.network;

import io.github.eggy03.cimari.entity.network.Win32NetworkAdapter;
import io.github.eggy03.cimari.mapping.network.Win32NetworkAdapterMapper;
import io.github.eggy03.cimari.service.CommonServiceInterface;
import io.github.eggy03.cimari.shell.query.Cimv2;
import io.github.eggy03.cimari.utility.TerminalUtility;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Unmodifiable;

import java.util.List;

/**
 * Service class for fetching network adapter information from the system.
 * <p>
 * This class executes the {@link Cimv2#WIN32_NETWORK_ADAPTER} PowerShell command
 * and maps the resulting JSON into an immutable list of {@link Win32NetworkAdapter} objects.
 * </p>
 *
 * <h2>Usage examples</h2>
 * <pre>{@code
 * Win32NetworkAdapterService service = new Win32NetworkAdapterService();
 * List<Win32NetworkAdapter> adapters = service.get(10);
 * }</pre>
 *
 * @since 1.0.0
 */
@Slf4j
public class Win32NetworkAdapterService implements CommonServiceInterface<Win32NetworkAdapter> {

    /**
     * Retrieves an immutable list of network adapters connected to the system
     * using an isolated PowerShell process with a configurable timeout.
     * <p>
     * Each invocation creates an isolated PowerShell process, which is
     * pre-maturely terminated if execution exceeds the specified timeout.
     * </p>
     *
     * @param timeout the maximum time (in seconds) to wait for the PowerShell
     *                command to complete before terminating the process
     * @return an immutable list of {@link Win32NetworkAdapter} objects representing the system's network adapters.
     * Returns an empty list if no adapters are detected.
     * @since 1.0.0
     */
    @Override
    public @NotNull @Unmodifiable List<Win32NetworkAdapter> get(long timeout) {
        String command = Cimv2.WIN32_NETWORK_ADAPTER.getQuery();
        String response = TerminalUtility.executeCommand(command, timeout);
        log.trace("PowerShell response for the apache terminal session: \n{}", response);
        return new Win32NetworkAdapterMapper().mapToList(response, Win32NetworkAdapter.class);
    }
}
