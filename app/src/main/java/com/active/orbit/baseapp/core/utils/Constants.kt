package com.active.orbit.baseapp.core.utils

import android.graphics.Bitmap
import com.bumptech.glide.load.engine.DiskCacheStrategy

/**
 * Utility class with constants values
 *
 * @author omar.brugna
 */
object Constants {

    const val EMPTY = ""
    const val INVALID = -1

    const val DATABASE_ENCRYPTION_KEY = "000Th3D4t4b4s31s3ncrYpt3d?000"

    const val DATE_FORMAT_ID = "yyyyMMddHHmmss"
    const val DATE_FORMAT_UTC = "yyyy-MM-dd HH:mm:ss"
    const val DATE_FORMAT_ISO = "yyyy-MM-dd'T'HH:mm:ssZZZZZ"
    const val DATE_FORMAT_ISO_NO = "yyyy-MM-dd'T'HH:mm:ss'Z'"
    const val DATE_FORMAT_ISO_MILLIS = "yyyy-MM-dd'T'HH:mm:ss.000'Z'"
    const val DATE_FORMAT_FULL = "dd/MM/yyyy HH:mm:ss"
    const val DATE_FORMAT_DAY_MONTH_YEAR = "dd/MM/yyyy"
    const val DATE_FORMAT_MONTH_YEAR = "MMMM yyyy"
    const val DATE_FORMAT_DAY_MONTH = "dd\nMMM"
    const val DATE_FORMAT_HOUR_MINUTE = "HH:mm"
    const val DATE_FORMAT_HOUR_MINUTE_SECONDS = "HH:mm:ss"
    const val DATE_FORMAT_YEAR_MONTH_DAY = "yyyy-MM-dd"
    const val DATE_FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND = "yyyy-MM-dd HH:mm:ss"
    const val DATE_FORMAT_DAY_MONTH_HOUR_MINUTE = "dd MMMM HH:mm"

    const val PRIORITY_CURRENT = -1
    const val PRIORITY_OTHER = 1
    const val PRIORITY_ZERO = 0

    const val TYPEFACE_REGULAR = "font/regular.ttf"
    const val TYPEFACE_BOLD = "font/bold.ttf"
    const val TYPEFACE_SEMIBOLD = "font/semibold.ttf"
    const val TYPEFACE_ITALIC = "font/italic.ttf"
    const val TYPEFACE_BLACK = "font/black.ttf"
    const val TYPEFACE_LIGHT = "font/light.ttf"
    const val TYPEFACE_BOLD_ITALIC = "font/bold-italic.ttf"

    const val TYPEFACE_REGULAR_INDEX = 0
    const val TYPEFACE_BOLD_INDEX = 1
    const val TYPEFACE_SEMIBOLD_INDEX = 2
    const val TYPEFACE_ITALIC_INDEX = 3
    const val TYPEFACE_BLACK_INDEX = 4
    const val TYPEFACE_LIGHT_INDEX = 5
    const val TYPEFACE_BOLD_ITALIC_INDEX = 6

    const val ALPHA_ENABLED = 1.0f
    const val ALPHA_DISABLED = 0.5f
    const val ALPHA_PRESSED = 0.5f

    const val ADMIN_PASSWORD = "123456"

    val IMAGE_DISK_STRATEGY_AUTOMATIC: DiskCacheStrategy = DiskCacheStrategy.AUTOMATIC
    val IMAGE_DISK_STRATEGY_NONE: DiskCacheStrategy = DiskCacheStrategy.NONE
    val IMAGE_OUTPUT_FORMAT = Bitmap.CompressFormat.JPEG
}