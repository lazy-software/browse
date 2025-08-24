/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. */

package org.mozilla.reference.browser.components

import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import mozilla.components.lib.crash.CrashReporter
import mozilla.components.lib.crash.sentry.SentryService
import mozilla.components.lib.crash.service.CrashReporterService
import mozilla.components.lib.crash.service.MozillaSocorroService
import org.mozilla.geckoview.BuildConfig.MOZ_APP_BUILDID
import org.mozilla.geckoview.BuildConfig.MOZ_APP_VENDOR
import org.mozilla.geckoview.BuildConfig.MOZ_APP_VERSION
import org.mozilla.geckoview.BuildConfig.MOZ_UPDATE_CHANNEL
import org.mozilla.reference.browser.BrowserApplication
import org.mozilla.reference.browser.BuildConfig
import org.mozilla.reference.browser.R
import org.mozilla.reference.browser.ext.components

/**
 * Component group for all functionality related to analytics e.g. crash
 * reporting and telemetry.
 */
class Analytics(
    private val context: Context,
) {
    val crashReporter: CrashReporter by lazy {
        val services: MutableList<CrashReporterService> = mutableListOf(NoOpCrashReporterService())

        val flags = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            PendingIntent.FLAG_IMMUTABLE
        } else {
            0
        }

        CrashReporter(
            context = context,
            services = services,
            telemetryServices = emptyList(),
            shouldPrompt = CrashReporter.Prompt.ALWAYS,
            promptConfiguration = CrashReporter.PromptConfiguration(
                appName = context.getString(R.string.app_name),
                organizationName = "Lazy Software",
            ),
            nonFatalCrashIntent = PendingIntent
                .getBroadcast(context, 0, Intent(BrowserApplication.NON_FATAL_CRASH_BROADCAST), flags),
            enabled = true,
        )
    }
}
