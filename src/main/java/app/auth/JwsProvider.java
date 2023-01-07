package app.auth;

import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jose.crypto.MACVerifier;
import java.security.SecureRandom;
import java.text.ParseException;

public class JwsProvider {

    SecureRandom random = new SecureRandom();

    private String secret = "f4h9t87t3g473HGufuJ8fFHU4j39j48fmu948cx48cu2j9fj";

    public String generateJws(String payload) throws JOSEException {
        JWSSigner signer = new MACSigner(secret);
        JWSObject jwsObject = new JWSObject(new JWSHeader(JWSAlgorithm.HS256), new Payload(payload));
        jwsObject.sign(signer);
        return jwsObject.serialize();
    }

    public boolean verify(String payload) throws ParseException, JOSEException {
        JWSObject jwsObject = JWSObject.parse(payload);
        JWSVerifier verifier = new MACVerifier(secret);
        return jwsObject.verify(verifier);
    }
}
