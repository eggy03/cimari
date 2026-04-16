/*
 * SPDX-License-Identifier: MIT
 * SPDX-FileCopyrightText: 2025 The ferrumx-windows contributors
 * SPDX-FileCopyrightText: 2026 Cimari contributors
 */
package io.github.eggy03.cimari.entity.network;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.github.eggy03.cimari.annotation.ShallowImmutable;
import io.github.eggy03.cimari.annotation.WmiClass;
import lombok.Builder;
import lombok.Value;

import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;
import tools.jackson.databind.ObjectMapper;

import java.util.List;

/**
 * Immutable representation of a DNS server configuration for a particular network adapter on a Windows system.
 * <p>
 * Fields correspond to properties retrieved from the {@code MSFT_DNSClientServerAddress} class in the
 * {@code root/StandardCimv2} namespace.
 * </p>
 * <p>
 * Together, with {@link MsftNetIpAddress}, this class aims to be
 * a replacement for {@link Win32NetworkAdapterConfiguration}
 * </p>
 *
 * <h2>Usage example</h2>
 * <pre>{@code
 * MsftDnsClientServerAddress dns = MsftDnsClientServerAddress.builder()
 *     .interfaceIndex(1)
 *     .interfaceAlias("Ethernet")
 *     .addressFamily(23)
 *     .build();
 *
 * // Create a modified copy
 * MsftDnsClientServerAddress updated = dns.toBuilder()
 *     .addressFamily(2)
 *     .build();
 * }</pre>
 *
 * <p>See {@link MsftNetAdapter}, for network adapter information.</p>
 * <p>See {@link MsftNetConnectionProfile}, for information regarding the current profile of a network adapter.</p>
 * <p>See {@link MsftNetIpAddress}, for IP address configuration information of a network adapter.</p>
 *
 *
 * @see <a href="https://learn.microsoft.com/en-us/windows/win32/fwp/wmi/dnsclientcimprov/msft-dnsclientserveraddress">MSFT_DNSClientServerAddress Documentation</a>
 * @since 1.0.0
 */
@Value
@Builder(toBuilder = true)
@ShallowImmutable
@WmiClass(className = "MSFT_DNSClientServerAddress")
@NullMarked
public class MsftDnsClientServerAddress {

    /**
     * Gets the user-friendly index of the server interface.
     * It's the unique interface index number used by the network stack.
     */
    @JsonProperty("InterfaceIndex")
    @Nullable
    Long interfaceIndex;

    /**
     * Gets the user-friendly name of the server interface.
     */
    @JsonProperty("InterfaceAlias")
    @Nullable
    String interfaceAlias;

    /**
     * Gets the address family of the server address.
     * <p>
     * Possible values:
     * <ul>
     *     <li>2  - IPv4</li>
     *     <li>23 - IPv6</li>
     * </ul>
     */
    @JsonProperty("AddressFamily")
    @Nullable
    Integer addressFamily;

    /**
     * Gets a list that contains the DNS server addresses.
     */
    @JsonProperty("ServerAddresses")
    @Nullable
    List<@Nullable String> dnsServerAddresses;

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
