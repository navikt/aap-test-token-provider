package maskinportern.client

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

    val maskinportenClient = MaskinportenClient(logger)
    val samtykkeClient = SamtykkeClient()
    val jwkClient = SamtykkeJwk()

    routing {
        route("/maskinporten") {
            get("/token") {
                call.respond(maskinportenClient.getToken())
            }
        }
        route("/samtykke") {
            get("/token") {
                call.respond(samtykkeClient.getToken())
            }
            get("/jwk") {
                call.respond(jwkClient.getJwk())
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