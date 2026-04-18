/*
 * SPDX-License-Identifier: MIT
 * SPDX-FileCopyrightText: 2025 The ferrumx-windows contributors
 * SPDX-FileCopyrightText: 2026 Cimari contributors
 */
package io.github.eggy03.cimari.entity.mainboard;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.github.eggy03.cimari.annotation.ImmutableEntityStyle;
import io.github.eggy03.cimari.annotation.WmiClass;
import org.immutables.value.Value;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;
import tools.jackson.databind.ObjectMapper;
import tools.jackson.databind.annotation.JsonDeserialize;
import tools.jackson.databind.annotation.JsonSerialize;

import java.util.List;

/**
 * Immutable representation of a motherboard port on a Windows system.
 * <p>
 * Fields correspond to properties retrieved from the {@code Win32_PortConnector} WMI class.
 * </p>
 *
 * {@link Win32Baseboard} contains the details of the motherboard this port belongs to.
 *
 * @see <a href="https://learn.microsoft.com/en-us/windows/win32/cimwin32prov/win32-portconnector">Win32_PortConnector Documentation</a>
 * @since 1.0.0
 */
@WmiClass(className = "Win32_PortConnector")
@NullMarked
@Value.Immutable
@ImmutableEntityStyle
@JsonSerialize(as = Win32PortConnector.class)
@JsonDeserialize(as = Win32PortConnector.class)
public abstract class AbstractWin32PortConnector {

    /**
     * Unique identifier of a port connection on the computer system.
     * <p>
     * Example: "Port Connector 1"
     */
    @JsonProperty("Tag")
    @Nullable
    public abstract String tag();

    /**
     * External reference designator of the port. External reference designators are identifiers
     * that determine the type and use of the port.
     * Example: "COM1"
     */
    @JsonProperty("ExternalReferenceDesignator")
    @Nullable
    public abstract String externalReferenceDesignator();

    /**
     * Internal reference designator of the port. Internal reference designators are specific
     * to the manufacturer, and identify the circuit board location or use of the port.
     * Example: "J101"
     */
    @JsonProperty("InternalReferenceDesignator")
    @Nullable
    public abstract String internalReferenceDesignator();

    /**
     * Function of the port.
     * <p>
     * Possible values include:
     * <ul>
     *   <li>None (0)</li>
     *   <li>Parallel Port XT/AT Compatible (1)</li>
     *   <li>Parallel Port PS/2 (2)</li>
     *   <li>Parallel Port ECP (3)</li>
     *   <li>Parallel Port EPP (4)</li>
     *   <li>Parallel Port ECP/EPP (5)</li>
     *   <li>Serial Port XT/AT Compatible (6)</li>
     *   <li>Serial Port 16450 Compatible (7)</li>
     *   <li>Serial Port 16550 Compatible (8)</li>
     *   <li>Serial Port 16550A Compatible (9)</li>
     *   <li>SCSI Port (10)</li>
     *   <li>MIDI Port (11)</li>
     *   <li>Joy Stick Port (12)</li>
     *   <li>Keyboard Port (13)</li>
     *   <li>Mouse Port (14)</li>
     *   <li>SSA SCSI (15)</li>
     *   <li>USB (16)</li>
     *   <li>FireWire (IEEE P1394) (17)</li>
     *   <li>PCMCIA Type I (18)</li>
     *   <li>PCMCIA Type II (19)</li>
     *   <li>PCMCIA Type III (20)</li>
     *   <li>Cardbus (21)</li>
     *   <li>Access Bus Port (22)</li>
     *   <li>SCSI II (23)</li>
     *   <li>SCSI Wide (24)</li>
     *   <li>PC-98 (25)</li>
     *   <li>PC-98-Hireso (26)</li>
     *   <li>PC-H98 (27)</li>
     *   <li>Video Port (28)</li>
     *   <li>Audio Port (29)</li>
     *   <li>Modem Port (30)</li>
     *   <li>Network Port (31)</li>
     *   <li>8251 Compatible (32)</li>
     *   <li>8251 FIFO Compatible (33)</li>
     * </ul>
     */
    @JsonProperty("PortType")
    @Nullable
    public abstract Integer portType();

    /**
     * Array of physical attributes of the connector used by this port.
     * <p>
     * Refer to the microsoft documentation provided at the class level for a list of possible values
     */
    @JsonProperty("ConnectorType")
    @Nullable
    public abstract List<@Nullable Integer> connectorType();

    /**
     * Retrieves the entity in a JSON pretty-print formatted string
     *
     * @return the {@link String} value of the object in JSON pretty-print format
     */
    @Override
    public String toString() {
        return new ObjectMapper()
                .writerWithDefaultPrettyPrinter()
                .writeValueAsString(this);
    }
}