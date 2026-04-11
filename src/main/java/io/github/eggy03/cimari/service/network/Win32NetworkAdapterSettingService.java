/*
 * SPDX-License-Identifier: MIT
 * SPDX-FileCopyrightText: 2025 The ferrumx-windows contributors
 * SPDX-FileCopyrightText: 2026 Cimari contributors
 */
package io.github.eggy03.cimari.service.network;

import com.profesorfalken.jpowershell.PowerShell;
import io.github.eggy03.cimari.annotation.IsolatedPowerShell;
import io.github.eggy03.cimari.entity.network.Win32NetworkAdapter;
import io.github.eggy03.cimari.entity.network.Win32NetworkAdapterConfiguration;
import io.github.eggy03.cimari.entity.network.Win32NetworkAdapterSetting;
import io.github.eggy03.cimari.mapping.network.Win32NetworkAdapterSettingMapper;
import io.github.eggy03.cimari.service.CommonServiceInterface;
import io.github.eggy03.cimari.shell.query.Cimv2;
import io.github.eggy03.cimari.utility.TerminalUtility;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Unmodifiable;

import java.util.List;

/**
 * Service class for fetching the association between a Network Adapter, and it's Configuration from the system.
 * <p>
 * This class executes the {@link Cimv2#WIN32_NETWORK_ADAPTER_SETTING} PowerShell command
 * and maps the resulting JSON into an immutable list of {@link Win32NetworkAdapterSetting} objects.
 * </p>
 *
 * <h2>Usage examples</h2>
 * <pre>{@code
 * // Convenience API (creates its own short-lived session)
 * Win32NetworkAdapterSettingServiceservice = new Win32NetworkAdapterSettingService();
 * List<Win32NetworkAdapterSetting> nas = service.get();
 *
 * // API with re-usable session (caller manages session lifecycle)
 * try (PowerShell session = PowerShell.openSession()) {
 *     Win32NetworkAdapterSettingServiceservice = new Win32NetworkAdapterSettingService();
 *     List<Win32NetworkAdapterSetting> nas = service.get(session);
 * }
 *
 * // API with execution timeout (auto-created session is terminated if the timeout is exceeded)
 * Win32NetworkAdapterSettingServiceservice = new Win32NetworkAdapterSettingService();
 * List<Win32NetworkAdapterSetting> nas = service.get(10);
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
 * @since 1.0.0
 */
@Slf4j
public class Win32NetworkAdapterSettingService implements CommonServiceInterface<Win32NetworkAdapterSetting> {


    /**
     * Retrieves an immutable list of {@link Win32NetworkAdapterSetting} entities
     * using an isolated PowerShell process with a configurable timeout.
     * <p>
     * Each invocation creates an isolated PowerShell process, which is
     * pre-maturely terminated if execution exceeds the specified timeout.
     * </p>
     *
     * @param timeout the maximum time (in seconds) to wait for the PowerShell
     *                command to complete before terminating the process
     * @return an immutable list of {@link Win32NetworkAdapterSetting} objects representing the association between
     * a {@link Win32NetworkAdapter} and it's {@link Win32NetworkAdapterConfiguration}.
     * Returns an empty list if none are detected.
     * @since 1.0.0
     */
    @Override
    @IsolatedPowerShell
    public @NotNull @Unmodifiable List<Win32NetworkAdapterSetting> get(long timeout) {
        String command = Cimv2.WIN32_NETWORK_ADAPTER_SETTING.getQuery();
        String response = TerminalUtility.executeCommand(command, timeout);
        log.trace("PowerShell response for the apache terminal session: \n{}", response);
        return new Win32NetworkAdapterSettingMapper().mapToList(response, Win32NetworkAdapterSetting.class);
    }
}