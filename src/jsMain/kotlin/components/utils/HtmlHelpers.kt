package components.utils

import kotlinx.browser.document
import kotlinx.html.HtmlBlockInlineTag
import kotlinx.html.dom.create
import kotlinx.html.js.a
import kotlinx.html.tabIndex
import org.khronos.webgl.Uint8Array
import org.w3c.dom.url.URL
import org.w3c.files.Blob
import org.w3c.files.BlobPropertyBag


fun HtmlBlockInlineTag.disableTab() {
    tabIndex = "-1"
}

fun saveAs(uint8Array: Uint8Array, name: String) {
    val blob = Blob(arrayOf(uint8Array), BlobPropertyBag(type = "octet/stream"))
    val url = URL.createObjectURL(blob)
    val link = document.create.a()
    link.style.display = "none"
    link.href = url
    link.download = name
    link.click()
    URL.revokeObjectURL(url)
}
