/*
 * SPDX-License-Identifier: MIT
 * SPDX-FileCopyrightText: 2025 The ferrumx-windows contributors
 * SPDX-FileCopyrightText: 2026 Cimari contributors
 */
package io.github.eggy03.cimari.service.network;


import io.github.eggy03.cimari.entity.network.Win32NetworkAdapterSetting;
import io.github.eggy03.cimari.mapping.network.Win32NetworkAdapterSettingMapper;
import io.github.eggy03.cimari.shell.query.Cimv2;
import io.github.eggy03.cimari.terminal.TerminalResult;
import io.github.eggy03.cimari.terminal.TerminalService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import tools.jackson.core.JacksonException;

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
class Win32NetworkAdapterSettingServiceTest {

    private final TerminalResult validTerminalResult = new TerminalResult("{}", "");
    private final TerminalResult invalidTerminalResult = new TerminalResult("invalid json", "");
    private final TerminalResult emptyTerminalResult = new TerminalResult("", "");

    private final Win32NetworkAdapterSetting expectedEthernetSetting = Win32NetworkAdapterSetting.builder()
            .networkAdapterDeviceId("1")
            .networkAdapterConfigurationIndex(1)
            .build();
    private final Win32NetworkAdapterSetting expectedWifiSetting = Win32NetworkAdapterSetting.builder()
            .networkAdapterDeviceId("2")
            .networkAdapterConfigurationIndex(2)
            .build();

    @Mock
    private TerminalService terminalService;

    @Mock
    private Win32NetworkAdapterSettingMapper mapper;

    @InjectMocks
    private Win32NetworkAdapterSettingService service;

    @Test
    void test_get_serviceReturnsMapperResult() {

        when(terminalService.executeQuery(any(Cimv2.class), anyLong()))
                .thenReturn(validTerminalResult);

        when(mapper.mapToList(anyString(), any()))
                .thenReturn(Arrays.asList(expectedEthernetSetting, expectedWifiSetting));

        List<Win32NetworkAdapterSetting> response = service.get(5L);
        assertThat(response).contains(expectedEthernetSetting, expectedWifiSetting); // Service should return mapper result unchanged

        verify(terminalService).executeQuery(Cimv2.WIN32_NETWORK_ADAPTER_SETTING, 5L);
        verify(mapper).mapToList(validTerminalResult.getResult(), Win32NetworkAdapterSetting.class);
        verifyNoMoreInteractions(terminalService);
        verifyNoMoreInteractions(mapper);
    }

    @Test
    void test_get_mapperThrows_servicePropagatesException() {

        when(terminalService.executeQuery(any(Cimv2.class), anyLong()))
                .thenReturn(invalidTerminalResult);

        when(mapper.mapToList(anyString(), any()))
                .thenThrow(JacksonException.class);

        assertThrows(JacksonException.class, () -> service.get(5L));

        verify(terminalService).executeQuery(Cimv2.WIN32_NETWORK_ADAPTER_SETTING, 5L);
        verify(mapper).mapToList(invalidTerminalResult.getResult(), Win32NetworkAdapterSetting.class);
        verifyNoMoreInteractions(terminalService);
        verifyNoMoreInteractions(mapper);
    }

    @Test
    void test_get_serviceReturnsEmpty_whenMapperReturnsEmpty() {

        when(terminalService.executeQuery(any(Cimv2.class), anyLong()))
                .thenReturn(emptyTerminalResult);

        when(mapper.mapToList(anyString(), any()))
                .thenReturn(Collections.emptyList());

        List<Win32NetworkAdapterSetting> response = service.get(5L);
        assertThat(response).isEmpty();

        verify(terminalService).executeQuery(Cimv2.WIN32_NETWORK_ADAPTER_SETTING, 5L);
        verify(mapper).mapToList(emptyTerminalResult.getResult(), Win32NetworkAdapterSetting.class);
        verifyNoMoreInteractions(terminalService);
        verifyNoMoreInteractions(mapper);
    }
}
