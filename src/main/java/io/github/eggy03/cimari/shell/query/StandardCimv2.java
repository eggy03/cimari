/*
 * SPDX-License-Identifier: MIT
 * SPDX-FileCopyrightText: 2026 The ferrumx-windows contributors
 * SPDX-FileCopyrightText: 2026 Cimari contributors
 */
package io.github.eggy03.cimari.shell.query;

import io.github.eggy03.cimari.entity.network.MsftDnsClientServerAddress;
import io.github.eggy03.cimari.entity.network.MsftNetAdapter;
import io.github.eggy03.cimari.entity.network.MsftNetConnectionProfile;
import io.github.eggy03.cimari.entity.network.MsftNetIpAddress;
import lombok.NonNull;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

/**
 * Enum representing the predefined MSFT Class queries for the classes available in the {@code root/StandardCimv2} namespace.
 * <p>
 * Each constant holds a PowerShell query that queries a specific class in the namespace
 * and returns the result in JSON format.
 * </p>
 *
 * @since 1.0.0
 */
public enum StandardCimv2 {

    /**
     * Query to fetch the properties of the {@code MSFT_NetAdapter} class
     * <p>Will not show hidden physical or logical network adapters unless explicitly stated</p>
     *
     * @since 1.0.0
     */
    MSFT_NET_ADAPTER(generateQuery("Get-NetAdapter", MsftNetAdapter.class)),

    /**
     * Query to fetch the properties of the {@code MSFT_NetIPAddress} class
     *
     * @since 1.0.0
     */
    MSFT_NET_IP_ADDRESS(generateQuery("Get-NetIPAddress", MsftNetIpAddress.class)),

    /**
     * Query to fetch the properties of the {@code MSFT_NetDNSClientServerAddress} class
     *
     * @since 1.0.0
     */
    MSFT_NET_DNS_CLIENT_SERVER_ADDRESS(generateQuery("Get-DNSClientServerAddress", MsftDnsClientServerAddress.class)),

    /**
     * Query to fetch the properties of the {@code MSFT_NetConnectionProfile} class
     *
     * @since 1.0.0
     */
    MSFT_NET_CONNECTION_PROFILE(generateQuery("Get-NetConnectionProfile", MsftNetConnectionProfile.class));

    private final String query;

    StandardCimv2(@NotNull String query) {
        this.query = Objects.requireNonNull(query, "query cannot be null");
    }

    @NotNull
    private static <T> String generateQuery(@NonNull String prefix, @NonNull Class<T> wmiClass) {

        Objects.requireNonNull(wmiClass, "wmiClass cannot be null");

        return prefix +
                " | Select-Object -Property " + QueryUtility.getPropertiesFromSerializedNameAnnotation(wmiClass) +
                " | ConvertTo-Json";

    }

    public @NotNull String getQuery() {
        return this.query;
    }
}
