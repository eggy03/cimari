/*
 * SPDX-License-Identifier: MIT
 * SPDX-FileCopyrightText: 2025 The ferrumx-windows contributors
 * SPDX-FileCopyrightText: 2026 Cimari contributors
 */
package io.github.eggy03.cimari.service.system;

import io.github.eggy03.cimari.entity.system.Win32PnPEntity;
import io.github.eggy03.cimari.mapping.system.Win32PnPEntityMapper;
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
 * Service class for fetching operating system information from the system.
 * <p>
 * This class executes the {@link Cimv2#WIN32_OPERATING_SYSTEM} PowerShell command
 * and maps the resulting output into an unmodifiable {@link List} of {@link Win32PnPEntity} objects.
 * </p>
 *
 * <h2>Usage examples</h2>
 * <pre>{@code
 * Win32PnPEntityService service = new Win32PnPEntityService();
 * List<Win32PnPEntity> pnpEntityList = service.get(10);
 * }</pre>
 *
 * @since 1.0.0
 */
public class Win32PnPEntityService implements CommonServiceInterface<Win32PnPEntity> {

    private final TerminalService terminalService;

    /**
     * Creates a {@link Win32PnPEntityService} object.
     */
    public Win32PnPEntityService() {
        this(new TerminalService());
    }

    /**
     * Creates a {@link  Win32PnPEntityService} with the provided {@link TerminalService}.
     * <p>
     * This constructor is package private and is primarily intended for testing
     * </p>
     *
     * @param terminalService the {@link TerminalService} to use, must not be {@code null}
     * @throws NullPointerException if {@code terminalService} is {@code null}
     */
    Win32PnPEntityService(TerminalService terminalService) {
        this.terminalService = Objects.requireNonNull(terminalService, "terminalService cannot be null");
    }
    
    /**
     * Retrieves an unmodifiable {@link List} of {@link Win32PnPEntity}
     * <p>
     * Each invocation creates an isolated PowerShell process, which is
     * pre-maturely terminated if execution exceeds the specified timeout.
     * </p>
     *
     * @param timeout maximum time (in seconds) to wait for the PowerShell
     *                command to complete before terminating the process
     * @return an unmodifiable {@link List} of {@link Win32PnPEntity} objects representing the system's pnp entities.
     * Returns a {@link Collections#emptyList()} if none are detected.
     * @since 1.0.0
     */
    @Override
    public @NotNull @Unmodifiable List<Win32PnPEntity> get(long timeout) {
        TerminalResult result = terminalService.executeQuery(Cimv2.WIN32_PNP_ENTITY, timeout);
        return new Win32PnPEntityMapper().mapToList(result.getResult(), Win32PnPEntity.class);
    }
}
