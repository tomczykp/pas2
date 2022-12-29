import jakarta.annotation.ManagedBean;
import jakarta.inject.Named;
import org.json.JSONArray;

import javax.faces.view.ViewScoped;
import java.io.Serializable;

@Named
@ManagedBean
@ViewScoped
public class filterBean implements Serializable {
    private String filterMessage;
    private JSONArray filteredCustomers;

    public String getFilterMessage() {
        return filterMessage;
    }

    public void setFilterMessage(String filterMessage) {
        this.filterMessage = filterMessage;
    }

    public JSONArray getFilteredCustomers() {
        return filteredCustomers;
    }

    public void setFilteredCustomers(JSONArray filteredCustomers) {
        this.filteredCustomers = filteredCustomers;
    }
}
