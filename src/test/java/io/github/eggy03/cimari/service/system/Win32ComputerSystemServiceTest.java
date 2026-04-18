/*
 * SPDX-License-Identifier: MIT
 * SPDX-FileCopyrightText: 2025 The ferrumx-windows contributors
 * SPDX-FileCopyrightText: 2026 Cimari contributors
 */
package io.github.eggy03.cimari.service.system;

import io.github.eggy03.cimari.entity.system.Win32ComputerSystem;
import io.github.eggy03.cimari.terminal.TerminalResult;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigInteger;
import java.util.Arrays;
import java.util.Collections;

@ExtendWith(MockitoExtension.class)
class Win32ComputerSystemServiceTest {

    private final TerminalResult validTerminalResult = new TerminalResult("{}", "");
    private final TerminalResult invalidTerminalResult = new TerminalResult("invalid json", "");
    private final TerminalResult emptyTerminalResult = new TerminalResult("", "");

    private final Win32ComputerSystem expectedComputerSystem = new Win32ComputerSystem.Builder()
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
}
