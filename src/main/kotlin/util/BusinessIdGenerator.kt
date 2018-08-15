package util


class BusinessIdGenerator {
    companion object {
        fun generate(): Long {
            return System.nanoTime()
        }
    }
}