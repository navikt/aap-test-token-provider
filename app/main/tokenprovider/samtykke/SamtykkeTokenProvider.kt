package tokenprovider.samtykke


import com.nimbusds.jose.JOSEObjectType
import com.nimbusds.jose.JWSAlgorithm
import com.nimbusds.jose.JWSHeader
import com.nimbusds.jose.JWSSigner
import com.nimbusds.jose.crypto.RSASSASigner
import com.nimbusds.jose.jwk.JWKSet
import com.nimbusds.jose.jwk.RSAKey
import com.nimbusds.jwt.JWTClaimsSet
import com.nimbusds.jwt.SignedJWT
import java.time.LocalDate
import java.util.Date

class SamtykkeTokenProvider {
    private val jwkSet: JWKSet get() = JWKSet.parse(this::class.java.getResource("/jwkset.json")!!.readText())
    private val rsaKey: RSAKey get() = jwkSet.getKeyByKeyId("samtykke") as RSAKey

    fun getToken() :String {
        val delegatedDate= LocalDate.now().toString()
        val validToDate = LocalDate.now().plusYears(1).toString()
        return createSignedJWT(rsaKey, JWTClaimsSet.Builder()
            .audience("https://aap-test-token-provider.intern.dev.nav.no")
            .issuer("https://aap-test-token-provider.intern.dev.nav.no")
            .claim("Services", arrayOf("5252_1","5252_1_fraOgMed=01.01.2022","5252_1_tilOgMed=01.01.2024"))
            .claim("OfferedBy", "1") //
            .claim("CoveredBy", "1") //
            .claim("DelegatedDate", delegatedDate) //
            .claim("ValidToDate", validToDate) //
            .claim("scope", System.getenv()["MASKINPORTEN_SCOPES"])
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
