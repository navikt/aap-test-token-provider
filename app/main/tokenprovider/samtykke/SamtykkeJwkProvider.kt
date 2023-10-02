package tokenprovider.samtykke

import com.nimbusds.jose.jwk.JWKSet

class SamtykkeJwkProvider {
    fun getJwk(): Jwk {
        val map = JWKSet.parse(this::class.java.getResource("/jwkset.json")!!.readText()).getKeyByKeyId("samtykke").toJSONObject()

        return Jwk(
            alg = map["alg"].toString(),
            e = map["e"].toString(),
            kid = map["kid"].toString(),
            kty = map["kty"].toString(),
            n = map["n"].toString(),
            use = map["use"].toString()
        )
    }
}

data class Jwk(
    val alg:String,
    val e:String,
    val kid:String,
    val kty:String,
    val n:String,
    val use:String
)
