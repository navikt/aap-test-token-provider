package tokenprovider.samtykke

import com.fasterxml.jackson.annotation.JsonProperty

class SamtykkeWellKnownProvider {
    fun getWellKnownOauth(): WellKnownOauth {
        return WellKnownOauth(
            issuer = "http://test-token-provider",
            jwksUri = "http://test-token-provider/samtykke/jwk",
            responseTypesSupported = arrayOf("code")
        )
    }
}

data class WellKnownOauth(
    val issuer: String,
    @JsonProperty("jwks_uri")
    val jwksUri: String,
    @JsonProperty("response_types_supported")
    val responseTypesSupported: Array<String>
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as WellKnownOauth

        if (issuer != other.issuer) return false
        if (jwksUri != other.jwksUri) return false
        if (!responseTypesSupported.contentEquals(other.responseTypesSupported)) return false

        return true
    }

    override fun hashCode(): Int {
        var result = issuer.hashCode()
        result = 31 * result + jwksUri.hashCode()
        result = 31 * result + responseTypesSupported.contentHashCode()
        return result
    }
}