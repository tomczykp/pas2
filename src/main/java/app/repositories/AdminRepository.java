package app.repositories;

import app.model.Administrator;
import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.ApplicationScoped;
import java.util.Map;
import java.util.Objects;

@ApplicationScoped
public class AdminRepository extends InMemoryRepository<Integer, Administrator> {

    @PostConstruct
    public void init() {
        counter = 1;
    }

    private Integer counter;

    @Override
    public Administrator insert (Administrator v) throws Exception {
        for (Map.Entry<Integer, Administrator> i : getMap().entrySet()) {
            if (Objects.equals(i.getValue().getUsername(), v.getUsername())) {
                throw new Exception("Username already exist");
            }
        }
        v.setAdministratorID(counter);
        return insert(counter++, v);
    }
}
