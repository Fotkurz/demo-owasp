package br.com.vapostore.security

import jakarta.inject.Singleton
import org.apache.commons.codec.binary.Hex
import java.security.SecureRandom
import javax.crypto.SecretKeyFactory
import javax.crypto.spec.PBEKeySpec

@Singleton
class PasswordEncoder {

    fun saltGen(): ByteArray {
        val random = SecureRandom()
        val salt = ByteArray(16)
        random.nextBytes(salt)
        return salt
    }

    fun encode(password: String, salt: ByteArray): String {
        // hashing the password
        val skf: SecretKeyFactory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA512")
        val spec = PBEKeySpec(password.toCharArray(), salt, 10000, 512)

        return Hex.encodeHexString(skf.generateSecret(spec).encoded)
    }

    fun compare(incomingPassword: String, storedPassword: String, storedSalt: ByteArray): Boolean {
        val encodedPw = encode(incomingPassword, storedSalt)

        if(encodedPw == storedPassword) return true
        return false
    }
}