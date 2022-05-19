package cc.x7f.gcauth.openid.utils;

import com.auth0.jwt.JWT;
import com.auth0.jwt.interfaces.DecodedJWT;

public class JWTTools {
    public static String getSubject(String token) {
        DecodedJWT dJwt = JWT.decode(token);
        return dJwt.getSubject();
    }

    public static String getClaim(String token, String claim) {
        DecodedJWT dJwt = JWT.decode(token);
        return dJwt.getClaim(claim).asString();
    }

    public static String[] getClaims(String token, String claim) {
        DecodedJWT dJwt = JWT.decode(token);
        return dJwt.getClaim(claim).asArray(String.class);
    }
}
