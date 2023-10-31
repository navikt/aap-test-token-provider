package tokenprovider

import com.fasterxml.jackson.databind.SerializationFeature
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.nimbusds.jose.jwk.RSAKey
import io.ktor.http.*
import io.ktor.serialization.jackson.*
import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import io.ktor.server.plugins.contentnegotiation.*
import io.ktor.server.plugins.statuspages.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import no.nav.aap.ktor.client.maskinporten.client.HttpClientMaskinportenTokenProvider
import no.nav.aap.ktor.client.maskinporten.client.MaskinportenConfig
import no.nav.aap.ktor.config.loadConfig
import org.slf4j.LoggerFactory
import tokenprovider.makinporten.MaskinportenTokenProvider
import tokenprovider.samtykke.SamtykkeTokenProvider
import tokenprovider.samtykke.SamtykkeJwkProvider
import tokenprovider.samtykke.SamtykkeWellKnownProvider

private val logger = LoggerFactory.getLogger("main")

fun main() {
    embeddedServer(Netty, port = 8080, module = Application::server).start(wait = true)
}

fun Application.server() {
    val config = loadConfig<Config>()

    install(ContentNegotiation) {
        jackson {
            registerModule(JavaTimeModule())
            disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
        }
    }

    install(StatusPages) {
        exception<Throwable> { call, cause ->
            logger.error("Feil", cause)
            call.respondText(text = "500: $cause", status = HttpStatusCode.InternalServerError)
        }
    }

    val maskinportenTokenProvider = MaskinportenTokenProvider()
    val samtykkeTokenProvider = SamtykkeTokenProvider()
    val jwkClient = SamtykkeJwkProvider()
    val wellKnownProvider = SamtykkeWellKnownProvider()
    val maskinporten = HttpClientMaskinportenTokenProvider(config.maskinporten.toMaskinportenConfig())

    routing {
        route("/maskinporten") {
            get("/token") {
                call.respond(maskinporten.getToken())
            }
        }
        route("/maskinporten-mock") {
            get("/token") {
                call.respond(maskinportenTokenProvider.getToken())
            }
        }
        route("/samtykke") {
            get("/token") {
                call.respond(samtykkeTokenProvider.getToken())
            }
            get("/jwk") {
                call.respond(jwkClient.getJwk())
            }
            get("/.well-known/oauth-authorization-server") {
                call.respond(wellKnownProvider.getWellKnownOauth())
            }
        }
        // Internal API
        route("/internal/liveness") {
            get {
                call.respond("Alive")
            }
        }
        route("/internal/readyness") {
            get {
                call.respond("Ready")
            }
        }
    }
}

internal data class Config(
    val maskinporten: InternalMaskinportConfig,
) {
    internal data class InternalMaskinportConfig(
        val tokenEndpointUrl: String,
        val clientId: String,
        val clientJwk: String,
        val scope: String,
        val audience: String,
        val issuer: String,
    ) {
        fun toMaskinportenConfig() = MaskinportenConfig(
            tokenEndpointUrl = tokenEndpointUrl,
            clientId = clientId,
            privateKey = RSAKey.parse(clientJwk),
            scope = scope,
            resource = audience,
            issuer = issuer
        )
    }
}
