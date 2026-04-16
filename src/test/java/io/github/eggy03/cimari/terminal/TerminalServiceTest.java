/*
 * SPDX-License-Identifier: MIT
 * SPDX-FileCopyrightText: 2026 The ferrumx-windows contributors
 * SPDX-FileCopyrightText: 2026 Cimari contributors
 */
package io.github.eggy03.cimari.terminal;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.EnabledOnOs;
import org.junit.jupiter.api.condition.OS;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.Objects;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * WARNING: This test will require a valid Windows system to pass because it spawns actual PowerShell sessions
 */
@EnabledOnOs(OS.WINDOWS)
class TerminalServiceTest {

    private static final long TIMEOUT = 15L;

    private TerminalService terminalService;

    @BeforeEach
    void setTerminalService() {
        terminalService = new TerminalService();
    }

    @Test
    void testValidCommand() {
        String validCommand = "10+20";
        TerminalResult result = terminalService.execute(validCommand, TIMEOUT);
        assertThat(result.getResult()).isEqualTo("30" + System.lineSeparator()); // PowerShell appends a new line after each command
        assertThat(result.getError()).isEmpty();
    }

    @Test
    void testInvalidCommand() {
        String invalidCommand = "invalidCommand";
        TerminalResult result = terminalService.execute(invalidCommand, TIMEOUT);
        assertThat(result.getResult()).isEmpty();
        assertThat(result.getError()).isNotEmpty();
    }

    @Test
    void testValidScript() {
        String validScript = "$a=10\n$a++\n$a";
        TerminalResult result = terminalService.execute(validScript, TIMEOUT);
        assertThat(result.getResult()).isEqualTo("11" + System.lineSeparator());
        assertThat(result.getError()).isEmpty();
    }

    @Test
    void testValidScriptFromResource() {

        BufferedReader reader = new BufferedReader(new InputStreamReader(Objects.requireNonNull(TerminalServiceTest.class.getResourceAsStream("/SimpleAdditionScript.ps1"))));
        StringBuilder script = new StringBuilder();
        reader.lines().forEach(line -> script.append(line).append(System.lineSeparator()));

        TerminalResult result = terminalService.execute(script.toString(), TIMEOUT);
        assertThat(result.getResult()).isEqualTo("3" + System.lineSeparator());
        assertThat(result.getError()).isEmpty();
    }


    @Test
    void testErrorStream() {
        String errorCommand = "Write-Error 'fail'";
        TerminalResult result = terminalService.execute(errorCommand, TIMEOUT);
        assertThat(result.getResult()).isEmpty();
        assertThat(result.getError()).contains("fail");
    }

    @Test
    void testMixedOutput() {
        String mixedCommand = "Write-Output 'ok'; Write-Error 'fail'";

        TerminalResult result = terminalService.execute(mixedCommand, TIMEOUT);
        assertThat(result.getResult()).isEqualTo("ok" + System.lineSeparator());
        assertThat(result.getError()).contains("fail");

    }

    @Test
    void testNegativeTimeout() {
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> terminalService.execute("Write-Output \"Hello\"", -2));
        assertThat(ex.getMessage()).isEqualTo("Timeout cannot be negative");
    }

    @Test
    void testEmptyInput() {
        String emptyInput = "";
        TerminalResult result = terminalService.execute(emptyInput, TIMEOUT);
        assertThat(result.getResult()).contains("Windows PowerShell");
        assertThat(result.getError()).isEmpty();
    }

    @Test
    @SuppressWarnings("all")
    void testNullInput() {
        NullPointerException ex = assertThrows(NullPointerException.class, () -> terminalService.execute(null, TIMEOUT));
        assertThat(ex.getMessage()).isEqualTo("query or script to be executed cannot be null");
    }
}