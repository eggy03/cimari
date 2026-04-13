/*
 * SPDX-License-Identifier: MIT
 * SPDX-FileCopyrightText: 2025 The ferrumx-windows contributors
 * SPDX-FileCopyrightText: 2026 Cimari contributors
 */
package io.github.eggy03.cimari.service.network;

import io.github.eggy03.cimari.entity.network.MsftNetConnectionProfile;
import io.github.eggy03.cimari.mapping.network.MsftNetConnectionProfileMapper;
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
 * Service class for fetching the connection profile for a network adapter.
 * <p>
 * This class executes the {@link StandardCimv2#MSFT_NET_CONNECTION_PROFILE} PowerShell command
 * and maps the resulting output into an unmodifiable {@link List} of {@link MsftNetConnectionProfile} objects.
 * </p>
 *
 * <h2>Usage examples</h2>
 * <pre>{@code
 * MsftNetConnectionProfileService service = new MsftNetConnectionProfileService();
 * List<MsftNetConnectionProfile> profiles = service.get(10);
 * }</pre>
 *
 * @since 1.0.0
 */
public class MsftNetConnectionProfileService implements CommonServiceInterface<MsftNetConnectionProfile> {

    private final TerminalService terminalService;

    /**
     * Creates a {@link MsftNetConnectionProfileService} object.
     */
    public MsftNetConnectionProfileService() {
        this(new TerminalService());
    }

    /**
     * Creates a {@link  MsftNetConnectionProfileService} with the provided {@link TerminalService}.
     * <p>
     * This constructor is package private and is primarily intended for testing
     * </p>
     *
     * @param terminalService the {@link TerminalService} to use, must not be {@code null}
     * @throws NullPointerException if {@code terminalService} is {@code null}
     */
    MsftNetConnectionProfileService(TerminalService terminalService) {
        this.terminalService = Objects.requireNonNull(terminalService, "terminalService cannot be null");
    }

    /**
     * Retrieves an unmodifiable {@link List} of {@link MsftNetConnectionProfile} objects
     * <p>
     * Each invocation creates an isolated PowerShell process, which is
     * pre-maturely terminated if execution exceeds the specified timeout.
     * </p>
     *
     * @param timeout maximum time (in seconds) to wait for the PowerShell
     *                command to complete before terminating the process
     * @return an unmodifiable {@link List} of {@link MsftNetConnectionProfile} objects representing the connection profiles.
     * Returns a {@link Collections#emptyList()} if no profiles are detected.
     * @since 1.0.0
     */
    @Override
    public @NotNull @Unmodifiable List<MsftNetConnectionProfile> get(long timeout) {
        TerminalResult result = terminalService.executeQuery(StandardCimv2.MSFT_NET_CONNECTION_PROFILE, timeout);
        return new MsftNetConnectionProfileMapper().mapToList(result.getResult(), MsftNetConnectionProfile.class);
    }
}
