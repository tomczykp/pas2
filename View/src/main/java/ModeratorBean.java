import jakarta.annotation.ManagedBean;
import jakarta.annotation.PostConstruct;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import modelBeans.Moderator;
import org.apache.http.HttpResponse;
import org.apache.http.impl.client.BasicResponseHandler;
import org.json.JSONArray;
import org.json.JSONObject;
import rest.RestClient;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import java.io.IOException;
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
        JSONArray arr;
        if (FacesContext.getCurrentInstance().getExternalContext().isUserInRole("MODERATOR")){
            String currentUserUsername = FacesContext.getCurrentInstance().getExternalContext().getRemoteUser();
            arr = restMethods.getAll(prefix + "moderators?username=" + currentUserUsername, jwtStorage.getJwt());
        } else {
            arr = restMethods.getAll(prefix + "moderators", jwtStorage.getJwt());
        }
        if (arr != null) {
            this.moderators = arr;
            this.editable.clear();
            for (int i = 0; i < arr.length(); i++) {
                JSONObject obj = (JSONObject) arr.get(i);
                editable.put(Integer.valueOf(obj.get("moderatorID").toString()), false);
            }
        }
    }

    public String update(Integer id) throws IOException {
        HttpResponse response = restMethods.getOne(prefix + "moderator/" + id, jwtStorage.getJwt());
        String jws = response.getFirstHeader("ETag").getValue();
        JSONObject obj = new JSONObject(new BasicResponseHandler().handleResponse(response));
        obj.put("email", this.getEmail());
        try {
            restMethods.update(obj, prefix + "moderator/update", jwtStorage.getJwt(), jws);
        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(e.getMessage()));
        }
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
        try {
            restMethods.putCustomer(moderator.getUsername(), moderator.getPassword(), moderator.getEmail(), "MODERATOR", prefix + "moderator/create", jwtStorage.getJwt());
        } catch (Exception e) {

        }
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
