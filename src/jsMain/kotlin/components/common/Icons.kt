package components.common

import kotlinext.js.jsObject
import react.RBuilder
import react.RProps
import react.createElement
import react.dom.svg

fun RBuilder.iconTime() = svg("icon-time") {
    attrs["height"] = 14.6
    attrs["width"] = 14.6
    child(createLine("st0", 1.5, 1.5, 13.1, 13.1, key = "Line_1"))
    child(createLine("st0", 13.1, 1.5, 1.5, 13.1, key = "Line_2"))
}


fun RBuilder.iconSun() = svg("icon-sun") {
    attrs["viewBox"] = "0 0 512 512"
    child(
        createPath(
            "M256 160c-52.9 0-96 43.1-96 96s43.1 96 96 96 96-43.1 96-96-43.1-96-96-96zm246.4 80.5l-94.7-47.3 33.5-100.4c4.5-13.6-8.4-26.5-21.9-21.9l-100.4 33.5-47.4-94.8c-6.4-12.8-24.6-12.8-31 0l-47.3 94.7L92.7 70.8c-13.6-4.5-26.5 8.4-21.9 21.9l33.5 100.4-94.7 47.4c-12.8 6.4-12.8 24.6 0 31l94.7 47.3-33.5 100.5c-4.5 13.6 8.4 26.5 21.9 21.9l100.4-33.5 47.3 94.7c6.4 12.8 24.6 12.8 31 0l47.3-94.7 100.4 33.5c13.6 4.5 26.5-8.4 21.9-21.9l-33.5-100.4 94.7-47.3c13-6.5 13-24.7.2-31.1zm-155.9 106c-49.9 49.9-131.1 49.9-181 0-49.9-49.9-49.9-131.1 0-181 49.9-49.9 131.1-49.9 181 0 49.9 49.9 49.9 131.1 0 181z",
            "currentColor"
        )
    )
}

fun RBuilder.iconMoon() = svg("icon-moon") {
    attrs["viewBox"] = "0 0 512 512"
    child(
        createPath(
            "M283.211 512c78.962 0 151.079-35.925 198.857-94.792 7.068-8.708-.639-21.43-11.562-19.35-124.203 23.654-238.262-71.576-238.262-196.954 0-72.222 38.662-138.635 101.498-174.394 9.686-5.512 7.25-20.197-3.756-22.23A258.156 258.156 0 0 0 283.211 0c-141.309 0-256 114.511-256 256 0 141.309 114.511 256 256 256z",
            "currentColor"
        )
    )
}

fun RBuilder.iconGitHub() = svg("icon-github") {
    attrs["viewBox"] = "0 0 496 512"
    child(
        createPath(
            "M165.9 397.4c0 2-2.3 3.6-5.2 3.6-3.3.3-5.6-1.3-5.6-3.6 0-2 2.3-3.6 5.2-3.6 3-.3 5.6 1.3 5.6 3.6zm-31.1-4.5c-.7 2 1.3 4.3 4.3 4.9 2.6 1 5.6 0 6.2-2s-1.3-4.3-4.3-5.2c-2.6-.7-5.5.3-6.2 2.3zm44.2-1.7c-2.9.7-4.9 2.6-4.6 4.9.3 2 2.9 3.3 5.9 2.6 2.9-.7 4.9-2.6 4.6-4.6-.3-1.9-3-3.2-5.9-2.9zM244.8 8C106.1 8 0 113.3 0 252c0 110.9 69.8 205.8 169.5 239.2 12.8 2.3 17.3-5.6 17.3-12.1 0-6.2-.3-40.4-.3-61.4 0 0-70 15-84.7-29.8 0 0-11.4-29.1-27.8-36.6 0 0-22.9-15.7 1.6-15.4 0 0 24.9 2 38.6 25.8 21.9 38.6 58.6 27.5 72.9 20.9 2.3-16 8.8-27.1 16-33.7-55.9-6.2-112.3-14.3-112.3-110.5 0-27.5 7.6-41.3 23.6-58.9-2.6-6.5-11.1-33.3 2.6-67.9 20.9-6.5 69 27 69 27 20-5.6 41.5-8.5 62.8-8.5s42.8 2.9 62.8 8.5c0 0 48.1-33.6 69-27 13.7 34.7 5.2 61.4 2.6 67.9 16 17.7 25.8 31.5 25.8 58.9 0 96.5-58.9 104.2-114.8 110.5 9.2 7.9 17 22.9 17 46.4 0 33.7-.3 75.4-.3 83.6 0 6.5 4.6 14.4 17.3 12.1C428.2 457.8 496 362.9 496 252 496 113.3 383.5 8 244.8 8zM97.2 352.9c-1.3 1-1 3.3.7 5.2 1.6 1.6 3.9 2.3 5.2 1 1.3-1 1-3.3-.7-5.2-1.6-1.6-3.9-2.3-5.2-1zm-10.8-8.1c-.7 1.3.3 2.9 2.3 3.9 1.6 1 3.6.7 4.3-.7.7-1.3-.3-2.9-2.3-3.9-2-.6-3.6-.3-4.3.7zm32.4 35.6c-1.6 1.3-1 4.3 1.3 6.2 2.3 2.3 5.2 2.6 6.5 1 1.3-1.3.7-4.3-1.3-6.2-2.2-2.3-5.2-2.6-6.5-1zm-11.4-14.7c-1.6 1-1.6 3.6 0 5.9 1.6 2.3 4.3 3.3 5.6 2.3 1.6-1.3 1.6-3.9 0-6.2-1.4-2.3-4-3.3-5.6-2z",
            "currentColor"
        )
    )
}

