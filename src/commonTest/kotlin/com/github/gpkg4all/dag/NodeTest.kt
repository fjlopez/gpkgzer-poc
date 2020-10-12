package com.github.gpkg4all.dag

import kotlin.test.Test
import kotlin.test.assertEquals

class NodeTest {

    /**
     * Expected:
     * ```
     * 1st < 2nd < 3rd (deprecates if before 2nd) < 4th -> 1st < 2nd < 4th
     * ```
     */
    @Test
    fun pruneList() {
        assertEquals(listOf(second, third, fourth), listOf(first, second, third, fourth).prune())
    }

    /**
     * Expected:
     * ```
     * given
     *   rule 2nd after 1st
     *   rule 3rd after 1st and remove 1st
     *   rule 4th after 2nd and 3rd
     * then
     *   (2nd, 3rd, 4th) or (3rd, 2nd, 4th)
     * ```
     */
    @Test
    fun sortList() {
        assertEquals(listOf(second, third, fourth), listOf(fourth, second, third, first).topoSort())
        assertEquals(listOf(third, second, fourth), listOf(fourth, third, second, first).topoSort())
    }

    companion object {
        class FixtureAddFeature(
            private val name: String,
            override val before: List<Node<Any>> = emptyList(),
            override val after: List<Node<Any>> = emptyList()
        ) : Node<Any> {
            override fun toString(): String = name
            override val value: Any
                get() = TODO("Not yet implemented")
        }

        class FixtureNodeUpdate(
            private val name: String,
            override val before: List<Node<Any>> = emptyList(),
            override val after: List<Node<Any>> = emptyList(),
            override val deprecates: List<Node<Any>> = emptyList()
        ) : NodeUpdate<Any> {
            override fun toString(): String = name
            override val value: Any
                get() = TODO("Not yet implemented")
        }


        val first = FixtureAddFeature("first")
        val second = FixtureAddFeature("second", after = listOf(first))
        val third = FixtureNodeUpdate("third", after = listOf(first), deprecates = listOf(first))
        val fourth = FixtureAddFeature("fourth", after = listOf(second, third))
    }
}