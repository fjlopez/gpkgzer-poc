package com.github.gpkg4all.dag

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull

class GraphTest {

    @Test
    fun provideTopologicalSortedOrder() {
        val expected = listOf("std", "ieee", "dware", "dw02", "dw05", "dw06", "dw07", "gtech", "dw01",
            "dw04", "ramlib", "std_cell_lib", "synopsys", "des_system_lib", "dw03")
        assertEquals(expected, sut.topoSortOrNull())
        assertNull(sut.copy(edges = sut.edges + listOf("dw01" to "dw04")).topoSortOrNull())
    }

    @Test
    fun returnNullWhenCyclesArePResent() {
        assertNull(sut.copy(edges = sut.edges + listOf("dw01" to "dw04")).topoSortOrNull())
    }

    companion object {
        private val vertices = listOf(
            "std", "ieee", "des_system_lib", "dw01", "dw02", "dw03", "dw04", "dw05", "dw06", "dw07",
            "dware", "gtech", "ramlib", "std_cell_lib", "synopsys"
        )

        private val edges = mutableListOf(
            2 to 0, 2 to 14, 2 to 13, 2 to 4, 2 to 3, 2 to 12, 2 to 1,
            3 to 1, 3 to 10, 3 to 11,
            4 to 1, 4 to 10,
            5 to 0, 5 to 14, 5 to 10, 5 to 4, 5 to 3, 5 to 1, 5 to 11,
            6 to 1, 6 to 3, 6 to 10, 6 to 11,
            7 to 1, 7 to 10,
            8 to 1, 8 to 10,
            9 to 1, 9 to 10,
            10 to 1,
            11 to 1,
            12 to 0, 12 to 1,
            13 to 1
        ).map { (i, r) -> Pair(vertices[i], vertices[r]) }

        val sut = Graph(vertices, edges)
    }
}