package maskinportern.client

import com.nimbusds.jose.jwk.JWKSet

class SamtykkeJwk {
    fun getJwk():Jwk {
        //read from file jwkset.json and return Jwk
        val jwkSet: JWKSet = JWKSet.parse(this::class.java.getResource("/jwkset.json")!!.readText())
        return Jwk(alg = jwkSet.toJSONObject().get("alg").toString(),
            e = jwkSet.toJSONObject().get("e").toString(),
            kid = jwkSet.toJSONObject().get("kid").toString(),
            kty = jwkSet.toJSONObject().get("kty").toString(),
            n = jwkSet.toJSONObject().get("n").toString(),
            use = jwkSet.toJSONObject().get("use").toString()
        )

    }
}

data class Jwk(val alg:String, val e:String, val kid:String, val kty:String, val n:String, val use:String)