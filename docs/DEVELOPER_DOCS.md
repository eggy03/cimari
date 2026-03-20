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
3) Map the processed data into POJOs and return them to the caller

Some of the secondary goals of the API can be described as:

1) Handle scenarios when PowerShell cannot be launched or throws an error during query runs
2) Handle scenarios where queries return incomplete or unexpected results
3) Safely manage null, empty, or malformed results
4) Handle mutability of mapped POJOs and collections