package util


import java.security.KeyPair
import java.security.KeyPairGenerator
import java.security.NoSuchAlgorithmException
import java.util.*

class GenerateRsaKeyPair {

    fun generateRsaKeyPair() {
        try {

//            // 1. generate public key and private key
//            val keyPairGenerator: KeyPairGenerator = KeyPairGenerator.getInstance("RSA")
//            keyPairGenerator.initialize(1024) // key length
//            val keyPair: KeyPair = keyPairGenerator.genKeyPair()
//            val privateKeyString: String =
//                android.util.Base64.encodeToString(
//                    keyPair.getPrivate().getEncoded(),
//                    Base64.DEFAULT
//                )
//            val publicKeyString: String =
//                Base64.encodeToString(keyPair.getPublic().getEncoded(), Base64.DEFAULT)
//
//            // 2. print both keys
//            println("rsa key pair generated\n")
//            println("privateKey\n$privateKeyString\n")
//            println("publicKey\n$publicKeyString\n\n")
        } catch (e: NoSuchAlgorithmException) {
            e.printStackTrace()
        }

    }
}