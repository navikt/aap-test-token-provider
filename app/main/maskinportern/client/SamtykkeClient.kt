package maskinportern.client


import com.auth0.jwt.algorithms.Algorithm
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
import java.time.LocalDate

class SamtykkeClient() {
    private val jwkSet: JWKSet get() = JWKSet.parse(this::class.java.getResource("/jwkset.json")!!.readText())
    private val rsaKey: RSAKey get() = jwkSet.getKeyByKeyId("samtykke") as RSAKey

    fun getToken() :String {
        return createSignedJWT(rsaKey, JWTClaimsSet.Builder()
            .audience("https://samtykke")
            .issuer("samtykke")
            .claim("Services", arrayOf("5252_1","5252_1_fraOgMed=01.01.2022","5252_1_tilOgMed=01.01.2024"))
            .claim("OfferedBy", "1") //
            .claim("CoveredBy", "1") //
            .claim("DelegatedDate", LocalDate.now()) //
            .claim("ValidToDate", LocalDate.now().plusYears(1)) //
            .claim("scope",System.getenv().get("MASKINPORTEN_SCOPES"))
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
