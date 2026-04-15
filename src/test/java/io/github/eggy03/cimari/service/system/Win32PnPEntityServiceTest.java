/*
 * SPDX-License-Identifier: MIT
 * SPDX-FileCopyrightText: 2025 The ferrumx-windows contributors
 * SPDX-FileCopyrightText: 2026 Cimari contributors
 */
package io.github.eggy03.cimari.service.system;


import io.github.eggy03.cimari.entity.system.Win32PnPEntity;
import io.github.eggy03.cimari.mapping.system.Win32PnPEntityMapper;
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
class Win32PnPEntityServiceTest {

    private final TerminalResult validTerminalResult = new TerminalResult("{}", "");
    private final TerminalResult invalidTerminalResult = new TerminalResult("invalid json", "");
    private final TerminalResult emptyTerminalResult = new TerminalResult("", "");

    private final Win32PnPEntity expectedDevice1 = Win32PnPEntity.builder()
            .deviceId("USB\\VID_045E&PID_07A5\\6&1A2C0F8&0&2")
            .pnpDeviceId("USB\\VID_045E&PID_07A5\\6&1A2C0F8&0&2")
            .hardwareId(Collections.singletonList("USB\\VID_045E&PID_07A5&REV_0100"))
            .compatibleId(Collections.singletonList("USB\\Class_03&SubClass_01&Prot_02"))
            .name("USB Composite Device")
            .description("Generic USB Composite Device")
            .manufacturer("Microsoft")
            .present(true)
            .status("OK")
            .build();

    private final Win32PnPEntity expectedDevice2 = Win32PnPEntity.builder()
            .deviceId("PCI\\VEN_10DE&DEV_1C82&SUBSYS_85AE1043&REV_A1\\4&2D77E6E1&0&0008")
            .pnpDeviceId("PCI\\VEN_10DE&DEV_1C82&SUBSYS_85AE1043&REV_A1\\4&2D77E6E1&0&0008")
            .hardwareId(Arrays.asList("PCI\\VEN_10DE&DEV_1C82&SUBSYS_85AE1043", "PCI\\VEN_10DE&DEV_1C82"))
            .compatibleId(Collections.singletonList("PCI\\CC_030000"))
            .name("NVIDIA GeForce GTX 1050 Ti")
            .description("Display Adapter")
            .manufacturer("NVIDIA")
            .present(true)
            .status("OK")
            .build();

    @Mock
    private TerminalService terminalService;

    @Mock
    private Win32PnPEntityMapper mapper;

    @InjectMocks
    private Win32PnPEntityService service;

    @Test
    void test_get_serviceReturnsMapperResult() {

        when(terminalService.executeQuery(any(Cimv2.class), anyLong()))
                .thenReturn(validTerminalResult);

        when(mapper.mapToList(anyString(), any()))
                .thenReturn(Arrays.asList(expectedDevice1, expectedDevice2));

        List<Win32PnPEntity> response = service.get(5L);
        assertThat(response).contains(expectedDevice1, expectedDevice1); // Service should return mapper result unchanged

        verify(terminalService).executeQuery(Cimv2.WIN32_PNP_ENTITY, 5L);
        verify(mapper).mapToList(validTerminalResult.getResult(), Win32PnPEntity.class);
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

        verify(terminalService).executeQuery(Cimv2.WIN32_PNP_ENTITY, 5L);
        verify(mapper).mapToList(invalidTerminalResult.getResult(), Win32PnPEntity.class);
        verifyNoMoreInteractions(terminalService);
        verifyNoMoreInteractions(mapper);
    }

    @Test
    void test_get_serviceReturnsEmpty_whenMapperReturnsEmpty() {

        when(terminalService.executeQuery(any(Cimv2.class), anyLong()))
                .thenReturn(emptyTerminalResult);

        when(mapper.mapToList(anyString(), any()))
                .thenReturn(Collections.emptyList());

        List<Win32PnPEntity> response = service.get(5L);
        assertThat(response).isEmpty();

        verify(terminalService).executeQuery(Cimv2.WIN32_PNP_ENTITY, 5L);
        verify(mapper).mapToList(emptyTerminalResult.getResult(), Win32PnPEntity.class);
        verifyNoMoreInteractions(terminalService);
        verifyNoMoreInteractions(mapper);
    }
}