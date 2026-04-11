/*
 * SPDX-License-Identifier: MIT
 * SPDX-FileCopyrightText: 2025 The ferrumx-windows contributors
 * SPDX-FileCopyrightText: 2026 Cimari contributors
 */
package io.github.eggy03.cimari.service.network;

import io.github.eggy03.cimari.entity.network.MsftNetAdapter;
import io.github.eggy03.cimari.mapping.network.MsftNetAdapterMapper;
import io.github.eggy03.cimari.service.CommonServiceInterface;
import io.github.eggy03.cimari.shell.query.StandardCimv2;
import io.github.eggy03.cimari.utility.TerminalUtility;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Unmodifiable;

import java.util.List;

/**
 * Service class for fetching network adapter information from the system.
 * <p>
 * This class executes the {@link StandardCimv2#MSFT_NET_ADAPTER} PowerShell command
 * and maps the resulting JSON into an immutable list of {@link MsftNetAdapter} objects.
 * </p>
 *
 * <h2>Usage examples</h2>
 * <pre>{@code
 * MsftNetAdapterService service = new MsftNetAdapterService();
 * List<MsftNetAdapter> adapters = service.get(10);
 * }</pre>
 *
 * @since 1.0.0
 */
@Slf4j
public class MsftNetAdapterService implements CommonServiceInterface<MsftNetAdapter> {

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
     * @return an immutable list of {@link MsftNetAdapter} objects representing the system's network adapters.
     * Returns an empty list if no adapters are detected.
     * @since 1.0.0
     */
    @Override
    public @NotNull @Unmodifiable List<MsftNetAdapter> get(long timeout) {
        String command = StandardCimv2.MSFT_NET_ADAPTER.getQuery();
        String response = TerminalUtility.executeCommand(command, timeout);
        log.trace("PowerShell response for the apache terminal session: \n{}", response);
        return new MsftNetAdapterMapper().mapToList(response, MsftNetAdapter.class);
    }
}
