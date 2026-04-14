/*
 * SPDX-License-Identifier: MIT
 * SPDX-FileCopyrightText: 2025 The ferrumx-windows contributors
 * SPDX-FileCopyrightText: 2026 Cimari contributors
 */
package io.github.eggy03.cimari.service.compounded;

import com.google.gson.JsonSyntaxException;
import io.github.eggy03.cimari.entity.compounded.Win32NetworkAdapterToConfiguration;
import io.github.eggy03.cimari.mapping.compounded.Win32NetworkAdapterToConfigurationMapper;
import io.github.eggy03.cimari.shell.script.ScriptEnum;
import io.github.eggy03.cimari.terminal.TerminalResult;
import io.github.eggy03.cimari.terminal.TerminalService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

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
class Win32NetworkAdapterToConfigurationServiceTest {

    private final TerminalResult validTerminalResult = new TerminalResult("{}", "");
    private final TerminalResult invalidTerminalResult = new TerminalResult("invalid json", "");
    private final TerminalResult emptyTerminalResult = new TerminalResult("", "");

    private final Win32NetworkAdapterToConfiguration expectedObject
            = Win32NetworkAdapterToConfiguration.builder()
            .deviceId("1L")
            .build();

    @Mock
    private TerminalService terminalService;

    @Mock
    private Win32NetworkAdapterToConfigurationMapper mapper;

    @InjectMocks
    private Win32NetworkAdapterToConfigurationService service;

    @Test
    void test_get_serviceReturnsMapperResult() {

        when(terminalService.executeScript(any(), anyLong()))
                .thenReturn(validTerminalResult);

        when(mapper.mapToList(anyString(), any()))
                .thenReturn(Collections.singletonList(expectedObject));

        List<Win32NetworkAdapterToConfiguration> response = service.get(5L);
        assertThat(response).contains(expectedObject); // Service should return mapper result unchanged

        verify(terminalService).executeScript(ScriptEnum.WIN32_NETWORK_ADAPTER_TO_CONFIGURATION, 5L);
        verify(mapper).mapToList(validTerminalResult.getResult(), Win32NetworkAdapterToConfiguration.class);
        verifyNoMoreInteractions(terminalService);
        verifyNoMoreInteractions(mapper);
    }

    @Test
    void test_get_mapperThrows_servicePropagatesException() {

        when(terminalService.executeScript(any(), anyLong()))
                .thenReturn(invalidTerminalResult);

        when(mapper.mapToList(anyString(), any()))
                .thenThrow(JsonSyntaxException.class);

        assertThrows(JsonSyntaxException.class, () -> service.get(5L));

        verify(terminalService).executeScript(ScriptEnum.WIN32_NETWORK_ADAPTER_TO_CONFIGURATION, 5L);
        verify(mapper).mapToList(invalidTerminalResult.getResult(), Win32NetworkAdapterToConfiguration.class);
        verifyNoMoreInteractions(terminalService);
        verifyNoMoreInteractions(mapper);
    }

    @Test
    void test_get_serviceReturnsEmpty_whenMapperReturnsEmpty() {

        when(terminalService.executeScript(any(), anyLong()))
                .thenReturn(emptyTerminalResult);

        when(mapper.mapToList(anyString(), any()))
                .thenReturn(Collections.emptyList());

        List<Win32NetworkAdapterToConfiguration> response = service.get(5L);
        assertThat(response).isEmpty();

        verify(terminalService).executeScript(ScriptEnum.WIN32_NETWORK_ADAPTER_TO_CONFIGURATION, 5L);
        verify(mapper).mapToList(emptyTerminalResult.getResult(), Win32NetworkAdapterToConfiguration.class);
        verifyNoMoreInteractions(terminalService);
        verifyNoMoreInteractions(mapper);
    }
}
