package app.endpoints;

import app.auth.InMemoryIdentityStore;
import app.dto.LoginDTO;
import app.auth.JwtProvider;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.security.enterprise.identitystore.CredentialValidationResult;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.SecurityContext;

import javax.annotation.security.RolesAllowed;
import javax.security.enterprise.credential.UsernamePasswordCredential;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.validation.constraints.NotNull;

@Path("/login")
@RequestScoped
public class AuthEndpoint {

    private JwtProvider jwtProvider = new JwtProvider();
    @Inject
    private InMemoryIdentityStore inMemoryIdentityStore;

    @Context
    private SecurityContext securityContext;

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @RolesAllowed({"ANONYMOUS"})
    public Response login(@NotNull LoginDTO loginDTO) {
        UsernamePasswordCredential usernamePasswordCredential = new UsernamePasswordCredential(loginDTO.username, loginDTO.password);
        CredentialValidationResult credentialValidationResult = inMemoryIdentityStore.validate(usernamePasswordCredential);
        if (credentialValidationResult.getStatus().equals(CredentialValidationResult.Status.VALID)) {
            String jwt = jwtProvider.generateJWT(loginDTO.username, credentialValidationResult.getCallerGroups().iterator().next());
            return Response.ok(jwt).build();
        }
        return Response.status(401).build();
    }
}
