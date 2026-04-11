/*
 * SPDX-License-Identifier: MIT
 * SPDX-FileCopyrightText: 2025 The ferrumx-windows contributors
 * SPDX-FileCopyrightText: 2026 Cimari contributors
 */
package io.github.eggy03.cimari.service.system;

import com.profesorfalken.jpowershell.PowerShell;
import io.github.eggy03.cimari.annotation.IsolatedPowerShell;
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
 * // Convenience API (creates its own short-lived session)
 * Win32ComputerSystemService service = new Win32ComputerSystemService();
 * Optional<Win32ComputerSystem> system = service.get();
 *
 * // API with re-usable session (caller manages session lifecycle)
 * try (PowerShell session = PowerShell.openSession()) {
 *     Win32ComputerSystemService service = new Win32ComputerSystemService();
 *     Optional<Win32ComputerSystem> system = service.get(session);
 * }
 *
 * // API with execution timeout (auto-created session is terminated if the timeout is exceeded)
 * Win32ComputerSystemService service = new Win32ComputerSystemService();
 * Optional<Win32ComputerSystem> system = service.get(10);
 * }</pre>
 *
 * <h2>Execution models and concurrency</h2>
 * <p>
 * This service supports multiple PowerShell execution strategies:
 * </p>
 *
 * <ul>
 *   <li>
 *     <b>jPowerShell-based execution</b> via {@link #get()} and
 *     {@link #get(PowerShell)}:
 *     <br>
 *     These methods rely on {@code jPowerShell} sessions. Due to internal
 *     global configuration of {@code jPowerShell}, the PowerShell sessions
 *     launched by it is <b>not safe to use concurrently across multiple
 *     threads or executors</b>. Running these methods in parallel may result
 *     in runtime exceptions.
 *   </li>
 *
 *   <li>
 *     <b>Isolated PowerShell execution</b> via {@link #get(long timeout)}:
 *     <br>
 *     This method doesn't rely on {@code jPowerShell} and instead, launches a
 *     standalone PowerShell process per invocation using
 *     {@link TerminalUtility}. Each call is fully isolated and
 *     <b>safe to use in multithreaded and executor-based environments</b>.
 *   </li>
 * </ul>
 *
 * <p>
 * For concurrent or executor-based workloads, prefer {@link #get(long timeout)}.
 * </p>
 *
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
    @IsolatedPowerShell
    public @NotNull Optional<Win32ComputerSystem> get(long timeout) {
        String command = Cimv2.WIN32_COMPUTER_SYSTEM.getQuery();
        String response = TerminalUtility.executeCommand(command, timeout);
        log.trace("PowerShell response for the apache terminal session: \n{}", response);
        return new Win32ComputerSystemMapper().mapToObject(response, Win32ComputerSystem.class);
    }
}
