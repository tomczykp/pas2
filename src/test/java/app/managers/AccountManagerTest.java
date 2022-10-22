package app.managers;

import app.model.AccountType;
import app.model.Address;
import app.model.Client;
import app.model.Person;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class AccountManagerTest {

	@Test
	public void accountTest () {
		Address adress = new Address("Poland", "Lodz", "al.Politechniki", "23");
		Client client = new Person(20000, "+48 533998311", adress, "Mateusz", "Sochacki", "236652");
		AccountManager am = new AccountManager();
		Assertions.assertDoesNotThrow(() -> am.createAccount(12.5, AccountType.Normal, client));
		System.out.println(am.get("10 1240 3456 00"));
	}

}
