package io.github.eggy03.cimari.terminal;

import org.jetbrains.annotations.NotNull;

public class TerminalResult {

    private final @NotNull String result;
    private final @NotNull String error;

    public TerminalResult(@NotNull String result, @NotNull String error) {
        this.result = result;
        this.error = error;
    }


    public @NotNull String getResult() {
        return result;
    }

    public @NotNull String getError() {
        return error;
    }

}