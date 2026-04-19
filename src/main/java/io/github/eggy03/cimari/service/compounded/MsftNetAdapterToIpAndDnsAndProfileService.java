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
import io.github.eggy03.cimari.terminal.TerminalResult;
import io.github.eggy03.cimari.terminal.TerminalService;
import org.jspecify.annotations.NonNull;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * Service class for fetching net adapter, ip, dns and profile configuration information from the system.
 * <p>
 * This class executes the {@link ScriptEnum#MSFT_NET_ADAPTER_TO_IP_AND_DNS_AND_PROFILE} script
 * and maps the resulting output into an unmodifiable {@link List} of {@link MsftNetAdapterToIpAndDnsAndProfile} objects.
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
public class MsftNetAdapterToIpAndDnsAndProfileService implements CommonServiceInterface<MsftNetAdapterToIpAndDnsAndProfile> {

    private final @NonNull TerminalService terminalService;
    private final @NonNull MsftNetAdapterToIpAndDnsAndProfileMapper mapper;

    /**
     * Creates {@link MsftNetAdapterToIpAndDnsAndProfileService} with default configuration.
     *
     * @since 1.0.0
     */
    public MsftNetAdapterToIpAndDnsAndProfileService() {
        this(new TerminalService(), new MsftNetAdapterToIpAndDnsAndProfileMapper());
    }

    /**
     * Package Private constructor with injectable dependencies
     *
     * @param terminalService the {@link TerminalService} instance to use, must not be {@code null}
     * @param mapper          the mapper instance to use, must not be {@code null}
     * @since 1.0.0
     */
    MsftNetAdapterToIpAndDnsAndProfileService(TerminalService terminalService, MsftNetAdapterToIpAndDnsAndProfileMapper mapper) {
        this.terminalService = Objects.requireNonNull(terminalService, "terminalService cannot be null");
        this.mapper = Objects.requireNonNull(mapper, "mapper cannot be null");
    }

    /**
     * Retrieves an unmodifiable {@link List} of {@link MsftNetAdapterToIpAndDnsAndProfile} objects
     * <p>
     * Each invocation creates an isolated PowerShell process, which is
     * pre-maturely terminated if execution exceeds the specified timeout.
     * </p>
     *
     * @param timeout maximum time (in seconds) to wait for the PowerShell
     *                command to complete before terminating the process
     * @return an unmodifiable {@link List} of {@link MsftNetAdapterToIpAndDnsAndProfile} objects.
     * Returns a {@link Collections#emptyList()} if no data is found.
     * @since 1.0.0
     */
    @Override
    public @NonNull List<MsftNetAdapterToIpAndDnsAndProfile> get(long timeout) {
        TerminalResult result = terminalService.executeScript(ScriptEnum.MSFT_NET_ADAPTER_TO_IP_AND_DNS_AND_PROFILE, timeout);
        return mapper.mapToList(result.getResult(), MsftNetAdapterToIpAndDnsAndProfile.class);
    }
}
