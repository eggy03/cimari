/*
 * SPDX-License-Identifier: MIT
 * SPDX-FileCopyrightText: 2025 The ferrumx-windows contributors
 * SPDX-FileCopyrightText: 2026 Cimari contributors
 */
package io.github.eggy03.cimari.service;

import io.github.eggy03.cimari.mapping.CommonMappingInterface;
import io.github.eggy03.cimari.terminal.TerminalService;

import java.util.List;

/**
 * Common service interface whose method implementations provide a way to fetch WMI data from PowerShell
 * in the form of a {@link List}
 * <p>
 * Useful for implementing services of classes which return more than one instance
 * such as the {@code Win32_NetworkAdapter} WMI class
 * </p>
 *
 * @param <S> the entity type returned by the service implementation
 * @see OptionalCommonServiceInterface
 * @since 0.1.0
 */
public interface CommonServiceInterface<S> {

    /**
     * Implementations of this method are expected to rely on
     * {@link TerminalService} to launch a PowerShell session, execute commands
     * and then map the results to the expected entity types using a custom implementation
     * or the default methods of {@link CommonMappingInterface}
     *
     * @param timeout the maximum time (in seconds) to wait for the PowerShell command to complete before terminating the process
     * @return a {@link List} of entities of type {@code <S>} defined by the caller
     * @since 0.1.0
     */
    List<S> get(long timeout);
}