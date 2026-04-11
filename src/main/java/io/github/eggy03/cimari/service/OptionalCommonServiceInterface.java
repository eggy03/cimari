/*
 * SPDX-License-Identifier: MIT
 * SPDX-FileCopyrightText: 2025 The ferrumx-windows contributors
 * SPDX-FileCopyrightText: 2026 Cimari contributors
 */
package io.github.eggy03.cimari.service;

import com.profesorfalken.jpowershell.PowerShell;
import io.github.eggy03.cimari.mapping.CommonMappingInterface;
import io.github.eggy03.cimari.utility.TerminalUtility;

import java.util.Optional;

/**
 * Common service interface whose method implementations provide a way to fetch WMI data from PowerShell
 * in the form of {@link Optional}
 * <p>
 * Useful for implementing services of classes which return exactly one instance
 * such as the {@code Win32_ComputerSystem} WMI class
 * </p>
 *
 * @param <S> the entity type returned by the service implementation
 *
 * @see CommonServiceInterface
 * @since 1.0.0
 */
public interface OptionalCommonServiceInterface<S> {

    /**
     * Implementations of this method are expected to skip {@link PowerShell} entirely and rely on
     * {@link TerminalUtility} instead for PowerShell session management
     * and then map the results to the expected entity types using a custom implementation
     * or the default methods of {@link CommonMappingInterface}
     *
     * @param timeout the maximum time (in seconds) to wait for the PowerShell command to complete before terminating the process
     * @return an {@link Optional} entity of type {@code <S>} defined by the caller
     * @since 1.0.0
     */
    Optional<S> get(long timeout);
}