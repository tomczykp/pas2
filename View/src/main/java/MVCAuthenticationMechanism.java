
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import javax.security.enterprise.AuthenticationStatus;
import javax.security.enterprise.authentication.mechanism.http.AutoApplySession;
import javax.security.enterprise.authentication.mechanism.http.HttpAuthenticationMechanism;
import javax.security.enterprise.authentication.mechanism.http.HttpMessageContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashSet;
import java.util.Set;

@AutoApplySession
@ApplicationScoped
public class MVCAuthenticationMechanism implements HttpAuthenticationMechanism {

    @Inject
    private JwtStorage jwtStorage;

    String secret = "f4h9t87t3g473HGufuJ8fFHU4j39j48fmu948cx48cu2j9fj";

    @Override
    public AuthenticationStatus validateRequest(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, HttpMessageContext httpMessageContext) {
        Set<String> roles = new HashSet<>();
        if (jwtStorage.getJwt().equals("")) {
            roles.add("ANONYMOUS");
            return httpMessageContext.notifyContainerAboutLogin("anonymous", roles);
        }
        Claims claims = Jwts.parser().setSigningKey(secret).parseClaimsJws(jwtStorage.getJwt()).getBody();
        roles.add(claims.get("role", String.class));
        return httpMessageContext.notifyContainerAboutLogin(claims.getSubject(), roles);
    }
}
