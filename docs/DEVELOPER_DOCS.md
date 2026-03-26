This documentation outlines the principal architecture of the library
and provides an overview of how its internal components work together.

_Last Updated: March 23, 2026_

> [!NOTE]
> This documentation reflects the workings of v4.1.0 of the library. Older or newer versions may have altered structure,
> behavior, or implementation.
> This documentation may be updated periodically to reflect the latest changes.

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
4) Address mutability concerns of mapped entity classes and collections

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

Since this query is executed every time a caller requests Win32_Processor information, we store it in an enum.

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

All entity classes can be found in the `io.github.eggy.ferrumx.windows.entity.*` package.
For processor-related mappings, a dedicated subpackage `entity.processor` is used.

As of v4.1.0, this package includes:

- `Win32_Processor`
- `Win32_CacheMemory`
- `Win32_AssociatedProcessorMemory`

```java
import com.google.gson.GsonBuilder;
import com.google.gson.annotations.SerializedName;
import io.github.eggy03.ferrumx.windows.annotation.ShallowImmutable;
import io.github.eggy03.ferrumx.windows.annotation.WmiClass;
import lombok.Builder;
import lombok.Value;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Value
@Builder(toBuilder = true)
@ShallowImmutable
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

`@Value` is from Lombok, and it makes the class shallowly immutable.

- Marks the class as shallowly immutable
- Generates:
  - private final fields
  - all-arguments constructor
  - getters

This ensures that field references cannot be reassigned after construction.

However, this does not guarantee deep immutability:

- If a field is a collection (List, Map, etc.), the reference is immutable
- But, the underlying collection may still be mutable depending on how it is constructed

In our implementation, collection fields (if present) remain mutable unless explicitly wrapped.

`@Builder` is from Lombok, and it provides a builder pattern for the entity.

Although the mapping layer typically deserializes JSON directly into the entity (bypassing the builder),
the builder is useful for:

- creating partial objects in tests
- creating modified objects in workflows from the source object

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

`@ShallowImmutable` is a custom annotation, and it lets the callers know that:

- All fields are final and cannot be reassigned
- Objects referenced by the fields may be mutable and can be modified externally
- The class is not inherently thread-safe

`@WmiClass` is a custom annotation, and it serves the following purposes:

- It holds information about the actual WMI class name.
- The annotation allows us to get the class name during dynamic construction of queries during runtime.

`@SerializedName` is from GSON and it serves the following purposes:

- It holds information about the actual WMI property name that the field corresponds to. Due to different naming
  conventions, we need to explicitly tell GSON that a property with the given name must be mapped to the given field.

- The annotation allows us to get the property name during dynamic construction of queries during runtime.

`@Nullable` and `@NotNull` are from JetBrains Annotations, and they're strictly used to tell users and static analysis
tools about the nullity of the given fields. It is always safe to assume that all fields for a given entity may be
null at some point or for different systems.

We also have a custom `toString()` implementation that returns a pretty printed JSON formatted String value of the
objects of the class.

### Dynamically Generating Queries

We will now refactor our [Cimv2](#designing-the-query) enum such that it can dynamically fetch the class name
and the properties to be queried.

Take a look at the following refactor:

```java
import io.github.eggy03.ferrumx.windows.entity.processor.Win32Processor;
import io.github.eggy03.ferrumx.windows.shell.query.QueryUtility;

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

We just need to pass `Win32Processor.class` as an argument and the utility will inspect the class.
The utility resides in the `io.github.eggy.ferrumx.windows.shell.query*` package.

We have also used Lombok to remove some constructor and getter boilerplate.

### Designing the Mapper

The mappers are responsible for deserializing the JSON output from PowerShell into typed entities.

All entity mappers reside in the `io.github.eggy.ferrumx.windows.mapping*` package.
We have an interface called `CommonMappingInterface` with default methods which are usually enough to deserialize any
JSON into typed entities. We achieve this via Google GSON.

The following pseudocode shows the interface with the default methods.
For full definition and implementation, check out `io.github.eggy.ferrumx.windows.mapping.CommonMappingInterface`.

```java
import com.google.gson.Gson;

import java.util.List;
import java.util.Optional;

// simplified representation
public interface CommonMappingInterface<S> {

  @NotNull
  Gson GSON = new Gson();

  @NotNull
  @Unmodifiable
  default List<S> mapToList(@NonNull String json, @NonNull Class<S> objectClass) {
        /*
         if JSON starts with "[" it means it's an array, and therefore we will serialize it into a List of type S
         However, there might be cases wherein, you expect an array of objects but get a single object instead.
         For example, WMI lists CPUs as an array of objects but in cases where there is only 1 CPU, it may
         either return as an array of single object or just the object, which will then start with { key = value, key2=value2}.
         
         In the latter case, we will wrap it in a singleton list.
         
         In any case, the returned List of objects is unmodifiable.
         Note that the returned List is unmodifiable,
         but the contained objects may still be mutable depending on their implementation.
        */
  }

  @NotNull
  default Optional<S> mapToObject(@NonNull String json, @NonNull Class<S> objectClass) {
    // You can use this method if you are 100% sure that the JSON will always serialize into a single object
    // One such example is Win32_ComputerSystem. It's documentation states that it always returns a single object.
  }

  // Refer to the actual implementation for details on null handling and edge-case behavior.
}
```

Till now, the mappers for all the entities have used the default implementation of the interface.
We will define our mapper by creating an empty class that implements the interface.

```java
import io.github.eggy03.ferrumx.windows.entity.processor.Win32Processor;
import io.github.eggy03.ferrumx.windows.mapping.CommonMappingInterface;

public class Win32ProcessorMapper implements CommonMappingInterface<Win32Processor> {
  // usually not needed, but you can write your custom implementations here
  // otherwise, the default methods from the interface are sufficient
}
```

