package maskinportern.client

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import io.ktor.server.plugins.statuspages.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.slf4j.LoggerFactory

private val logger = LoggerFactory.getLogger("main")

fun main() {
    embeddedServer(Netty, port = 8080, module = Application::server).start(wait = true)
}

fun Application.server() {
    install(StatusPages) {
        exception<Throwable> { call, cause ->
            logger.error("Feil", cause)
            call.respondText(text = "500: $cause", status = HttpStatusCode.InternalServerError)
        }
    }

    val maskinportenClient = MaskinportenClient(logger)
    val samtykkeClient = SamtykkeClient(logger)

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
                call.respond("public key i jwks format")
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