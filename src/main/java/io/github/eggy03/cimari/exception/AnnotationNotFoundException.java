/*
 * SPDX-License-Identifier: MIT
 * SPDX-FileCopyrightText: 2026 The ferrumx-windows contributors
 * SPDX-FileCopyrightText: 2026 Cimari contributors
 */
package io.github.eggy03.cimari.exception;

import lombok.experimental.StandardException;

/**
 * Indicates that a given annotation is not found during runtime.
 *
 * @since 4.1.0
 */
@StandardException
public class AnnotationNotFoundException extends RuntimeException {
}
