/*
 * SPDX-License-Identifier: MIT
 * SPDX-FileCopyrightText: 2025 The ferrumx-windows contributors
 * SPDX-FileCopyrightText: 2026 Cimari contributors
 */
package io.github.eggy03.cimari.exception;

import io.github.eggy03.cimari.utility.TerminalUtility;
import lombok.experimental.StandardException;

/**
 * A generic wrapper of all exceptions occurring from {@link TerminalUtility}
 *
 * @since 3.1.0
 */
@StandardException
public class TerminalExecutionException extends RuntimeException {

}