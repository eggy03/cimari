/*
 * SPDX-License-Identifier: MIT
 * SPDX-FileCopyrightText: 2025 The ferrumx-windows contributors
 * SPDX-FileCopyrightText: 2026 Cimari contributors
 */
package io.github.eggy03.cimari.service.network;

import io.github.eggy03.cimari.entity.network.Win32NetworkAdapter;
import io.github.eggy03.cimari.entity.network.Win32NetworkAdapterConfiguration;
import io.github.eggy03.cimari.entity.network.Win32NetworkAdapterSetting;
import io.github.eggy03.cimari.mapping.network.Win32NetworkAdapterSettingMapper;
import io.github.eggy03.cimari.service.CommonServiceInterface;
import io.github.eggy03.cimari.service.TerminalService;
import io.github.eggy03.cimari.shell.query.Cimv2;
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
 * Win32NetworkAdapterSettingServiceservice = new Win32NetworkAdapterSettingService();
 * List<Win32NetworkAdapterSetting> nas = service.get(10);
 * }</pre>
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
    public @NotNull @Unmodifiable List<Win32NetworkAdapterSetting> get(long timeout) {
        String command = Cimv2.WIN32_NETWORK_ADAPTER_SETTING.getQuery();
        String response = TerminalService.executeCommand(command, timeout);
        log.trace("PowerShell response for the apache terminal session: \n{}", response);
        return new Win32NetworkAdapterSettingMapper().mapToList(response, Win32NetworkAdapterSetting.class);
    }
}