import jakarta.annotation.ManagedBean;
import jakarta.annotation.PostConstruct;
import jakarta.inject.Named;
import modelBeans.ModeratorBean;
import org.json.JSONArray;
import org.json.JSONObject;
import rest.RestMethods;

import javax.faces.view.ViewScoped;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

@Named
@ManagedBean
@ViewScoped
public class Moderator implements Serializable {

    private JSONArray moderators;
    private final RestMethods restMethods;
    private ModeratorBean moderatorBean;
    private Boolean isUpdating = false;

    private String email;
    private final Map<Integer, Boolean> editable = new HashMap<>();

    private String adminPrefix = "http://localhost:8081/rest/api/administrator/";

    public Moderator() {
        this.restMethods = new RestMethods();
        this.moderatorBean = new ModeratorBean();
    }

    @PostConstruct
    public void fillArray() {
        JSONArray arr = restMethods.getAll(adminPrefix + "moderators");
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
        JSONObject obj = restMethods.getOne(adminPrefix + "moderator/" + id);
        obj.put("email", this.getEmail());
        restMethods.update(obj, adminPrefix + "update/moderator");
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
        restMethods.putCustomer(moderatorBean.getUsername(), moderatorBean.getPassword(),  moderatorBean.getEmail(), "MODERATOR", adminPrefix + "create/moderator");
        this.fillArray();
        return "createMod";
    }

    public String deleteModerator(Integer id) {
        restMethods.delete(adminPrefix + id);
        this.fillArray();
        return "deleteMod";
    }

    public JSONArray getModerators() {
        return moderators;
    }

    public void setModerators(JSONArray moderators) {
        this.moderators = moderators;
    }

    public ModeratorBean getModeratorBean() {
        return moderatorBean;
    }

    public void setModeratorBean(ModeratorBean moderatorBean) {
        this.moderatorBean = moderatorBean;
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
