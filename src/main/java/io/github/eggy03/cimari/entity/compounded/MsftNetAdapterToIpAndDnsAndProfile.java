/*
 * SPDX-License-Identifier: MIT
 * SPDX-FileCopyrightText: 2025 The ferrumx-windows contributors
 * SPDX-FileCopyrightText: 2026 Cimari contributors
 */
package io.github.eggy03.cimari.entity.compounded;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.github.eggy03.cimari.annotation.ImmutableEntityStyle;
import io.github.eggy03.cimari.entity.network.MsftDnsClientServerAddress;
import io.github.eggy03.cimari.entity.network.MsftNetAdapter;
import io.github.eggy03.cimari.entity.network.MsftNetConnectionProfile;
import io.github.eggy03.cimari.entity.network.MsftNetIpAddress;
import org.immutables.value.Value;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;
import tools.jackson.databind.ObjectMapper;
import tools.jackson.databind.annotation.JsonDeserialize;
import tools.jackson.databind.annotation.JsonSerialize;

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
 * @see MsftNetAdapter
 * @see MsftNetIpAddress
 * @see MsftDnsClientServerAddress
 * @see MsftNetConnectionProfile
 * @since 0.1.0
 */
@NullMarked
@Value.Immutable
@ImmutableEntityStyle
@JsonSerialize(as = ImmutableMsftNetAdapterToIpAndDnsAndProfile.class)
@JsonDeserialize(as = ImmutableMsftNetAdapterToIpAndDnsAndProfile.class)
public abstract class MsftNetAdapterToIpAndDnsAndProfile {

    /**
     * The unique index identifying the {@link MsftNetAdapter} instance
     */
    @JsonProperty("InterfaceIndex")
    @Nullable
    public abstract Long interfaceIndex();

    /**
     * The {@link MsftNetAdapter} associated with the index: {@link #interfaceIndex}
     */
    @JsonProperty("NetworkAdapter")
    @Nullable
    public abstract MsftNetAdapter adapter();

    /**
     * A list of {@link MsftNetIpAddress} associated with the index: {@link #interfaceIndex}
     */
    @JsonProperty("IPAddresses")
    @Nullable
    public abstract List<@Nullable MsftNetIpAddress> ipAddressList();

    /**
     * A list of {@link MsftDnsClientServerAddress} associated with the index: {@link #interfaceIndex}
     */
    @JsonProperty("DNSServers")
    @Nullable
    public abstract List<@Nullable MsftDnsClientServerAddress> dnsClientServerAddressList();

    /**
     * A list of {@link MsftNetConnectionProfile} associated with the index: {@link #interfaceIndex}
     */
    @JsonProperty("Profile")
    @Nullable
    public abstract List<@Nullable MsftNetConnectionProfile> netConnectionProfileList();

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