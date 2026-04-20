/*
 * SPDX-License-Identifier: MIT
 * SPDX-FileCopyrightText: 2025 The ferrumx-windows contributors
 * SPDX-FileCopyrightText: 2026 Cimari contributors
 */
package io.github.eggy03.cimari.service.system;

import io.github.eggy03.cimari.entity.system.Win32ComputerSystem;
import io.github.eggy03.cimari.mapping.system.Win32ComputerSystemMapper;
import io.github.eggy03.cimari.service.OptionalCommonServiceInterface;
import io.github.eggy03.cimari.shell.query.Cimv2;
import io.github.eggy03.cimari.terminal.TerminalResult;
import io.github.eggy03.cimari.terminal.TerminalService;
import org.jspecify.annotations.NonNull;

import java.util.Objects;
import java.util.Optional;

/**
 * Service class for fetching the computer system information running Windows.
 * <p>
 * This class executes the {@link Cimv2#WIN32_COMPUTER_SYSTEM} PowerShell command
 * and maps the resulting output into an {@link Optional} {@link Win32ComputerSystem} with default configuration.
 * </p>
 *
 * <h2>Usage examples</h2>
 * <pre>{@code
 * Win32ComputerSystemService service = new Win32ComputerSystemService();
 * Optional<Win32ComputerSystem> system = service.get(10);
 * }</pre>
 *
 * @since 0.1.0
 */
public class Win32ComputerSystemService implements OptionalCommonServiceInterface<Win32ComputerSystem> {

    private final @NonNull TerminalService terminalService;
    private final @NonNull Win32ComputerSystemMapper mapper;

    /**
     * Creates {@link Win32ComputerSystemService} with default configuration.
     *
     * @since 0.1.0
     */
    public Win32ComputerSystemService() {
        this(new TerminalService(), new Win32ComputerSystemMapper());
    }

    /**
     * Package Private constructor with injectable dependencies
     *
     * @param terminalService the {@link TerminalService} instance to use, must not be {@code null}
     * @param mapper          the mapper instance to use, must not be {@code null}
     * @since 0.1.0
     */
    Win32ComputerSystemService(TerminalService terminalService, Win32ComputerSystemMapper mapper) {
        this.terminalService = Objects.requireNonNull(terminalService, "terminalService cannot be null");
        this.mapper = Objects.requireNonNull(mapper, "mapper cannot be null");
    }

    /**
     * Retrieves an {@link Optional} of {@link Win32ComputerSystem}
     * <p>
     * Each invocation creates an isolated PowerShell process, which is
     * pre-maturely terminated if execution exceeds the specified timeout.
     * </p>
     *
     * @param timeout maximum time (in seconds) to wait for the PowerShell
     *                command to complete before terminating the process
     * @return an {@link Optional} of {@link Win32ComputerSystem} representing
     * the HWID. Returns {@link Optional#empty()} if no information
     * is detected.
     * @since 0.1.0
     */
    @Override
    public @NonNull Optional<Win32ComputerSystem> get(long timeout) {
        TerminalResult result = terminalService.executeQuery(Cimv2.WIN32_COMPUTER_SYSTEM, timeout);
        return mapper.mapToObject(result.getResult(), Win32ComputerSystem.class);
    }
}
