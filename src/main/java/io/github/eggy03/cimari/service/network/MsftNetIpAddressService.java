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
import io.github.eggy03.cimari.terminal.TerminalResult;
import io.github.eggy03.cimari.terminal.TerminalService;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Unmodifiable;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * Service class for fetching the IPv4 and IPv6 configs for a network adapter.
 * <p>
 * This class executes the {@link StandardCimv2#MSFT_NET_IP_ADDRESS} PowerShell command
 * and maps the resulting output into an unmodifiable {@link List} of {@link MsftNetIpAddress} objects.
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
public class MsftNetIpAddressService implements CommonServiceInterface<MsftNetIpAddress> {

    private final TerminalService terminalService;

    /**
     * Creates a {@link MsftNetIpAddressService} object.
     */
    public MsftNetIpAddressService() {
        this(new TerminalService());
    }

    /**
     * Creates a {@link  MsftNetIpAddressService} with the provided {@link TerminalService}.
     * <p>
     * This constructor is package private and is primarily intended for testing
     * </p>
     *
     * @param terminalService the {@link TerminalService} to use, must not be {@code null}
     * @throws NullPointerException if {@code terminalService} is {@code null}
     */
    MsftNetIpAddressService(TerminalService terminalService) {
        this.terminalService = Objects.requireNonNull(terminalService, "terminalService cannot be null");
    }

    /**
     * Retrieves an unmodifiable {@link List} of {@link MsftNetIpAddress} objects
     * <p>
     * Each invocation creates an isolated PowerShell process, which is
     * pre-maturely terminated if execution exceeds the specified timeout.
     * </p>
     *
     * @param timeout maximum time (in seconds) to wait for the PowerShell
     *                command to complete before terminating the process
     * @return an unmodifiable {@link List} of {@link MsftNetIpAddress} objects representing the IPv4 and IPv6 configs.
     * Returns a {@link Collections#emptyList()} if no configs are detected.
     * @since 1.0.0
     */
    @Override
    public @NotNull @Unmodifiable List<MsftNetIpAddress> get(long timeout) {
        TerminalResult result = terminalService.executeQuery(StandardCimv2.MSFT_NET_IP_ADDRESS, timeout);
        return new MsftNetIpAddressMapper().mapToList(result.getResult(), MsftNetIpAddress.class);
    }
}
