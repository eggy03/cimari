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
import io.github.eggy03.cimari.entity.network.Win32NetworkAdapterConfiguration;
import io.github.eggy03.cimari.service.TerminalService;
import io.github.eggy03.cimari.service.network.Win32NetworkAdapterConfigurationService;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mockStatic;

class Win32NetworkAdapterConfigurationServiceTest {

    private static Win32NetworkAdapterConfiguration expectedEthernetConfig;
    private static Win32NetworkAdapterConfiguration expectedWifiConfig;
    private static String json;
    private Win32NetworkAdapterConfigurationService service;

    @BeforeAll
    static void setConfigs() {
        expectedEthernetConfig = Win32NetworkAdapterConfiguration.builder()
                .index(1)
                .description("Intel(R) Ethernet Connection I219-V")
                .caption("Ethernet Adapter Configuration")
                .settingId("{A1B2C3D4-E5F6-7890-1234-56789ABCDEF0}")
                .ipEnabled(true)
                .ipAddress(Collections.singletonList("192.168.0.101"))
                .ipSubnet(Collections.singletonList("255.255.255.0"))
                .defaultIpGateway(Collections.singletonList("192.168.0.1"))
                .dhcpEnabled(true)
                .dhcpServer("192.168.0.1")
                .dhcpLeaseObtained("2024-07-12T10:00:00Z")
                .dhcpLeaseExpires("2024-07-13T10:00:00Z")
                .dnsHostName("DESKTOP-ETHERNET")
                .dnsServerSearchOrder(Arrays.asList("8.8.8.8", "8.8.4.4"))
                .build();

        expectedWifiConfig = Win32NetworkAdapterConfiguration.builder()
                .index(2)
                .description("Intel(R) Wi-Fi 6 AX200 160MHz")
                .caption("Wi-Fi Adapter Configuration")
                .settingId("{B2C3D4E5-F6A1-2345-6789-0ABCDEF12345}")
                .ipEnabled(true)
                .ipAddress(Collections.singletonList("192.168.1.150"))
                .ipSubnet(Collections.singletonList("255.255.255.0"))
                .defaultIpGateway(Collections.singletonList("192.168.1.1"))
                .dhcpEnabled(true)
                .dhcpServer("192.168.1.1")
                .dhcpLeaseObtained("2024-07-12T11:00:00Z")
                .dhcpLeaseExpires("2024-07-13T11:00:00Z")
                .dnsHostName("LAPTOP-WIFI")
                .dnsServerSearchOrder(Arrays.asList("1.1.1.1", "1.0.0.1"))
                .build();
    }

    @BeforeAll
    static void setupJson() {
        JsonArray configs = new JsonArray();

        JsonObject eth = new JsonObject();
        eth.addProperty("Index", 1);
        eth.addProperty("Description", "Intel(R) Ethernet Connection I219-V");
        eth.addProperty("Caption", "Ethernet Adapter Configuration");
        eth.addProperty("SettingID", "{A1B2C3D4-E5F6-7890-1234-56789ABCDEF0}");
        eth.addProperty("IPEnabled", true);
        eth.add("IPAddress", new Gson().toJsonTree(Collections.singletonList("192.168.0.101")));
        eth.add("IPSubnet", new Gson().toJsonTree(Collections.singletonList("255.255.255.0")));
        eth.add("DefaultIPGateway", new Gson().toJsonTree(Collections.singletonList("192.168.0.1")));
        eth.addProperty("DHCPEnabled", true);
        eth.addProperty("DHCPServer", "192.168.0.1");
        eth.addProperty("DHCPLeaseObtained", "2024-07-12T10:00:00Z");
        eth.addProperty("DHCPLeaseExpires", "2024-07-13T10:00:00Z");
        eth.addProperty("DNSHostName", "DESKTOP-ETHERNET");
        eth.add("DNSServerSearchOrder", new Gson().toJsonTree(Arrays.asList("8.8.8.8", "8.8.4.4")));

        JsonObject wifi = new JsonObject();
        wifi.addProperty("Index", 2);
        wifi.addProperty("Description", "Intel(R) Wi-Fi 6 AX200 160MHz");
        wifi.addProperty("Caption", "Wi-Fi Adapter Configuration");
        wifi.addProperty("SettingID", "{B2C3D4E5-F6A1-2345-6789-0ABCDEF12345}");
        wifi.addProperty("IPEnabled", true);
        wifi.add("IPAddress", new Gson().toJsonTree(Collections.singletonList("192.168.1.150")));
        wifi.add("IPSubnet", new Gson().toJsonTree(Collections.singletonList("255.255.255.0")));
        wifi.add("DefaultIPGateway", new Gson().toJsonTree(Collections.singletonList("192.168.1.1")));
        wifi.addProperty("DHCPEnabled", true);
        wifi.addProperty("DHCPServer", "192.168.1.1");
        wifi.addProperty("DHCPLeaseObtained", "2024-07-12T11:00:00Z");
        wifi.addProperty("DHCPLeaseExpires", "2024-07-13T11:00:00Z");
        wifi.addProperty("DNSHostName", "LAPTOP-WIFI");
        wifi.add("DNSServerSearchOrder", new Gson().toJsonTree(Arrays.asList("1.1.1.1", "1.0.0.1")));

        configs.add(eth);
        configs.add(wifi);

        json = new GsonBuilder().serializeNulls().create().toJson(configs);
    }


    @BeforeEach
    void setUp() {
        service = new Win32NetworkAdapterConfigurationService();
    }

    @Test
    void test_getWithTimeout_success() {

        try (MockedStatic<TerminalService> mockedTerminal = mockStatic(TerminalService.class)) {
            mockedTerminal
                    .when(() -> TerminalService.executeCommand(anyString(), anyLong()))
                    .thenReturn(json);

            List<Win32NetworkAdapterConfiguration> configs = service.get(5L);
            assertEquals(2, configs.size());

            assertThat(configs.get(0)).usingRecursiveComparison().isEqualTo(expectedEthernetConfig);
            assertThat(configs.get(1)).usingRecursiveComparison().isEqualTo(expectedWifiConfig);
        }
    }

    @Test
    void test_getWithTimeout_invalidJson_throwsException() {

        try (MockedStatic<TerminalService> mockedTerminal = mockStatic(TerminalService.class)) {
            mockedTerminal
                    .when(() -> TerminalService.executeCommand(anyString(), anyLong()))
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
        Field[] declaredClassFields = Win32NetworkAdapterConfiguration.class.getDeclaredFields();
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