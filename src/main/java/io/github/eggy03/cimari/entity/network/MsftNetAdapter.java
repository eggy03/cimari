/*
 * SPDX-License-Identifier: MIT
 * SPDX-FileCopyrightText: 2025 The ferrumx-windows contributors
 * SPDX-FileCopyrightText: 2026 Cimari contributors
 */
package io.github.eggy03.cimari.entity.network;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.github.eggy03.cimari.annotation.ImmutableEntityStyle;
import io.github.eggy03.cimari.annotation.WmiClass;
import org.immutables.value.Value;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;
import tools.jackson.databind.ObjectMapper;
import tools.jackson.databind.annotation.JsonDeserialize;
import tools.jackson.databind.annotation.JsonSerialize;

/**
 * Immutable modern representation of a network adapter on a Windows system.
 * <p>
 * Fields correspond to properties retrieved from the {@code MSFT_NetAdapter} class in the
 * {@code root/StandardCimv2} namespace. This class is a modern replacement for the now deprecated
 * {@code Win32_NetworkAdapter} WMI class in the {@code root/cimv2} namespace.
 * </p>
 * <p>
 * However, the {@link Win32NetworkAdapter} class which represents {@code Win32_NetworkAdapter},
 * will remain accessible for backwards compatibility will not be marked as deprecated by the library at this time.
 * </p>
 * <p>
 * Unlike {@link Win32NetworkAdapter} which stores it's configuration in {@link Win32NetworkAdapterConfiguration} and
 * requires {@link Win32NetworkAdapterSetting} to establish an association between them, {@link MsftNetAdapter} stores configuration
 * inside {@link MsftNetIpAddress}, {@link MsftDnsClientServerAddress} and {@link MsftNetConnectionProfile}
 * and all of them are directly linked via the {@code interfaceIndex} field.
 * </p>
 *
 * <p>See {@link Win32NetworkAdapter}, the now deprecated equivalent WMI class.</p>
 * <p>See {@link MsftNetIpAddress}, for IP address configuration information of a network adapter.</p>
 * <p>See {@link MsftNetConnectionProfile}, for information regarding the current profile of a network adapter.</p>
 * <p>See {@link MsftDnsClientServerAddress}, for configuration information regarding the DNS servers of a network adapter.</p>
 *
 * @see <a href="https://learn.microsoft.com/en-us/windows/win32/fwp/wmi/netadaptercimprov/msft-netadapter">MSFT_NetAdapter Documentation</a>
 * @since 1.0.0
 */
@WmiClass(className = "MSFT_NetAdapter")
@NullMarked
@Value.Immutable
@ImmutableEntityStyle
@JsonSerialize(as = ImmutableMsftNetAdapter.class)
@JsonDeserialize(as = ImmutableMsftNetAdapter.class)
public abstract class MsftNetAdapter {

    /**
     * Uniquely identifies the network adapter on the system.
     */
    @JsonProperty("DeviceID")
    @Nullable
    public abstract String deviceId();

    /**
     * Plug and Play (PnP) device identifier assigned to the adapter by Windows.
     */
    @JsonProperty("PnPDeviceID")
    @Nullable
    public abstract String pnpDeviceId();

    /**
     * The unique interface index number used by the network stack.
     * <p>Used to associate related objects such as {@link MsftNetIpAddress},
     * {@link MsftDnsClientServerAddress}, {@link MsftNetConnectionProfile}.</p>
     * <p>Example: 12</p>
     */
    @JsonProperty("InterfaceIndex")
    @Nullable
    public abstract Long interfaceIndex();

    /**
     * Name of the network adapter interface.
     * <p>Example: "Realtek PCIe GbE Family Controller"</p>
     */
    @JsonProperty("InterfaceName")
    @Nullable
    public abstract String interfaceName();

    /**
     * Type of interface as defined by the IANA Interface Type registry.
     */
    @JsonProperty("InterfaceType")
    @Nullable
    public abstract Long interfaceType();

