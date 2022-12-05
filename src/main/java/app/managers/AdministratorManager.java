package app.managers;

import app.FunctionThrows;
import app.dto.AdministratorDTO;
import app.dto.CustomerDTO;
import app.dto.ModeratorDTO;
import app.model.Administrator;
import app.model.Customer;
import app.model.CustomerType;
import app.model.Moderator;
import app.exceptions.NotFoundException;
import app.repositories.AdminRepository;
import app.repositories.CustomerRepository;
import app.repositories.ModeratorRepository;
import jakarta.inject.Inject;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Predicate;

public class AdministratorManager {

    @Inject
    private AdminRepository adminRepository;

    @Inject
    private CustomerRepository customerRepository;

    @Inject
    private ModeratorRepository moderatorRepository;

    synchronized public AdministratorDTO createAdministrator(Administrator admin) throws Exception {
        return new AdministratorDTO(adminRepository.insert(admin));
    }

    synchronized public CustomerDTO createCustomer(Customer customer) throws Exception{
        return new CustomerDTO(customerRepository.insert(customer));
    }

    synchronized public ModeratorDTO createModerator(Moderator moderator) throws Exception {
        return  new ModeratorDTO(moderatorRepository.insert(moderator));
    }

    synchronized public void deleteAdministrator (int id) {
        adminRepository.delete(id);
    }

    synchronized public void deleteCustomer (int id) {
        customerRepository.delete(id);
    }

    synchronized public void deleteModerator (int id) {
        moderatorRepository.delete(id);
    }

    public Customer getCustomer (int id) throws NotFoundException {
        return customerRepository.get(id);
    }

    public Moderator getModerator (int id) throws NotFoundException {
        return moderatorRepository.get(id);
    }

    public Administrator getAdministrator (int id) throws NotFoundException {
        return adminRepository.get(id);
    }

    public Customer modifyCustomer (int id, FunctionThrows<Customer> func) throws Exception {
        return customerRepository.modify(id, func);
    }

    public Moderator modifyModerator (int id, FunctionThrows<Moderator> func) throws Exception {
        return moderatorRepository.modify(id, func);
    }

    public Map<Integer, CustomerDTO> getCustomers (Predicate<Customer> predicate) {
        try {
            HashMap<Integer, CustomerDTO> res = new HashMap<>();
            for (Map.Entry<Integer, Customer> entry : customerRepository.get(predicate).entrySet()) {
                res.put(entry.getKey(), new CustomerDTO(entry.getValue()));
            }
            return res;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public Map<Integer, ModeratorDTO> getModerators (Predicate<Moderator> predicate) {
        try {
            HashMap<Integer, ModeratorDTO> res = new HashMap<>();
            for (Map.Entry<Integer, Moderator> entry : moderatorRepository.get(predicate).entrySet()) {
                res.put(entry.getKey(), new ModeratorDTO(entry.getValue()));
            }
            return res;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public Map<Integer, AdministratorDTO> getAdministrators (Predicate<Administrator> predicate) {
        try {
            HashMap<Integer, AdministratorDTO> res = new HashMap<>();
            for (Map.Entry<Integer, Administrator> entry : adminRepository.get(predicate).entrySet()) {
                res.put(entry.getKey(), new AdministratorDTO(entry.getValue()));
            }
            return res;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public HashMap<Integer, CustomerDTO> getCustomerMap () {
        HashMap<Integer, CustomerDTO> res = new HashMap<>();
        for (Map.Entry<Integer, Customer> entry : customerRepository.getMap().entrySet()) {
           if (entry.getValue().getType() == CustomerType.CUSTOMER) {
                res.put(entry.getKey(), new CustomerDTO(entry.getValue()));
            }
        }
        return res;
    }

    public HashMap<Integer, AdministratorDTO> getAdministratorMap () {
        HashMap<Integer, AdministratorDTO> res = new HashMap<>();
        for (Map.Entry<Integer, Administrator> entry : adminRepository.getMap().entrySet()) {
            if (entry.getValue().getType() == CustomerType.ADMINISTRATOR) {
                res.put(entry.getKey(), new AdministratorDTO(entry.getValue()));
            }
        }
        return res;
    }

    public HashMap<Integer, ModeratorDTO> getModeratorMap () {
        HashMap<Integer, ModeratorDTO> res = new HashMap<>();
        for (Map.Entry<Integer, Moderator> entry : moderatorRepository.getMap().entrySet()) {
            res.put(entry.getKey(), new ModeratorDTO(entry.getValue()));
        }
        return res;
    }
}
