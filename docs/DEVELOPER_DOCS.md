This documentation outlines the principal architecture of the library and aims to provide the viewers
with an understanding of how the internal components work together.

> [!NOTE]
> This documentation reflects the workings of v4.1.0 of the library. Older or newer versions may have altered structure,
> behavior or implementation.
> I will try to update this documentation frequently to reflect the latest changes.

# Project Structure

```
src
├───main
│   ├───java
│   │   └───io.github.eggy.ferrumx.windows
│   │       ├───annotation
│   │       ├───entity
│   │       ├───exception
│   │       ├───mapping
│   │       ├───service
│   │       ├───shell
│   │       └───utility
│   └───resources
└───test
    ├───java
    │   └───unit
    │       ├───mapper
    │       ├───service
    │       ├───shell
    │       └───utility
    └───resources
```

# Intent

The primary goal of the API can be divided into three simple steps:

1) Initialize and manage a PowerShell session to execute WMI queries
2) Retrieve and process the command output
3) Map the processed data into Java entities and return them to the caller

Some of the secondary goals of the API can be described as:

1) Handle scenarios when PowerShell cannot be launched or throws an error during query runs
2) Handle scenarios where queries return incomplete or unexpected results
3) Safely manage null, empty, or malformed results
4) Handle mutability of mapped entity classes and collections

# Working

For this example, we will query the `Win32_Processor` class and
map a subset of its properties into a custom-defined entity class.

### Checking out the Documentation

To start with, we need to understand the structure of the `Win32_Processor` class.
The complete list of available fields can be found in the official Microsoft documentation:

Reference: https://learn.microsoft.com/en-us/windows/win32/cimwin32prov/win32-processor

The section below shows the fields revealed by the `Win32_Processor` class:

```
[Dynamic, Provider("CIMWin32"), UUID("{8502C4BB-5FBB-11D2-AAC1-006008C78BC7}"), AMENDMENT]
class Win32_Processor : CIM_Processor
{
  uint16  AddressWidth;
  uint16  Architecture;
  string  AssetTag;
  uint16  Availability;
  string  Caption;
  uint32  Characteristics;
  uint32  ConfigManagerErrorCode;
  boolean ConfigManagerUserConfig;
  uint16  CpuStatus;
  string  CreationClassName;
  uint32  CurrentClockSpeed;
  uint16  CurrentVoltage;
  uint16  DataWidth;
  string  Description;
  string  DeviceID;
  boolean ErrorCleared;
  string  ErrorDescription;
  uint32  ExtClock;
  uint16  Family;
  datetime InstallDate;
  uint32  L2CacheSize;
  uint32  L2CacheSpeed;
  uint32  L3CacheSize;
  uint32  L3CacheSpeed;
  uint32  LastErrorCode;
  uint16  Level;
  uint16  LoadPercentage;
  string  Manufacturer;
  uint32  MaxClockSpeed;
  string  Name;
  uint32  NumberOfCores;
  uint32  NumberOfEnabledCore;
  uint32  NumberOfLogicalProcessors;
  string  OtherFamilyDescription;
  string  PartNumber;
  string  PNPDeviceID;
  uint16  PowerManagementCapabilities[];
  boolean PowerManagementSupported;
  string  ProcessorId;
  uint16  ProcessorType;
  uint16  Revision;
  string  Role;
  boolean SecondLevelAddressTranslationExtensions;
  string  SerialNumber;
  string  SocketDesignation;
  string  Status;
  uint16  StatusInfo;
  string  Stepping;
  string  SystemCreationClassName;
  string  SystemName;
  uint32  ThreadCount;
  string  UniqueId;
  uint16  UpgradeMethod;
  string  Version;
  boolean VirtualizationFirmwareEnabled;
  boolean VMMonitorModeExtensions;
  uint32  VoltageCaps;
};
```

While the class exposes a large number of properties, it may not be necessary to map all of them.

### Designing the Query

We begin by executing a PowerShell command to retrieve selected properties in JSON format:

```shell
Get-CimInstance -ClassName Win32_Processor | Select-Object Name, NumberOfCores, Manufacturer | ConvertTo-Json
```

Executing this query produces output similar to:

```json
{
  "Name": "AMD Ryzen 5 5600G with Radeon Graphics",
  "NumberOfCores": 6,
  "Manufacturer": "AuthenticAMD"
}
```

Since this executed every time a caller wants to fetch Win32_Processor information, we will store this query in an enum.

Our enum will reside in the `io.github.eggy.ferrumx.windows.shell.query*` package.

We define an enum named `Cimv2`, derived from the namespace to which `Win32_Processor` belongs.

```java

@SuppressWarnings("LombokGetterMayBeUsed")
public enum Cimv2 {

    WIN32_PROCESSOR(
            "Get-CimInstance -ClassName Win32_Processor |" +
                    " Select-Object Name, NumberOfCores, Manufacturer |" +
                    " ConvertTo-Json"
    );

    private final String query;

    Cimv2(String query) {
        this.query = query;
    }

    public String getQuery() {
        return this.query;
    }

}
```