    /**
     * Interface Description, also known as "ifDesc" or display name
     * is a unique name assigned to the network adapter during installation.
     * This name cannot be changed and is persisted as long as the network adapter is not uninstalled.
     */
    @JsonProperty("InterfaceDescription")
    @Nullable
    public abstract String interfaceDescription();

    /**
     * Friendly alias name assigned to the network interface by the operating system or user.
     * <p>Example: "Ethernet"</p>
     */
    @JsonProperty("InterfaceAlias")
    @Nullable
    public abstract String interfaceAlias();

    /**
     * Current operational status of the network adapter interface.
     * <p>Possible values: </p>
     * <ul>
     *     <li>1-Up</li>
     *     <li>2-Down</li>
     *     <li>3-Testing</li>
     *     <li>4-Unknown</li>
     *     <li>5-Dormant</li>
     *     <li>6-NotPresent</li>
     *     <li>7-LowerLayerDown</li>
     * </ul>
     */
    @JsonProperty("InterfaceOperationalStatus")
    @Nullable
    public abstract Long interfaceOperationalStatus();

    /**
     * Indicates whether this adapter represents a virtual interface.
     * <p>Example: true for Hyper-V virtual adapters</p>
     */
    @JsonProperty("Virtual")
    @Nullable
    public abstract Boolean virtual();

    /**
     * Indicates whether the adapter supports full-duplex mode.
     */
    @JsonProperty("FullDuplex")
    @Nullable
    public abstract Boolean fullDuplex();

    /**
     * Indicates whether the adapter is hidden from the user interface.
     */
    @JsonProperty("Hidden")
    @Nullable
    public abstract Boolean hidden();

    /**
     * Current operational status of the net adapter device.
     * <p>Possible OPERATIONAL values:</p>
     * <ul>
     *   <li>"OK"</li>
     *   <li>"Degraded"</li>
     *   <li>"Pred Fail"</li>
     * </ul>
     * <p>Possible NON-OPERATIONAL values:</p>
     * <ul>
     *   <li>"Unknown"</li>
     *   <li>"Error"</li>
     *   <li>"Starting"</li>
     *   <li>"Stopping"</li>
     *   <li>"Service"</li>
     * </ul>
     * <p>Possible OTHER values:</p>
     * <ul>
     *   <li>"Stressed"</li>
     *   <li>"NonRecover"</li>
     *   <li>"No Contact"</li>
     *   <li>"Lost Comm"</li>
     * </ul>
     */
    @JsonProperty("Status")
    @Nullable
    public abstract String status();

    /**
     * The physical (MAC) address of the network adapter.
     * <p>Example: "00:1A:2B:3C:4D:5E"</p>
     */
    @JsonProperty("LinkLayerAddress")
    @Nullable
    public abstract String linkLayerAddress();

    /**
     * The current link speed as a formatted string, if available.
     * <p>Example: "1 Gbps"</p>
     */
    @JsonProperty("LinkSpeed")
    @Nullable
    public abstract String linkSpeed();

    /**
     * Raw value of the current receive link speed in bits per second.
     */
    @JsonProperty("ReceiveLinkSpeed")
    @Nullable
    public abstract Long receiveLinkSpeedRaw();

    /**
     * Raw value of the current transmit link speed in bits per second.
     */
    @JsonProperty("TransmitLinkSpeed")
    @Nullable
    public abstract Long transmitLinkSpeedRaw();

    /**
     * Name of the network adapter driver.
     * <p>Example: "rt640x64.sys"</p>
     */
    @JsonProperty("DriverName")
    @Nullable
    public abstract String driverName();

    /**
     * Version number of the network adapter driver.
     * <p>Example: "12.18.9.10"</p>
     */
    @JsonProperty("DriverVersion")
    @Nullable
    public abstract String driverVersion();

