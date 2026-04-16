/*
 * SPDX-License-Identifier: MIT
 * SPDX-FileCopyrightText: 2025 The ferrumx-windows contributors
 * SPDX-FileCopyrightText: 2026 Cimari contributors
 */
package io.github.eggy03.cimari.entity.compounded;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.github.eggy03.cimari.entity.network.MsftDnsClientServerAddress;
import io.github.eggy03.cimari.entity.network.MsftNetAdapter;
import io.github.eggy03.cimari.entity.network.MsftNetConnectionProfile;
import io.github.eggy03.cimari.entity.network.MsftNetIpAddress;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;
import tools.jackson.databind.ObjectMapper;

import java.util.List;

/**
 * Immutable representation of a {@link MsftNetAdapter} and its {@code 1:N} relationships
 * with {@link MsftNetIpAddress}, {@link MsftDnsClientServerAddress},
 * and {@link MsftNetConnectionProfile} configurations in a Windows system.
 * <p>
 * Each instance represents a single {@code NetAdapter} identified by {@link #interfaceIndex},
 * and maintains a one-to-many mapping with related
 * {@code IpAddress}, {@code DnsClientServerAddress},
 * and {@code ConnectionProfile} entities.
 * </p>
 *
 * <h2>Usage example</h2>
 * <pre>{@code
 * MsftNetAdapterToIpAndDnsAndProfile adapterInfo = MsftNetAdapterToIpAndDnsAndProfile.builder()
 *     .interfaceIndex(12L)
 *     .adapter(msftNetAdapter)
 *     .ipAddressList(ipAddresses)
 *     .dnsClientServerAddressList(dnsServers)
 *     .netConnectionProfileList(connectionProfiles)
 *     .build();
 * }</pre>
 *
 * <p>
 * This is purely a convenience class designed to simplify data retrieval
 * for all related network configuration entities through a single call.
 * The individual entity classes remain accessible for direct use if you need to map everything by yourself.
 * </p>
 * <p>
 * An equivalent convenience class {@link Win32NetworkAdapterToConfiguration} is also available for use, although
 * {@code Win32_NetworkAdapter} is deprecated by Microsoft in favor of the MSFT classes.
 * </p>
 *
 *
 * @see MsftNetAdapter
 * @see MsftNetIpAddress
 * @see MsftDnsClientServerAddress
 * @see MsftNetConnectionProfile
 * @since 1.0.0
 */
@NullMarked
public class MsftNetAdapterToIpAndDnsAndProfile {

    /**
     * The unique index identifying the {@link MsftNetAdapter} instance
     */
    @JsonProperty("InterfaceIndex")
    @Nullable
    Long interfaceIndex;

    /**
     * The {@link MsftNetAdapter} associated with the index: {@link #interfaceIndex}
     */
    @JsonProperty("NetworkAdapter")
    @Nullable
    MsftNetAdapter adapter;

    /**
     * A list of {@link MsftNetIpAddress} associated with the index: {@link #interfaceIndex}
     */
    @JsonProperty("IPAddresses")
    @Nullable
    List<@Nullable MsftNetIpAddress> ipAddressList;

    /**
     * A list of {@link MsftDnsClientServerAddress} associated with the index: {@link #interfaceIndex}
     */
    @JsonProperty("DNSServers")
    @Nullable
    List<@Nullable MsftDnsClientServerAddress> dnsClientServerAddressList;

    /**
     * A list of {@link MsftNetConnectionProfile} associated with the index: {@link #interfaceIndex}
     */
    @JsonProperty("Profile")
    @Nullable
    List<@Nullable MsftNetConnectionProfile> netConnectionProfileList;

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