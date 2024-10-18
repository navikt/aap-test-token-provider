package tokenprovider.makinporten

import com.nimbusds.jose.JOSEObjectType
import com.nimbusds.jose.JWSAlgorithm
import com.nimbusds.jose.JWSHeader
import com.nimbusds.jose.crypto.RSASSASigner
import com.nimbusds.jose.jwk.RSAKey
import com.nimbusds.jwt.JWTClaimsSet
import com.nimbusds.jwt.SignedJWT
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.util.logging.*
import org.slf4j.LoggerFactory
import java.util.*

private val logger = LoggerFactory.getLogger("MaskinportenTokenProvider")

class MaskinportenTokenProvider {

    private val httpClient = HttpClient(io.ktor.client.engine.java.Java)
    suspend fun getToken(): String {
        val tokenUrl = System.getenv()["MASKINPORTEN_TOKEN_ENDPOINT"]!!
        val response = try {
            httpClient.post(tokenUrl) {

                header(HttpHeaders.ContentType, ContentType.Application.FormUrlEncoded)
                val rsaKey = RSAKey.parse(System.getenv()["MASKINPORTEN_CLIENT_JWK"])
                val signedJWT = SignedJWT(
                    JWSHeader.Builder(JWSAlgorithm.RS256)
                        .keyID(rsaKey.keyID)
                        .type(JOSEObjectType.JWT)
                        .build(),
                    JWTClaimsSet.Builder()
                        .audience("https://test.maskinporten.no/")
                        .issuer(System.getenv().get("MASKINPORTEN_CLIENT_ID"))
                        .claim("scope", System.getenv().get("MASKINPORTEN_SCOPES"))
                        .issueTime(Date())
                        .expirationTime(twoMinutesFromDate(Date()))
                        .build()
                )

                signedJWT.sign(RSASSASigner(rsaKey.toRSAPrivateKey()))
                val kropp =
                    "grant_type=urn:ietf:params:oauth:grant-type:jwt-bearer" + "&assertion=" + signedJWT.serialize()
                logger.info("kaller maskinporten på $tokenUrl med body: $kropp")
                setBody(kropp)
            }
        } catch (e: Exception) {
            logger.error(e)
            null
        }

        val respons = response?.body<String>()

        logger.info("Svar fra maskinporten: $respons")
        return respons ?: "Bad Token"
    }

    fun twoMinutesFromDate(date: Date): Date {
        val calendar = Calendar.getInstance()
        calendar.time = date;
        calendar.add(Calendar.MINUTE, 2)

        return calendar.time
    }
}