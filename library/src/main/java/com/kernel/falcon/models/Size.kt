package com.kernel.falcon.models

class Size(val value: Int, private val unit: Unit) : Comparable<Size> {

    init {
        require(value >= 0) { "value must be >= 0, was $value" }
    }

    val valueAsByte: Int
        get() = Unit.B.convert(value, unit)

    val isZero: Boolean get() = this == ZERO

    fun valueAs(unit: Unit) {
        unit.convert(value, unit)
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || javaClass != other.javaClass) return false
        val duration = other as Size
        return valueAsByte == duration.valueAsByte
    }

    override fun hashCode(): Int {
        var result = (value xor value.ushr(32)).toInt()
        result = 31 * result + unit.hashCode()
        return result
    }

    override fun toString(): String {
        return "Size{" +
                "unit=" + unit +
                ", value=" + value +
                '}'.toString()
    }

    override fun compareTo(other: Size): Int {
        val x = valueAsByte
        val y = other.valueAsByte
        return if (x < y) -1 else if (x == y) 0 else 1
    }

    companion object {
        val ZERO = Size(0, Unit.B)
        val ONE_MB = Size(1, Unit.MB)
    }

    enum class Unit {
        B {
            override fun toBytes(d: Int): Int { return d; }
            override fun toKiloBytes(d: Int): Int { return d / 1024; }
            override fun toMegaBytes(d: Int): Int { return toKiloBytes(d) / 1024; }
            override fun convert(d: Int, u: Unit): Int { return u.toBytes(d); }
        },
        KB {
            override fun toBytes(d: Int): Int { return d * 1000; }
            override fun toKiloBytes(d: Int): Int { return d; }
            override fun toMegaBytes(d: Int): Int { return d / 1024; }
            override fun convert(d: Int, u: Unit): Int { return u.toKiloBytes(d); }
        },
        MB {
            override fun toBytes(d: Int): Int { return toKiloBytes(d) * 1000; }
            override fun toKiloBytes(d: Int): Int { return d * 1000; }
            override fun toMegaBytes(d: Int): Int { return d; }
            override fun convert(d: Int, u: Unit): Int { return u.toMegaBytes(d); }
        };

        open fun convert(open: Int, u: Unit): Int {
            throw AbstractMethodError()
        }

        open fun toBytes(d: Int): Int {
            throw AbstractMethodError()
        }

        open fun toKiloBytes(d: Int): Int {
            throw AbstractMethodError()
        }

        open fun toMegaBytes(d: Int): Int {
            throw AbstractMethodError()
        }
    }
}