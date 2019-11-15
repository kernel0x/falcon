# Falcon
Thread-safe, crypto-protected, multi-level LRU caching with a life cycle.

## Gradle Dependency

Add it in your root build.gradle at the end of repositories:

````java
allprojects {
  repositories {
    ...
    maven { url "https://jitpack.io"}
  }
}
````

Add the dependency:

````java
dependencies {
  androidTestImplementation 'com.github.kernel0x:falcon:1.0.0'
}
````

## How to use

Create an object using the builder.

````java
val cache = Cache.Builder().build<Any>(context)
````
OR with special type

````java
val cache = Cache.Builder().build<Cat>(context)
````
Available methods in the Builder

- **defaultLifetime** *default cache lifetime*
- **maxSize** *maximum cache size (file and ram)*
- **caseSensitiveKeys** *key case sensitivity*
- **autoCleanup** *auto clean timer time*
- **dualCacheMode** *caching mode*
- **encryptStrategy** *encryption algorithm*

Everything is simple. Now you can cache something.
Important! Cached objects must implement Serializable

### Examples:
````java
cache[KEY] = Cat()
````
````java
cache[KEY, Cat()] = DualCacheMode.ONLY_DISK
````
````java
cache[KEY, Cat()] = DualCacheMode.ONLY_RAM
````
````java
cache[KEY, Cat()] = ONE_SECOND
````

## Features

* encryption
* cache location selection (file or memory)
* LRU, cache extrusion method
* cache lifetime selection

## Tests
It's totally tested. [Check the tests!](/app/src/test/java/com/kernel/falcon) :wink:

## Releases
Checkout the [Releases](https://github.com/kernel0x/falcon/releases) tab for all release info.
