package app.managers;

import app.FunctionThrows;
import app.dto.AdministratorDTO;
import app.dto.CustomerDTO;
import app.dto.ModeratorDTO;
import app.exceptions.NotFoundException;
import app.model.*;
import app.repositories.UserRepository;
import jakarta.inject.Inject;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.SecurityContext;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Predicate;

public class UserManager {

    @Inject
    UserRepository userRepository;

    @Context
    private SecurityContext securityContext;

    synchronized public AdministratorDTO createAdministrator(User administrator) throws Exception {
        if (this.getUserByUsername((u)-> u.getUsername().equals(administrator.getUsername())).isEmpty()) {
            return new AdministratorDTO((Administrator) userRepository.insert(administrator));
        }
        throw new Exception("this username already exists!");
    }

    synchronized public ModeratorDTO createModerator(User moderator) throws Exception {
        if (this.getUserByUsername((u)-> u.getUsername().equals(moderator.getUsername())).isEmpty()) {
            return new ModeratorDTO((Moderator) userRepository.insert(moderator));
        }
        throw new Exception("this username already exists!");
    }

    synchronized public CustomerDTO createCustomer(User customer) throws Exception {
        if (this.getUserByUsername((u)-> u.getUsername().equals(customer.getUsername())).isEmpty()) {
            return new CustomerDTO((Customer) userRepository.insert(customer));
        }
        throw new Exception("this username already exists!");
    }

    synchronized public void deleteUser(int id) {
        userRepository.delete(id);
    }

    synchronized public Customer getCustomer(int id) throws NotFoundException {
        return (Customer) userRepository.get(id);
    }

    synchronized public Moderator getModerator(int id) throws NotFoundException {
        return (Moderator) userRepository.get(id);
    }

    synchronized public Administrator getAdministrator(int id) throws NotFoundException {
        return (Administrator) userRepository.get(id);
    }

    public void modifyUser (int id, FunctionThrows<User> func) throws Exception {
        userRepository.modify(id, func);
    }

    public void modifyCustomer(int id, FunctionThrows<Customer> func) throws Exception {
        userRepository.modifyCustomer(id, func);
    }

    public void modifyModerator(int id, FunctionThrows<Moderator> func) throws Exception {
        userRepository.modifyModerator(id, func);
    }

    public void modifyAdministrator(int id, FunctionThrows<Administrator> func) throws Exception {
        userRepository.modifyAdministrator(id, func);
    }

    public boolean changePassword(String oldPassword, String newPassword) throws Exception {
        User user = this.getUserFromServerContext();
        if (!user.getPassword().equals(oldPassword)) {
            throw new Exception("Wrong password!");
        }
        user.setPassword(newPassword);
        return true;
    }

    public Map<Integer, CustomerDTO> getCustomers(Predicate<User> predicate) {
        try {
            HashMap<Integer, CustomerDTO> res = new HashMap<>();
            for (Map.Entry<Integer, User> entry : userRepository.get(predicate).entrySet()) {
                res.put(entry.getKey(), new CustomerDTO((Customer) entry.getValue()));
            }
            return res;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public Map<Integer, ModeratorDTO> getModerators(Predicate<User> predicate) {
        try {
            HashMap<Integer, ModeratorDTO> res = new HashMap<>();
            for (Map.Entry<Integer, User> entry : userRepository.get(predicate).entrySet()) {
                res.put(entry.getKey(), new ModeratorDTO((Moderator) entry.getValue()));
            }
            return res;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public Map<Integer, User> getUserByUsername(Predicate<User> predicate) {
        try {
            HashMap<Integer, User> res = new HashMap<>(userRepository.get(predicate));
            return res;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public Map<Integer, AdministratorDTO> getAdministrators(Predicate<User> predicate) {
        try {
            HashMap<Integer, AdministratorDTO> res = new HashMap<>();
            for (Map.Entry<Integer, User> entry : userRepository.get(predicate).entrySet()) {
                res.put(entry.getKey(), new AdministratorDTO((Administrator) entry.getValue()));
            }
            return res;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public HashMap<Integer, CustomerDTO> getCustomerMap() {
        HashMap<Integer, CustomerDTO> res = new HashMap<>();
        for (Map.Entry<Integer, User> entry : userRepository.getMap().entrySet()) {
            if (entry.getValue().getType() == CustomerType.CUSTOMER) {
                res.put(entry.getKey(), new CustomerDTO((Customer) entry.getValue()));
            }
        }
        return res;
    }

    public HashMap<Integer, ModeratorDTO> getModeratorMap() {
        HashMap<Integer, ModeratorDTO> res = new HashMap<>();
        for (Map.Entry<Integer, User> entry : userRepository.getMap().entrySet()) {
            if (entry.getValue().getType() == CustomerType.MODERATOR) {
                res.put(entry.getKey(), new ModeratorDTO((Moderator) entry.getValue()));
            }
        }
        return res;
    }

    public HashMap<Integer, AdministratorDTO> getAdministratorMap() {
        HashMap<Integer, AdministratorDTO> res = new HashMap<>();
        for (Map.Entry<Integer, User> entry : userRepository.getMap().entrySet()) {
            if (entry.getValue().getType() == CustomerType.ADMINISTRATOR) {
                res.put(entry.getKey(), new AdministratorDTO((Administrator) entry.getValue()));
            }
        }
        return res;
    }

    public User getUserFromServerContext() {
        return this.getUserByUsername((u)-> u.getUsername().equals(securityContext.getUserPrincipal().getName())).entrySet().iterator().next().getValue();
    }
}
