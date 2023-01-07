package app.auth;

import app.exceptions.InactiveClientException;
import app.model.Customer;
import app.model.CustomerType;
import app.model.User;
import app.repositories.UserRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.security.enterprise.identitystore.CredentialValidationResult;
import jakarta.security.enterprise.identitystore.IdentityStore;
import javax.security.enterprise.credential.UsernamePasswordCredential;
import java.util.*;

@ApplicationScoped
public class InMemoryValidation {

    @Inject
    private UserRepository userRepository;

    public CredentialValidationResult validate(UsernamePasswordCredential credential) throws InactiveClientException {

        UsernamePasswordCredential user = credential;
        List<User> users = new ArrayList<>(userRepository.getMap().values());
        if (users.size() > 0) {
            for (User u : users) {
                if (user.getCaller().equals(u.getUsername()) && user.getPasswordAsString().equals(u.getPassword())) {
                    if (u.getType().equals(CustomerType.CUSTOMER)) {
                        if (!((Customer) u).isActive()) {
                            throw new InactiveClientException();
                        }
                    }
                    return new CredentialValidationResult(u.getUsername(), new HashSet<>(Collections.singleton(u.getType().toString())));
                }
            }
        }
        return CredentialValidationResult.INVALID_RESULT;
    }
}
