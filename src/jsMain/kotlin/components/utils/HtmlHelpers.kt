package components.utils

import kotlinx.html.HtmlBlockInlineTag
import kotlinx.html.tabIndex


fun HtmlBlockInlineTag.disableTab() {
    tabIndex = "-1"
}