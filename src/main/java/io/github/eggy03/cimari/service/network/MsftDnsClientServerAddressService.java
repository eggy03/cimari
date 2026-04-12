/*
 * SPDX-License-Identifier: MIT
 * SPDX-FileCopyrightText: 2025 The ferrumx-windows contributors
 * SPDX-FileCopyrightText: 2026 Cimari contributors
 */
package io.github.eggy03.cimari.service.network;

import io.github.eggy03.cimari.entity.network.MsftDnsClientServerAddress;
import io.github.eggy03.cimari.mapping.network.MsftDnsClientServerAddressMapper;
import io.github.eggy03.cimari.service.CommonServiceInterface;
import io.github.eggy03.cimari.shell.query.StandardCimv2;
import io.github.eggy03.cimari.utility.TerminalService;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Unmodifiable;

import java.util.List;

/**
 * Service class for fetching DNS Client and Server information for a network adapter.
 * <p>
 * This class executes the {@link StandardCimv2#MSFT_NET_DNS_CLIENT_SERVER_ADDRESS} PowerShell command
 * and maps the resulting JSON into an immutable list of {@link MsftDnsClientServerAddress} objects.
 * </p>
 *
 * <h2>Usage examples</h2>
 * <pre>{@code
 * MsftDnsClientServerAddressService service = new MsftDnsClientServerAddressService();
 * List<MsftDnsClientServerAddress> dns = service.get(10);
 * }</pre>
 *
 * @since 1.0.0
 */
@Slf4j
public class MsftDnsClientServerAddressService implements CommonServiceInterface<MsftDnsClientServerAddress> {

    /**
     * Retrieves an immutable list of DNS Server and Client configuration for all network adapters present in the system
     * using an isolated PowerShell process with a configurable timeout.
     * <p>
     * Each invocation creates an isolated PowerShell process, which is
     * pre-maturely terminated if execution exceeds the specified timeout.
     * </p>
     *
     * @param timeout the maximum time (in seconds) to wait for the PowerShell
     *                command to complete before terminating the process
     * @return an immutable list of {@link MsftDnsClientServerAddress} objects representing the DNS configs.
     * Returns an empty list if no configs are detected.
     * @since 1.0.0
     */
    @Override
    public @NotNull @Unmodifiable List<MsftDnsClientServerAddress> get(long timeout) {
        String command = StandardCimv2.MSFT_NET_DNS_CLIENT_SERVER_ADDRESS.getQuery();
        String response = TerminalService.executeCommand(command, timeout);
        log.trace("PowerShell response for the apache terminal session: \n{}", response);
        return new MsftDnsClientServerAddressMapper().mapToList(response, MsftDnsClientServerAddress.class);
    }
}
