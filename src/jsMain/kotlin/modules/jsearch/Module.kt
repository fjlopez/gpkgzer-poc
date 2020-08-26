/**
 * See https://fkhadra.github.io/react-toastify/introduction
 */

@file:JsModule("js-search")
@file:JsNonModule

package modules.jsearch

@JsName("Search")
external class Search<T>(uidFieldName: dynamic) {
    fun addIndex(field: String)
    fun addDocument(document: T)
    fun search(query: String): Array<T>
}

