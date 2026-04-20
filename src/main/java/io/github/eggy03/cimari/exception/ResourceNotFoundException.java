/*
 * SPDX-License-Identifier: MIT
 * SPDX-FileCopyrightText: 2026 The ferrumx-windows contributors
 * SPDX-FileCopyrightText: 2026 Cimari contributors
 */
package io.github.eggy03.cimari.exception;

/**
 * Used to indicate that a critical resource could not be resolved, without which, it is impossible to
 * proceed with further operations.
 *
 * @since 0.1.0
 */
public class ResourceNotFoundException extends RuntimeException {

    @SuppressWarnings("unused")
    public ResourceNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    @SuppressWarnings("unused")
    public ResourceNotFoundException(String message) {
        super(message);
    }

    @SuppressWarnings("unused")
    public ResourceNotFoundException(Throwable cause) {
        super("A required resource was not found", cause);
    }
}
