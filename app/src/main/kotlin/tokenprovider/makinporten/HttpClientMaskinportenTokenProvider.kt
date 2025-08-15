package tokenprovider.makinporten

import com.nimbusds.jwt.SignedJWT
import io.ktor.client.*

class HttpClientMaskinportenTokenProvider(
    private val config: MaskinportenConfig,
    client: HttpClient = defaultHttpClient
) {
    private val grants = JwtGrantFactory(config.toJwtConfig())
    private val tokenClient = TokenClient(client)

    suspend fun getToken(): String {
        val token = tokenClient.getAccessToken(config.tokenEndpointUrl, config.scope) {
            """
                grant_type=urn:ietf:params:oauth:grant-type:jwt-bearer&
                assertion=${grants.jwt}
            """.asUrlPart()
        }
        return token.let(SignedJWT::parse).parsedString
    }
}


internal fun String.asUrlPart() =
    this.trimIndent().replace("\n", "")
