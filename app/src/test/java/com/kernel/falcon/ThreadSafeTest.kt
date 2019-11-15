package com.kernel.falcon

import com.kernel.falcon.utils.RandomUtil

import org.junit.Before
import org.junit.Test

import java.util.concurrent.atomic.AtomicBoolean
import java.util.concurrent.atomic.AtomicInteger

import org.junit.Assert.assertEquals

class ThreadSafeTest : BaseTest() {

    lateinit var cache: Cache<String>

    @Before
    fun init() {
        cache = Cache.Builder().build(context)
    }

    @Test
    @Throws(InterruptedException::class)
    fun canEditWhileIteratingWithRandomKeys() {
        val errors = AtomicInteger(0)
        val writerFinished1 = AtomicBoolean(false)
        val writerFinished2 = AtomicBoolean(false)
        val readerFinished = AtomicBoolean(false)
        val removerFinished = AtomicBoolean(false)

        val writer1 = object : Thread() {
            override fun run() {
                for (i in 0 until ITERATIONS) {
                    try {
                        cache[RandomUtil.generateString(), BaseTest.Companion.A_VALUE] = 0
                    } catch (e: Exception) {
                        errors.set(errors.get() + 1)
                    }

                }
                writerFinished1.set(true)
            }
        }
        val writer2 = object : Thread() {
            override fun run() {
                for (i in 0 until ITERATIONS) {
                    try {
                        cache[RandomUtil.generateString(), BaseTest.Companion.B_VALUE] = 0
                    } catch (e: Exception) {
                        errors.set(errors.get() + 1)
                    }

                }
                writerFinished2.set(true)
            }
        }
        val reader = object : Thread() {
            override fun run() {
                while (!writerFinished1.get() || !writerFinished2.get()) {
                    try {
                        cache[RandomUtil.generateString()]
                    } catch (e: Exception) {
                        errors.set(errors.get() + 1)
                    }

                }
                readerFinished.set(true)
            }
        }
        val remover = object : Thread() {
            override fun run() {
                while (!writerFinished1.get() || !writerFinished2.get()) {
                    try {
                        cache.remove(RandomUtil.generateString())
                    } catch (e: Exception) {
                        errors.set(errors.get() + 1)
                    }

                }
                removerFinished.set(true)
            }
        }
        writer1.start()
        writer2.start()
        reader.start()
        remover.start()

        while (!removerFinished.get() || !readerFinished.get() || !writerFinished1.get() || !writerFinished2.get()) {
            Thread.sleep(100)
            assertEquals(0, errors.get().toLong())
        }
    }

    @Test
    @Throws(InterruptedException::class)
    fun canEditWhileIteratingWithOneKey() {
        val errors = AtomicInteger(0)
        val writerFinished1 = AtomicBoolean(false)
        val writerFinished2 = AtomicBoolean(false)
        val readerFinished = AtomicBoolean(false)
        val removerFinished = AtomicBoolean(false)

        val writer1 = object : Thread() {
            override fun run() {
                for (i in 0 until ITERATIONS) {
                    try {
                        cache[BaseTest.Companion.A_KEY, RandomUtil.generateString()] = 0
                    } catch (e: Exception) {
                        errors.set(errors.get() + 1)
                    }

                }
                writerFinished1.set(true)
            }
        }
        val writer2 = object : Thread() {
            override fun run() {
                for (i in 0 until ITERATIONS) {
                    try {
                        cache[BaseTest.Companion.A_KEY, RandomUtil.generateString()] = 0
                    } catch (e: Exception) {
                        errors.set(errors.get() + 1)
                    }

                }
                writerFinished2.set(true)
            }
        }
        val reader = object : Thread() {
            override fun run() {
                while (!writerFinished1.get() || !writerFinished2.get()) {
                    try {
                        cache[BaseTest.Companion.A_KEY]
                    } catch (e: Exception) {
                        errors.set(errors.get() + 1)
                    }

                }
                readerFinished.set(true)
            }
        }
        val remover = object : Thread() {
            override fun run() {
                while (!writerFinished1.get() || !writerFinished2.get()) {
                    try {
                        cache.remove(BaseTest.Companion.A_KEY)
                    } catch (e: Exception) {
                        errors.set(errors.get() + 1)
                    }

                }
                removerFinished.set(true)
            }
        }
        writer1.start()
        writer2.start()
        reader.start()
        remover.start()

        while (!removerFinished.get() || !readerFinished.get() || !writerFinished1.get() || !writerFinished2.get()) {
            Thread.sleep(100)
            assertEquals(0, errors.get().toLong())
        }
    }

    companion object {
        private val ITERATIONS = 10000
    }

}
