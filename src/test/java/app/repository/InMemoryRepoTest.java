package app.repository;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class InMemoryRepoTest {

	@Test
	public void getTest() {
		Repository<Integer, Client> repo = new InMemoryRepository<>();
		Address a1 = new Address("Poland", "Lodz", "Politechniki", "6A");
		Client client = new Client(100, "+48 908067", a1);
		Assertions.assertEquals(InMemoryRepository.class, repo.getClass());
		Assertions.assertNotNull(a1);
		Assertions.assertNotNull(client);
		Assertions.assertEquals(repo.getLenght(), 0);
	}

	@Test
	public void insertTest() {
		Repository<Integer, Client> repo = new InMemoryRepository<>();
		Address a1 = new Address("Poland", "Lodz", "Politechniki", "6A");
		Client client = new Client(100, "+48 908067", a1);
		Client client1 = new Client(10000, "+48 9080667", a1);

		Assertions.assertEquals(client, repo.insert(1, client));
		Assertions.assertEquals(client, repo.get(1));
		Assertions.assertNull(repo.get(2));
		Assertions.assertEquals(1, repo.getLenght());

		Assertions.assertEquals(client1, repo.insert(1, client1));
		Assertions.assertNotEquals(client, repo.get(1));

		repo.insert(2, client);
		Assertions.assertEquals(client, repo.get(2));
		Assertions.assertEquals(client1, repo.get(1));
		Assertions.assertEquals(2, repo.getLenght());
	}

	@Test
	public void deleteTest() {
		Repository<Integer, Client> repo = new InMemoryRepository<>();
		Address a1 = new Address("Poland", "Lodz", "Politechniki", "6A");
		Client client = new Client(100, "+48 908067", a1);
		Client client1 = new Client(10000, "+48 9080667", a1);

		Assertions.assertEquals(client, repo.insert(1, client));
		Assertions.assertEquals(client1, repo.insert(2, client1));
		Assertions.assertEquals(2, repo.getLenght());
		Assertions.assertEquals(client1, repo.get(2));
		Assertions.assertEquals(client, repo.get(1));

		repo.delete(1);
		Assertions.assertEquals(1, repo.getLenght());
		Assertions.assertEquals(client1, repo.get(2));
		Assertions.assertNull(repo.get(1));

		Assertions.assertEquals(client, repo.insert(1, client));
		Assertions.assertEquals(2, repo.getLenght());
		Assertions.assertEquals(client1, repo.get(2));
		Assertions.assertEquals(client, repo.get(1));
	}


	@Test
	public void modifyTest() throws Exception {
		Repository<Integer, Client> repo = new InMemoryRepository<>();
		Address a1 = new Address("Poland", "Lodz", "Politechniki", "6A");
		Client client = new Client(100, "+48 908067", a1);
		Client client1 = new Client(10000, "+48 9080667", a1);

		Assertions.assertEquals(client, repo.insert(1, client));
		Assertions.assertEquals(client1, repo.insert(2, client1));
		Assertions.assertEquals(2, repo.getLenght());
		Assertions.assertEquals(client1, repo.get(2));
		Assertions.assertEquals(client, repo.get(1));

		Assertions.assertDoesNotThrow(() -> {
				repo.modify(1, (c) -> {
						c.setIncome(100);
						return c;
					});
				});

		Client m1 = repo.modify(1, (c) -> {
			c.setIncome(100);
			return c;
		});

		Assertions.assertEquals(2, repo.getLenght());
		Assertions.assertEquals(m1, repo.get(1));
		Assertions.assertEquals(client1, repo.get(2));
		Assertions.assertEquals(100, repo.get(1).getIncome());
		Assertions.assertEquals(100, m1.getIncome());

	}
}
