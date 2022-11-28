package app.repositories;

import app.model.Moderator;
import jakarta.annotation.PostConstruct;

import java.util.Map;
import java.util.Objects;

public class ModeratorRepository extends InMemoryRepository<Integer, Moderator> {

    @PostConstruct
    public void init() {
        counter = 1;
    }

    private Integer counter;

    @Override
    public Moderator insert (Moderator v) throws Exception {
        for (Map.Entry<Integer, Moderator> i : getMap().entrySet()) {
            if (Objects.equals(i.getValue().getUsername(), v.getUsername())) {
                throw new Exception("Username already exist");
            }
        }
        v.setModeratorID(counter);
        return insert(counter++, v);
    }
}
