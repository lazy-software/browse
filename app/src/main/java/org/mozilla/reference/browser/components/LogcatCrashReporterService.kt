package org.mozilla.reference.browser.components

import android.util.Log
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import mozilla.components.concept.base.crash.Breadcrumb
import mozilla.components.lib.crash.Crash
import mozilla.components.lib.crash.service.CrashReporterService
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.TimeZone

class LogcatCrashReporterService(
    override val id: String = this::class.java.simpleName,
    override val name: String = this::class.java.simpleName,
) : CrashReporterService {

    companion object {
        private val TAG = this::class.java.simpleName
    }

    override fun createCrashReportUrl(identifier: String) = null

    override fun report(crash: Crash.UncaughtExceptionCrash): String? {
        log(crash.breadcrumbs, crash.runtimeTags, crash.throwable)
        return null
    }

    override fun report(crash: Crash.NativeCodeCrash): String? {
        log(crash.breadcrumbs, crash.runtimeTags, null)
        return null
    }

    override fun report(throwable: Throwable, breadcrumbs: ArrayList<Breadcrumb>): String? {
        log(breadcrumbs, null, throwable)
        return null
    }

    private fun log(breadcrumbs: ArrayList<Breadcrumb>, runtimeTags: Map<String, String>?, t: Throwable?) {
        val msg = LogMsg(breadcrumbs.map { BreadcrumbWrapper.from(it) }.toList(), runtimeTags)
        Log.e(TAG, Json.encodeToString(msg), t)
    }

    @Serializable
    private data class LogMsg(
        val breadcrumbs: List<BreadcrumbWrapper>,
        val runtimeTags: Map<String, String>? = emptyMap(),
    )

    @Serializable
    data class BreadcrumbWrapper(
        val message: String = "",
        val data: Map<String, String> = emptyMap(),
        val category: String = "",
        val level: String = "",
        val type: String = "",
        val date: String = "",
    ) {
        companion object {
            fun from(breadcrumb: Breadcrumb): BreadcrumbWrapper =
                BreadcrumbWrapper(
                    message = breadcrumb.message,
                    data = breadcrumb.data,
                    category = breadcrumb.category,
                    level = breadcrumb.level.value,
                    type = breadcrumb.type.value,
                    date = toIsoString(breadcrumb.date),
                )

            private fun toIsoString(date: Date): String {
                val format = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.US)
                format.timeZone = TimeZone.getTimeZone("UTC")
                return format.format(date)
            }
        }

    }

}
