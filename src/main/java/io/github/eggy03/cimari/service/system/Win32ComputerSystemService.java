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
import org.jetbrains.annotations.NotNull;

import java.util.Optional;

/**
 * Service class for fetching the computer system information running Windows.
 * <p>
 * This class executes the {@link Cimv2#WIN32_COMPUTER_SYSTEM} PowerShell command
 * and maps the resulting output into an {@link Optional} {@link Win32ComputerSystem} object.
 * </p>
 *
 * <h2>Usage examples</h2>
 * <pre>{@code
 * Win32ComputerSystemService service = new Win32ComputerSystemService();
 * Optional<Win32ComputerSystem> system = service.get(10);
 * }</pre>
 *
 * @since 1.0.0
 */
public class Win32ComputerSystemService implements OptionalCommonServiceInterface<Win32ComputerSystem> {

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
     * @since 1.0.0
     */
    @Override
    public @NotNull Optional<Win32ComputerSystem> get(long timeout) {
        TerminalResult result = new TerminalService().executeQuery(Cimv2.WIN32_COMPUTER_SYSTEM, timeout);
        return new Win32ComputerSystemMapper().mapToObject(result.getResult(), Win32ComputerSystem.class);
    }
}
