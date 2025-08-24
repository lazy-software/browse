package org.mozilla.reference.browser.components

import mozilla.components.concept.base.crash.Breadcrumb
import mozilla.components.lib.crash.Crash
import mozilla.components.lib.crash.service.CrashReporterService

class NoOpCrashReporterService(override val id: String = "", override val name: String = "") : CrashReporterService {
    override fun createCrashReportUrl(identifier: String): String? = ""

    override fun report(crash: Crash.UncaughtExceptionCrash): String? = ""

    override fun report(crash: Crash.NativeCodeCrash): String? = ""

    override fun report(
        throwable: Throwable,
        breadcrumbs: ArrayList<Breadcrumb>,
    ): String? = ""
}
