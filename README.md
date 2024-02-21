# GrandPower

Simple Energy API with support for long amounts, and full interoperability with Forge Energy.

- (Sort of) Standard `long`-based energy API for NeoForge.
- Automated 1:1 conversion to and from Forge Energy (`IEnergyStorage`).
- Useful default implementations and utility methods.

## Adding GrandPower to your project
TODO

## Using GrandPower
Just use [`ILongEnergyStorage`](src/main/java/dev/technici4n/grandpower/api/ILongEnergyStorage.java)
instead of `IEnergyStorage` in your project. It's that simple!

You can use the `BLOCK`, `ENTITY`, and `ITEM` capabilities from `ILongEnergyStorage`,
and everything will be automatically be converted to and from Forge Energy.
To have a look at how the conversion works,
check out [`GrandPowerImpl`](src/main/java/dev/technici4n/grandpower/impl/GrandPowerImpl.java).
