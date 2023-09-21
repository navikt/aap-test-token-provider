package aap.maskinportern.client

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
import io.ktor.server.application.*
import java.util.*
import io.ktor.util.logging.*

class MaskinportenClient(private val log: Logger, private val environment: ApplicationEnvironment) {

    private val httpClient = HttpClient(io.ktor.client.engine.java.Java)
    suspend fun getToken() :String {
        val tokenUrl = System.getenv()["MASKINPORTEN_TOKEN_ENDPOINT"]!!
        val response = httpClient.post(tokenUrl) {

            header(HttpHeaders.ContentType, ContentType.Application.FormUrlEncoded)
            val rsaKey = RSAKey.parse(System.getenv()["MASKINPORTEN_CLIENT_JWK"])
            val signedJWT = SignedJWT(
                JWSHeader.Builder(JWSAlgorithm.RS256)
                    .keyID(rsaKey.keyID)
                    .type(JOSEObjectType.JWT)
                    .build(),
                JWTClaimsSet.Builder()
                    .audience(environment.config.property("maskinporten.audience").getString())
                    .issuer(System.getenv().get("MASKINPORTEN_CLIENT_ID"))
                    .claim("scope", System.getenv().get("MASKINPORTEN_SCOPES"))
                    .issueTime(Date())
                    .expirationTime(twoMinutesFromDate(Date()))
                    .build()
            )

            signedJWT.sign(RSASSASigner(rsaKey.toRSAPrivateKey()))
            val kropp = "grant_type=urn:ietf:params:oauth:grant-type:jwt-bearer" + "&assertion=" + signedJWT.serialize()
            log.info("kaller maskinporten på $tokenUrl med body: $kropp")
            setBody(kropp)
        }

        val respons = response.body<String>()

        log.info("Svar fra maskinporten: $respons")
        return respons
    }

    fun twoMinutesFromDate(date: Date) :Date {
        val calendar = Calendar.getInstance()
        calendar.time = date;
        calendar.add(Calendar.MINUTE, 2)

        return calendar.time
    }
}