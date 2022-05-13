package com.task.wikicats.model

import com.google.gson.annotations.SerializedName

data class WikiResponseModel(val query: WikiQuery) {
    fun toCatList() = query.pages.map { pageMap -> pageMap.value }.map { page -> page.toCat() }
}

data class WikiQuery(val pages: Map<Long, WikiPage>)

data class WikiPage(
    @SerializedName("pageid")
    val pageId: Long,
    val title: String,
    val thumbnail: WikiImage,
    val original: WikiImage,
    val extract: String
) {
    fun toCat() = Cat(
        id = pageId,
        name = title,
        description = cropDescription(extract),
        thumbnail = thumbnail.source,
        image = original.source
    )

    private fun cropDescription(extract: String): String {
        val stringBuilder = StringBuilder(extract)
        ExtraContent.values().forEach { extra ->
            val index = stringBuilder.indexOf(extra.startTag)
            if (index != -1)
                stringBuilder.delete(index, stringBuilder.length)
        }
        return stringBuilder.toString()
    }

    enum class ExtraContent(val startTag: String) {
        SEE_ALSO("<h2><span id=\"See_also\">See also</span></h2>"),
        REFERENCES("<h2><span id=\"References\">References</span></h2>"),
        EXTERNAL_LINKS("<h2><span id=\"External_links\">External links</span></h2>")
    }
}

data class WikiImage(val source: String)