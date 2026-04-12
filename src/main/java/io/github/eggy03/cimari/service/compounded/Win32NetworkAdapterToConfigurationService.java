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
import io.github.eggy03.cimari.shell.script.ScriptUtility;
import io.github.eggy03.cimari.utility.TerminalService;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Unmodifiable;

import java.util.List;

/**
 * Service class for fetching network adapter and related configuration information from the system.
 * <p>
 * This class executes the {@link ScriptEnum#WIN32_NETWORK_ADAPTER_TO_CONFIGURATION} script
 * and maps the resulting JSON into an immutable list of {@link Win32NetworkAdapterToConfiguration} objects.
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
@Slf4j
public class Win32NetworkAdapterToConfigurationService implements CommonServiceInterface<Win32NetworkAdapterToConfiguration> {

    /**
     * Retrieves an immutable list of network adapters and related configuration connected in the system
     * using an isolated PowerShell process with a configurable timeout.
     * <p>
     * Each invocation creates an isolated PowerShell process, which is
     * pre-maturely terminated if execution exceeds the specified timeout.
     * </p>
     *
     * @param timeout the maximum time (in seconds) to wait for the PowerShell
     *                command to complete before terminating the process
     * @return an immutable list of {@link Win32NetworkAdapterToConfiguration} objects representing connected network adapter and related configuration.
     * Returns an empty list if no network adapter and related configuration are detected.
     * @since 1.0.0
     */
    @Override
    public @NotNull @Unmodifiable List<Win32NetworkAdapterToConfiguration> get(long timeout) {

        String command = ScriptUtility.loadScript(ScriptEnum.WIN32_NETWORK_ADAPTER_TO_CONFIGURATION.getScriptPath());
        String response = TerminalService.executeCommand(command, timeout);
        log.trace("PowerShell response for the apache terminal session: \n{}", response);
        return new Win32NetworkAdapterToConfigurationMapper().mapToList(response, Win32NetworkAdapterToConfiguration.class);
    }
}
