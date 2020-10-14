package components.utils

import kotlinx.browser.document
import kotlinx.html.HtmlBlockInlineTag
import kotlinx.html.dom.create
import kotlinx.html.js.a
import kotlinx.html.tabIndex
import org.w3c.dom.url.URL
import org.w3c.files.Blob
import org.w3c.files.BlobPropertyBag


fun HtmlBlockInlineTag.disableTab() {
    tabIndex = "-1"
}

/**
 * Handy helper for downloading files from the browser.
 */
fun downloadFile(filename: String, mimetype: String, content: dynamic) {
    val blob = Blob(arrayOf(content), BlobPropertyBag(type = mimetype))
    val url = URL.createObjectURL(blob)
    val link = document.create.a()
    link.style.display = "none"
    link.href = url
    link.download = filename
    link.click()
    URL.revokeObjectURL(url)
}
