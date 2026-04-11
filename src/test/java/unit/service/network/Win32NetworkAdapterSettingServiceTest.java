/*
 * SPDX-License-Identifier: MIT
 * SPDX-FileCopyrightText: 2025 The ferrumx-windows contributors
 * SPDX-FileCopyrightText: 2026 Cimari contributors
 */
package unit.service.network;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import com.google.gson.annotations.SerializedName;
import io.github.eggy03.cimari.entity.network.Win32NetworkAdapterSetting;
import io.github.eggy03.cimari.service.network.Win32NetworkAdapterSettingService;
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

class Win32NetworkAdapterSettingServiceTest {

    private static Win32NetworkAdapterSetting expectedEthernetSetting;
    private static Win32NetworkAdapterSetting expectedWifiSetting;
    private static String json;
    private Win32NetworkAdapterSettingService service;

    @BeforeAll
    static void setSettings() {
        expectedEthernetSetting = Win32NetworkAdapterSetting.builder()
                .networkAdapterDeviceId("1")
                .networkAdapterConfigurationIndex(1)
                .build();

        expectedWifiSetting = Win32NetworkAdapterSetting.builder()
                .networkAdapterDeviceId("2")
                .networkAdapterConfigurationIndex(2)
                .build();
    }

    @BeforeAll
    static void setupJson() {
        JsonArray settings = new JsonArray();

        JsonObject ethernet = new JsonObject();
        ethernet.addProperty("NetworkAdapterDeviceID", "1");
        ethernet.addProperty("NetworkAdapterConfigurationIndex", 1);

        JsonObject wifi = new JsonObject();
        wifi.addProperty("NetworkAdapterDeviceID", "2");
        wifi.addProperty("NetworkAdapterConfigurationIndex", 2);

        settings.add(ethernet);
        settings.add(wifi);

        json = new GsonBuilder().serializeNulls().create().toJson(settings);
    }


    @BeforeEach
    void setService() {
        service = new Win32NetworkAdapterSettingService();
    }

    @Test
    void test_getWithTimeout_success() {

        try (MockedStatic<TerminalUtility> mockedTerminal = mockStatic(TerminalUtility.class)) {
            mockedTerminal
                    .when(() -> TerminalUtility.executeCommand(anyString(), anyLong()))
                    .thenReturn(json);

            List<Win32NetworkAdapterSetting> networkAdapterSettingList = service.get(5L);
            assertEquals(2, networkAdapterSettingList.size());

            assertThat(networkAdapterSettingList.get(0)).usingRecursiveComparison().isEqualTo(expectedEthernetSetting);
            assertThat(networkAdapterSettingList.get(1)).usingRecursiveComparison().isEqualTo(expectedWifiSetting);
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
        Field[] declaredClassFields = Win32NetworkAdapterSetting.class.getDeclaredFields();
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
