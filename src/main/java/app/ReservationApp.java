package app;

import app.model.*;
import app.repositories.ProductRepository;
import app.repositories.UserRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.ApplicationPath;
import jakarta.ws.rs.core.Application;
import javax.annotation.PostConstruct;
import javax.annotation.security.DeclareRoles;
import javax.security.enterprise.authentication.mechanism.http.AutoApplySession;
import java.util.Random;

@ApplicationScoped
@DeclareRoles({"CUSTOMER", "MODERATOR", "ADMINISTRATOR", "ANONYMOUS"})
@ApplicationPath("/api")
public class ReservationApp extends Application {

        @Inject
        UserRepository userRepository;

        @Inject
        ProductRepository productRepository;

        @PostConstruct
        public void addAdmin() {
                User user = new Administrator("admin", "admin");
                User user1 = new Customer("customer", "customer@customer.pl", "customer");
                User user2 = new Moderator("moderator","moderator@moderator.pl", "moderator");
                Random random = new Random();
                for (int i = 0; i < 10; i++) {
                        productRepository.insert(new Product(random.nextDouble()));
                }
                try {
                        userRepository.insert(user);
                        userRepository.insert(user1);
                        userRepository.insert(user2);
                } catch (Exception e ) {

                }
        }
}
