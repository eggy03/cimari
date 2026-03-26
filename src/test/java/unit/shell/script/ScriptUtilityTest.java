package unit.shell.script;

import io.github.eggy03.ferrumx.windows.exception.ResourceNotFoundException;
import io.github.eggy03.ferrumx.windows.shell.script.ScriptUtility;
import org.jspecify.annotations.NonNull;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.io.BufferedReader;
import java.util.Iterator;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

class ScriptUtilityTest {

    public static @NonNull Stream<Arguments> scriptProvider() {

        String multiLineScriptString = "$numberOne = 1" + System.lineSeparator() + "$numberTwo = 2" + System.lineSeparator() + System.lineSeparator() + "$numberOne + $numberTwo";
        String singleLineScriptString = "echo \"Hello\"";
        String emptyLineScriptString = "";

        return Stream.of(
                Arguments.of("/SimpleAdditionScript.ps1", multiLineScriptString),
                Arguments.of("/SingleLineScript.ps1", singleLineScriptString),
                Arguments.of("/EmptyScript.ps1", emptyLineScriptString)
        );
    }

    @ParameterizedTest
    @MethodSource("scriptProvider")
    @SuppressWarnings("resource")
    void loadAsBufferedReader_success(String scriptPath, String mockScriptString) {

        BufferedReader result = ScriptUtility.loadAsBufferedReader(scriptPath);
        StringBuilder resultString = new StringBuilder();
        Iterator<String> resultIterator = result.lines().iterator();

        while (resultIterator.hasNext()) {
            resultString.append(resultIterator.next());
            if (resultIterator.hasNext())
                resultString.append(System.lineSeparator());
        }

        assertThat(resultString).hasToString(mockScriptString);
    }

    @Test
    @SuppressWarnings("resource")
    void loadAsBufferedReader_invalidScriptPath_throwsException() {
        assertThrows(ResourceNotFoundException.class, () -> ScriptUtility.loadAsBufferedReader("/invalidPath"));
    }

    @ParameterizedTest
    @MethodSource("scriptProvider")
    void loadScript_success(String scriptPath, String mockScriptString) {
        String multiLineResult = ScriptUtility.loadScript(scriptPath);
        assertThat(multiLineResult).isEqualTo(mockScriptString);
    }

    @Test
    void loadScript_invalidScriptPath_throwsException() {
        assertThrows(ResourceNotFoundException.class, () -> ScriptUtility.loadScript("/invalidPath"));
    }
}
