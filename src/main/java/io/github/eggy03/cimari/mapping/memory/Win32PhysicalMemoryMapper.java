/*
 * SPDX-License-Identifier: MIT
 * SPDX-FileCopyrightText: 2025 The ferrumx-windows contributors
 * SPDX-FileCopyrightText: 2026 Cimari contributors
 */
package io.github.eggy03.cimari.mapping.memory;

import io.github.eggy03.cimari.entity.memory.Win32PhysicalMemory;
import io.github.eggy03.cimari.mapping.CommonMappingInterface;

/**
 * Provides a type-safe implementation of {@link CommonMappingInterface}
 * and maps JSON strings from PowerShell to objects or lists of {@link Win32PhysicalMemory}
 *
 * @since 1.0.0
 */
public class Win32PhysicalMemoryMapper implements CommonMappingInterface<Win32PhysicalMemory> {
}
