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
import io.github.eggy03.cimari.terminal.TerminalResult;
import io.github.eggy03.cimari.terminal.TerminalService;
import org.jspecify.annotations.NonNull;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * Service class for fetching network adapter information from the system.
 * <p>
 * This class executes the {@link StandardCimv2#MSFT_NET_ADAPTER} PowerShell command
 * and maps the resulting output into an unmodifiable {@link List} of {@link MsftNetAdapter} objects.
 * </p>
 *
 * <h2>Usage examples</h2>
 * <pre>{@code
 * MsftNetAdapterService service = new MsftNetAdapterService();
 * List<MsftNetAdapter> adapters = service.get(10);
 * }</pre>
 *
 * @since 0.1.0
 */
public class MsftNetAdapterService implements CommonServiceInterface<MsftNetAdapter> {

    private final @NonNull TerminalService terminalService;
    private final @NonNull MsftNetAdapterMapper mapper;

    /**
     * Creates {@link MsftNetAdapterService} with default configuration.
     *
     * @since 0.1.0
     */
    public MsftNetAdapterService() {
        this(new TerminalService(), new MsftNetAdapterMapper());
    }

    /**
     * Package Private constructor with injectable dependencies
     *
     * @param terminalService the {@link TerminalService} instance to use, must not be {@code null}
     * @param mapper          the mapper instance to use, must not be {@code null}
     * @since 0.1.0
     */
    MsftNetAdapterService(TerminalService terminalService, MsftNetAdapterMapper mapper) {
        this.terminalService = Objects.requireNonNull(terminalService, "terminalService cannot be null");
        this.mapper = Objects.requireNonNull(mapper, "mapper cannot be null");
    }

    /**
     * Retrieves an unmodifiable {@link List} of {@link MsftNetAdapter} objects
     * <p>
     * Each invocation creates an isolated PowerShell process, which is
     * pre-maturely terminated if execution exceeds the specified timeout.
     * </p>
     *
     * @param timeout maximum time (in seconds) to wait for the PowerShell
     *                command to complete before terminating the process
     * @return an unmodifiable {@link List} of {@link MsftNetAdapter} objects representing the system's network adapters.
     * Returns a {@link Collections#emptyList()} if no adapters are detected.
     * @since 0.1.0
     */
    @Override
    public @NonNull List<MsftNetAdapter> get(long timeout) {
        TerminalResult result = terminalService.executeQuery(StandardCimv2.MSFT_NET_ADAPTER, timeout);
        return mapper.mapToList(result.getResult(), MsftNetAdapter.class);
    }
}
