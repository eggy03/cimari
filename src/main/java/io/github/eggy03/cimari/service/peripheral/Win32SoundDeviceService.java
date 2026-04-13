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
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Unmodifiable;

import java.util.Collections;
import java.util.List;

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
 * @since 1.0.0
 */
public class Win32SoundDeviceService implements CommonServiceInterface<Win32SoundDevice> {

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
     * @since 1.0.0
     */
    @Override
    public @NotNull @Unmodifiable List<Win32SoundDevice> get(long timeout) {
        TerminalResult result = new TerminalService().executeQuery(Cimv2.WIN32_SOUND_DEVICE, timeout);
        return new Win32SoundDeviceMapper().mapToList(result.getResult(), Win32SoundDevice.class);
    }
}
