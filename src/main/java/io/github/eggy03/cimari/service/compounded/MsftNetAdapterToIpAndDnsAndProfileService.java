/*
 * SPDX-License-Identifier: MIT
 * SPDX-FileCopyrightText: 2025 The ferrumx-windows contributors
 * SPDX-FileCopyrightText: 2026 Cimari contributors
 */
package io.github.eggy03.cimari.service.compounded;

import io.github.eggy03.cimari.entity.compounded.MsftNetAdapterToIpAndDnsAndProfile;
import io.github.eggy03.cimari.mapping.compounded.MsftNetAdapterToIpAndDnsAndProfileMapper;
import io.github.eggy03.cimari.service.CommonServiceInterface;
import io.github.eggy03.cimari.service.network.MsftDnsClientServerAddressService;
import io.github.eggy03.cimari.service.network.MsftNetAdapterService;
import io.github.eggy03.cimari.service.network.MsftNetConnectionProfileService;
import io.github.eggy03.cimari.service.network.MsftNetIpAddressService;
import io.github.eggy03.cimari.shell.script.ScriptEnum;
import io.github.eggy03.cimari.shell.script.ScriptUtility;
import io.github.eggy03.cimari.utility.TerminalService;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Unmodifiable;

import java.util.List;

/**
 * Service class for fetching net adapter, ip, dns and profile configuration information from the system.
 * <p>
 * This class executes the {@link ScriptEnum#MSFT_NET_ADAPTER_TO_IP_AND_DNS_AND_PROFILE} script
 * and maps the resulting JSON into an immutable list of {@link MsftNetAdapterToIpAndDnsAndProfile} objects.
 * </p>
 *
 * <h2>Usage examples</h2>
 * <pre>{@code
 * MsftNetAdapterToIpAndDnsAndProfileService service = new MsftNetAdapterToIpAndDnsAndProfileService();
 * List<MsftNetAdapterToIpAndDnsAndProfile> compoundAdapters = service.get(10);
 * }</pre>
 *
 * @see MsftNetAdapterService
 * @see MsftNetIpAddressService
 * @see MsftDnsClientServerAddressService
 * @see MsftNetConnectionProfileService
 * @see Win32NetworkAdapterToConfigurationService
 * @since 1.0.0
 */
@Slf4j
public class MsftNetAdapterToIpAndDnsAndProfileService implements CommonServiceInterface<MsftNetAdapterToIpAndDnsAndProfile> {


    /**
     * Retrieves an immutable list of adapters and their configs connected to the system
     * using an isolated PowerShell process with a configurable timeout.
     * <p>
     * Each invocation creates an isolated PowerShell process, which is
     * pre-maturely terminated if execution exceeds the specified timeout.
     * </p>
     *
     * @param timeout the maximum time (in seconds) to wait for the PowerShell
     *                command to complete before terminating the process
     * @return an immutable list of {@link MsftNetAdapterToIpAndDnsAndProfile} objects representing connected adapters
     * and their configs. Returns an empty list if no data is found.
     * @since 1.0.0
     */
    @Override
    public @NotNull @Unmodifiable List<MsftNetAdapterToIpAndDnsAndProfile> get(long timeout) {

        String script = ScriptUtility.loadScript(ScriptEnum.MSFT_NET_ADAPTER_TO_IP_AND_DNS_AND_PROFILE.getScriptPath());
        String response = TerminalService.executeCommand(script, timeout);
        log.trace("PowerShell response for the apache terminal session: \n{}", response);
        return new MsftNetAdapterToIpAndDnsAndProfileMapper().mapToList(response, MsftNetAdapterToIpAndDnsAndProfile.class);
    }
}
