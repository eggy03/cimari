/*
 * SPDX-License-Identifier: MIT
 * SPDX-FileCopyrightText: 2025 The ferrumx-windows contributors
 * SPDX-FileCopyrightText: 2026 Cimari contributors
 */
package unit.service.system;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import com.google.gson.annotations.SerializedName;
import io.github.eggy03.cimari.entity.system.Win32Environment;
import io.github.eggy03.cimari.service.system.Win32EnvironmentService;
import io.github.eggy03.cimari.utility.TerminalUtility;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;

import java.lang.reflect.Field;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mockStatic;

class Win32EnvironmentTest {

    private static Win32Environment sysVar;
    private static Win32Environment userVar;
    private static String json;
    private Win32EnvironmentService service;

    @BeforeAll
    static void setEnvironmentVariables() {
        sysVar = Win32Environment.builder()
                .name("PATH")
                .systemVariable(true)
                .variableValue("C:\\Windows\\System32")
                .build();

        userVar = Win32Environment.builder()
                .name("TEMP")
                .systemVariable(false)
                .variableValue("C:\\Users\\User\\AppData\\Local\\Temp")
                .build();
    }

    @BeforeAll
    static void setupJson() {
        JsonObject sysEnv = new JsonObject();
        sysEnv.addProperty("Name", "PATH");
        sysEnv.addProperty("SystemVariable", true);
        sysEnv.addProperty("VariableValue", "C:\\Windows\\System32");

        JsonObject userEnv = new JsonObject();
        userEnv.addProperty("Name", "TEMP");
        userEnv.addProperty("SystemVariable", false);
        userEnv.addProperty("VariableValue", "C:\\Users\\User\\AppData\\Local\\Temp");

        JsonArray array = new JsonArray();
        array.add(sysEnv);
        array.add(userEnv);

        json = new GsonBuilder().serializeNulls().create().toJson(array);
    }


    @BeforeEach
    void setUp() {
        service = new Win32EnvironmentService();
    }

    @Test
    void test_getWithTimeout_success() {

        try (MockedStatic<TerminalUtility> mockedTerminal = mockStatic(TerminalUtility.class)) {
            mockedTerminal
                    .when(() -> TerminalUtility.executeCommand(anyString(), anyLong()))
                    .thenReturn(json);

            List<Win32Environment> envList = service.get(5L);
            assertEquals(2, envList.size());

            assertThat(envList.get(0)).usingRecursiveComparison().isEqualTo(sysVar);
            assertThat(envList.get(1)).usingRecursiveComparison().isEqualTo(userVar);
        }
    }

    @Test
    void test_getWithTimeout_invalidJson_throwsException() {

        try (MockedStatic<TerminalUtility> mockedTerminal = mockStatic(TerminalUtility.class)) {
            mockedTerminal
                    .when(() -> TerminalUtility.executeCommand(anyString(), anyLong()))
                    .thenReturn("invalid json");

            assertThrows(JsonSyntaxException.class, () -> service.get(5L));
        }
    }

    /*
     * This test ensures that the test JSON has keys matching all @SerializedName
     * (or raw field names if not annotated) declared in the entity class.
     *
     * The test fails if:
     * - any field is added or removed in the entity without updating the test JSON
     * - any @SerializedName value changes without updating the test JSON
     */
    @Test
    void test_entityFieldParity_withTestJson() {

        // get the serialized name for each field, in a set
        // store the field name in case no serialized names are found
        Field[] declaredClassFields = Win32Environment.class.getDeclaredFields();
        Set<String> serializedNames = new HashSet<>();

        for (Field field : declaredClassFields) {
            SerializedName s = field.getAnnotation(SerializedName.class);
            serializedNames.add(s != null ? s.value() : field.getName());
        }

        // Extract JSON keys from the static test JSON
        Set<String> jsonKeys = new Gson().fromJson(json, JsonArray.class)
                .get(0).getAsJsonObject().keySet();

        // Validate equality of keys vs serialized names
        assertThat(serializedNames)
                .as("Entity fields and JSON keys must match exactly")
                .containsExactlyInAnyOrderElementsOf(jsonKeys);
    }
}
