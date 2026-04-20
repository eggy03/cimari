/*
 * SPDX-License-Identifier: MIT
 * SPDX-FileCopyrightText: 2026 The ferrumx-windows contributors
 * SPDX-FileCopyrightText: 2026 Cimari contributors
 */
package io.github.eggy03.cimari.exception;

/**
 * Usually thrown to indicate that necessary I/O operations on a resource has failed
 *
 * @since 0.1.0
 */

public class ResourceOperationException extends RuntimeException {

    @SuppressWarnings("unused")
    public ResourceOperationException(String message, Throwable cause) {
        super(message, cause);
    }

    @SuppressWarnings("unused")
    public ResourceOperationException(String message) {
        super(message);
    }

    @SuppressWarnings("unused")
    public ResourceOperationException(Throwable cause) {
        super("I/O operations failed on resource", cause);
    }
}
