package com.github.gpkg4all.dag

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull

class GraphTest {

    @Test
    fun provideTopologicalSortedOrder() {
        val expected = listOf(
            "std", "ieee", "dware", "dw02", "dw05", "dw06", "dw07", "gtech", "dw01",
            "dw04", "ramlib", "std_cell_lib", "synopsys", "des_system_lib", "dw03"
        )
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
            2 after 0, 2 after 14, 2 after 13, 2 after 4, 2 after 3, 2 after 12, 2 after 1,
            3 after 1, 3 after 10, 3 after 11,
            4 after 1, 4 after 10,
            5 after 0, 5 after 14, 5 after 10, 5 after 4, 5 after 3, 5 after 1, 5 after 11,
            6 after 1, 6 after 3, 6 after 10, 6 after 11,
            7 after 1, 7 after 10,
            8 after 1, 8 after 10,
            9 after 1, 9 after 10,
            10 after 1,
            11 after 1,
            12 after 0, 12 after 1,
            13 after 1
        ).map { (i, r) -> DirectedEdge(vertices[i], vertices[r]) }

        val sut = Graph(vertices, edges)
    }
}