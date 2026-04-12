/*
 * SPDX-License-Identifier: MIT
 * SPDX-FileCopyrightText: 2025 The ferrumx-windows contributors
 * SPDX-FileCopyrightText: 2026 Cimari contributors
 */
package io.github.eggy03.cimari.service.network;

import io.github.eggy03.cimari.entity.network.Win32NetworkAdapterConfiguration;
import io.github.eggy03.cimari.mapping.network.Win32NetworkAdapterConfigurationMapper;
import io.github.eggy03.cimari.service.CommonServiceInterface;
import io.github.eggy03.cimari.shell.query.Cimv2;
import io.github.eggy03.cimari.utility.TerminalService;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Unmodifiable;

import java.util.List;

/**
 * Service class for fetching network adapter configuration information from the system.
 * <p>
 * This class executes the {@link Cimv2#WIN32_NETWORK_ADAPTER_CONFIGURATION} PowerShell command
 * and maps the resulting JSON into an immutable list of {@link Win32NetworkAdapterConfiguration} objects.
 * </p>
 *
 * <h2>Usage examples</h2>
 * <pre>{@code
 * Win32NetworkAdapterConfigurationService service = new Win32NetworkAdapterConfigurationService();
 * List<Win32NetworkAdapterConfiguration> configs = service.get(10);
 * }</pre>
 *
 * @since 1.0.0
 */
@Slf4j
public class Win32NetworkAdapterConfigurationService implements CommonServiceInterface<Win32NetworkAdapterConfiguration> {

    /**
     * Retrieves an immutable list of network adapter configurations connected to the system
     * using an isolated PowerShell process with a configurable timeout.
     * <p>
     * Each invocation creates an isolated PowerShell process, which is
     * pre-maturely terminated if execution exceeds the specified timeout.
     * </p>
     *
     * @param timeout the maximum time (in seconds) to wait for the PowerShell
     *                command to complete before terminating the process
     * @return an immutable list of {@link Win32NetworkAdapterConfiguration} objects representing the system's network adapters.
     * Returns an empty list if no configurations are detected.
     * @since 1.0.0
     */
    @Override
    public @NotNull @Unmodifiable List<Win32NetworkAdapterConfiguration> get(long timeout) {
        String command = Cimv2.WIN32_NETWORK_ADAPTER_CONFIGURATION.getQuery();
        String response = TerminalService.executeCommand(command, timeout);
        log.trace("PowerShell response for the apache terminal session: \n{}", response);
        return new Win32NetworkAdapterConfigurationMapper().mapToList(response, Win32NetworkAdapterConfiguration.class);
    }

}
