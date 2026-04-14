/*
 * SPDX-License-Identifier: MIT
 * SPDX-FileCopyrightText: 2025 The ferrumx-windows contributors
 * SPDX-FileCopyrightText: 2026 Cimari contributors
 */
package io.github.eggy03.cimari.service.peripheral;

import com.google.gson.JsonSyntaxException;
import io.github.eggy03.cimari.entity.peripheral.Win32SoundDevice;
import io.github.eggy03.cimari.mapping.peripheral.Win32SoundDeviceMapper;
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
class Win32SoundDeviceServiceTest {

    private final TerminalResult validTerminalResult = new TerminalResult("{}", "");
    private final TerminalResult invalidTerminalResult = new TerminalResult("invalid json", "");
    private final TerminalResult emptyTerminalResult = new TerminalResult("", "");

    private final Win32SoundDevice expectedDevice1 = Win32SoundDevice.builder()
            .deviceId("AUDIO\\0001")
            .name("Realtek High Definition Audio")
            .pnpDeviceId("HDAUDIO\\FUNC_01&VEN_10EC&DEV_0256&SUBSYS_10431A00&REV_1000")
            .manufacturer("Realtek Semiconductor Corp.")
            .status("OK")
            .statusInfo(3)
            .build();
    private final Win32SoundDevice expectedDevice2 = Win32SoundDevice.builder()
            .deviceId("AUDIO\\0002")
            .name("NVIDIA High Definition Audio")
            .pnpDeviceId("HDAUDIO\\FUNC_01&VEN_10DE&DEV_0080&SUBSYS_10DE1467&REV_1001")
            .manufacturer("NVIDIA Corporation")
            .status("OK")
            .statusInfo(3)
            .build();

    @Mock
    private TerminalService terminalService;

    @Mock
    private Win32SoundDeviceMapper mapper;

    @InjectMocks
    private Win32SoundDeviceService service;

    @Test
    void test_get_serviceReturnsMapperResult() {

        when(terminalService.executeQuery(any(Cimv2.class), anyLong()))
                .thenReturn(validTerminalResult);

        when(mapper.mapToList(anyString(), any()))
                .thenReturn(Arrays.asList(expectedDevice1, expectedDevice2));

        List<Win32SoundDevice> response = service.get(5L);
        assertThat(response).contains(expectedDevice1, expectedDevice2); // Service should return mapper result unchanged

        verify(terminalService).executeQuery(Cimv2.WIN32_SOUND_DEVICE, 5L);
        verify(mapper).mapToList(validTerminalResult.getResult(), Win32SoundDevice.class);
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

        verify(terminalService).executeQuery(Cimv2.WIN32_SOUND_DEVICE, 5L);
        verify(mapper).mapToList(invalidTerminalResult.getResult(), Win32SoundDevice.class);
        verifyNoMoreInteractions(terminalService);
        verifyNoMoreInteractions(mapper);
    }

    @Test
    void test_get_serviceReturnsEmpty_whenMapperReturnsEmpty() {

        when(terminalService.executeQuery(any(Cimv2.class), anyLong()))
                .thenReturn(emptyTerminalResult);

        when(mapper.mapToList(anyString(), any()))
                .thenReturn(Collections.emptyList());

        List<Win32SoundDevice> response = service.get(5L);
        assertThat(response).isEmpty();

        verify(terminalService).executeQuery(Cimv2.WIN32_SOUND_DEVICE, 5L);
        verify(mapper).mapToList(emptyTerminalResult.getResult(), Win32SoundDevice.class);
        verifyNoMoreInteractions(terminalService);
        verifyNoMoreInteractions(mapper);
    }

}
