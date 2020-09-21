package com.github.gpkg4all.dag

/**
 * @param vertices list of vertices
 * @param edges list of edges.
 */
data class Graph<T>(
    val vertices: List<T>,
    val edges: List<Pair<T, T>>
)

/**
 * Topological sorting with [Kahn's algorithm](https://en.wikipedia.org/wiki/Topological_sorting#Kahn's_algorithm).
 *
 * Each edge (u,v) implies u comes after v in the ordering.
 */
fun <T> Graph<T>.topoSortOrNull(): List<T>? = runCatching {
    val edgesIdx = edges.map { Pair(vertices.indexOf(it.first), vertices.indexOf(it.second)) }
    val adjacency = List(vertices.size) { BooleanArray(vertices.size) }
    for ((i, r) in edgesIdx) adjacency[i][r] = true
    val todo = IntRange(0, vertices.size - 1).toMutableList()
    val result = mutableListOf<T>()
    outer@ while (todo.isNotEmpty()) {
        for ((i, r) in todo.withIndex()) {
            if (!todo.any { adjacency[r][it] }) {
                todo.removeAt(i)
                result.add(vertices[r])
                continue@outer
            }
        }
        throw Exception("Graph has cycles")
    }
    result
}.getOrNull()
