/*
 * SPDX-License-Identifier: MIT
 * SPDX-FileCopyrightText: 2025 The ferrumx-windows contributors
 * SPDX-FileCopyrightText: 2026 Cimari contributors
 */
package io.github.eggy03.cimari.service.peripheral;

import com.google.gson.JsonSyntaxException;
import io.github.eggy03.cimari.entity.peripheral.Win32Printer;
import io.github.eggy03.cimari.mapping.peripheral.Win32PrinterMapper;
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
class Win32PrinterServiceTest {

    private final TerminalResult validTerminalResult = new TerminalResult("{}", "");
    private final TerminalResult invalidTerminalResult = new TerminalResult("invalid json", "");
    private final TerminalResult emptyTerminalResult = new TerminalResult("", "");

    private final Win32Printer expectedPrinter1 = Win32Printer.builder()
            .deviceId("PRN1")
            .name("HP LaserJet Pro M404dn")
            .pnpDeviceId("USBPRINT\\Hewlett-PackardHP_LaB1A1\\7&1111A11&0&USB001")
            .capabilities(Arrays.asList(1, 2, 3))
            .capabilityDescriptions(Arrays.asList("Duplex", "Color", "Staple"))
            .horizontalResolution(1200L)
            .verticalResolution(1200L)
            .paperSizesSupported(Arrays.asList(1, 5, 9))
            .printerPaperNames(Arrays.asList("A4", "Letter", "Legal"))
            .printerStatus(0)
            .printJobDataType("RAW")
            .printProcessor("winprint")
            .driverName("HP Universal Printing PCL 6")
            .shared(true)
            .shareName("HP_LaserJet_Office")
            .spoolEnabled(true)
            .hidden(false)
            .build();
    private final Win32Printer expectedPrinter2 = Win32Printer.builder()
            .deviceId("PRN2")
            .name("Canon PIXMA G3020")
            .pnpDeviceId("USBPRINT\\CanonG3020_SERIES\\7&2222B22&0&USB002")
            .capabilities(Arrays.asList(2, 4))
            .capabilityDescriptions(Arrays.asList("Color", "Scan"))
            .horizontalResolution(600L)
            .verticalResolution(600L)
            .paperSizesSupported(Arrays.asList(1, 9))
            .printerPaperNames(Arrays.asList("A4", "Legal"))
            .printerStatus(1)
            .printJobDataType("RAW")
            .printProcessor("winprint")
            .driverName("Canon G3000 series Printer")
            .shared(false)
            .shareName(null)
            .spoolEnabled(true)
            .hidden(false)
            .build();

    @Mock
    private TerminalService terminalService;

    @Mock
    private Win32PrinterMapper mapper;

    @InjectMocks
    private Win32PrinterService service;

    @Test
    void test_get_serviceReturnsMapperResult() {

        when(terminalService.executeQuery(any(Cimv2.class), anyLong()))
                .thenReturn(validTerminalResult);

        when(mapper.mapToList(anyString(), any()))
                .thenReturn(Arrays.asList(expectedPrinter1, expectedPrinter2));

        List<Win32Printer> response = service.get(5L);
        assertThat(response).contains(expectedPrinter1, expectedPrinter2); // Service should return mapper result unchanged

        verify(terminalService).executeQuery(Cimv2.WIN32_PRINTER, 5L);
        verify(mapper).mapToList(validTerminalResult.getResult(), Win32Printer.class);
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

        verify(terminalService).executeQuery(Cimv2.WIN32_PRINTER, 5L);
        verify(mapper).mapToList(invalidTerminalResult.getResult(), Win32Printer.class);
        verifyNoMoreInteractions(terminalService);
        verifyNoMoreInteractions(mapper);
    }

    @Test
    void test_get_serviceReturnsEmpty_whenMapperReturnsEmpty() {

        when(terminalService.executeQuery(any(Cimv2.class), anyLong()))
                .thenReturn(emptyTerminalResult);

        when(mapper.mapToList(anyString(), any()))
                .thenReturn(Collections.emptyList());

        List<Win32Printer> response = service.get(5L);
        assertThat(response).isEmpty();

        verify(terminalService).executeQuery(Cimv2.WIN32_PRINTER, 5L);
        verify(mapper).mapToList(emptyTerminalResult.getResult(), Win32Printer.class);
        verifyNoMoreInteractions(terminalService);
        verifyNoMoreInteractions(mapper);
    }
}
