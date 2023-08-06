package org.swm.att.common_ui.util.token

import android.util.Log
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.Base64
import java.util.Date
import java.util.TimeZone

object JWTUtils {
    private val simpleDateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
    private val dateTimeFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")

    fun decodeToken(jwtToken: String): String {
        val parts = jwtToken.split(".")
        return try {
            val charset = charset("UTF-8")
            val payload = String(Base64.getDecoder().decode(parts[1]), charset)
            "$payload"
        } catch (e: Exception) {
            e.message.toString()
        }
    }

    fun unixTimeToDateTime(unixTime: Long): String {
        simpleDateFormat.timeZone = TimeZone.getTimeZone("UTC")
        val date = Date(unixTime * 1000)
        return simpleDateFormat.format(date)
    }

    fun isTokenExpired(expiration: String): Boolean {
        val currentDateTime = LocalDateTime.now(ZoneId.of("UTC"))
        val expirationDateTime = LocalDateTime.parse(expiration, dateTimeFormat)
        return !expirationDateTime.isAfter(currentDateTime)
    }
}