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
import org.jetbrains.annotations.NotNull;

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
 * @since 1.0.0
 */
public class HardwareIdService implements OptionalCommonServiceInterface<HardwareId> {

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
     * @since 1.0.0
     */
    @Override
    public @NotNull Optional<HardwareId> get(long timeout) {
        TerminalResult result = new TerminalService().executeScript(ScriptEnum.HWID, timeout);
        return new HardwareIdMapper().mapToObject(result.getResult(), HardwareId.class);
    }
}