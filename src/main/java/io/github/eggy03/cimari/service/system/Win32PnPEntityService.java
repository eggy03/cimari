/*
 * SPDX-License-Identifier: MIT
 * SPDX-FileCopyrightText: 2025 The ferrumx-windows contributors
 * SPDX-FileCopyrightText: 2026 Cimari contributors
 */
package io.github.eggy03.cimari.service.system;

import io.github.eggy03.cimari.entity.system.Win32PnPEntity;
import io.github.eggy03.cimari.mapping.system.Win32PnPEntityMapper;
import io.github.eggy03.cimari.service.CommonServiceInterface;
import io.github.eggy03.cimari.shell.query.Cimv2;
import io.github.eggy03.cimari.terminal.TerminalService;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Unmodifiable;

import java.util.List;

/**
 * Service class for fetching operating system information from the system.
 * <p>
 * This class executes the {@link Cimv2#WIN32_OPERATING_SYSTEM} PowerShell command
 * and maps the resulting JSON into an immutable list of {@link Win32PnPEntity} objects.
 * </p>
 *
 * <h2>Usage examples</h2>
 * <pre>{@code
 * Win32PnPEntityService service = new Win32PnPEntityService();
 * List<Win32PnPEntity> pnpEntityList = service.get(10);
 * }</pre>
 *
 * @since 1.0.0
 */
@Slf4j
public class Win32PnPEntityService implements CommonServiceInterface<Win32PnPEntity> {

    /**
     * Retrieves an immutable list of pnp entities connected to the system
     * using an isolated PowerShell process with a configurable timeout.
     * <p>
     * Each invocation creates an isolated PowerShell process, which is
     * pre-maturely terminated if execution exceeds the specified timeout.
     * </p>
     *
     * @param timeout the maximum time (in seconds) to wait for the PowerShell
     *                command to complete before terminating the process
     * @return an immutable list of {@link Win32PnPEntity} objects representing the system's pnp entities.
     * Returns an empty list if none are detected.
     * @since 1.0.0
     */
    @Override
    public @NotNull @Unmodifiable List<Win32PnPEntity> get(long timeout) {
        String command = Cimv2.WIN32_PNP_ENTITY.getQuery();
        String response = TerminalService.executeCommand(command, timeout);
        log.trace("PowerShell response for the apache terminal session: \n{}", response);
        return new Win32PnPEntityMapper().mapToList(response, Win32PnPEntity.class);
    }
}
