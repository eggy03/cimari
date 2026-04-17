/*
 * SPDX-License-Identifier: MIT
 * SPDX-FileCopyrightText: 2025 The ferrumx-windows contributors
 * SPDX-FileCopyrightText: 2026 Cimari contributors
 */
package io.github.eggy03.cimari.exception;

import io.github.eggy03.cimari.terminal.TerminalService;

/**
 * A generic wrapper for all IO exceptions occurring from {@link TerminalService}
 *
 * @since 1.0.0
 */
public class TerminalIOException extends RuntimeException {

    @SuppressWarnings("unused")
    public TerminalIOException(String message, Throwable cause) {
        super(message, cause);
    }

    @SuppressWarnings("unused")
    public TerminalIOException(String message) {
        super(message);
    }

    @SuppressWarnings("unused")
    public TerminalIOException(Throwable cause) {
        super("Terminal IO Exception", cause);
    }
}