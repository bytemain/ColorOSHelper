package me.lengthmin.dualapp

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.ContentValues
import android.content.Context
import android.net.Uri
import android.os.Build
import android.widget.Toast
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat

private const val MIN_REFRESH_RATE = "min_refresh_rate"
private const val PEAK_REFRESH_RATE = "peak_refresh_rate"


fun setConfig(context: Context, key: String, value: String) {
    val contentResolver = context.contentResolver
    try {
        val contentValues = ContentValues(2)
        contentValues.put("name", key)
        contentValues.put("value", value)
        contentResolver.insert(Uri.parse("content://settings/system"), contentValues)
    } catch (th: Exception) {
        Toast.makeText(context, "Failed to set value $value", Toast.LENGTH_SHORT).show()
        th.printStackTrace()
    }
}

fun getRefreshRateDesc(context: Context): String {
    val contentResolver = context.contentResolver
    try {
        val list: ArrayList<String> = ArrayList()
        var cursor = contentResolver.query(Uri.parse("content://settings/system"), arrayOf("name", "value"), null, null, null)
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                do {
                    if (cursor.getString(0).contains("refresh_rate")) {
                        list.add("${cursor.getString(0)}: ${cursor.getString(1)}")
                    }
                } while (cursor.moveToNext())
            }
            cursor.close()
        }
        return list.joinToString("\n")
    } catch (th: Exception) {
        Toast.makeText(context, "Failed to get refresh rate", Toast.LENGTH_SHORT).show()
        th.printStackTrace()
    }
    return  ""
}


const val CHANNEL_ID = "boot_channel"

fun createNotificationChannel(context: Context) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        val name = "开机通知"
        val descriptionText = "通知描述"
        val importance = NotificationManager.IMPORTANCE_DEFAULT
        val channel = NotificationChannel(CHANNEL_ID, name, importance).apply {
            description = descriptionText
        }
        // Register the channel with the system
        val notificationManager: NotificationManager =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(channel)
    }
}

fun notify(context: Context) {
    val notificationBuilder =
        NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_menu_gallery)
            .setContentTitle("设置成功")
            .setContentText("设置 30Hz 成功")
            .setCategory(NotificationCompat.CATEGORY_STATUS)
            .setAutoCancel(true)
    createNotificationChannel(context)
    with(NotificationManagerCompat.from(context)) {
        notify(1, notificationBuilder.build())
    }
}

fun setRefreshRateByContext(context: Context, rate: String) {
    setConfig(context, MIN_REFRESH_RATE, rate)
    setConfig(context, PEAK_REFRESH_RATE, rate)
}

fun Context.setRefreshRate(rate: String) {
    Toast.makeText(this, "刷新率: 正在设置 ${rate}Hz，请稍等", Toast.LENGTH_SHORT).show()
    setRefreshRateByContext(this, rate)
    Toast.makeText(this, "刷新率: 设置成功。", Toast.LENGTH_SHORT).show()
}
