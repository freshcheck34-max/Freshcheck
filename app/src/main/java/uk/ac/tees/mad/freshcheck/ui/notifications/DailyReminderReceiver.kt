package uk.ac.tees.mad.freshcheck.ui.notifications

import android.Manifest
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.annotation.RequiresPermission
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import uk.ac.tees.mad.freshcheck.R

class DailyReminderReceiver : BroadcastReceiver() {

    @RequiresPermission(Manifest.permission.POST_NOTIFICATIONS)
    override fun onReceive(context: Context, intent: Intent) {
        val builder = NotificationCompat.Builder(context, "daily_reminder")
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setContentTitle("FreshCheck Reminder")
            .setContentText("Check items that may be expiring soon!")
            .setPriority(NotificationCompat.PRIORITY_HIGH)

        NotificationManagerCompat.from(context).notify(99, builder.build())
    }
}
