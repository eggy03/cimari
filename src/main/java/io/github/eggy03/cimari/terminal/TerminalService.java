/*
 * SPDX-License-Identifier: MIT
 * SPDX-FileCopyrightText: 2025 The ferrumx-windows contributors
 * SPDX-FileCopyrightText: 2026 Cimari contributors
 */
package io.github.eggy03.cimari.terminal;

import io.github.eggy03.cimari.exception.TerminalIOException;
import io.github.eggy03.cimari.shell.query.Cimv2;
import io.github.eggy03.cimari.shell.query.StandardCimv2;
import io.github.eggy03.cimari.shell.script.ScriptEnum;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.exec.CommandLine;
import org.apache.commons.exec.DefaultExecutor;
import org.apache.commons.exec.ExecuteException;
import org.apache.commons.exec.ExecuteWatchdog;
import org.apache.commons.exec.PumpStreamHandler;
import org.jspecify.annotations.NonNull;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.Duration;
import java.util.Objects;

/**
 * A service class that provides a way to launch a PowerShell session and execute scripts or commands in it
 * <p>
 * <b>Mostly for internal use </b>
 * </p>
 * @since 1.0.0
 */
@Slf4j
public class TerminalService {

    /**
     * Launches a standalone PowerShell session, executes {@link Cimv2} queries and returns the result
     *
     * @param queryEnum      The non-null enum value containing the loaded query which shall be executed
     * @param timeoutSeconds The non-null, positive value of time in seconds after which the session will be force stopped.
     * @return The result of the query executed, wrapped in {@link TerminalResult}
     */
    public @NonNull TerminalResult executeQuery(@NonNull Cimv2 queryEnum, long timeoutSeconds) {
        Objects.requireNonNull(queryEnum, "queryEnum cannot be null");
        return execute(queryEnum.getQuery(), timeoutSeconds);
    }

    /**
     * Launches a standalone PowerShell session, executes {@link StandardCimv2} queries and returns the result
     *
     * @param queryEnum      The non-null enum value containing the loaded query which shall be executed
     * @param timeoutSeconds The non-null, positive value of time in seconds after which the session will be force stopped.
     * @return The result of the query executed, wrapped in {@link TerminalResult}
     */
    public @NonNull TerminalResult executeQuery(@NonNull StandardCimv2 queryEnum, long timeoutSeconds) {
        Objects.requireNonNull(queryEnum, "queryEnum cannot be null");
        return execute(queryEnum.getQuery(), timeoutSeconds);
    }

    /**
     * Launches a standalone PowerShell session, executes {@link ScriptEnum} scripts and returns the result
     *
     * @param scriptEnum     The non-null enum value containing the loaded query which shall be executed
     * @param timeoutSeconds The non-null, positive value of time in seconds after which the session will be force stopped.
     * @return The result of the script executed, wrapped in {@link TerminalResult}
     */
    public @NonNull TerminalResult executeScript(@NonNull ScriptEnum scriptEnum, long timeoutSeconds) {
        Objects.requireNonNull(scriptEnum, "scriptEnum cannot be null");
        return execute(scriptEnum.getScript(), timeoutSeconds);
    }

    /**
     * Launches a standalone PowerShell session and executes commands and returns the result
     *
     * @param command The command to be executed in the PowerShell, must not be null
     * @param timeout Time in seconds after which the session will be force stopped, must not be null.
     * @return The result of the command executed, wrapped in {@link TerminalResult}
     * @throws IllegalArgumentException if timeout is in negative.
     * @since 1.0.0
     */
    @NonNull TerminalResult execute(@NonNull String command, long timeout) {

        Objects.requireNonNull(command, "query or script to be executed cannot be null");

        if(timeout < 0)
            throw new IllegalArgumentException("Timeout cannot be negative");

        CommandLine cmdLine = new CommandLine("powershell.exe");
        cmdLine.addArgument("-NoProfile");
        cmdLine.addArgument("-NonInteractive");
        cmdLine.addArgument(command, false);

        ByteArrayOutputStream resultStream = new ByteArrayOutputStream();
        ByteArrayOutputStream errorStream = new ByteArrayOutputStream();

        ExecuteWatchdog watchdog = ExecuteWatchdog.builder().setTimeout(Duration.ofSeconds(timeout)).get();

        DefaultExecutor executor = DefaultExecutor.builder().get();
        executor.setStreamHandler(new PumpStreamHandler(resultStream, errorStream));
        executor.setWatchdog(watchdog);

        try {
            int exitCode = executor.execute(cmdLine);
            log.debug("\nPowerShell Execution - SUCCESS\nExit code: {}\nCommand: {}\nStdout: {}\nStderr: {}\n", exitCode, command, resultStream, errorStream);
            return new TerminalResult(resultStream.toString(), errorStream.toString());
        } catch (ExecuteException e) {

            boolean processKilled = watchdog.killedProcess();
            if (log.isDebugEnabled())
                log.debug("\nPowerShell Execution - FAILURE\nProcess Killed: {}\nTimeout: {}\nCommand: {}\nStdout: {}\nStderr: {}\n", processKilled, timeout, command, resultStream, errorStream, e);
            else
                log.warn("\nPowerShell Execution - FAILURE\nProcess Killed: {}\nEnable DEBUG mode to see the commands it tried to execute\n", processKilled, e);

            return new TerminalResult(resultStream.toString(), errorStream.toString());

        } catch (IOException e) {
            throw new TerminalIOException("An I/O Exception occurred while running PowerShell", e);
        }
    }
}
