/*
 * SPDX-License-Identifier: MIT
 * SPDX-FileCopyrightText: 2026 The ferrumx-windows contributors
 * SPDX-FileCopyrightText: 2026 Cimari contributors
 */
/**
 * Contains classes responsible for executing PowerShell queries and retrieving their output.
 * <p>
 * Most service classes in this package implement either
 * {@link io.github.eggy03.cimari.service.CommonServiceInterface} or
 * {@link io.github.eggy03.cimari.service.OptionalCommonServiceInterface}
 * to provide a unified mechanism for fetching and mapping PowerShell results.
 * Some services may define custom logic which does not use or require implementation of a common interface.
 * </p>
 * <p>Service classes in this package typically:</p>
 * <ul>
 *   <li>Query the PowerShell using the pre-defined queries from the {@link io.github.eggy03.cimari.shell} package</li>
 *   <li>Map the result using mappers defined in the {@link io.github.eggy03.cimari.mapping} package</li>
 *   <li>Into instances of entity classes defined in the {@link io.github.eggy03.cimari.entity} package</li>
 * </ul>
 *
 * @since 0.1.0
 */
package io.github.eggy03.cimari.service;