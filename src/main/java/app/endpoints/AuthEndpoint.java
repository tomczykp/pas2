package app.endpoints;

import app.auth.InMemoryIdentityStore;
import app.dto.LoginDTO;
import app.auth.JwtProvider;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.security.enterprise.identitystore.CredentialValidationResult;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.json.JSONObject;

import javax.annotation.security.RolesAllowed;
import javax.security.enterprise.credential.UsernamePasswordCredential;

@Path("/login")
@RequestScoped
public class AuthEndpoint {

    private JwtProvider jwtProvider = new JwtProvider();
    @Inject
    private InMemoryIdentityStore inMemoryIdentityStore;

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @RolesAllowed({"ANONYMOUS"})
    public Response login(LoginDTO loginDTO) {
        UsernamePasswordCredential usernamePasswordCredential = new UsernamePasswordCredential(loginDTO.username, loginDTO.password);
        CredentialValidationResult credentialValidationResult = inMemoryIdentityStore.validate(usernamePasswordCredential);
        if (credentialValidationResult.getStatus().equals(CredentialValidationResult.Status.VALID)) {
            String jwt = jwtProvider.generateJWT(loginDTO.username, credentialValidationResult.getCallerGroups().iterator().next());
            return Response.ok(jwt).build();
        }
        return Response.status(401).build();
    }
}
