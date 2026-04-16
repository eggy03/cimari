/*
 * SPDX-License-Identifier: MIT
 * SPDX-FileCopyrightText: 2025 The ferrumx-windows contributors
 * SPDX-FileCopyrightText: 2026 Cimari contributors
 */
package io.github.eggy03.cimari.entity.network;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.github.eggy03.cimari.annotation.ShallowImmutable;
import io.github.eggy03.cimari.annotation.WmiClass;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.Value;

import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;
import tools.jackson.databind.ObjectMapper;

import java.util.List;

/**
 * Immutable representation of a network adapter configuration on a Windows system.
 * <p>
 * Fields correspond to properties retrieved from the {@code Win32_NetworkAdapterConfiguration} WMI class.
 * </p>
 *
 * <h2>Usage examples</h2>
 * <pre>{@code
 * // Create a new configuration using the builder
 * Win32NetworkAdapterConfiguration config = Win32NetworkAdapterConfiguration.builder()
 *     .index(1)
 *     .description("Ethernet Adapter")
 *     .ipEnabled(true)
 *     .ipAddress(List.of("192.168.1.100"))
 *     .dnsServerSearchOrder(List.of("8.8.8.8", "8.8.4.4"))
 *     .build();
 *
 * // Create a modified copy using the builder
 * Win32NetworkAdapterConfiguration updated = config.toBuilder()
 *     .description("Updated Ethernet Adapter")
 *     .build();
 *
 * }</pre>
 * <p>
 * See {@link Win32NetworkAdapter} for the corresponding adapter entity
 *
 *
 * @see <a href="https://learn.microsoft.com/en-us/windows/win32/cimwin32prov/win32-networkadapterconfiguration">Win32_NetworkAdapterConfiguration Documentation</a>
 * @since 1.0.0
 */

@Value
@Builder(toBuilder = true)
@ShallowImmutable
@WmiClass(className = "Win32_NetworkAdapterConfiguration")
@NullMarked
public class Win32NetworkAdapterConfiguration {

    /**
     * Index number of the Windows network adapter configuration.
     * Used when multiple configurations exist.
     */
    @JsonProperty("Index")
    @Nullable
    Integer index;

    /**
     * Textual description of the network adapter configuration.
     */
    @JsonProperty("Description")
    @Nullable
    String description;

    /**
     * Short textual caption describing the object.
     */
    @JsonProperty("Caption")
    @Nullable
    String caption;

    /**
     * Unique identifier by which the configuration instance is known.
     */
    @JsonProperty("SettingID")
    @Nullable
    String settingId;

    /**
     * Indicates whether TCP/IP is bound and enabled on this adapter.
     */
    @Getter(AccessLevel.NONE)
    @JsonProperty("IPEnabled")
    @Nullable
    Boolean ipEnabled;
    /**
     * List of IP addresses associated with this network adapter.
     * May contain IPv4 and/or IPv6 addresses.
     */
    @JsonProperty("IPAddress")
    @Nullable
    List<@Nullable String> ipAddress;
    /**
     * Subnet masks associated with each IP address on this adapter.
     */
    @JsonProperty("IPSubnet")
    @Nullable
    List<@Nullable String> ipSubnet;
    /**
     * List of default gateway IP addresses used by this system.
     */
    @JsonProperty("DefaultIPGateway")
    @Nullable
    List<@Nullable String> defaultIpGateway;
    /**
     * Indicates whether DHCP is enabled for this adapter.
     */
    @Getter(AccessLevel.NONE)
    @JsonProperty("DHCPEnabled")
    @Nullable
    Boolean dhcpEnabled;
    /**
     * IP address of the DHCP server that assigned this configuration.
     */
    @JsonProperty("DHCPServer")
    @Nullable
    String dhcpServer;
    /**
     * Date and time when the DHCP lease was obtained.
     */
    @JsonProperty("DHCPLeaseObtained")
    @Nullable
    String dhcpLeaseObtained;
    /**
     * Date and time when the DHCP lease expires.
     */
    @JsonProperty("DHCPLeaseExpires")
    @Nullable
    String dhcpLeaseExpires;
    /**
     * Host name used to identify this computer on the network.
     */
    @JsonProperty("DNSHostName")
    @Nullable
    String dnsHostName;
    /**
     * List of DNS server IP addresses used for name resolution.
     */
    @JsonProperty("DNSServerSearchOrder")
    @Nullable
    List<@Nullable String> dnsServerSearchOrder;

    public @Nullable Boolean isIPEnabled() {
        return ipEnabled;
    }

    public @Nullable Boolean isDHCPEnabled() {
        return dhcpEnabled;
    }

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