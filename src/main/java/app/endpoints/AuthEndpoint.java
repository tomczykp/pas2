package app.endpoints;

import app.auth.InMemoryValidation;
import app.dto.LoginDTO;
import app.auth.JwtProvider;
import app.exceptions.InactiveClientException;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.security.enterprise.identitystore.CredentialValidationResult;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.SecurityContext;
import org.json.JSONObject;

import javax.annotation.security.RolesAllowed;
import javax.security.enterprise.credential.UsernamePasswordCredential;
import javax.validation.constraints.NotNull;

@Path("/login")
@RequestScoped
public class AuthEndpoint {

    private JwtProvider jwtProvider = new JwtProvider();
    @Inject
    private InMemoryValidation inMemoryValidation;

    @Context
    private SecurityContext securityContext;

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @RolesAllowed({"ANONYMOUS"})
    public Response login(@NotNull LoginDTO loginDTO) {
        UsernamePasswordCredential usernamePasswordCredential = new UsernamePasswordCredential(loginDTO.username, loginDTO.password);
        try {
            CredentialValidationResult credentialValidationResult = inMemoryValidation.validate(usernamePasswordCredential);
            if (credentialValidationResult.getStatus().equals(CredentialValidationResult.Status.VALID)) {
                String role = credentialValidationResult.getCallerGroups().iterator().next();
                String jwt = jwtProvider.generateJWT(loginDTO.username, role);
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("jwt", jwt);
                jsonObject.put("role", role);
                return Response.ok(jsonObject.toString()).build();
            }
        } catch (InactiveClientException e) {
            return Response.status(400).build();
        }
        return Response.status(401).build();
    }
}
