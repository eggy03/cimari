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
import io.github.eggy03.cimari.utility.TerminalUtility;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;

/**
 * Service class for fetching the computer system information running Windows.
 * <p>
 * This class executes the {@link Cimv2#WIN32_COMPUTER_SYSTEM} PowerShell command
 * and maps the resulting JSON into an {@link Optional} {@link Win32ComputerSystem} object.
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
@Slf4j
public class Win32ComputerSystemService implements OptionalCommonServiceInterface<Win32ComputerSystem> {

    /**
     * Retrieves an {@link Optional} containing the Computer System information
     * using an isolated PowerShell process with a configurable timeout.
     * <p>
     * Each invocation creates an isolated PowerShell process, which is
     * pre-maturely terminated if execution exceeds the specified timeout.
     * </p>
     *
     * @param timeout the maximum time (in seconds) to wait for the PowerShell
     *                command to complete before terminating the process
     * @return an {@link Optional} of {@link Win32ComputerSystem} representing
     * the HWID. Returns {@link Optional#empty()} if no information
     * is detected.
     * @since 1.0.0
     */
    @Override
    public @NotNull Optional<Win32ComputerSystem> get(long timeout) {
        String command = Cimv2.WIN32_COMPUTER_SYSTEM.getQuery();
        String response = TerminalUtility.executeCommand(command, timeout);
        log.trace("PowerShell response for the apache terminal session: \n{}", response);
        return new Win32ComputerSystemMapper().mapToObject(response, Win32ComputerSystem.class);
    }
}
