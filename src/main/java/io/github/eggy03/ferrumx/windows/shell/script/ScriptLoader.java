/*
 * © 2026 The ferrumx-windows contributors
 * Licensed under the MIT License.
 * See the LICENSE file in the project root for more information.
 */
package io.github.eggy03.ferrumx.windows.shell.script;

import lombok.NonNull;
import lombok.experimental.UtilityClass;
import org.jetbrains.annotations.NotNull;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Objects;

/**
 * <p>
 * Utility class for loading scripts from a provided path, as various streams.
 * </p>
 * <p>
 * <b>For Internal Use Only</b>
 * </p>
 *
 * @author Sayan Bhattacharjee (Egg-03/Eggy)
 * @since 4.1.0
 */
@UtilityClass
public class ScriptLoader {

    /**
     * Loads a PowerShell script from the classpath and wraps it in a {@link BufferedReader}.
     *
     * <p>The script is read using UTF-8 encoding.</p>
     *
     * @param scriptPath the absolute classpath location of the script (e.g. {@code "/script.ps1"})
     * @return a {@link BufferedReader} for the requested script
     * @throws NullPointerException if the script resource cannot be found
     */
    @NotNull
    public static BufferedReader loadAsBufferedReader(@NonNull String scriptPath) {
        return new BufferedReader(
                new InputStreamReader( // TODO throw ResourceNotFoundException in case of null returns
                        Objects.requireNonNull(ScriptLoader.class.getResourceAsStream(scriptPath))
                        , StandardCharsets.UTF_8
                )
        );
    }

    /**
     * Loads a PowerShell script from the classpath and returns its full contents as a {@link String}.
     *
     * <p>Each line of the script is preserved and separated using
     * {@link System#lineSeparator()}.</p>
     *
     * @param scriptPath the absolute classpath location of the script (e.g. {@code "/script.ps1"})
     * @return the complete script contents as a {@link String}
     * @throws NullPointerException if the script resource cannot be found
     */
    @NotNull
    public static String loadScript(@NonNull String scriptPath) {

        BufferedReader reader = new BufferedReader(
                new InputStreamReader( // TODO throw ResourceNotFoundException in case of null returns
                        Objects.requireNonNull(ScriptLoader.class.getResourceAsStream(scriptPath))
                )
        );

        StringBuilder script = new StringBuilder();
        reader.lines().forEach(line -> script.append(line).append(System.lineSeparator()));
        return script.toString();
    }

}
