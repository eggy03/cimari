/*
 * SPDX-License-Identifier: MIT
 * SPDX-FileCopyrightText: 2025 The ferrumx-windows contributors
 * SPDX-FileCopyrightText: 2026 Cimari contributors
 */
package io.github.eggy03.cimari.service.mainboard;


import io.github.eggy03.cimari.entity.mainboard.ImmutableWin32Baseboard;
import io.github.eggy03.cimari.entity.mainboard.Win32Baseboard;
import io.github.eggy03.cimari.mapping.mainboard.Win32BaseboardMapper;
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
class Win32BaseboardServiceTest {

    private final TerminalResult validTerminalResult = new TerminalResult("{}", "");
    private final TerminalResult invalidTerminalResult = new TerminalResult("invalid json", "");
    private final TerminalResult emptyTerminalResult = new TerminalResult("", "");

    private final Win32Baseboard expectedBoard1 =
            new ImmutableWin32Baseboard.Builder()
                    .manufacturer("ASUS")
                    .model("ROG STRIX Z790-E GAMING WIFI")
                    .product("Z790-E")
                    .serialNumber("ABC123456789")
                    .version("Rev 1.xx")
                    .build();

    private final Win32Baseboard expectedBoard2 =
            new ImmutableWin32Baseboard.Builder()
                    .manufacturer("MSI")
                    .model("MAG B650 TOMAHAWK WIFI")
                    .product("B650 TOMAHAWK")
                    .serialNumber("XYZ987654321")
                    .version("Rev 2.00")
                    .build();

    @Mock
    private TerminalService terminalService;

    @Mock
    private Win32BaseboardMapper mapper;

    @InjectMocks
    private Win32BaseboardService service;

    @Test
    void test_get_serviceReturnsMapperResult() {

        when(terminalService.executeQuery(any(Cimv2.class), anyLong()))
                .thenReturn(validTerminalResult);

        when(mapper.mapToList(anyString(), any()))
                .thenReturn(Arrays.asList(expectedBoard1, expectedBoard2));

        List<Win32Baseboard> response = service.get(5L);
        assertThat(response).contains(expectedBoard1, expectedBoard2); // Service should return mapper result unchanged

        verify(terminalService).executeQuery(Cimv2.WIN32_BASEBOARD, 5L);
        verify(mapper).mapToList(validTerminalResult.getResult(), Win32Baseboard.class);
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

        verify(terminalService).executeQuery(Cimv2.WIN32_BASEBOARD, 5L);
        verify(mapper).mapToList(invalidTerminalResult.getResult(), Win32Baseboard.class);
        verifyNoMoreInteractions(terminalService);
        verifyNoMoreInteractions(mapper);
    }

    @Test
    void test_get_serviceReturnsEmpty_whenMapperReturnsEmpty() {

        when(terminalService.executeQuery(any(Cimv2.class), anyLong()))
                .thenReturn(emptyTerminalResult);

        when(mapper.mapToList(anyString(), any()))
                .thenReturn(Collections.emptyList());

        List<Win32Baseboard> response = service.get(5L);
        assertThat(response).isEmpty();

        verify(terminalService).executeQuery(Cimv2.WIN32_BASEBOARD, 5L);
        verify(mapper).mapToList(emptyTerminalResult.getResult(), Win32Baseboard.class);
        verifyNoMoreInteractions(terminalService);
        verifyNoMoreInteractions(mapper);
    }
}