This mapper will reside in the `mapping.processor` sub-package and can be simply called
via: `new Win32ProcessorMapper().mapToList(...) or mapToObject(...)`.

### Designing the Service

Service classes are responsible for orchestrating the full workflow of:

- executing PowerShell queries
- retrieving output
- mapping results into entities

All service classes reside in the `io.github.eggy.ferrumx.windows.service*` package.

At a minimum, a service method should:

1) Initialize or receive a PowerShell session
2) Generate and Execute the required query
3) Retrieve the output
4) Delegate deserialization to the mapper
5) Return the mapped result to the caller

Service classes may also:

1) Manage the lifecycle of PowerShell sessions when not provided by the caller
2) Handle failures in session creation or execution
3) Handle malformed, empty, or unexpected outputs (usually handled by the mappers)
4) Clearly document concurrency characteristics (e.g., whether a session is reusable across threads)

Let's take a look at the two interfaces that all the service classes implement.

```java
import com.profesorfalken.jpowershell.PowerShell;

import java.util.List;

public interface CommonServiceInterface<S> {

  List<S> get();

  List<S> get(PowerShell powerShell);

  List<S> get(long timeout);
}
```

```java
import com.profesorfalken.jpowershell.PowerShell;

import java.util.Optional;

public interface OptionalCommonServiceInterface<S> {

  Optional<S> get();

  Optional<S> get(PowerShell powerShell);

  Optional<S> get(long timeout);
}
```

Unlike `CommonMappingInterface`, these two interfaces do not have default methods and the classes that
implement these interfaces must override these methods to have their own definitions.

Let's create a `service.processor` subpackage and provide a concrete definition for `Win32Processor`.

```java
import com.profesorfalken.jpowershell.PowerShell;
import com.profesorfalken.jpowershell.PowerShellResponse;
import io.github.eggy03.ferrumx.windows.annotation.IsolatedPowerShell;
import io.github.eggy03.ferrumx.windows.annotation.UsesJPowerShell;
import io.github.eggy03.ferrumx.windows.entity.processor.Win32Processor;
import io.github.eggy03.ferrumx.windows.mapping.processor.Win32ProcessorMapper;
import io.github.eggy03.ferrumx.windows.shell.query.Cimv2;
import io.github.eggy03.ferrumx.windows.utility.TerminalUtility;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Unmodifiable;

import java.util.List;

public class Win32ProcessorService implements CommonServiceInterface<Win32Processor> {

  @Override
  @UsesJPowerShell
  public @NotNull @Unmodifiable List<Win32Processor> get() {
    PowerShellResponse response = PowerShell.executeSingleCommand(Cimv2.WIN32_PROCESSOR.getQuery());
    return new Win32ProcessorMapper().mapToList(response.getCommandOutput(), Win32Processor.class);
  }

  @Override
  @UsesJPowerShell
  public @NotNull @Unmodifiable List<Win32Processor> get(@NonNull PowerShell powerShell) {
    PowerShellResponse response = powerShell.executeCommand(Cimv2.WIN32_PROCESSOR.getQuery());
    return new Win32ProcessorMapper().mapToList(response.getCommandOutput(), Win32Processor.class);
  }

  @Override
  @IsolatedPowerShell
  public @NotNull @Unmodifiable List<Win32Processor> get(long timeout) {
    String command = Cimv2.WIN32_PROCESSOR.getQuery();
    String response = TerminalUtility.executeCommand(command, timeout);
    return new Win32ProcessorMapper().mapToList(response, Win32Processor.class);
  }
}
```

`get()`

- Uses an auto-managed PowerShell session
- Session is scoped within the current method call and cannot be re-used
- Annotated with `@UsesJPowerShell`, indicating that it relies on the `jPowerShell` library
  and that its current implementation is not suitable for concurrent usage

`get(PowerShell)`

- Uses a caller-provided session
- Session scope is controlled by the caller, which gives them full lifecycle control, allowing re-use
- Annotated with `@UsesJPowerShell`, indicating that it relies on the `jPowerShell` library
  and that its current implementation is not suitable for concurrent usage

`get(long timeout)`

- Uses an alternative auto-managed PowerShell session via `TerminalUtility` and does not require `jPowerShell`
- Session is scoped within the current method call and cannot be re-used
- Annotated with @IsolatedPowerShell, indicating that it does not rely on the `jPowerShell` library
  and that its current implementation is suitable for concurrent usage

`TerminalUtility` is found in the `io.github.eggy03.ferrumx.windows.utility` package

### Writing Tests

Unit tests for the library can be found under the `src/test` directory. Check them out to see what cases are handled
and suggest improvements or write your own tests.

### Putting Everything Together

With the service layer in place, we can finally call our service class.

The following example retrieves processor information using an isolated PowerShell execution with a timeout of 15
seconds.

```java
import io.github.eggy03.ferrumx.windows.entity.processor.Win32Processor;
import io.github.eggy03.ferrumx.windows.service.processor.Win32ProcessorService;

import java.util.List;

@SuppressWarnings("all")
public class Win32ProcessorExample {

  public static void main(String[] args) {
    List<Win32Processor> cpuList = new Win32ProcessorService().get(15);
    cpuList.forEach(System.out::println);
  }
}
```

# References

- [Javadocs](https://eggy03.github.io/ferrumx-windows-documentation/)
- [Examples](https://github.com/eggy03/ferrumx-windows-examples)
- [Microsoft Docs for Win32_Processor](https://learn.microsoft.com/en-us/windows/win32/cimwin32prov/win32-processor)
- [Additional Helpful Docs from `powershell.one`](https://powershell.one/wmi/root/cimv2)