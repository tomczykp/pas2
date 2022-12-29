package app;

import app.model.Administrator;
import app.model.Customer;
import app.model.Moderator;
import app.model.User;
import app.repositories.UserRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.ApplicationPath;
import jakarta.ws.rs.core.Application;
import javax.annotation.PostConstruct;
import javax.annotation.security.DeclareRoles;

@ApplicationScoped
@DeclareRoles({"CUSTOMER", "MODERATOR", "ADMINISTRATOR", "ANONYMOUS"})
@ApplicationPath("/api")
public class ReservationApp extends Application {

        @Inject
        UserRepository userRepository;

        @PostConstruct
        public void addAdmin() {
                User user = new Administrator("admin", "admin");
                User user1 = new Customer("customer", "customer@customer.pl", "customer");
                User user2 = new Moderator("moderator","moderator@moderator.pl", "moderator");
                try {
                        userRepository.insert(user);
                        userRepository.insert(user1);
                        userRepository.insert(user2);
                } catch (Exception e ) {

                }
        }
}
