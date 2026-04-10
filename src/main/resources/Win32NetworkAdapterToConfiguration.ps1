# SPDX-License-Identifier: MIT
# SPDX-FileCopyrightText: 2025 The ferrumx-windows contributors
# SPDX-FileCopyrightText: 2026 Cimari contributors

$adapters = Get-CimInstance Win32_NetworkAdapter

$result = foreach ($adapter in $adapters)
{

    $configurations = Get-CimAssociatedInstance -InputObject $adapter -ResultClassName Win32_NetworkAdapterConfiguration

    [PSCustomObject]@{
        DeviceID = $adapter.DeviceID
        Adapter = $adapter
        Configurations = @($configurations)
    }

}

$result | ConvertTo-Json -Depth 5