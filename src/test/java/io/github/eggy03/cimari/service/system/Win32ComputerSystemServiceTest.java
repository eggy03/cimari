/*
 * SPDX-License-Identifier: MIT
 * SPDX-FileCopyrightText: 2025 The ferrumx-windows contributors
 * SPDX-FileCopyrightText: 2026 Cimari contributors
 */
package io.github.eggy03.cimari.service.system;

import io.github.eggy03.cimari.entity.system.ImmutableWin32ComputerSystem;
import io.github.eggy03.cimari.entity.system.Win32ComputerSystem;
import io.github.eggy03.cimari.mapping.system.Win32ComputerSystemMapper;
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
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class Win32ComputerSystemServiceTest {

    private final TerminalResult validTerminalResult = new TerminalResult("{}", "");
    private final TerminalResult invalidTerminalResult = new TerminalResult("invalid json", "");
    private final TerminalResult emptyTerminalResult = new TerminalResult("", "");

    private final Win32ComputerSystem expectedComputerSystem = new ImmutableWin32ComputerSystem.Builder()
            .adminPasswordStatus(3)
            .keyboardPasswordStatus(3)
            .powerOnPasswordStatus(3)
            .bootupState("Normal boot")
            .bootStatus(Arrays.asList(0, 1))
            .automaticResetBootOption(true)
            .powerState(1)
            .powerSupplyState(3)
            .powerManagementCapabilities(Arrays.asList(1, 2, 3))
            .powerManagementSupported(true)
            .resetCapability(4)
            .resetCount(1)
            .resetLimit(5)
            .frontPanelResetStatus(2)
            .automaticResetCapability(true)
            .name("DESKTOP-12345")
            .caption("Workstation PC")
            .description("High-end office workstation")
            .manufacturer("ASUSTeK COMPUTER INC.")
            .model("ProArt B760-Creator D4")
            .primaryOwnerName("User")
            .primaryOwnerContact("user@example.com")
            .roles(Arrays.asList("LM_Workstation", "LM_Server"))
            .chassisSKUNumber("PROD-1234")
            .systemSKUNumber("Default String")
            .systemFamily("Desktop")
            .systemType("x64-based PC")
            .userName("DESKTOP-12345\\User")
            .workgroup("WORKGROUP")
            .oemStringArray(Collections.singletonList("Default String"))
            .numberOfProcessors(1L)
            .numberOfLogicalProcessors(20L)
            .totalPhysicalMemory(BigInteger.valueOf(17122615296L))
            .automaticManagedPagefile(true)
            .infraredSupported(false)
            .networkServerModeEnabled(true)
            .hypervisorPresent(false)
            .thermalState(3)
            .currentTimeZone(330)
            .daylightInEffect(true)
            .build();

    @Mock
    private TerminalService terminalService;

    @Mock
    private Win32ComputerSystemMapper mapper;

    @InjectMocks
    private Win32ComputerSystemService service;

    @Test
    void test_get_serviceReturnsMapperResult() {

        when(terminalService.executeQuery(any(Cimv2.class), anyLong()))
                .thenReturn(validTerminalResult);

        when(mapper.mapToObject(anyString(), any()))
                .thenReturn(Optional.of(expectedComputerSystem));

        Optional<Win32ComputerSystem> response = service.get(5L);
        assertThat(response).contains(expectedComputerSystem); // Service should return mapper result unchanged

        verify(terminalService).executeQuery(Cimv2.WIN32_COMPUTER_SYSTEM, 5L);
        verify(mapper).mapToObject(validTerminalResult.getResult(), Win32ComputerSystem.class);
        verifyNoMoreInteractions(terminalService);
        verifyNoMoreInteractions(mapper);
    }

    @Test
    void test_get_mapperThrows_servicePropagatesException() {

        when(terminalService.executeQuery(any(Cimv2.class), anyLong()))
                .thenReturn(invalidTerminalResult);

        when(mapper.mapToObject(anyString(), any()))
                .thenThrow(JacksonException.class);

        assertThrows(JacksonException.class, () -> service.get(5L));

        verify(terminalService).executeQuery(Cimv2.WIN32_COMPUTER_SYSTEM, 5L);
        verify(mapper).mapToObject(invalidTerminalResult.getResult(), Win32ComputerSystem.class);
        verifyNoMoreInteractions(terminalService);
        verifyNoMoreInteractions(mapper);
    }

    @Test
    void test_get_serviceReturnsEmpty_whenMapperReturnsEmpty() {

        when(terminalService.executeQuery(any(Cimv2.class), anyLong()))
                .thenReturn(emptyTerminalResult);

        when(mapper.mapToObject(anyString(), any()))
                .thenReturn(Optional.empty());

        Optional<Win32ComputerSystem> response = service.get(5L);
        assertThat(response).isEmpty();

        verify(terminalService).executeQuery(Cimv2.WIN32_COMPUTER_SYSTEM, 5L);
        verify(mapper).mapToObject(emptyTerminalResult.getResult(), Win32ComputerSystem.class);
        verifyNoMoreInteractions(terminalService);
        verifyNoMoreInteractions(mapper);
    }
}
