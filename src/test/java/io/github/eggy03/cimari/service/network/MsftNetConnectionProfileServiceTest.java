/*
 * SPDX-License-Identifier: MIT
 * SPDX-FileCopyrightText: 2025 The ferrumx-windows contributors
 * SPDX-FileCopyrightText: 2026 Cimari contributors
 */
package io.github.eggy03.cimari.service.network;

import com.google.gson.JsonSyntaxException;
import io.github.eggy03.cimari.entity.network.MsftNetConnectionProfile;
import io.github.eggy03.cimari.mapping.network.MsftNetConnectionProfileMapper;
import io.github.eggy03.cimari.shell.query.StandardCimv2;
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
class MsftNetConnectionProfileServiceTest {

    private final TerminalResult validTerminalResult = new TerminalResult("{}", "");
    private final TerminalResult invalidTerminalResult = new TerminalResult("invalid json", "");
    private final TerminalResult emptyTerminalResult = new TerminalResult("", "");

    private final MsftNetConnectionProfile expectedEthernetProfile = MsftNetConnectionProfile.builder()
            .interfaceIndex(1L)
            .interfaceAlias("Ethernet")
            .networkCategory(1L) // Private
            .domainAuthenticationKind(0L) // None
            .ipv4Connectivity(4L) // Internet
            .ipv6Connectivity(1L) // NoTraffic
            .build();

    private final MsftNetConnectionProfile expectedWifiProfile = MsftNetConnectionProfile.builder()
            .interfaceIndex(2L)
            .interfaceAlias("Wi-Fi")
            .networkCategory(0L) // Public
            .domainAuthenticationKind(0L) // None
            .ipv4Connectivity(4L) // Internet
            .ipv6Connectivity(4L) // Internet
            .build();

    @Mock
    private TerminalService terminalService;

    @Mock
    private MsftNetConnectionProfileMapper mapper;

    @InjectMocks
    private MsftNetConnectionProfileService service;

    @Test
    void test_get_serviceReturnsMapperResult() {

        when(terminalService.executeQuery(any(StandardCimv2.class), anyLong()))
                .thenReturn(validTerminalResult);

        when(mapper.mapToList(anyString(), any()))
                .thenReturn(Arrays.asList(expectedEthernetProfile, expectedWifiProfile));

        List<MsftNetConnectionProfile> response = service.get(5L);
        assertThat(response).contains(expectedEthernetProfile, expectedWifiProfile); // Service should return mapper result unchanged

        verify(terminalService).executeQuery(StandardCimv2.MSFT_NET_CONNECTION_PROFILE, 5L);
        verify(mapper).mapToList(validTerminalResult.getResult(), MsftNetConnectionProfile.class);
        verifyNoMoreInteractions(terminalService);
        verifyNoMoreInteractions(mapper);
    }

    @Test
    void test_get_mapperThrows_servicePropagatesException() {

        when(terminalService.executeQuery(any(StandardCimv2.class), anyLong()))
                .thenReturn(invalidTerminalResult);

        when(mapper.mapToList(anyString(), any()))
                .thenThrow(JsonSyntaxException.class);

        assertThrows(JsonSyntaxException.class, () -> service.get(5L));

        verify(terminalService).executeQuery(StandardCimv2.MSFT_NET_CONNECTION_PROFILE, 5L);
        verify(mapper).mapToList(invalidTerminalResult.getResult(), MsftNetConnectionProfile.class);
        verifyNoMoreInteractions(terminalService);
        verifyNoMoreInteractions(mapper);
    }

    @Test
    void test_get_serviceReturnsEmpty_whenMapperReturnsEmpty() {

        when(terminalService.executeQuery(any(StandardCimv2.class), anyLong()))
                .thenReturn(emptyTerminalResult);

        when(mapper.mapToList(anyString(), any()))
                .thenReturn(Collections.emptyList());

        List<MsftNetConnectionProfile> response = service.get(5L);
        assertThat(response).isEmpty();

        verify(terminalService).executeQuery(StandardCimv2.MSFT_NET_CONNECTION_PROFILE, 5L);
        verify(mapper).mapToList(emptyTerminalResult.getResult(), MsftNetConnectionProfile.class);
        verifyNoMoreInteractions(terminalService);
        verifyNoMoreInteractions(mapper);
    }
}
