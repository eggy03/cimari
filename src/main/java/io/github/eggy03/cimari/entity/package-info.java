/*
 * SPDX-License-Identifier: MIT
 * SPDX-FileCopyrightText: 2026 The ferrumx-windows contributors
 * SPDX-FileCopyrightText: 2026 Cimari contributors
 */
/**
 * Contains immutable representations of WMI Win32 classes, MSFT classes and a few custom classes.
 * <p>
 * Most classes in this package directly map to a corresponding WMI Win32 class or an MSFT class
 * (e.g., {@code Win32_DesktopMonitor}, {@code Win32_Processor}, {@code MSFT_NetAdapter}).
 * </p>
 * <p>
 * Some classes, notably the classes in the {@link io.github.eggy03.cimari.entity.compounded} subpackage
 * represent custom entities which is modeled after multiple Win32 Provider or MSFT classes grouped together
 * </p>
 *
 * @since 1.0.0
 */
package io.github.eggy03.cimari.entity;