    /**
     * Date of the currently installed driver.
     */
    @JsonProperty("DriverDate")
    @Nullable
    public abstract String driverDate();

    /**
     * Maximum Transmission Unit (MTU) size of the adapter, in bytes.
     * <p>Example: 1500</p>
     */
    @JsonProperty("MtuSize")
    @Nullable
    public abstract Long mtuSize();

    /**
     * The current media connection state of the adapter.
     * <p>Possible values: </p>
     * <ul>
     *     <li>0-Unknown</li>
     *     <li>1-Connected</li>
     *     <li>2-Disconnected</li>
     * </ul>
     */
    @JsonProperty("MediaConnectState")
    @Nullable
    public abstract Long mediaConnectState();

    /**
     * Network adapter media type.
     * Numeric equivalent of {@link #mediaType}
     * <p>Possible values:</p>
     * <ul>
     *     <li>0 – 802.3</li>
     *     <li>1 – 802.5</li>
     *     <li>2 – FDDI</li>
     *     <li>3 – WAN</li>
     *     <li>4 – LocalTalk</li>
     *     <li>5 – DIX</li>
     *     <li>6 – Raw Arcnet</li>
     *     <li>7 – 878.2</li>
     *     <li>8 – ATM</li>
     *     <li>9 – Wireless WAN</li>
     *     <li>10 – IrDA</li>
     *     <li>11 – BPC</li>
     *     <li>12 – Connection Oriented WAN</li>
     *     <li>13 – IP 1394</li>
     *     <li>14 – InfiniBand (IB)</li>
     *     <li>15 – Tunnel</li>
     *     <li>16 – Native 802.11</li>
     *     <li>17 – Loopback</li>
     *     <li>18 – WiMAX</li>
     *     <li>19 – IP</li>
     * </ul>
     */
    @JsonProperty("NdisMedium")
    @Nullable
    public abstract Long ndisMedium();

    /**
     * The types of physical media that the network adapter supports.
     * Numeric equivalent of {@link #physicalMediaType}
     * <p>Possible values:</p>
     * <ul>
     *     <li>0 – Unspecified</li>
     *     <li>1 – Wireless LAN</li>
     *     <li>2 – Cable Modem</li>
     *     <li>3 – Phone Line</li>
     *     <li>4 – Power Line</li>
     *     <li>5 – DSL</li>
     *     <li>6 – Fibre Channel (FC)</li>
     *     <li>7 – IEEE 1394</li>
     *     <li>8 – Wireless WAN</li>
     *     <li>9 – Native 802.11</li>
     *     <li>10 – Bluetooth</li>
     *     <li>11 – InfiniBand</li>
     *     <li>12 – WiMAX</li>
     *     <li>13 – Ultra-Wideband (UWB)</li>
     *     <li>14 – 802.3</li>
     *     <li>15 – 802.5</li>
     *     <li>16 – IrDA</li>
     *     <li>17 – Wired WAN</li>
     *     <li>18 – Wired Connection Oriented WAN</li>
     *     <li>19 – Other</li>
     * </ul>
     */
    @JsonProperty("NdisPhysicalMedium")
    @Nullable
    public abstract Long ndisPhysicalMedium();

    /**
     * Type of network media currently in use (for example, Ethernet or Wi-Fi).
     * String equivalent of {@link #ndisMedium}
     */
    @JsonProperty("MediaType")
    @Nullable
    public abstract String mediaType();

    /**
     * Physical type of network media
     * String equivalent of {@link #ndisPhysicalMedium}
     * <p>Example: "802.3"</p>
     */
    @JsonProperty("PhysicalMediaType")
    @Nullable
    public abstract String physicalMediaType();

    /**
     * Retrieves the entity in a JSON pretty-print formatted string
     *
     * @return the {@link String} value of the object in JSON pretty-print format
     */
    public String toJson() {
        return new ObjectMapper()
                .writerWithDefaultPrettyPrinter()
                .writeValueAsString(this);
    }
}
