/*
 * SPDX-License-Identifier: MIT
 * SPDX-FileCopyrightText: 2026 The ferrumx-windows contributors
 * SPDX-FileCopyrightText: 2026 Cimari contributors
 */
package io.github.eggy03.cimari.exception;

/**
 * Indicates that a given annotation is not found during runtime.
 *
 * @since 0.1.0
 */
public class AnnotationNotFoundException extends RuntimeException {

    @SuppressWarnings("unused")
    public AnnotationNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    @SuppressWarnings("unused")
    public AnnotationNotFoundException(String message) {
        super(message);
    }

    @SuppressWarnings("unused")
    public AnnotationNotFoundException(Throwable cause) {
        super("A required annotation was not found", cause);
    }
}
