/*
 * SPDX-License-Identifier: MIT
 * SPDX-FileCopyrightText: 2025 The ferrumx-windows contributors
 * SPDX-FileCopyrightText: 2026 Cimari contributors
 */
package io.github.eggy03.cimari.service.display;

import io.github.eggy03.cimari.entity.display.Win32VideoController;
import io.github.eggy03.cimari.mapping.display.Win32VideoControllerMapper;
import io.github.eggy03.cimari.service.CommonServiceInterface;
import io.github.eggy03.cimari.shell.query.Cimv2;
import io.github.eggy03.cimari.terminal.TerminalResult;
import io.github.eggy03.cimari.terminal.TerminalService;
import org.jspecify.annotations.NonNull;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * Service class for fetching video controller (GPU) information from the system.
 * <p>
 * This class executes the {@link Cimv2#WIN32_VIDEO_CONTROLLER} PowerShell command
 * and maps the resulting output into an unmodifiable {@link List} of {@link Win32VideoController} objects.
 * </p>
 *
 * <h2>Usage examples</h2>
 * <pre>{@code
 * Win32VideoControllerService service = new Win32VideoControllerService();
 * List<Win32VideoController> controllers = service.get(10);
 * }</pre>
 *
 * @since 1.0.0
 */
public class Win32VideoControllerService implements CommonServiceInterface<Win32VideoController> {

    private final @NonNull TerminalService terminalService;
    private final @NonNull Win32VideoControllerMapper mapper;

    /**
     * Creates {@link Win32VideoControllerService} with default configuration.
     *
     * @since 1.0.0
     */
    public Win32VideoControllerService() {
        this(new TerminalService(), new Win32VideoControllerMapper());
    }

    /**
     * Package Private constructor with injectable dependencies
     *
     * @param terminalService the {@link TerminalService} instance to use, must not be {@code null}
     * @param mapper          the mapper instance to use, must not be {@code null}
     * @since 1.0.0
     */
    Win32VideoControllerService(TerminalService terminalService, Win32VideoControllerMapper mapper) {
        this.terminalService = Objects.requireNonNull(terminalService, "terminalService cannot be null");
        this.mapper = Objects.requireNonNull(mapper, "mapper cannot be null");
    }

    /**
     * Retrieves an unmodifiable {@link List} of {@link Win32VideoController} objects
     * <p>
     * Each invocation creates an isolated PowerShell process, which is
     * pre-maturely terminated if execution exceeds the specified timeout.
     * </p>
     *
     * @param timeout maximum time (in seconds) to wait for the PowerShell
     *                command to complete before terminating the process
     * @return an unmodifiable {@link List} of {@link Win32VideoController} objects representing the video controllers.
     * Returns a {@link Collections#emptyList()} if none are detected.
     * @since 1.0.0
     */
    @Override
    public @NonNull List<Win32VideoController> get(long timeout) {
        TerminalResult result = terminalService.executeQuery(Cimv2.WIN32_VIDEO_CONTROLLER, timeout);
        return mapper.mapToList(result.getResult(), Win32VideoController.class);
    }
}