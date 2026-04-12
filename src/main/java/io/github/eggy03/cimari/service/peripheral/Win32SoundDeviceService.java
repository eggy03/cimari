/*
 * SPDX-License-Identifier: MIT
 * SPDX-FileCopyrightText: 2025 The ferrumx-windows contributors
 * SPDX-FileCopyrightText: 2026 Cimari contributors
 */
package io.github.eggy03.cimari.service.peripheral;

import io.github.eggy03.cimari.entity.peripheral.Win32SoundDevice;
import io.github.eggy03.cimari.mapping.peripheral.Win32SoundDeviceMapper;
import io.github.eggy03.cimari.service.CommonServiceInterface;
import io.github.eggy03.cimari.service.TerminalService;
import io.github.eggy03.cimari.shell.query.Cimv2;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Unmodifiable;

import java.util.List;

/**
 * Service class for fetching sound device information from the system.
 * <p>
 * This class executes the {@link Cimv2#WIN32_SOUND_DEVICE} PowerShell command
 * and maps the resulting JSON into an immutable list of {@link Win32SoundDevice} objects.
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
@Slf4j
public class Win32SoundDeviceService implements CommonServiceInterface<Win32SoundDevice> {

    /**
     * Retrieves an immutable list of sound devices present on the system
     * using an isolated PowerShell process with a configurable timeout.
     * <p>
     * Each invocation creates an isolated PowerShell process, which is
     * pre-maturely terminated if execution exceeds the specified timeout.
     * </p>
     *
     * @param timeout the maximum time (in seconds) to wait for the PowerShell
     *                command to complete before terminating the process
     * @return an immutable list of {@link Win32SoundDevice} objects representing the system's sound devices.
     * If no sound devices are present, returns an empty list.
     * @since 1.0.0
     */
    @Override
    public @NotNull @Unmodifiable List<Win32SoundDevice> get(long timeout) {
        String command = Cimv2.WIN32_SOUND_DEVICE.getQuery();
        String response = TerminalService.executeCommand(command, timeout);
        log.trace("PowerShell response for the apache terminal session: \n{}", response);
        return new Win32SoundDeviceMapper().mapToList(response, Win32SoundDevice.class);
    }
}
