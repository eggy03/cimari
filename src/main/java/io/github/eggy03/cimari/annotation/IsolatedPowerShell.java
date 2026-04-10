/*
 * SPDX-License-Identifier: MIT
 * SPDX-FileCopyrightText: 2026 The ferrumx-windows contributors
 * SPDX-FileCopyrightText: 2026 Cimari contributors
 */
package io.github.eggy03.cimari.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Indicates that the annotated component executes PowerShell commands
 * using isolated processes and does not rely on shared {@code jPowerShell} state.
 *
 * <p>
 * Each invocation is performed in a separate PowerShell process, ensuring that
 * no global configuration or shared state is reused between executions.
 * </p>
 *
 * <h2>Implications</h2>
 * <ul>
 *   <li>Safe for concurrent use across multiple threads and executors</li>
 *   <li>Each session is isolated per call</li>
 * </ul>
 *
 * <p>
 * This annotation is for documentation purposes only and does not enforce or verify any behavior.
 * </p>
 *
 * @see UsesJPowerShell
 * @since 1.0.0
 */
@Retention(RetentionPolicy.CLASS)
@Target(ElementType.METHOD)
@Documented
public @interface IsolatedPowerShell {
}
