package tokenprovider

import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.plugins.logging.*
import io.ktor.serialization.jackson.*
import org.slf4j.LoggerFactory


private val log = LoggerFactory.getLogger("HttpClient")

val loggingHttpClient = HttpClient(CIO) {
    install(ContentNegotiation) {
        jackson {
            registerModule(JavaTimeModule())
            disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES)
        }
    }

    install(Logging) {
        logger = object : Logger {
            override fun log(message: String) {
                log.info(message)
            }

        }
        level = LogLevel.ALL
    }
}
