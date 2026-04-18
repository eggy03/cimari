/*
 * SPDX-License-Identifier: MIT
 * SPDX-FileCopyrightText: 2025 The ferrumx-windows contributors
 * SPDX-FileCopyrightText: 2026 Cimari contributors
 */
package io.github.eggy03.cimari.service.network;


import io.github.eggy03.cimari.entity.network.MsftNetAdapter;
import io.github.eggy03.cimari.mapping.network.MsftNetAdapterMapper;
import io.github.eggy03.cimari.shell.query.StandardCimv2;
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
class MsftNetAdapterServiceTest {

    private final TerminalResult validTerminalResult = new TerminalResult("{}", "");
    private final TerminalResult invalidTerminalResult = new TerminalResult("invalid json", "");
    private final TerminalResult emptyTerminalResult = new TerminalResult("", "");

    private final MsftNetAdapter expectedEthernet = new MsftNetAdapter.Builder()
            .deviceId("NET1")
            .pnpDeviceId("PCI\\VEN_8086&DEV_15BB&SUBSYS_07B01028&REV_10\\3&11583659&0&FE")
            .interfaceIndex(1L)
            .interfaceName("Ethernet")
            .interfaceType(6L) // Ethernet
            .interfaceDescription("Intel(R) Ethernet Connection I219-V")
            .interfaceAlias("Ethernet")
            .interfaceOperationalStatus(1L)
            .virtual(false)
            .fullDuplex(true)
            .hidden(false)
            .status("Up")
            .linkLayerAddress("00:1A:2B:3C:4D:5E")
            .linkSpeed("1 Gbps")
            .receiveLinkSpeedRaw(1000000000L)
            .transmitLinkSpeedRaw(1000000000L)
            .driverName("e1d68x64.sys")
            .driverVersion("12.19.1.37")
            .driverDate("2023-10-12")
            .mtuSize(1500L)
            .mediaConnectState(1L)
            .mediaType("802.3")
            .physicalMediaType("Unspecified")
            .ndisMedium(1L)
            .ndisPhysicalMedium(1L)
            .build();

    private final MsftNetAdapter expectedWifi = new MsftNetAdapter.Builder()
            .deviceId("NET2")
            .pnpDeviceId("PCI\\VEN_14E4&DEV_43A0&SUBSYS_061114E4&REV_03\\4&2AAB3B17&0&00E1")
            .interfaceIndex(2L)
            .interfaceName("Wi-Fi")
            .interfaceType(71L) // Wireless
            .interfaceDescription("Broadcom 802.11ac Network Adapter")
            .interfaceAlias("Wi-Fi")
            .interfaceOperationalStatus(1L)
            .virtual(false)
            .fullDuplex(true)
            .hidden(false)
            .status("Up")
            .linkLayerAddress("44:1C:A8:9D:3E:7F")
            .linkSpeed("866 Mbps")
            .receiveLinkSpeedRaw(866000000L)
            .transmitLinkSpeedRaw(866000000L)
            .driverName("bcmwl63a.sys")
            .driverVersion("7.35.333.0")
            .driverDate("2022-05-01")
            .mtuSize(1500L)
            .mediaConnectState(1L)
            .mediaType("802.11")
            .physicalMediaType("Wireless LAN")
            .ndisMedium(2L)
            .ndisPhysicalMedium(2L)
            .build();

    @Mock
    private TerminalService terminalService;

    @Mock
    private MsftNetAdapterMapper mapper;

    @InjectMocks
    private MsftNetAdapterService service;

    @Test
    void test_get_serviceReturnsMapperResult() {

        when(terminalService.executeQuery(any(StandardCimv2.class), anyLong()))
                .thenReturn(validTerminalResult);

        when(mapper.mapToList(anyString(), any()))
                .thenReturn(Arrays.asList(expectedEthernet, expectedWifi));

        List<MsftNetAdapter> response = service.get(5L);
        assertThat(response).contains(expectedEthernet, expectedWifi); // Service should return mapper result unchanged

        verify(terminalService).executeQuery(StandardCimv2.MSFT_NET_ADAPTER, 5L);
        verify(mapper).mapToList(validTerminalResult.getResult(), MsftNetAdapter.class);
        verifyNoMoreInteractions(terminalService);
        verifyNoMoreInteractions(mapper);
    }

    @Test
    void test_get_mapperThrows_servicePropagatesException() {

        when(terminalService.executeQuery(any(StandardCimv2.class), anyLong()))
                .thenReturn(invalidTerminalResult);

        when(mapper.mapToList(anyString(), any()))
                .thenThrow(JacksonException.class);

        assertThrows(JacksonException.class, () -> service.get(5L));

        verify(terminalService).executeQuery(StandardCimv2.MSFT_NET_ADAPTER, 5L);
        verify(mapper).mapToList(invalidTerminalResult.getResult(), MsftNetAdapter.class);
        verifyNoMoreInteractions(terminalService);
        verifyNoMoreInteractions(mapper);
    }

    @Test
    void test_get_serviceReturnsEmpty_whenMapperReturnsEmpty() {

        when(terminalService.executeQuery(any(StandardCimv2.class), anyLong()))
                .thenReturn(emptyTerminalResult);

        when(mapper.mapToList(anyString(), any()))
                .thenReturn(Collections.emptyList());

        List<MsftNetAdapter> response = service.get(5L);
        assertThat(response).isEmpty();

        verify(terminalService).executeQuery(StandardCimv2.MSFT_NET_ADAPTER, 5L);
        verify(mapper).mapToList(emptyTerminalResult.getResult(), MsftNetAdapter.class);
        verifyNoMoreInteractions(terminalService);
        verifyNoMoreInteractions(mapper);
    }
}
