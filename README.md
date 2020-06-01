# Falcon
A lightweight and simple library to cache any serializable objects, using the LRU algorithm, two storage levels (files, memory), encryption and life cycle.

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
  implementation 'com.github.kernel0x:falcon:1.0.2'
}
````

## How to use

Create an object using the builder.

````java
Cache<Object> cache = new Cache.Builder().build(getContext());
````
OR with special type

````java
Cache<Cat> cache = new Cache.Builder().build(getContext());
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
cache.set(KEY, new Cat());
````
````java
cache.set(KEY, new Cat(), DualCacheMode.ONLY_DISK);
````
````java
cache.set(KEY, new Cat(), DualCacheMode.ONLY_RAM);
````
````java
cache.set(KEY, new Cat(), new Duration(1, TimeUnit.SECONDS));
````

## Features

* encryption
* cache location selection (file or memory)
* LRU, cache extrusion method
* cache lifetime selection
* thread safe

## Tests
It's totally tested. [Check the tests!](/app/src/test/java/com/kernel/falcon) :wink:

## Releases
Checkout the [Releases](https://github.com/kernel0x/falcon/releases) tab for all release info.
