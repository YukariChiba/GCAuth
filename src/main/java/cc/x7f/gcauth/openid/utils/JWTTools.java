package cc.x7f.gcauth.openid.utils;

import com.auth0.jwt.JWT;
import com.auth0.jwt.interfaces.DecodedJWT;

public class JWTTools {
    public static String decode(String token) {
        DecodedJWT dJwt = JWT.decode(token);
        return  dJwt.getClaim("email").asString();
    }
}
