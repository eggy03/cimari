/*
 * SPDX-License-Identifier: MIT
 * SPDX-FileCopyrightText: 2026 The ferrumx-windows contributors
 * SPDX-FileCopyrightText: 2026 Cimari contributors
 */
package io.github.eggy03.cimari.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * <p>
 * Associates a Java entity with its corresponding WMI class name.
 * </p>
 * <p>
 * A typical use case of this annotation is to get the WMI class name from the entity class via reflection
 * and dynamically create WQL queries during runtime.
 * </p>
 * <p>
 * Example Usage:
 * </p>
 * <pre>{@code
 * @WmiClass("Win32_Processor")
 * public class Win32Processor {
 *
 *      private final String deviceId;
 *
 *      private final String name;
 * }
 * }</pre>
 *
 *
 * @since 1.0.0
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface WmiClass {
    String className();
}
