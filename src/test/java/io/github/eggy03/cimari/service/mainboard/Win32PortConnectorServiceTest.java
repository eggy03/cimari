/*
 * SPDX-License-Identifier: MIT
 * SPDX-FileCopyrightText: 2025 The ferrumx-windows contributors
 * SPDX-FileCopyrightText: 2026 Cimari contributors
 */
package io.github.eggy03.cimari.service.mainboard;

import com.google.gson.JsonSyntaxException;
import io.github.eggy03.cimari.entity.mainboard.Win32PortConnector;
import io.github.eggy03.cimari.mapping.mainboard.Win32PortConnectorMapper;
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
class Win32PortConnectorServiceTest {

    private final TerminalResult validTerminalResult = new TerminalResult("{}", "");
    private final TerminalResult invalidTerminalResult = new TerminalResult("invalid json", "");
    private final TerminalResult emptyTerminalResult = new TerminalResult("", "");

    private final Win32PortConnector expectedPort1 = Win32PortConnector.builder()
            .tag("PortConnector1")
            .externalReferenceDesignator("USB3_0")
            .internalReferenceDesignator("JUSB1")
            .connectorType(Arrays.asList(1, 2, 3))
            .portType(1)
            .build();

    private final Win32PortConnector expectedPort2 = Win32PortConnector.builder()
            .tag("PortConnector2")
            .externalReferenceDesignator("HDMI_OUT")
            .internalReferenceDesignator("JHDMI1")
            .connectorType(Arrays.asList(4, 5, 6))
            .portType(2)
            .build();

    @Mock
    private TerminalService terminalService;

    @Mock
    private Win32PortConnectorMapper mapper;

    @InjectMocks
    private Win32PortConnectorService service;

    @Test
    void test_get_serviceReturnsMapperResult() {

        when(terminalService.executeQuery(any(Cimv2.class), anyLong()))
                .thenReturn(validTerminalResult);

        when(mapper.mapToList(anyString(), any()))
                .thenReturn(Arrays.asList(expectedPort1, expectedPort2));

        List<Win32PortConnector> response = service.get(5L);
        assertThat(response).contains(expectedPort1, expectedPort2); // Service should return mapper result unchanged

        verify(terminalService).executeQuery(Cimv2.WIN32_PORT_CONNECTOR, 5L);
        verify(mapper).mapToList(validTerminalResult.getResult(), Win32PortConnector.class);
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

        verify(terminalService).executeQuery(Cimv2.WIN32_PORT_CONNECTOR, 5L);
        verify(mapper).mapToList(invalidTerminalResult.getResult(), Win32PortConnector.class);
        verifyNoMoreInteractions(terminalService);
        verifyNoMoreInteractions(mapper);
    }

    @Test
    void test_get_serviceReturnsEmpty_whenMapperReturnsEmpty() {

        when(terminalService.executeQuery(any(Cimv2.class), anyLong()))
                .thenReturn(emptyTerminalResult);

        when(mapper.mapToList(anyString(), any()))
                .thenReturn(Collections.emptyList());

        List<Win32PortConnector> response = service.get(5L);
        assertThat(response).isEmpty();

        verify(terminalService).executeQuery(Cimv2.WIN32_PORT_CONNECTOR, 5L);
        verify(mapper).mapToList(emptyTerminalResult.getResult(), Win32PortConnector.class);
        verifyNoMoreInteractions(terminalService);
        verifyNoMoreInteractions(mapper);
    }

}