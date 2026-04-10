# SPDX-License-Identifier: MIT
# SPDX-FileCopyrightText: 2025 The ferrumx-windows contributors
# SPDX-FileCopyrightText: 2026 Cimari contributors

$partitions = Get-CimInstance Win32_DiskPartition

$result = foreach ($partition in $partitions)
{

    $logicalDisks = Get-CimAssociatedInstance -InputObject $partition -ResultClassName Win32_LogicalDisk

    [PSCustomObject]@{
        PartitionID = $partition.DeviceID
        Partition = $partition
        LogicalDisks = @($logicalDisks)
    }
}

$result | ConvertTo-Json -Depth 5