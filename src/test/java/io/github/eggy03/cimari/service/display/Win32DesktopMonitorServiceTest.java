/*
 * SPDX-License-Identifier: MIT
 * SPDX-FileCopyrightText: 2025 The ferrumx-windows contributors
 * SPDX-FileCopyrightText: 2026 Cimari contributors
 */
package io.github.eggy03.cimari.service.display;


import io.github.eggy03.cimari.entity.display.ImmutableWin32DesktopMonitor;
import io.github.eggy03.cimari.entity.display.Win32DesktopMonitor;
import io.github.eggy03.cimari.mapping.display.Win32DesktopMonitorMapper;
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
class Win32DesktopMonitorServiceTest {

    private final TerminalResult validTerminalResult = new TerminalResult("{}", "");
    private final TerminalResult invalidTerminalResult = new TerminalResult("invalid json", "");
    private final TerminalResult emptyTerminalResult = new TerminalResult("", "");

    private final Win32DesktopMonitor expectedMonitor1 = new ImmutableWin32DesktopMonitor.Builder()
            .deviceId("MON1")
            .name("Dell U2720Q")
            .pnpDeviceId("DISPLAY\\\\DELA0B1\\\\5&12345&0&UID4352")
            .status("OK")
            .monitorManufacturer("Dell Inc.")
            .monitorType("LCD")
            .pixelsPerXLogicalInch(96)
            .pixelsPerYLogicalInch(96)
            .build();

    private final Win32DesktopMonitor expectedMonitor2 = new ImmutableWin32DesktopMonitor.Builder()
            .deviceId("MON2")
            .name("LG UltraGear 27GL850")
            .pnpDeviceId("DISPLAY\\\\LGD1234\\\\5&67890&0&UID9832")
            .status("OK")
            .monitorManufacturer("LG Electronics")
            .monitorType("LED")
            .pixelsPerXLogicalInch(110)
            .pixelsPerYLogicalInch(110)
            .build();

    @Mock
    private TerminalService terminalService;

    @Mock
    private Win32DesktopMonitorMapper mapper;

    @InjectMocks
    private Win32DesktopMonitorService service;

    @Test
    void test_get_serviceReturnsMapperResult() {

        when(terminalService.executeQuery(any(Cimv2.class), anyLong()))
                .thenReturn(validTerminalResult);

        when(mapper.mapToList(anyString(), any()))
                .thenReturn(Arrays.asList(expectedMonitor1, expectedMonitor2));

        List<Win32DesktopMonitor> response = service.get(5L);
        assertThat(response).contains(expectedMonitor1, expectedMonitor2); // Service should return mapper result unchanged

        verify(terminalService).executeQuery(Cimv2.WIN32_DESKTOP_MONITOR, 5L);
        verify(mapper).mapToList(validTerminalResult.getResult(), Win32DesktopMonitor.class);
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

        verify(terminalService).executeQuery(Cimv2.WIN32_DESKTOP_MONITOR, 5L);
        verify(mapper).mapToList(invalidTerminalResult.getResult(), Win32DesktopMonitor.class);
        verifyNoMoreInteractions(terminalService);
        verifyNoMoreInteractions(mapper);
    }

    @Test
    void test_get_serviceReturnsEmpty_whenMapperReturnsEmpty() {

        when(terminalService.executeQuery(any(Cimv2.class), anyLong()))
                .thenReturn(emptyTerminalResult);

        when(mapper.mapToList(anyString(), any()))
                .thenReturn(Collections.emptyList());

        List<Win32DesktopMonitor> response = service.get(5L);
        assertThat(response).isEmpty();

        verify(terminalService).executeQuery(Cimv2.WIN32_DESKTOP_MONITOR, 5L);
        verify(mapper).mapToList(emptyTerminalResult.getResult(), Win32DesktopMonitor.class);
        verifyNoMoreInteractions(terminalService);
        verifyNoMoreInteractions(mapper);
    }
}
