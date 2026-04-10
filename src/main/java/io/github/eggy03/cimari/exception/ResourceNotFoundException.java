/*
 * SPDX-License-Identifier: MIT
 * SPDX-FileCopyrightText: 2026 The ferrumx-windows contributors
 * SPDX-FileCopyrightText: 2026 Cimari contributors
 */
package io.github.eggy03.cimari.exception;

import lombok.experimental.StandardException;

/**
 * Used to indicate that a critical resource could not be resolved, without which, it is impossible to
 * proceed with further operations.
 *
 * @since 1.0.0
 */
@StandardException
public class ResourceNotFoundException extends RuntimeException {
}
