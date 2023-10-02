package tokenprovider

import com.fasterxml.jackson.databind.SerializationFeature
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import io.ktor.http.*
import io.ktor.serialization.jackson.*
import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import io.ktor.server.plugins.contentnegotiation.*
import io.ktor.server.plugins.statuspages.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
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

    routing {
        route("/maskinporten") {
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