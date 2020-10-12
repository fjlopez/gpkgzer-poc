package com.github.gpkg4all.dag

interface Node<T> {
    val value: T
    val before: List<Node<T>>
    val after: List<Node<T>>
}

interface NodeUpdate<T> : Node<T> {
    val deprecates: List<Node<T>>
}

fun <T> List<Node<T>>.prune(): List<Node<T>> {
    tailrec fun pruneAcc(acc: List<Node<T>>, nodes: List<Node<T>>): List<Node<T>> = if (nodes.isEmpty()) {
        acc
    } else {
        val head = nodes.first()
        val tail = nodes.drop(1)
        if (tail.filterIsInstance<NodeUpdate<T>>().any { head in it.deprecates })
            pruneAcc(acc, tail)
        else
            pruneAcc(acc + head, tail)
    }
    return pruneAcc(emptyList(), this)
}


fun <T> List<Node<T>>.topoSort(): List<Node<T>>? {
    val nodes = (this + flatMap { it.before } + flatMap { it.after }).distinct()
    val edges = flatMap { u -> u.before.map { v -> u before v } + u.after.map { v -> u after v } }
    val sort = Graph(nodes, edges).topoSortOrNull() ?: return null
    return sort.prune()
}