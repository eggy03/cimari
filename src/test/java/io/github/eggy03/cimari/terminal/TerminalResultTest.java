package io.github.eggy03.cimari.terminal;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

class TerminalResultTest {

    @Test
    @SuppressWarnings("all")
    void testForNullInputsInConstructor_throwsException() {
        NullPointerException ex1 = assertThrows(NullPointerException.class, () -> new TerminalResult(null, ""));
        NullPointerException ex2 = assertThrows(NullPointerException.class, () -> new TerminalResult("", null));
        assertThat(ex1.getMessage()).isEqualTo("result cannot be null");
        assertThat(ex2.getMessage()).isEqualTo("error cannot be null");

    }

    @Test
    void testGetterValue() {
        TerminalResult terminalResult = new TerminalResult("a", "b");
        assertThat(terminalResult.getResult()).isEqualTo("a");
        assertThat(terminalResult.getError()).isEqualTo("b");
    }
}
