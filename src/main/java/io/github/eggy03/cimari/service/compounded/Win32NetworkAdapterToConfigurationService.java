/*
 * SPDX-License-Identifier: MIT
 * SPDX-FileCopyrightText: 2025 The ferrumx-windows contributors
 * SPDX-FileCopyrightText: 2026 Cimari contributors
 */
package io.github.eggy03.cimari.service.compounded;

import io.github.eggy03.cimari.entity.compounded.Win32NetworkAdapterToConfiguration;
import io.github.eggy03.cimari.mapping.compounded.Win32NetworkAdapterToConfigurationMapper;
import io.github.eggy03.cimari.service.CommonServiceInterface;
import io.github.eggy03.cimari.service.network.Win32NetworkAdapterConfigurationService;
import io.github.eggy03.cimari.service.network.Win32NetworkAdapterService;
import io.github.eggy03.cimari.service.network.Win32NetworkAdapterSettingService;
import io.github.eggy03.cimari.shell.script.ScriptEnum;
import io.github.eggy03.cimari.terminal.TerminalResult;
import io.github.eggy03.cimari.terminal.TerminalService;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Unmodifiable;

import java.util.Collections;
import java.util.List;

/**
 * Service class for fetching network adapter and related configuration information from the system.
 * <p>
 * This class executes the {@link ScriptEnum#WIN32_NETWORK_ADAPTER_TO_CONFIGURATION} script
 * and maps the resulting output into an unmodifiable {@link List} of {@link Win32NetworkAdapterToConfiguration} objects.
 * </p>
 *
 * <h2>Usage examples</h2>
 * <pre>{@code
 * Win32NetworkAdapterToConfigurationService service = new Win32NetworkAdapterToConfigurationService();
 * List<Win32NetworkAdapterToConfiguration> netAdapConList = service.get(10);
 * }</pre>
 *
 * @see Win32NetworkAdapterService
 * @see Win32NetworkAdapterConfigurationService
 * @see Win32NetworkAdapterSettingService
 * @see MsftNetAdapterToIpAndDnsAndProfileService
 * @since 1.0.0
 */
public class Win32NetworkAdapterToConfigurationService implements CommonServiceInterface<Win32NetworkAdapterToConfiguration> {

    /**
     * Retrieves an unmodifiable {@link List} of {@link Win32NetworkAdapterToConfiguration} objects
     * <p>
     * Each invocation creates an isolated PowerShell process, which is
     * pre-maturely terminated if execution exceeds the specified timeout.
     * </p>
     *
     * @param timeout maximum time (in seconds) to wait for the PowerShell
     *                command to complete before terminating the process
     * @return an unmodifiable {@link List} of {@link Win32NetworkAdapterToConfiguration} objects representing connected network adapter and related configuration.
     * Returns a {@link Collections#emptyList()} if no network adapter and related configuration are detected.
     * @since 1.0.0
     */
    @Override
    public @NotNull @Unmodifiable List<Win32NetworkAdapterToConfiguration> get(long timeout) {
        TerminalResult result = new TerminalService().executeScript(ScriptEnum.WIN32_NETWORK_ADAPTER_TO_CONFIGURATION, timeout);
        return new Win32NetworkAdapterToConfigurationMapper().mapToList(result.getResult(), Win32NetworkAdapterToConfiguration.class);
    }
}
