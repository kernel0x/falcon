package com.kernel.falcon;

import com.kernel.falcon.models.Duration;

import org.junit.Test;

import java.util.concurrent.TimeUnit;

public class IntroducingCacheTest extends BaseTest {

    @SuppressWarnings("UnusedAssignment")
    @Test
    public void theCodeOfTheReadmeWorks() {
        //##The sample

        Cache cache = new Cache.Builder().build(getContext());

        cache.set("key", "value", 60 * 1000); // It can store things for a minute,
        cache.set("key", "value", 60 * 60 * 1000); // for an hour,
        cache.set("key", "value", 0); // or forever.
        cache.set("key", "value"); // And also for the short version of forever.

        cache.get("key"); // It can get them again,
        cache.remove("key"); // and remove it if you want.

        cache.get("unExistingKey"); // If something doesn't exists, it returns null
        cache.get("tooOldKey"); // The same if a key is too old

        cache.clear(); // You can also clean it,
        cache.size(); // and ask it how many elements it has

        Cache<String> stringCache = new Cache.Builder().build(getContext()); //You can also make it typesafe
        //stringCache.set("key", 42); //so this will not compile :)

        //##Let's talk about the memory
        cache = new Cache.Builder().autoCleanup(Duration.Companion.getONE_SECOND()).build(getContext()); //frees the memory every second
        cache = new Cache.Builder().autoCleanup(Duration.Companion.getONE_MINUTE()).build(getContext()); //frees the memory every minute
        cache = new Cache.Builder().autoCleanup(new Duration(1, TimeUnit.HOURS)).build(getContext()); //frees the memory every hour
        cache = new Cache.Builder().build(getContext()); //never frees the memory

        //##Are the keys case sensitive?
        cache = new Cache.Builder().caseSensitiveKeys(true).build(getContext()); //"key" and "KEY" will be different items
        cache = new Cache.Builder().caseSensitiveKeys(false).build(getContext()); //"key" and "KEY" will be the same
        cache = new Cache.Builder().build(getContext()); //"key" and "KEY" will be different items

        //##It's possible to change the default lifetime?
        cache = new Cache.Builder().defaultLifetime(Duration.Companion.getONE_SECOND()).build(getContext()); //a lifetime of one second
        cache = new Cache.Builder().defaultLifetime(Duration.Companion.getONE_MINUTE()).build(getContext()); //a lifetime of one minute
        cache = new Cache.Builder().build(getContext()); //the default lifetime: remember it forever!
    }
}
