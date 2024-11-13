# GrandPower

Simple Energy API with support for long amounts, and full interoperability with Forge Energy.

- (Sort of) Standard `long`-based energy API for NeoForge.
- Automated 1:1 conversion to and from Forge Energy (`IEnergyStorage`).
- Useful default implementations and utility methods.

## Adding GrandPower to your project
Add the modmaven repository:
```gradle
repositories {
    maven {
        url "https://modmaven.dev/"
        content {
            includeGroup "dev.technici4n"
        }
    }
}
```

Add the version you want to use as a Gradle property:
```properties
# Latest version can be found at https://modmaven.dev/dev/technici4n/GrandPower/
# Make sure you choose a version that is compatible with your Minecraft version (e.g. 3.x.x for Minecraft 1.21.1).
grandpower_version=3.0.0
```

Then, add GrandPower to your project:
```gradle
dependencies {
    api "dev.technici4n:GrandPower:${project.grandpower_version}"
}
```

If you want to include GrandPower in your mod jar directly, you can use the following line instead:
(This assumes that you are using the [ModDevGradle](https://github.com/neoforged/ModDevGradle) plugin.)
```gradle
dependencies {
    api jarJar("dev.technici4n:GrandPower:${project.grandpower_version}")
}
```


## Using GrandPower
Just use [`ILongEnergyStorage`](src/main/java/dev/technici4n/grandpower/api/ILongEnergyStorage.java)
instead of `IEnergyStorage` in your project. It's that simple!

You can use the `BLOCK`, `ENTITY`, and `ITEM` capabilities from `ILongEnergyStorage`,
and everything will be automatically be converted to and from Forge Energy.
To have a look at how the conversion works,
check out [`GrandPowerImpl`](src/main/java/dev/technici4n/grandpower/impl/GrandPowerImpl.java).
