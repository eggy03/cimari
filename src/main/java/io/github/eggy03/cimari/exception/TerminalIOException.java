/*
 * SPDX-License-Identifier: MIT
 * SPDX-FileCopyrightText: 2025 The ferrumx-windows contributors
 * SPDX-FileCopyrightText: 2026 Cimari contributors
 */
package io.github.eggy03.cimari.exception;

import io.github.eggy03.cimari.terminal.TerminalService;
import lombok.experimental.StandardException;

/**
 * A generic wrapper of all exceptions occurring from {@link TerminalService}
 *
 * @since 1.0.0
 */
@StandardException
public class TerminalIOException extends RuntimeException {

}