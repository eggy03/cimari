/*
 * SPDX-License-Identifier: MIT
 * SPDX-FileCopyrightText: 2026 The ferrumx-windows contributors
 * SPDX-FileCopyrightText: 2026 Cimari contributors
 */
package io.github.eggy03.cimari.exception;

import lombok.experimental.StandardException;

/**
 * Usually thrown to indicate that necessary I/O operations on a resource has failed
 *
 * @since 1.0.0
 */
@StandardException
public class ResourceOperationException extends RuntimeException {
}
