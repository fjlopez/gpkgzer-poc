package com.github.gpkg4all.dag

interface Node {
    val before: List<Node>
    val after: List<Node>
}

interface AddFeature : Node

interface ReplaceFeature : Node {
    val deprecates: List<Node>
}

fun List<Node>.prune(): List<Node> {
    tailrec fun pruneAcc(acc: List<Node>, nodes: List<Node>): List<Node> = if (nodes.isEmpty()) {
        acc
    } else {
        val head = nodes.first()
        val tail = nodes.drop(1)
        if (tail.filterIsInstance<ReplaceFeature>().any { head in it.deprecates })
            pruneAcc(acc, tail)
        else
            pruneAcc(acc + head, tail)
    }
    return pruneAcc(emptyList(), this)
}



fun List<Node>.topoSort(): List<Node>? {
    val nodes = (this + flatMap { it.before } + flatMap { it.after }).distinct()
    val edges = flatMap { u -> u.before.map { v -> u before v } + u.after.map { v -> u after v } }
    val sort = Graph(nodes, edges).topoSortOrNull() ?: return null
    return sort.prune()
}