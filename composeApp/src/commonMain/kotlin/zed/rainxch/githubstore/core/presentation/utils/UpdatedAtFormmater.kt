package zed.rainxch.githubstore.core.presentation.utils

import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import kotlin.time.Clock
import kotlin.time.Duration
import kotlin.time.ExperimentalTime
import kotlin.time.Instant

@OptIn(ExperimentalTime::class)
fun formatUpdatedAt(isoInstant: String): String {
    val updated = Instant.parse(isoInstant)
    val now = Instant.fromEpochMilliseconds(Clock.System.now().toEpochMilliseconds())
    val diff: Duration = now - updated

    val hoursDiff = diff.inWholeHours
    val daysDiff = diff.inWholeDays

    return when {
        hoursDiff < 1 -> "updated just now"
        hoursDiff < 24 -> "updated ${hoursDiff} hour${if (hoursDiff == 1L) "" else "s"} ago"
        daysDiff == 1L -> "updated yesterday"
        daysDiff < 7 -> "updated ${daysDiff} day${if (daysDiff == 1L) "" else "s"} ago"
        else -> {
            val date = updated.toLocalDateTime(TimeZone.currentSystemDefault()).date
            "updated on $date"
        }
    }
}
