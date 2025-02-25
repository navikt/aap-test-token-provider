package tokenprovider

import io.ktor.client.request.*
import io.ktor.server.testing.*
import org.assertj.core.api.Assertions.assertThat
import org.intellij.lang.annotations.Language
import org.junit.jupiter.api.Test

@Language("JSON")
internal const val AZURE_JWK: String = """{
  "kty": "RSA",
  "d": "O4HE82G7UP-KVIryTboX-VqbxBbSo16_shQ-zIGUiHo0DVoTBJYfmRWSIx4bPT-n80imaYhohHd79UO1lqWMF-GrZdFJaYjU7yzKGc_W7Pw5QVVng9JZRlgIuz_L7Zl-q3R1gV0-FZWhRZtkhIbETl8216cBFjSrUVF04Fpv4n9dBV3ySgjfG_0MMuysAWx6gZFyP2g1IOnuCY7v32kLR9wdLWPSKFz-icm66AR5DX0hyMdUuwQ56DEBAzf6-1MqznqKiwg-whL6zcHHLdaWzj02J8bMLpeZ9PylbxdTHEWdMP6HaXNdqVMx920UnWmCVVcFOIxs53PdGnyJThVPEQ",
  "e": "AQAB",
  "use": "sig",
  "kid": "localhost-signer",
  "alg": "RS256",
  "n": "wQkxSymiZJ8k4zBTo8HhjmvMB-OZl6F1qg_ZsPXwfa8jTzxbxkicAAPKowh7T0vT_dQAR_Vhy9G6v2jkUUnlbvxULqOt395TTUEB-MBPb0gxIk9O65Ws9eRj12hWo6gDaHBuxWEEjzvVHEDAmqHs7mswoY7nkn2ktxYDPdCjKystyCyR6TCMxkOMXLt0gUfdZyGir60d4ZsGeSIV66L2_pGI0qsEELGvXCLKQe7-UceyYioxmjRs_GGl8Zd1psSiXiZYXHUYIIslZakPUPNUM5_2eFwTbwQPybhJ0WLqUxWEGfoZjyMflR0FTTo5ZLOKLZAsXCpZlR7nY_tuMNWWhw"
}
  """

class AppTest {

    @Test
    fun `funker å be om token`() {
        val azureFake = AzureFake()

        azureFake.start()


        testApplication {
            application {
                server(
                    Config.InternalMaskinportConfig(
                        scope = "nav:aap:afpprivat.read",
                        tokenEndpointUrl = "xxx",
                        clientId = "123",
                        clientJwk = AZURE_JWK,
                        issuer = "issuer"
                    ),
                    Config.InternalMaskinportConfig(
                        scope = "nav:aap:afpoffentlig.read",
                        tokenEndpointUrl = "http://localhost:${azureFake.port()}/jwks",
                        clientId = "123",
                        clientJwk = AZURE_JWK,
                        issuer = "issuer"
                    ),
                    Config.InternalMaskinportConfig(
                        scope = "nav:aap:tpordningen.read",
                        tokenEndpointUrl = "http://localhost:${azureFake.port()}/jwks",
                        clientId = "123",
                        clientJwk = AZURE_JWK,
                        issuer = "issuer"
                    )
                )
            }

            val response =
                client.get("/maskinporten/token/afpprivat/maskinporten/token/afpoffentlig")

            assertThat(response.status.value).isEqualTo(response.status.value)
        }

        azureFake.close()
    }

    @Test
    fun `funker å be om toke for afpoffentlig`() {
        val azureFake = AzureFake()

        azureFake.start()


        testApplication {
            application {
                server(
                    Config.InternalMaskinportConfig(
                        scope = "nav:aap:afpprivat.read",
                        tokenEndpointUrl = "xxx",
                        clientId = "123",
                        clientJwk = AZURE_JWK,
                        issuer = "issuer"
                    ),
                    Config.InternalMaskinportConfig(
                        scope = "nav:aap:afpoffentlig.read",
                        tokenEndpointUrl = "http://localhost:${azureFake.port()}/jwks",
                        clientId = "123",
                        clientJwk = AZURE_JWK,
                        issuer = "issuer"
                    ),
                    Config.InternalMaskinportConfig(
                        scope = "nav:aap:tpordningen.read",
                        tokenEndpointUrl = "http://localhost:${azureFake.port()}/jwks",
                        clientId = "123",
                        clientJwk = AZURE_JWK,
                        issuer = "issuer"
                    )
                )
            }

            val response = client.get("/maskinporten/token/tpordningen")

            assertThat(response.status.value).isEqualTo(response.status.value)
        }

        azureFake.close()
    }
}
