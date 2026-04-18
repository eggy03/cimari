/*
 * SPDX-License-Identifier: MIT
 * SPDX-FileCopyrightText: 2025 The ferrumx-windows contributors
 * SPDX-FileCopyrightText: 2026 Cimari contributors
 */
package io.github.eggy03.cimari.service.system;


import io.github.eggy03.cimari.entity.system.Win32OperatingSystem;
import io.github.eggy03.cimari.mapping.system.Win32OperatingSystemMapper;
import io.github.eggy03.cimari.shell.query.Cimv2;
import io.github.eggy03.cimari.terminal.TerminalResult;
import io.github.eggy03.cimari.terminal.TerminalService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import tools.jackson.core.JacksonException;

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
class Win32OperatingSystemServiceTest {

    private final TerminalResult validTerminalResult = new TerminalResult("{}", "");
    private final TerminalResult invalidTerminalResult = new TerminalResult("invalid json", "");
    private final TerminalResult emptyTerminalResult = new TerminalResult("", "");

    private final Win32OperatingSystem expectedOs = new Win32OperatingSystem.Builder()
            .name("Microsoft Windows 11 Pro|C:\\WINDOWS|\\Device\\Harddisk0\\Partition4")
            .caption("Microsoft Windows 11 Pro")
            .installDate("20240101000000.000000+330")
            .csName("DESKTOP-12345")
            .lastBootUpTime("20241102123000.000000+330")
            .localDateTime("20251103180000.000000+330")
            .distributed(false)
            .numberOfUsers(1)
            .version("10.0.22631")
            .bootDevice("\\Device\\HarddiskVolume3")
            .buildNumber("22631")
            .buildType("Multiprocessor Free")
            .manufacturer("Microsoft Corporation")
            .osArchitecture("64-bit")
            .muiLanguages(Collections.singletonList("en-US"))
            .portableOperatingSystem(false)
            .primary(true)
            .registeredUser("User")
            .serialNumber("00330-80000-00000-AA123")
            .servicePackMajorVersion(0)
            .servicePackMinorVersion(0)
            .systemDirectory("C:\\WINDOWS\\system32")
            .systemDrive("C:")
            .windowsDirectory("C:\\WINDOWS")
            .build();

    @Mock
    private TerminalService terminalService;

    @Mock
    private Win32OperatingSystemMapper mapper;

    @InjectMocks
    private Win32OperatingSystemService service;

    @Test
    void test_get_serviceReturnsMapperResult() {

        when(terminalService.executeQuery(any(Cimv2.class), anyLong()))
                .thenReturn(validTerminalResult);

        when(mapper.mapToList(anyString(), any()))
                .thenReturn(Collections.singletonList(expectedOs));

        List<Win32OperatingSystem> response = service.get(5L);
        assertThat(response).contains(expectedOs); // Service should return mapper result unchanged

        verify(terminalService).executeQuery(Cimv2.WIN32_OPERATING_SYSTEM, 5L);
        verify(mapper).mapToList(validTerminalResult.getResult(), Win32OperatingSystem.class);
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

        verify(terminalService).executeQuery(Cimv2.WIN32_OPERATING_SYSTEM, 5L);
        verify(mapper).mapToList(invalidTerminalResult.getResult(), Win32OperatingSystem.class);
        verifyNoMoreInteractions(terminalService);
        verifyNoMoreInteractions(mapper);
    }

    @Test
    void test_get_serviceReturnsEmpty_whenMapperReturnsEmpty() {

        when(terminalService.executeQuery(any(Cimv2.class), anyLong()))
                .thenReturn(emptyTerminalResult);

        when(mapper.mapToList(anyString(), any()))
                .thenReturn(Collections.emptyList());

        List<Win32OperatingSystem> response = service.get(5L);
        assertThat(response).isEmpty();

        verify(terminalService).executeQuery(Cimv2.WIN32_OPERATING_SYSTEM, 5L);
        verify(mapper).mapToList(emptyTerminalResult.getResult(), Win32OperatingSystem.class);
        verifyNoMoreInteractions(terminalService);
        verifyNoMoreInteractions(mapper);
    }
}