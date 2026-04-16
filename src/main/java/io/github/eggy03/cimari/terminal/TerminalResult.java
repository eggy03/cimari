package io.github.eggy03.cimari.terminal;

import org.jspecify.annotations.NonNull;

import java.util.Objects;

/**
 * Represents the result of a command or script executed via {@link TerminalService}.
 * <p>
 * This class encapsulates both the standard output (stdout) and standard error (stderr)
 * produced during execution.
 * </p>
 *
 * @since 1.0.0
 */
public class TerminalResult {

    private final @NonNull String result;
    private final @NonNull String error;

    /**
     * Constructs a {@link TerminalResult}.
     *
     * @param result the non-null standard output (stdout) produced by the execution
     * @param error  the non-null standard error (stderr) produced by the execution
     * @throws NullPointerException if either {@code result} or {@code error} is {@code null}
     */
    public TerminalResult(@NonNull String result, @NonNull String error) {
        this.result = Objects.requireNonNull(result, "result cannot be null");
        this.error = Objects.requireNonNull(error, "error cannot be null");
    }


    /**
     * Returns the standard output (stdout) produced by the execution.
     *
     * @return a non-null string containing the command output
     */
    public @NonNull String getResult() {
        return result;
    }

    /**
     * Returns the standard error (stderr) produced by the execution.
     *
     * @return a non-null string containing the error output
     */
    public @NonNull String getError() {
        return error;
    }

}