/*
 * SPDX-License-Identifier: MIT
 * SPDX-FileCopyrightText: 2025 The ferrumx-windows contributors
 * SPDX-FileCopyrightText: 2026 Cimari contributors
 */
package io.github.eggy03.cimari.service.peripheral;

import io.github.eggy03.cimari.entity.peripheral.Win32SoundDevice;
import io.github.eggy03.cimari.mapping.peripheral.Win32SoundDeviceMapper;
import io.github.eggy03.cimari.service.CommonServiceInterface;
import io.github.eggy03.cimari.shell.query.Cimv2;
import io.github.eggy03.cimari.terminal.TerminalResult;
import io.github.eggy03.cimari.terminal.TerminalService;
import org.jspecify.annotations.NonNull;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * Service class for fetching sound device information from the system.
 * <p>
 * This class executes the {@link Cimv2#WIN32_SOUND_DEVICE} PowerShell command
 * and maps the resulting output into an unmodifiable {@link List} of {@link Win32SoundDevice} objects.
 * </p>
 *
 * <h2>Usage examples</h2>
 * <pre>{@code
 * Win32SoundDeviceService service = new Win32SoundDeviceService();
 * List<Win32SoundDevice> devices = service.get(10);
 * }</pre>
 *
 * @since 0.1.0
 */
public class Win32SoundDeviceService implements CommonServiceInterface<Win32SoundDevice> {

    private final @NonNull TerminalService terminalService;
    private final @NonNull Win32SoundDeviceMapper mapper;

    /**
     * Creates {@link Win32SoundDeviceService} with default configuration.
     *
     * @since 0.1.0
     */
    public Win32SoundDeviceService() {
        this(new TerminalService(), new Win32SoundDeviceMapper());
    }

    /**
     * Package Private constructor with injectable dependencies
     *
     * @param terminalService the {@link TerminalService} instance to use, must not be {@code null}
     * @param mapper          the mapper instance to use, must not be {@code null}
     * @since 0.1.0
     */
    Win32SoundDeviceService(TerminalService terminalService, Win32SoundDeviceMapper mapper) {
        this.terminalService = Objects.requireNonNull(terminalService, "terminalService cannot be null");
        this.mapper = Objects.requireNonNull(mapper, "mapper cannot be null");
    }

    /**
     * Retrieves an unmodifiable {@link List} of {@link Win32SoundDevice} objects
     * <p>
     * Each invocation creates an isolated PowerShell process, which is
     * pre-maturely terminated if execution exceeds the specified timeout.
     * </p>
     *
     * @param timeout maximum time (in seconds) to wait for the PowerShell
     *                command to complete before terminating the process
     * @return an unmodifiable {@link List} of {@link Win32SoundDevice} objects representing the system's sound devices.
     * If no sound devices are present, returns a {@link Collections#emptyList()}.
     * @since 0.1.0
     */
    @Override
    public @NonNull List<Win32SoundDevice> get(long timeout) {
        TerminalResult result = terminalService.executeQuery(Cimv2.WIN32_SOUND_DEVICE, timeout);
        return mapper.mapToList(result.getResult(), Win32SoundDevice.class);
    }
}
