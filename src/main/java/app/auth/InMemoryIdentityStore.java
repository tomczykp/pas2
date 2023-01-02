package app.auth;

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
public class InMemoryIdentityStore implements IdentityStore {

    @Inject
    private UserRepository userRepository;

    @Override
    public int priority() {
        return 70;
    }

    @Override
    public Set<ValidationType> validationTypes() {
        return EnumSet.of(ValidationType.VALIDATE);
    }

    @Override
    public Set<String> getCallerGroups(CredentialValidationResult validationResult) {
        User user = new ArrayList<>(userRepository.get((u) -> u.getUsername().equals(validationResult.getCallerPrincipal().getName())).values()).get(0);
        return new HashSet<>(Collections.singleton(user.getType().toString()));
    }

    public CredentialValidationResult validate(UsernamePasswordCredential credential) {

        UsernamePasswordCredential user = credential;
        List<User> users = new ArrayList<>(userRepository.getMap().values());
        if (users.size() > 0) {
            for (User u : users) {
                if (user.getCaller().equals(u.getUsername()) && user.getPasswordAsString().equals(u.getPassword())) {
                    if (u.getType().equals(CustomerType.CUSTOMER)) {
                        if (!((Customer) u).isActive()) {
                            return CredentialValidationResult.INVALID_RESULT;
                        }
                    }
                    return new CredentialValidationResult(u.getUsername(), new HashSet<>(Collections.singleton(u.getType().toString())));
                }
            }
        }
        return CredentialValidationResult.INVALID_RESULT;
    }
}