This will allow us to fetch the entire query by just calling `Cimv2.WIN32_PROCESSOR.getQuery()`.

In the next section, we will refactor this enum to dynamically construct the properties and the class name required
to build the query, at runtime, via reflection.
We will also use `Lombok` to reduce boilerplate code.

### Designing the Entity Class

Based on the fields we want, we will now design our entity class.

All our entity classes can be found in the `io.github.eggy.ferrumx.windows.entity.*` package.
For processor-related mappings, a dedicated subpackage `entity.processor` is used.

As of v4.1.0, this package includes:

- `Win32_Processor`
- `Win32_CacheMemory`
- `Win32_AssociatedProcessorMemory`

```java
@Value
@Builder(toBuilder = true)
@WmiClass(className = "Win32_Processor")
public class Win32Processor {

    @SerializedName("Name")
    @Nullable
    String name;

    @SerializedName("NumberOfCores")
    @Nullable
    Integer numberOfCores;

    @SerializedName("Manufacturer")
    @Nullable
    String manufacturer;

    @Override
    @NotNull
    public String toString() {
        return new GsonBuilder()
                .serializeNulls()
                .setPrettyPrinting()
                .create()
                .toJson(this);
    }
}
```

What we have here is an annotation heavy entity class that's designed based on the properties we want to map.

Let's talk about the annotations and their purpose:

- `@Value` is from Lombok, and it makes the class shallowly immutable.

All fields are made private and final, and Lombok generates an all-arguments constructor along with getters.
This prevents modification at the reference level.
However, if your class contains fields which are a part of the Collections, such as List, Map, Set,
`@Value` would make their references final but the actual Collections may still be immutable depending on the
implementation.
In our implementation, the collections stay mutable.

- `@Builder` is from Lombok, and it provides a builder pattern for the entity.

While the mapping layer typically deserializes PowerShell output directly into the entity (without using the builder),
the builder is included to simplify manual object creation—particularly in testing and custom workflows.

Without a builder, creating an instance with nullable fields would require:

```text
new Win32Processor(null, null, null);
```

This approach quickly becomes impractical as the number of fields increases.

Using the builder, the same can be expressed more clearly and flexibly:

```text
Win32Processor.builder().build();
```

Additionally, only the required fields can be set while leaving others unset:

```text
Win32Processor.builder()
    .name("AMD Ryzen 5 5600G")
    .build();
```

Note that `toBuilder()` creates a new instance of the builder class with the current values from the original object
but does not recursively clone or deep copy mutable fields such as collections or other objects.

The shallow copy means that if fields in the original object point to mutable objects, changes to these objects via
the builder will reflect on the original instance.

- `@WmiClass` is a custom annotation, and it serves the following purposes:

1) It holds information about the actual WMI class name.
2) The annotation allows us to get the class name during dynamic construction of queries during runtime.

- `@SerializedName` is from GSON and it serves the following purposes:

1) It holds information about the actual WMI property name that the field corresponds to. Due to different naming
   conventions, we need to explicitly tell GSON that a property with the given name must be mapped to the given field.
2) The annotation allows us to get the property name during dynamic construction of queries during runtime.

- `@Nullable` and `@NotNull` are from JetBrains Annotations, and they're strictly used to tell users and static analysis
  tools about the nullity of the given fields. It is always safe to assume that all fields for a given entity may be
  null
  at some point or for different systems.

We also have a custom `toString()` implementation that returns a pretty printed JSON formatted String value of the
objects of the class.

### Dynamically Generating Queries

We will now refactor our [Cimv2](#designing-the-query) enum such that it can dynamically fetch the class name
and the properties to be queried.

Take a look at the following refactor:

```java

@RequiredArgsConstructor
@Getter
public enum Cimv2 {

    WIN32_PROCESSOR(generateQuery(Win32Processor.class));

    @NonNull
    private final String query;

    @NotNull
    private static <T> String generateQuery(@NonNull Class<T> wmiClass) {
        return "Get-CimInstance -ClassName " + QueryUtility.getClassNameFromWmiClassAnnotation(wmiClass) +
                " | Select-Object -Property " + QueryUtility.getPropertiesFromSerializedNameAnnotation(wmiClass) +
                " | ConvertTo-Json";

    }
}
```

We have introduced a Utility Class called `QueryUtility` which uses reflection to get the class name and properties
from our `Win32Processor` class during runtime.

`@WmiClass(className = "Win32_Processor")` and `@SerializedName("...")` provides all the data required to fetch the
class name and the properties to be queried, respectively.

We just need to pass `Win32Processor.class` as an argument and the utility will scan the class.
The utility resides in the `io.github.eggy.ferrumx.windows.shell.query*` package.

We have also used Lombok to remove some constructor and getter boilerplate.
