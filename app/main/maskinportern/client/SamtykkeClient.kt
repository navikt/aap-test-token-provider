package maskinportern.client


import com.nimbusds.jose.JOSEObjectType
import com.nimbusds.jose.JWSAlgorithm
import com.nimbusds.jose.JWSHeader
import com.nimbusds.jose.JWSSigner
import com.nimbusds.jose.crypto.RSASSASigner
import com.nimbusds.jose.jwk.JWKSet
import com.nimbusds.jose.jwk.RSAKey
import com.nimbusds.jwt.JWTClaimsSet
import com.nimbusds.jwt.SignedJWT
import io.ktor.util.logging.*
import org.slf4j.LoggerFactory

class SamtykkeClient() {
    private val jwkSet: JWKSet get() = JWKSet.parse(this::class.java.getResource("/jwkset.json")!!.readText())
    private val rsaKey: RSAKey get() = jwkSet.getKeyByKeyId("samtykke") as RSAKey

    fun getToken() :String {
        return createSignedJWT(rsaKey, JWTClaimsSet.Builder()
            .audience("https://samtykke")
            .issuer("samtykke")
            .claim("scope", "samtykke")
            .build()
        ).serialize()
    }

    private fun createSignedJWT(rsaJwk: RSAKey, claimsSet: JWTClaimsSet): SignedJWT {
        val header = JWSHeader.Builder(JWSAlgorithm.RS256).keyID(rsaJwk.keyID).type(JOSEObjectType.JWT).build()
        val signer: JWSSigner = RSASSASigner(rsaJwk.toPrivateKey())
        return SignedJWT(header, claimsSet).apply {
            sign(signer)
        }
    }

}
