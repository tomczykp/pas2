import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Named;

import javax.faces.view.ViewScoped;
import java.io.Serializable;

@Named
@ApplicationScoped
public class ChosenID implements Serializable {
    private Integer id;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public void bindValue(Integer id) {
        this.id = id;
    }
}
