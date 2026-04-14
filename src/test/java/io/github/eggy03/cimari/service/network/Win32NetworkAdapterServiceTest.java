/*
 * SPDX-License-Identifier: MIT
 * SPDX-FileCopyrightText: 2025 The ferrumx-windows contributors
 * SPDX-FileCopyrightText: 2026 Cimari contributors
 */
package io.github.eggy03.cimari.service.network;

import com.google.gson.JsonSyntaxException;
import io.github.eggy03.cimari.entity.network.Win32NetworkAdapter;
import io.github.eggy03.cimari.mapping.network.Win32NetworkAdapterMapper;
import io.github.eggy03.cimari.shell.query.Cimv2;
import io.github.eggy03.cimari.terminal.TerminalResult;
import io.github.eggy03.cimari.terminal.TerminalService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class Win32NetworkAdapterServiceTest {

    private final TerminalResult validTerminalResult = new TerminalResult("{}", "");
    private final TerminalResult invalidTerminalResult = new TerminalResult("invalid json", "");
    private final TerminalResult emptyTerminalResult = new TerminalResult("", "");

    private final Win32NetworkAdapter expectedEthernetAdapter = Win32NetworkAdapter.builder()
            .deviceId("1")
            .index(1)
            .name("Ethernet")
            .description("Intel(R) Ethernet Connection I219-V")
            .pnpDeviceId("PCI\\VEN_8086&DEV_15B8&SUBSYS_06A41028&REV_31\\3&11583659&0&FE")
            .macAddress("00:1A:2B:3C:4D:5E")
            .installed(true)
            .netEnabled(true)
            .netConnectionId("Ethernet")
            .physicalAdapter(true)
            .timeOfLastReset("2024-07-12T15:30:00Z")
            .build();
    private final Win32NetworkAdapter expectedWifiAdapter = Win32NetworkAdapter.builder()
            .deviceId("2")
            .index(2)
            .name("Wi-Fi")
            .description("Intel(R) Wi-Fi 6 AX200 160MHz")
            .pnpDeviceId("PCI\\VEN_8086&DEV_2723&SUBSYS_00848086&REV_1A\\3&11583659&0&A3")
            .macAddress("A0:B1:C2:D3:E4:F5")
            .installed(true)
            .netEnabled(false)
            .netConnectionId("Wi-Fi")
            .physicalAdapter(true)
            .timeOfLastReset("2024-07-12T15:45:00Z")
            .build();

    @Mock
    private TerminalService terminalService;

    @Mock
    private Win32NetworkAdapterMapper mapper;

    @InjectMocks
    private Win32NetworkAdapterService service;

    @Test
    void test_get_serviceReturnsMapperResult() {

        when(terminalService.executeQuery(any(Cimv2.class), anyLong()))
                .thenReturn(validTerminalResult);

        when(mapper.mapToList(anyString(), any()))
                .thenReturn(Arrays.asList(expectedEthernetAdapter, expectedWifiAdapter));

        List<Win32NetworkAdapter> response = service.get(5L);
        assertThat(response).contains(expectedEthernetAdapter, expectedWifiAdapter); // Service should return mapper result unchanged

        verify(terminalService).executeQuery(Cimv2.WIN32_NETWORK_ADAPTER, 5L);
        verify(mapper).mapToList(validTerminalResult.getResult(), Win32NetworkAdapter.class);
        verifyNoMoreInteractions(terminalService);
        verifyNoMoreInteractions(mapper);
    }

    @Test
    void test_get_mapperThrows_servicePropagatesException() {

        when(terminalService.executeQuery(any(Cimv2.class), anyLong()))
                .thenReturn(invalidTerminalResult);

        when(mapper.mapToList(anyString(), any()))
                .thenThrow(JsonSyntaxException.class);

        assertThrows(JsonSyntaxException.class, () -> service.get(5L));

        verify(terminalService).executeQuery(Cimv2.WIN32_NETWORK_ADAPTER, 5L);
        verify(mapper).mapToList(invalidTerminalResult.getResult(), Win32NetworkAdapter.class);
        verifyNoMoreInteractions(terminalService);
        verifyNoMoreInteractions(mapper);
    }

    @Test
    void test_get_serviceReturnsEmpty_whenMapperReturnsEmpty() {

        when(terminalService.executeQuery(any(Cimv2.class), anyLong()))
                .thenReturn(emptyTerminalResult);

        when(mapper.mapToList(anyString(), any()))
                .thenReturn(Collections.emptyList());

        List<Win32NetworkAdapter> response = service.get(5L);
        assertThat(response).isEmpty();

        verify(terminalService).executeQuery(Cimv2.WIN32_NETWORK_ADAPTER, 5L);
        verify(mapper).mapToList(emptyTerminalResult.getResult(), Win32NetworkAdapter.class);
        verifyNoMoreInteractions(terminalService);
        verifyNoMoreInteractions(mapper);
    }
}