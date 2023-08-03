package com.active.orbit.baseapp.core.utils

import android.text.TextUtils
import android.util.Patterns
import java.util.regex.Matcher
import java.util.regex.Pattern

/**
 * Utility class that provides some useful methods for data validation
 *
 * @author omar.brugna
 */
@Suppress("unused", "MemberVisibilityCanBePrivate")
object Validator {

    /**
     * Validate mail address
     *
     * @param mailAddress mail address to validate
     * @return true if mail address is valid
     */
    fun validateMail(mailAddress: String?): Boolean {
        return !TextUtils.isEmpty(mailAddress) && Patterns.EMAIL_ADDRESS.matcher(mailAddress!!).matches()
    }

    /**
     * Validate website url
     *
     * @param url the url to validate
     * @return true if mail address is valid
     */
    fun validateUrl(url: String?): Boolean {
        return !TextUtils.isEmpty(url) && Patterns.WEB_URL.matcher(url!!).matches()
    }


    fun validateNhsNumber(number: String): Boolean {

        var total = 0
        var multiplyWith = 10


        if (number.length == 10) {
            for ((index, value) in number.withIndex()) {
                if (index == number.length - 1) {
                    break
                } else {
                    val a = value.digitToInt() * multiplyWith
                    multiplyWith -= 1
                    total += a
                }
            }

            val remainder = total.rem(11)
            val digit = 11 - remainder

            val summary = when (digit) {
                10 -> {
                    return false
                }
                11 -> {
                    0
                }
                else -> {
                    digit
                }
            }
            return summary == number.last().digitToInt()
        }
        return false

    }


    fun validatePostcode(postcode: String): Boolean {
        if (!TextUtils.isEmpty(postcode)) {
            val regex = "^[A-Z]{1,2}[0-9R][0-9A-Z]? [0-9][ABD-HJLNP-UW-Z]{2}$"

            val pattern: Pattern = Pattern.compile(regex)

            val matcher: Matcher = pattern.matcher(postcode)

            return matcher.matches()
        }
        return false
    }



    fun validatePhone(phone: String): Boolean {
        if (!TextUtils.isEmpty(phone)) {
            val regex = "^((\\+44)|(0)) ?\\d{4} ?\\d{6}\$"

            val pattern: Pattern = Pattern.compile(regex)

            val matcher: Matcher = pattern.matcher(phone)

            return matcher.matches()
        }
        return false
    }
}