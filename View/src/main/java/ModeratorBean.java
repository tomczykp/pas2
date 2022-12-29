import jakarta.annotation.ManagedBean;
import jakarta.annotation.PostConstruct;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import modelBeans.Moderator;
import org.json.JSONArray;
import org.json.JSONObject;
import rest.RestClient;

import javax.faces.view.ViewScoped;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

@Named
@ManagedBean
@ViewScoped
public class ModeratorBean implements Serializable {

    @Inject
    private JwtStorage jwtStorage;

    private JSONArray moderators;
    private final RestClient restMethods;
    private Moderator moderator;
    private Boolean isUpdating = false;

    private String email;
    private final Map<Integer, Boolean> editable = new HashMap<>();

    private final String prefix = "http://localhost:8081/rest/api/";

    public ModeratorBean() {
        this.restMethods = new RestClient();
        this.moderator = new Moderator();
    }

    @PostConstruct
    public void fillArray() {
        JSONArray arr = restMethods.getAll(prefix + "moderators", jwtStorage.getJwt());
        if (arr != null) {
            this.moderators = arr;
            this.editable.clear();
            for (int i = 0; i < arr.length(); i++) {
                JSONObject obj = (JSONObject) arr.get(i);
                editable.put(Integer.valueOf(obj.get("moderatorID").toString()), false);
            }
        }
    }

    public String update(Integer id) {
        JSONObject obj = restMethods.getOne(prefix + "moderator/" + id, jwtStorage.getJwt());
        obj.put("email", this.getEmail());
        restMethods.update(obj, prefix + "moderator/update", jwtStorage.getJwt());
        this.fillArray();
        this.setEmail("");
        this.isUpdating = false;
        return "submitCustomer";
    }

    public void edit(Integer id, String email) {
        if (!isUpdating) {
            this.email = email;
            this.editable.replace(id, true);
            isUpdating = true;
        }
    }

    public String createModerator() {
        restMethods.putCustomer(moderator.getUsername(), moderator.getPassword(),  moderator.getEmail(), "MODERATOR", prefix + "moderator/create", jwtStorage.getJwt());
        this.fillArray();
        return "createMod";
    }

    public String deleteModerator(Integer id) {
        restMethods.delete(prefix + id, jwtStorage.getJwt());
        this.fillArray();
        return "deleteMod";
    }

    public JSONArray getModerators() {
        return moderators;
    }

    public void setModerators(JSONArray moderators) {
        this.moderators = moderators;
    }

    public Moderator getModerator() {
        return moderator;
    }

    public void setModerator(Moderator moderator) {
        this.moderator = moderator;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Boolean getUpdating() {
        return isUpdating;
    }

    public void setUpdating(Boolean updating) {
        isUpdating = updating;
    }

    public boolean getEditable(Integer id) {
        return editable.get(id);
    }
}
