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
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Unmodifiable;

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

    private final TerminalService terminalService;

    /**
     * Creates a {@link Win32BiosService} object.
     */
    public Win32BiosService() {
        this(new TerminalService());
    }

    /**
     * Creates a {@link  Win32BiosService} with the provided {@link TerminalService}.
     * <p>
     * This constructor is package private and is primarily intended for testing
     * </p>
     *
     * @param terminalService the {@link TerminalService} to use, must not be {@code null}
     * @throws NullPointerException if {@code terminalService} is {@code null}
     */
    Win32BiosService(TerminalService terminalService) {
        this.terminalService = Objects.requireNonNull(terminalService, "terminalService cannot be null");
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
    public @NotNull @Unmodifiable List<Win32Bios> get(long timeout) {
        TerminalResult result = terminalService.executeQuery(Cimv2.WIN32_BIOS, timeout);
        return new Win32BiosMapper().mapToList(result.getResult(), Win32Bios.class);
    }
}