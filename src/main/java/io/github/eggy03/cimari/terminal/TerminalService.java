/*
 * SPDX-License-Identifier: MIT
 * SPDX-FileCopyrightText: 2025 The ferrumx-windows contributors
 * SPDX-FileCopyrightText: 2026 Cimari contributors
 */
package io.github.eggy03.cimari.terminal;

import io.github.eggy03.cimari.exception.TerminalExecutionException;
import lombok.NonNull;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.exec.CommandLine;
import org.apache.commons.exec.DefaultExecutor;
import org.apache.commons.exec.ExecuteException;
import org.apache.commons.exec.ExecuteWatchdog;
import org.apache.commons.exec.PumpStreamHandler;
import org.jetbrains.annotations.NotNull;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.Duration;

/**
 * A utility class that provides a way to launch a PowerShell session and execute scripts or commands in it
 * <p>
 * <b>Mostly for internal use </b>
 * </p>
 * @since 1.0.0
 */
@UtilityClass
@Slf4j
public class TerminalService {


    /**
     * Launches a standalone PowerShell session and executes commands and returns the result
     *
     * @param command        The command to be executed in the PowerShell
     * @param timeoutSeconds Time in seconds after which the session will be force stopped
     * @return The result of the command executed
     * @throws TerminalExecutionException When the process is killed pre-maturely upon reaching the timeout or when the command yields an error
     * @throws IllegalArgumentException   If the provided timeout is in the negative
     * @since 1.0.0
     */
    public static @NotNull String executeCommand(@NonNull String command, long timeoutSeconds) {

        if (timeoutSeconds < 0)
            throw new IllegalArgumentException("Timeout cannot be negative");

        CommandLine cmdLine = new CommandLine("powershell.exe");
        cmdLine.addArgument(command, false);

        ByteArrayOutputStream result = new ByteArrayOutputStream();
        ByteArrayOutputStream err = new ByteArrayOutputStream();

        ExecuteWatchdog watchdog = ExecuteWatchdog.builder().setTimeout(Duration.ofSeconds(timeoutSeconds)).get();

        DefaultExecutor executor = DefaultExecutor.builder().get();
        executor.setStreamHandler(new PumpStreamHandler(result, err));
        executor.setWatchdog(watchdog);

        try {
            int exitCode = executor.execute(cmdLine);
            log.debug("\nCommand Executed: {}\nExit code: {}\nError Stream: {}\nResult Stream: {}\n", command, exitCode, err, result);
            return result.toString();
        } catch (ExecuteException e) {
            String reason = watchdog.killedProcess() ?
                    "\nProcess executing the following command: " + command + "\nWas killed after a timeout of " + timeoutSeconds + " seconds\n" :
                    "\nProcess executing the following command: " + command + "\nExited with a non-zero exit code\nTerminal Error Output: " + err;

            throw new TerminalExecutionException(reason, e);
        } catch (IOException e) {
            String reason = "An I/O Exception occurred during executing the following command:\n" + command;
            throw new TerminalExecutionException(reason, e);
        }
    }
}
