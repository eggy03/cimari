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
import io.github.eggy03.cimari.terminal.TerminalResult;
import io.github.eggy03.cimari.terminal.TerminalService;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Unmodifiable;

import java.util.Collections;
import java.util.List;

/**
 * Service class for fetching network adapter information from the system.
 * <p>
 * This class executes the {@link Cimv2#WIN32_NETWORK_ADAPTER} PowerShell command
 * and maps the resulting output into an unmodifiable {@link List} of {@link Win32NetworkAdapter} objects.
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
public class Win32NetworkAdapterService implements CommonServiceInterface<Win32NetworkAdapter> {

    /**
     * Retrieves an unmodifiable {@link List} of n{@link Win32NetworkAdapter} objects
     * <p>
     * Each invocation creates an isolated PowerShell process, which is
     * pre-maturely terminated if execution exceeds the specified timeout.
     * </p>
     *
     * @param timeout maximum time (in seconds) to wait for the PowerShell
     *                command to complete before terminating the process
     * @return an unmodifiable {@link List} of {@link Win32NetworkAdapter} objects representing the system's network adapters.
     * Returns a {@link Collections#emptyList()} if no adapters are detected.
     * @since 1.0.0
     */
    @Override
    public @NotNull @Unmodifiable List<Win32NetworkAdapter> get(long timeout) {
        TerminalResult result = new TerminalService().executeQuery(Cimv2.WIN32_NETWORK_ADAPTER, timeout);
        return new Win32NetworkAdapterMapper().mapToList(result.getResult(), Win32NetworkAdapter.class);
    }
}
