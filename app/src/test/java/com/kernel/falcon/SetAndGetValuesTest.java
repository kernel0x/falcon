package com.kernel.falcon;

import com.kernel.falcon.mock.MockDateProvider;
import com.kernel.falcon.models.Duration;

import org.junit.Before;
import org.junit.Test;

import java.util.concurrent.TimeUnit;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class SetAndGetValuesTest extends BaseTest {

    Cache<String> cache;
    MockDateProvider dateProvider;

    @Before
    public void init() {
        cache = new Cache.Builder().build(getContext());
        cache.clear();
        dateProvider = new MockDateProvider();
        cache.setDateProvider(dateProvider);
    }

    @Test
    public void savingSomethingForOneSecondsShouldReturnTheSameImmediatelly() {
        cache.set(BaseTest.A_KEY, BaseTest.A_VALUE, Companion.getONE_SECOND());

        assertEquals(BaseTest.A_VALUE, cache.get(BaseTest.A_KEY));
    }

    @Test
    public void savingSomethingForeverShouldReturnTheSameImmediatelly() {
        cache.set(BaseTest.A_KEY, BaseTest.A_VALUE, Companion.getFOREVER());

        assertEquals(BaseTest.A_VALUE, cache.get(BaseTest.A_KEY));
    }

    @Test
    public void savingSomethingWithoudSpecifyingTheLifetimeValueShouldReturnTheSameAfterThreeDays() {
        cache.set(BaseTest.A_KEY, BaseTest.A_VALUE);

        dateProvider.setFixed(threeDaysFromNow());

        assertEquals(BaseTest.A_VALUE, cache.get(BaseTest.A_KEY));
    }

    @Test
    public void savingSomethingForeverShouldReturnTheSameAfterThreeDays() {
        cache.set(BaseTest.A_KEY, BaseTest.A_VALUE, Companion.getFOREVER());

        dateProvider.setFixed(threeDaysFromNow());

        assertEquals(BaseTest.A_VALUE, cache.get(BaseTest.A_KEY));
    }

    @Test
    public void savingAValueForTwoHoursShouldReturnNullAfterThreeDays() {
        cache.set(BaseTest.A_KEY, BaseTest.A_VALUE, Companion.getTWO_HOURS());

        dateProvider.setFixed(threeDaysFromNow());

        assertNull(cache.get(BaseTest.A_KEY));
    }

    @Test
    public void savingSomethingForANegativeTimeWillBeIgnored() {
        cache.set(BaseTest.A_KEY, BaseTest.A_VALUE, -1);
        cache.set(BaseTest.B_KEY, BaseTest.B_VALUE, Companion.getONE_SECOND());

        assertNull(cache.get(BaseTest.A_KEY));
        assertEquals(1, cache.size());
        assertEquals(1, cache.sizeDeadAndAliveElements());
    }

    @Test
    public void gettingAIgnoredValueWithGetAndRemoveIfDeadWorks() {
        cache.set(BaseTest.A_KEY, BaseTest.A_VALUE, -1);
        cache.set(BaseTest.B_KEY, BaseTest.B_VALUE, Companion.getTHREE_DAYS());

        assertNull(cache.getAndCleanupIfDead(BaseTest.A_KEY));
        assertEquals(1, cache.size());
        assertEquals(1, cache.sizeDeadAndAliveElements());
    }

    @Test
    public void gettingADeadValueWithGetAndRemoveIfDeadWorks() {
        cache.set(BaseTest.A_KEY, BaseTest.A_VALUE, Companion.getONE_SECOND());
        cache.set(BaseTest.B_KEY, BaseTest.B_VALUE, Companion.getTHREE_DAYS());

        dateProvider.setFixed(twoHoursFromNow());

        assertNull(cache.getAndCleanupIfDead(BaseTest.A_KEY));
        assertEquals(1, cache.size());
        assertEquals(1, cache.sizeDeadAndAliveElements());
    }

    @Test
    public void gettingAnAliveValueWithGetAndRemoveIfDeadWorks() {
        cache.set(BaseTest.A_KEY, BaseTest.A_VALUE, Companion.getONE_SECOND());
        cache.set(BaseTest.B_KEY, BaseTest.B_VALUE, Companion.getTHREE_DAYS());

        dateProvider.setFixed(twoHoursFromNow());

        assertEquals(BaseTest.B_VALUE, cache.getAndCleanupIfDead(BaseTest.B_KEY));
        assertEquals(1, cache.size());
        assertEquals(2, cache.sizeDeadAndAliveElements());
    }

    @Test
    public void removingAValueWorks() {
        cache.set(BaseTest.A_KEY, BaseTest.A_VALUE, Companion.getTWO_HOURS());
        cache.set(BaseTest.B_KEY, BaseTest.B_VALUE, Companion.getTHREE_DAYS());

        cache.remove(BaseTest.A_KEY);

        assertNull(cache.get(BaseTest.A_KEY));
        assertEquals(BaseTest.B_VALUE, cache.get(BaseTest.B_KEY));
    }

    @Test
    public void removingAllValuesWorks() {
        cache.set(BaseTest.A_KEY, BaseTest.A_VALUE, Companion.getTWO_HOURS());
        cache.set(BaseTest.B_KEY, BaseTest.B_VALUE, Companion.getTHREE_DAYS());

        cache.clear();

        assertNull(cache.get(BaseTest.A_KEY));
        assertNull(cache.get(BaseTest.B_KEY));
    }

    @Test
    public void replacingAValueForANewOneReturnTheNewOne() {
        cache.set(BaseTest.A_KEY, BaseTest.A_VALUE, Companion.getTWO_HOURS());
        cache.set(BaseTest.A_KEY, BaseTest.B_VALUE, Companion.getTHREE_DAYS());

        assertEquals(BaseTest.B_VALUE, cache.get(BaseTest.A_KEY));
    }

    @Test
    public void replacingAValueForANewOneCanMakeItDead() {
        cache.set(BaseTest.A_KEY, BaseTest.A_VALUE, Companion.getTHREE_DAYS());
        cache.set(BaseTest.A_KEY, BaseTest.B_VALUE, Companion.getONE_SECOND());

        dateProvider.setFixed(twoHoursFromNow());

        assertNull(cache.get(BaseTest.A_KEY));
    }

    @Test
    public void replacingAValueForNullWillRemoveIt() {
        cache.set(BaseTest.A_KEY, BaseTest.A_VALUE, Companion.getTHREE_DAYS());
        cache.set(BaseTest.A_KEY, null, Companion.getONE_SECOND());

        assertNull(cache.get(BaseTest.A_KEY));
    }

    @Test
    public void containsReturnsTrueIfSomethingExists() {
        cache.set(BaseTest.A_KEY, BaseTest.A_VALUE, Companion.getTHREE_DAYS());

        assertTrue(cache.contains(BaseTest.A_KEY));
    }

    @Test
    public void containsReturnsFalseSomethingDoesntExist() {
        assertFalse(cache.contains(BaseTest.A_KEY));
    }

    @Test
    public void savingSomethingForOneSecondsShouldReturnTheSameImmediatelly_usingSecondsTimeUnit() {
        cache.set(BaseTest.A_KEY, BaseTest.A_VALUE, new Duration(1, TimeUnit.SECONDS));

        assertEquals(BaseTest.A_VALUE, cache.get(BaseTest.A_KEY));
    }

    @Test
    public void savingSomethingForOneSecondsShouldReturnTheSameImmediatelly_usingMillisTimeUnit() {
        cache.set(BaseTest.A_KEY, BaseTest.A_VALUE, new Duration(1000, TimeUnit.MILLISECONDS));

        assertEquals(BaseTest.A_VALUE, cache.get(BaseTest.A_KEY));
    }

}
