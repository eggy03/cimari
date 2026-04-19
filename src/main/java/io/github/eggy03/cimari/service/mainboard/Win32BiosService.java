/*
 * SPDX-License-Identifier: MIT
 * SPDX-FileCopyrightText: 2025 The ferrumx-windows contributors
 * SPDX-FileCopyrightText: 2026 Cimari contributors
 */
package io.github.eggy03.cimari.service.mainboard;

import io.github.eggy03.cimari.entity.mainboard.Win32Bios;
import io.github.eggy03.cimari.mapping.mainboard.Win32BiosMapper;
import io.github.eggy03.cimari.service.CommonServiceInterface;
import io.github.eggy03.cimari.shell.query.Cimv2;
import io.github.eggy03.cimari.terminal.TerminalResult;
import io.github.eggy03.cimari.terminal.TerminalService;
import org.jspecify.annotations.NonNull;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * Service class for fetching BIOS information from the system.
 * <p>
 * This class executes the {@link Cimv2#WIN32_BIOS} PowerShell command
 * and maps the resulting output into an unmodifiable {@link List} of {@link Win32Bios} objects.
 * </p>
 *
 * <h2>Usage examples</h2>
 * <pre>{@code
 * Win32BiosService service = new Win32BiosService();
 * List<Win32Bios> biosList = service.get(10);
 * }</pre>
 *
 * @since 1.0.0
 */
public class Win32BiosService implements CommonServiceInterface<Win32Bios> {

    private final @NonNull TerminalService terminalService;
    private final @NonNull Win32BiosMapper mapper;

    /**
     * Creates {@link Win32BiosService} with default configuration.
     *
     * @since 1.0.0
     */
    public Win32BiosService() {
        this(new TerminalService(), new Win32BiosMapper());
    }

    /**
     * Package Private constructor with injectable dependencies
     *
     * @param terminalService the {@link TerminalService} instance to use, must not be {@code null}
     * @param mapper          the mapper instance to use, must not be {@code null}
     * @since 1.0.0
     */
    Win32BiosService(TerminalService terminalService, Win32BiosMapper mapper) {
        this.terminalService = Objects.requireNonNull(terminalService, "terminalService cannot be null");
        this.mapper = Objects.requireNonNull(mapper, "mapper cannot be null");
    }

    /**
     * Retrieves an unmodifiable {@link List} of {@link Win32Bios} objects
     * <p>
     * Each invocation creates an isolated PowerShell process, which is
     * pre-maturely terminated if execution exceeds the specified timeout.
     * </p>
     *
     * @param timeout maximum time (in seconds) to wait for the PowerShell
     *                command to complete before terminating the process
     * @return an unmodifiable {@link List} of {@link Win32Bios} objects representing the system BIOS.
     * Returns a {@link Collections#emptyList()} if no BIOS entries are detected.
     * @since 1.0.0
     */
    @Override
    public @NonNull List<Win32Bios> get(long timeout) {
        TerminalResult result = terminalService.executeQuery(Cimv2.WIN32_BIOS, timeout);
        return mapper.mapToList(result.getResult(), Win32Bios.class);
    }
}