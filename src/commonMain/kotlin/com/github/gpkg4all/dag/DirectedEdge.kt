package com.github.gpkg4all.dag

typealias DirectedEdge<T> = Pair<T, T>

val <T> DirectedEdge<T>.u: T get() = first

val <T> DirectedEdge<T>.v: T get() = second

infix fun <T> T.after(that: T): DirectedEdge<T> = DirectedEdge(this, that)

infix fun <T> T.before(that: T): DirectedEdge<T> = DirectedEdge(that, this)
