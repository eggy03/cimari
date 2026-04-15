/*
 * SPDX-License-Identifier: MIT
 * SPDX-FileCopyrightText: 2025 The ferrumx-windows contributors
 * SPDX-FileCopyrightText: 2026 Cimari contributors
 */
package io.github.eggy03.cimari.service.memory;


import io.github.eggy03.cimari.entity.memory.Win32PhysicalMemory;
import io.github.eggy03.cimari.mapping.memory.Win32PhysicalMemoryMapper;
import io.github.eggy03.cimari.shell.query.Cimv2;
import io.github.eggy03.cimari.terminal.TerminalResult;
import io.github.eggy03.cimari.terminal.TerminalService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import tools.jackson.core.JacksonException;

import java.math.BigInteger;
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
class Win32PhysicalMemoryServiceTest {

    private final TerminalResult validTerminalResult = new TerminalResult("{}", "");
    private final TerminalResult invalidTerminalResult = new TerminalResult("invalid json", "");
    private final TerminalResult emptyTerminalResult = new TerminalResult("", "");

    private final Win32PhysicalMemory expectedMemory1 = Win32PhysicalMemory.builder()
            .tag("PhysicalMemory1")
            .name("Corsair Vengeance LPX DDR4")
            .manufacturer("Corsair")
            .model("CMK16GX4M2B3200C16")
            .otherIdentifyingInfo("DDR4-3200 16GB Module")
            .partNumber("CMK16GX4M2B3200C16")
            .formFactor(8)
            .bankLabel("BANK 0")
            .capacity(BigInteger.valueOf(16L * 1024 * 1024 * 1024))
            .dataWidth(64)
            .speed(3200L)
            .configuredClockSpeed(3200L)
            .deviceLocator("DIMM_A1")
            .serialNumber("ABC123456789")
            .build();
    private final Win32PhysicalMemory expectedMemory2 = Win32PhysicalMemory.builder()
            .tag("PhysicalMemory2")
            .name("G.Skill Trident Z5 DDR5")
            .manufacturer("G.Skill")
            .model("F5-6000J3238F16GX2-TZ5RK")
            .otherIdentifyingInfo("DDR5-6000 16GB Module")
            .partNumber("F5-6000J3238F16GX2-TZ5RK")
            .formFactor(8)
            .bankLabel("BANK 1")
            .capacity(BigInteger.valueOf(16L * 1024 * 1024 * 1024))
            .dataWidth(64)
            .speed(6000L)
            .configuredClockSpeed(6000L)
            .deviceLocator("DIMM_B1")
            .serialNumber("XYZ987654321")
            .build();

    @Mock
    private TerminalService terminalService;

    @Mock
    private Win32PhysicalMemoryMapper mapper;

    @InjectMocks
    private Win32PhysicalMemoryService service;

    @Test
    void test_get_serviceReturnsMapperResult() {

        when(terminalService.executeQuery(any(Cimv2.class), anyLong()))
                .thenReturn(validTerminalResult);

        when(mapper.mapToList(anyString(), any()))
                .thenReturn(Arrays.asList(expectedMemory1, expectedMemory2));

        List<Win32PhysicalMemory> response = service.get(5L);
        assertThat(response).contains(expectedMemory1, expectedMemory2); // Service should return mapper result unchanged

        verify(terminalService).executeQuery(Cimv2.WIN32_PHYSICAL_MEMORY, 5L);
        verify(mapper).mapToList(validTerminalResult.getResult(), Win32PhysicalMemory.class);
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

        verify(terminalService).executeQuery(Cimv2.WIN32_PHYSICAL_MEMORY, 5L);
        verify(mapper).mapToList(invalidTerminalResult.getResult(), Win32PhysicalMemory.class);
        verifyNoMoreInteractions(terminalService);
        verifyNoMoreInteractions(mapper);
    }

    @Test
    void test_get_serviceReturnsEmpty_whenMapperReturnsEmpty() {

        when(terminalService.executeQuery(any(Cimv2.class), anyLong()))
                .thenReturn(emptyTerminalResult);

        when(mapper.mapToList(anyString(), any()))
                .thenReturn(Collections.emptyList());

        List<Win32PhysicalMemory> response = service.get(5L);
        assertThat(response).isEmpty();

        verify(terminalService).executeQuery(Cimv2.WIN32_PHYSICAL_MEMORY, 5L);
        verify(mapper).mapToList(emptyTerminalResult.getResult(), Win32PhysicalMemory.class);
        verifyNoMoreInteractions(terminalService);
        verifyNoMoreInteractions(mapper);
    }
}
