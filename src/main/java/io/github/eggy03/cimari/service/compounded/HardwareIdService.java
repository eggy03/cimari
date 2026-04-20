/*
 * SPDX-License-Identifier: MIT
 * SPDX-FileCopyrightText: 2025 The ferrumx-windows contributors
 * SPDX-FileCopyrightText: 2026 Cimari contributors
 */
package io.github.eggy03.cimari.service.compounded;

import io.github.eggy03.cimari.entity.compounded.HardwareId;
import io.github.eggy03.cimari.mapping.compounded.HardwareIdMapper;
import io.github.eggy03.cimari.service.OptionalCommonServiceInterface;
import io.github.eggy03.cimari.shell.script.ScriptEnum;
import io.github.eggy03.cimari.terminal.TerminalResult;
import io.github.eggy03.cimari.terminal.TerminalService;
import org.jspecify.annotations.NonNull;

import java.util.Objects;
import java.util.Optional;

/**
 * Service class for fetching the HWID information from a system running Windows.
 * <p>
 * This class executes the {@link ScriptEnum#HWID} PowerShell script
 * and maps the resulting output into an {@link Optional} of {@link HardwareId}.
 * </p>
 *
 * <h2>Usage examples</h2>
 * <pre>{@code
 * HardwareIdService service = new HardwareIdService();
 * Optional<HardwareId> hwid = service.get(10);
 * }</pre>
 *
 * @since 0.1.0
 */
public class HardwareIdService implements OptionalCommonServiceInterface<HardwareId> {

    private final @NonNull TerminalService terminalService;
    private final @NonNull HardwareIdMapper mapper;

    /**
     * Creates {@link HardwareIdService} with default configuration.
     *
     * @since 0.1.0
     */
    public HardwareIdService() {
        this(new TerminalService(), new HardwareIdMapper());
    }

    /**
     * Package Private constructor with injectable dependencies
     *
     * @param terminalService the {@link TerminalService} instance to use, must not be {@code null}
     * @param mapper          the mapper instance to use, must not be {@code null}
     * @since 0.1.0
     */
    HardwareIdService(TerminalService terminalService, HardwareIdMapper mapper) {
        this.terminalService = Objects.requireNonNull(terminalService, "terminalService cannot be null");
        this.mapper = Objects.requireNonNull(mapper, "mapper cannot be null");
    }

    /**
     * Retrieves an {@link Optional} of {@link HardwareId}
     * <p>
     * Each invocation creates an isolated PowerShell process, which is
     * pre-maturely terminated if execution exceeds the specified timeout.
     * </p>
     *
     * @param timeout maximum time (in seconds) to wait for the PowerShell
     *                command to complete before terminating the process
     * @return an {@link Optional} of {@link HardwareId} representing
     * the HWID. Returns {@link Optional#empty()} if no information
     * is detected.
     * @since 0.1.0
     */
    @Override
    public @NonNull Optional<HardwareId> get(long timeout) {
        TerminalResult result = terminalService.executeScript(ScriptEnum.HWID, timeout);
        return mapper.mapToObject(result.getResult(), HardwareId.class);
    }
}