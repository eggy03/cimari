/*
 * SPDX-License-Identifier: MIT
 * SPDX-FileCopyrightText: 2025 The ferrumx-windows contributors
 * SPDX-FileCopyrightText: 2026 Cimari contributors
 */
package io.github.eggy03.cimari.service.network;

import io.github.eggy03.cimari.entity.network.MsftNetIpAddress;
import io.github.eggy03.cimari.mapping.network.MsftNetIpAddressMapper;
import io.github.eggy03.cimari.service.CommonServiceInterface;
import io.github.eggy03.cimari.shell.query.StandardCimv2;
import io.github.eggy03.cimari.terminal.TerminalService;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Unmodifiable;

import java.util.List;

/**
 * Service class for fetching the IPv4 and IPv6 configs for a network adapter.
 * <p>
 * This class executes the {@link StandardCimv2#MSFT_NET_IP_ADDRESS} PowerShell command
 * and maps the resulting JSON into an immutable list of {@link MsftNetIpAddress} objects.
 * </p>
 *
 * <h2>Usage examples</h2>
 * <pre>{@code
 * MsftNetIpAddressService service = new MsftNetIpAddressService();
 * List<MsftNetIpAddress> address = service.get(10);
 * }</pre>
 *
 * @since 1.0.0
 */
@Slf4j
public class MsftNetIpAddressService implements CommonServiceInterface<MsftNetIpAddress> {

    /**
     * Retrieves an immutable list of IPv4 and IPv6 configs for all network adapters connected to the system
     * using an isolated PowerShell process with a configurable timeout.
     * <p>
     * Each invocation creates an isolated PowerShell process, which is
     * pre-maturely terminated if execution exceeds the specified timeout.
     * </p>
     *
     * @param timeout the maximum time (in seconds) to wait for the PowerShell
     *                command to complete before terminating the process
     * @return an immutable list of {@link MsftNetIpAddress} objects representing the IPv4 and IPv6 configs.
     * Returns an empty list if no configs are detected.
     * @since 1.0.0
     */
    @Override
    public @NotNull @Unmodifiable List<MsftNetIpAddress> get(long timeout) {
        String command = StandardCimv2.MSFT_NET_IP_ADDRESS.getQuery();
        String response = TerminalService.executeCommand(command, timeout);
        log.trace("PowerShell response for the apache terminal session: \n{}", response);
        return new MsftNetIpAddressMapper().mapToList(response, MsftNetIpAddress.class);
    }
}
