package util

import android.util.Base64.encodeToString
import java.text.DateFormat
import java.util.*

class EncryptionUtil {

    fun encryptData( data:String){
        //Get password
        val password = CharArray(data.length)
//        login_password.text.getChars(0, data.length, password, 0)

//Base64 the data
        val currentDateTimeString = DateFormat.getDateTimeInstance().format(Date())
// 1
//        val map =
//            Encryption().encrypt(currentDateTimeString.toByteArray(Charsets.UTF_8), password)
// 2
//        val valueBase64String = Base64.encodeToString(map["encrypted"], Base64.NO_WRAP)

    }
}