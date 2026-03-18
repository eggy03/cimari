/*
 * © 2026 The ferrumx-windows contributors
 * Licensed under the MIT License.
 * See the LICENSE file in the project root for more information.
 */
package io.github.eggy03.ferrumx.windows.shell.script;

import io.github.eggy03.ferrumx.windows.exception.ResourceNotFoundException;
import io.github.eggy03.ferrumx.windows.exception.ResourceOperationException;
import lombok.NonNull;
import lombok.experimental.UtilityClass;
import org.jetbrains.annotations.NotNull;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

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
public class ScriptUtility {

    /**
     * Loads a PowerShell script from the classpath and wraps it in a {@link BufferedReader}.
     *
     * <p>The script is read using UTF-8 encoding.</p>
     *
     * @param scriptPath the absolute classpath location of the script (e.g. {@code "/script.ps1"})
     * @return a {@link BufferedReader} for the requested script
     * @throws ResourceNotFoundException if the script resource cannot be found
     */
    @NotNull
    public static BufferedReader loadAsBufferedReader(@NonNull String scriptPath) {

        InputStream resource = ScriptUtility.class.getResourceAsStream(scriptPath);
        if (resource == null)
            throw new ResourceNotFoundException("Script was not found in: " + scriptPath);

        InputStreamReader resourceStreamReader = new InputStreamReader(resource, StandardCharsets.UTF_8);

        return new BufferedReader(resourceStreamReader);
    }

    /**
     * Loads a PowerShell script from the classpath and returns its full contents as a {@link String}.
     *
     * <p>Each line of the script is preserved and separated using
     * {@link System#lineSeparator()}.</p>
     *
     * @param scriptPath the absolute classpath location of the script (e.g. {@code "/script.ps1"})
     * @return the complete script contents as a {@link String}
     * @throws ResourceNotFoundException if the script resource cannot be found
     * @throws ResourceOperationException when I/O operations on the resource fail
     */
    @NotNull
    public static String loadScript(@NonNull String scriptPath) {

        InputStream resource = ScriptUtility.class.getResourceAsStream(scriptPath);
        if (resource == null)
            throw new ResourceNotFoundException("Script was not found in: " + scriptPath);

        InputStreamReader resourceStreamReader = new InputStreamReader(resource, StandardCharsets.UTF_8);
        // wrapping buffered reader on try-with-resources is enough since it will also close the other chained readers
        try (BufferedReader resourceBuffer = new BufferedReader(resourceStreamReader)) {

            StringBuilder script = new StringBuilder();
            resourceBuffer.lines().forEach(line -> script.append(line).append(System.lineSeparator()));
            return script.toString();

        } catch (IOException e) {
            throw new ResourceOperationException("Failed to read script: " + scriptPath, e);
        }
    }
}