fun RBuilder.iconTwitter() = svg("icon-twitter") {
    attrs["viewBox"] = "0 0 512 512"
    child(
        createPath(
            "M459.37 151.716c.325 4.548.325 9.097.325 13.645 0 138.72-105.583 298.558-298.558 298.558-59.452 0-114.68-17.219-161.137-47.106 8.447.974 16.568 1.299 25.34 1.299 49.055 0 94.213-16.568 130.274-44.832-46.132-.975-84.792-31.188-98.112-72.772 6.498.974 12.995 1.624 19.818 1.624 9.421 0 18.843-1.3 27.614-3.573-48.081-9.747-84.143-51.98-84.143-102.985v-1.299c13.969 7.797 30.214 12.67 47.431 13.319-28.264-18.843-46.781-51.005-46.781-87.391 0-19.492 5.197-37.36 14.294-52.954 51.655 63.675 129.3 105.258 216.365 109.807-1.624-7.797-2.599-15.918-2.599-24.04 0-57.828 46.782-104.934 104.934-104.934 30.213 0 57.502 12.67 76.67 33.137 23.715-4.548 46.456-13.32 66.599-25.34-7.798 24.366-24.366 44.833-46.132 57.827 21.117-2.273 41.584-8.122 60.426-16.243-14.292 20.791-32.161 39.308-52.628 54.253z",
            "currentColor"
        )
    )
}

fun RBuilder.iconRemove() = svg("icon-twitter") {
    attrs["height"] = 26.0
    attrs["width"] = 26.0
    attrs["viewBox"] = "0 0 512 512"
    child(
        createGraph(
            "Layer_1_1_",
            createPath(
                "M494,256c0,131.4-106.6,238-238,238S18,387.4,18,256S124.6,18,256,18S494,124.6,494,256z",
                className = "st0",
                key = "Layer_1_1_1"
            ),
        )
    )
    child(
        createGraph(
            "Layer_2_1_",
            createLine("st1", 114.4, 260.0, 397.5, 260.0, "Layer_2_1_1")
        )
    )
}

fun RBuilder.iconEnter() = svg("icon-enter") {
    attrs["viewBox"] = "0 0 128 128"
    child(
        createGraph(
            "Layer_3_1_",
            createPolygon(
                """
                112.5,23.3 112.5,75.8 37.5,75.8 37.5,56 0.5,83.3 37.5,110.7 37.5,90.8 112.5,90.8 
                112.5,90.8 127.5,90.8 127.5,23.3""", "Layer_3_1_1"
            )

        )
    )
}

interface LineProps : RProps {
    var className: String
    var x1: Double
    var y1: Double
    var x2: Double
    var y2: Double
    var key: String?
}

interface PathProps : RProps {
    var fill: String
    var d: String
    var className: String
    var key: String?
}

interface Polygon : RProps {
    var points: String
    var key: String?
}

interface GraphProps : RProps {
    var id: String?
}

fun createLine(className: String, x1: Double, y1: Double, x2: Double, y2: Double, key: String? = "") =
    createElement("line",
        jsObject<LineProps> {
            this.className = className
            this.x1 = x1
            this.y1 = y1
            this.x2 = x2
            this.y2 = y2
            key?.let { this.key = it }
        }
    )

fun createPath(d: String, fill: String? = null, className: String? = null, key: String? = null) =
    createElement("path",
        jsObject<PathProps> {
            key?.let { this.key = it }
            className?.let { this.className = it }
            fill?.let { this.fill = it }
            this.d = d
        }
    )

fun createPolygon(points: String, key: String? = null) =
    createElement("polygon",
        jsObject<Polygon> {
            key?.let { this.key = it }
            this.points = points
        }
    )

fun createGraph(id: String? = null, vararg child: Any?) =
    createElement(
        "g",
        jsObject<GraphProps> {
            id?.let { this.id = it }
        },
        child
    )

