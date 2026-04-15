/*
 * SPDX-License-Identifier: MIT
 * SPDX-FileCopyrightText: 2025 The ferrumx-windows contributors
 * SPDX-FileCopyrightText: 2026 Cimari contributors
 */
package io.github.eggy03.cimari.service.network;


import io.github.eggy03.cimari.entity.network.Win32NetworkAdapterConfiguration;
import io.github.eggy03.cimari.mapping.network.Win32NetworkAdapterConfigurationMapper;
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
class Win32NetworkAdapterConfigurationServiceTest {

    private final TerminalResult validTerminalResult = new TerminalResult("{}", "");
    private final TerminalResult invalidTerminalResult = new TerminalResult("invalid json", "");
    private final TerminalResult emptyTerminalResult = new TerminalResult("", "");

    private final Win32NetworkAdapterConfiguration expectedEthernetConfig = Win32NetworkAdapterConfiguration.builder()
            .index(1)
            .description("Intel(R) Ethernet Connection I219-V")
            .caption("Ethernet Adapter Configuration")
            .settingId("{A1B2C3D4-E5F6-7890-1234-56789ABCDEF0}")
            .ipEnabled(true)
            .ipAddress(Collections.singletonList("192.168.0.101"))
            .ipSubnet(Collections.singletonList("255.255.255.0"))
            .defaultIpGateway(Collections.singletonList("192.168.0.1"))
            .dhcpEnabled(true)
            .dhcpServer("192.168.0.1")
            .dhcpLeaseObtained("2024-07-12T10:00:00Z")
            .dhcpLeaseExpires("2024-07-13T10:00:00Z")
            .dnsHostName("DESKTOP-ETHERNET")
            .dnsServerSearchOrder(Arrays.asList("8.8.8.8", "8.8.4.4"))
            .build();

    private final Win32NetworkAdapterConfiguration expectedWifiConfig = Win32NetworkAdapterConfiguration.builder()
            .index(2)
            .description("Intel(R) Wi-Fi 6 AX200 160MHz")
            .caption("Wi-Fi Adapter Configuration")
            .settingId("{B2C3D4E5-F6A1-2345-6789-0ABCDEF12345}")
            .ipEnabled(true)
            .ipAddress(Collections.singletonList("192.168.1.150"))
            .ipSubnet(Collections.singletonList("255.255.255.0"))
            .defaultIpGateway(Collections.singletonList("192.168.1.1"))
            .dhcpEnabled(true)
            .dhcpServer("192.168.1.1")
            .dhcpLeaseObtained("2024-07-12T11:00:00Z")
            .dhcpLeaseExpires("2024-07-13T11:00:00Z")
            .dnsHostName("LAPTOP-WIFI")
            .dnsServerSearchOrder(Arrays.asList("1.1.1.1", "1.0.0.1"))
            .build();

    @Mock
    private TerminalService terminalService;

    @Mock
    private Win32NetworkAdapterConfigurationMapper mapper;

    @InjectMocks
    private Win32NetworkAdapterConfigurationService service;

    @Test
    void test_get_serviceReturnsMapperResult() {

        when(terminalService.executeQuery(any(Cimv2.class), anyLong()))
                .thenReturn(validTerminalResult);

        when(mapper.mapToList(anyString(), any()))
                .thenReturn(Arrays.asList(expectedEthernetConfig, expectedWifiConfig));

        List<Win32NetworkAdapterConfiguration> response = service.get(5L);
        assertThat(response).contains(expectedEthernetConfig, expectedWifiConfig); // Service should return mapper result unchanged

        verify(terminalService).executeQuery(Cimv2.WIN32_NETWORK_ADAPTER_CONFIGURATION, 5L);
        verify(mapper).mapToList(validTerminalResult.getResult(), Win32NetworkAdapterConfiguration.class);
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

        verify(terminalService).executeQuery(Cimv2.WIN32_NETWORK_ADAPTER_CONFIGURATION, 5L);
        verify(mapper).mapToList(invalidTerminalResult.getResult(), Win32NetworkAdapterConfiguration.class);
        verifyNoMoreInteractions(terminalService);
        verifyNoMoreInteractions(mapper);
    }

    @Test
    void test_get_serviceReturnsEmpty_whenMapperReturnsEmpty() {

        when(terminalService.executeQuery(any(Cimv2.class), anyLong()))
                .thenReturn(emptyTerminalResult);

        when(mapper.mapToList(anyString(), any()))
                .thenReturn(Collections.emptyList());

        List<Win32NetworkAdapterConfiguration> response = service.get(5L);
        assertThat(response).isEmpty();

        verify(terminalService).executeQuery(Cimv2.WIN32_NETWORK_ADAPTER_CONFIGURATION, 5L);
        verify(mapper).mapToList(emptyTerminalResult.getResult(), Win32NetworkAdapterConfiguration.class);
        verifyNoMoreInteractions(terminalService);
        verifyNoMoreInteractions(mapper);
    }
}