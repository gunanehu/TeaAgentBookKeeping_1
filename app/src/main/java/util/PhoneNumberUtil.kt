package util

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.telephony.TelephonyManager
import androidx.annotation.RequiresPermission

object PhoneNumberUtil {

    private val NUMBER_REGEX = "[0-9]".toRegex()
    private const val NUMBERS = "0123456789"
    private const val ILLEGAL_CHARS = "+() –x"

    /**
     * Method to fetch phone number
     */
    @JvmStatic
    @SuppressLint("HardwareIds")
    @RequiresPermission(Manifest.permission.READ_PHONE_STATE)
    fun getPhoneNumber(context: Context): String? {

        var phoneNumber: String? = null

        val result = context.getSystemService(Context.TELEPHONY_SERVICE)
        result?.let {
            val telephonyManager: TelephonyManager = result as TelephonyManager
            val line1Number = telephonyManager.line1Number
            phoneNumber = if (validatePhoneNumber(line1Number)) {
                sanitizePhoneNumber(line1Number)
            } else {
                null
            }
        }

        return phoneNumber
    }

    /**
     * Validates phone number.
     * Has basic validation to accommodate all phone number formats globally.
     * To be used with platform API output only & not with user input.
     */
    private fun validatePhoneNumber(phoneNumber: String?): Boolean {
        var valid = false
        if (phoneNumber.isNullOrEmpty()) {
            valid = false
        } else if (phoneNumber.contains(NUMBER_REGEX)) {
            val count = phoneNumber.count { it in NUMBERS }
            valid = count > 2
        }
        return valid
    }

    private fun sanitizePhoneNumber(phoneNumberFromAPI: String): String {
        var phoneNumber = phoneNumberFromAPI
        for (illegalChar in ILLEGAL_CHARS) {
            phoneNumber = phoneNumber.replace("$illegalChar", "")
        }
        return phoneNumber
    }

}