package maskinportern.client

import com.nimbusds.jose.jwk.JWKSet

class SamtykkeJwk {
    fun getJwk():Jwk {
        //read from file jwkset.json and return Jwk
        val map = JWKSet.parse(this::class.java.getResource("/jwkset.json")!!.readText()).getKeyByKeyId("samtykke").toJSONObject()

        return Jwk(
            alg = map.get("alg").toString(),
            e = map.get("e").toString(),
            kid = map.get("kid").toString(),
            kty = map.get("kty").toString(),
            n = map.get("n").toString(),
            use = map.get("use").toString()
        )
    }
}

data class Jwk(val alg:String, val e:String, val kid:String, val kty:String, val n:String, val use:String)
