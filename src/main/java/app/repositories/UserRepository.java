package app.repositories;

import app.FunctionThrows;
import app.exceptions.NotFoundException;
import app.model.Customer;
import app.model.User;
import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.Map;
import java.util.Objects;

@ApplicationScoped
public class UserRepository extends InMemoryRepository<Integer, User> {

        @PostConstruct
        public void init() {
            counter = 1;
        }

        private Integer counter;

        @Override
        public User insert (User v) throws Exception {
            for (Map.Entry<Integer, User> i : getMap().entrySet()) {
                if (Objects.equals(i.getValue().getUsername(), v.getUsername())) {
                    throw new Exception("Username already exist");
                }
            }
            v.setUserID(counter);
            return insert(counter++, v);
        }
}
