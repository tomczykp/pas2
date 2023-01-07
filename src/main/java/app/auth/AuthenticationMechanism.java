package app.auth;

import io.jsonwebtoken.Claims;
import jakarta.enterprise.context.ApplicationScoped;
import javax.security.enterprise.AuthenticationStatus;
import javax.security.enterprise.authentication.mechanism.http.HttpAuthenticationMechanism;
import javax.security.enterprise.authentication.mechanism.http.HttpMessageContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashSet;
import java.util.Set;

@ApplicationScoped
public class AuthenticationMechanism implements HttpAuthenticationMechanism {

    private JwtProvider provider = new JwtProvider();

    @Override
    public AuthenticationStatus validateRequest(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, HttpMessageContext httpMessageContext) {
        String authHeader = httpServletRequest.getHeader("Authorization");
        Set<String> roles = new HashSet<>();
        if (authHeader != null) {
            if (authHeader.startsWith("Bearer ")) {
                String token = authHeader.replace("Bearer ", "");
                Claims claims = provider.parseJWT(token).getBody();
                roles.add(claims.get("role", String.class));
                return httpMessageContext.notifyContainerAboutLogin(claims.getSubject(), roles);
            }
        }
        roles.add("ANONYMOUS");
        return httpMessageContext.notifyContainerAboutLogin("anonymous", roles);
    }
}
