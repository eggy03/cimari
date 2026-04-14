/*
 * SPDX-License-Identifier: MIT
 * SPDX-FileCopyrightText: 2025 The ferrumx-windows contributors
 * SPDX-FileCopyrightText: 2026 Cimari contributors
 */
package io.github.eggy03.cimari.service.peripheral;

import com.google.gson.JsonSyntaxException;
import io.github.eggy03.cimari.entity.peripheral.Win32Battery;
import io.github.eggy03.cimari.mapping.peripheral.Win32BatteryMapper;
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
class Win32BatteryServiceTest {

    private final TerminalResult validTerminalResult = new TerminalResult("{}", "");
    private final TerminalResult invalidTerminalResult = new TerminalResult("invalid json", "");
    private final TerminalResult emptyTerminalResult = new TerminalResult("", "");

    private final Win32Battery expectedPrimaryBattery = Win32Battery.builder()
            .deviceId("BAT0")
            .caption("Primary Battery")
            .description("Internal Lithium-Ion Battery")
            .name("Battery #1")
            .status("OK")
            .powerManagementCapabilities(Arrays.asList(1, 2, 3))
            .powerManagementSupported(true)
            .batteryStatus(2)
            .chemistry(6)
            .designCapacity(50000)
            .designVoltage(11000)
            .estimatedChargeRemaining(87L)
            .estimatedRunTime(120L)
            .build();
    private final Win32Battery expectedSecondaryBattery = Win32Battery.builder()
            .deviceId("BAT1")
            .caption("Backup Battery")
            .description("External Lithium-Polymer Battery")
            .name("Battery #2")
            .status("Charging")
            .powerManagementCapabilities(Arrays.asList(1, 2))
            .powerManagementSupported(true)
            .batteryStatus(6)
            .chemistry(7)
            .designCapacity(30000)
            .designVoltage(7400)
            .estimatedChargeRemaining(45L)
            .estimatedRunTime(60L)
            .build();

    @Mock
    private TerminalService terminalService;

    @Mock
    private Win32BatteryMapper mapper;

    @InjectMocks
    private Win32BatteryService service;

    @Test
    void test_get_serviceReturnsMapperResult() {

        when(terminalService.executeQuery(any(Cimv2.class), anyLong()))
                .thenReturn(validTerminalResult);

        when(mapper.mapToList(anyString(), any()))
                .thenReturn(Arrays.asList(expectedPrimaryBattery, expectedSecondaryBattery));

        List<Win32Battery> response = service.get(5L);
        assertThat(response).contains(expectedPrimaryBattery, expectedSecondaryBattery); // Service should return mapper result unchanged

        verify(terminalService).executeQuery(Cimv2.WIN32_BATTERY, 5L);
        verify(mapper).mapToList(validTerminalResult.getResult(), Win32Battery.class);
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

        verify(terminalService).executeQuery(Cimv2.WIN32_BATTERY, 5L);
        verify(mapper).mapToList(invalidTerminalResult.getResult(), Win32Battery.class);
        verifyNoMoreInteractions(terminalService);
        verifyNoMoreInteractions(mapper);
    }

    @Test
    void test_get_serviceReturnsEmpty_whenMapperReturnsEmpty() {

        when(terminalService.executeQuery(any(Cimv2.class), anyLong()))
                .thenReturn(emptyTerminalResult);

        when(mapper.mapToList(anyString(), any()))
                .thenReturn(Collections.emptyList());

        List<Win32Battery> response = service.get(5L);
        assertThat(response).isEmpty();

        verify(terminalService).executeQuery(Cimv2.WIN32_BATTERY, 5L);
        verify(mapper).mapToList(emptyTerminalResult.getResult(), Win32Battery.class);
        verifyNoMoreInteractions(terminalService);
        verifyNoMoreInteractions(mapper);
    }
}