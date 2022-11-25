//package app.repositories;
//
//import app.model.Customer;
//import org.junit.jupiter.api.Assertions;
//import org.junit.jupiter.api.Test;
//
//public class InMemoryRepoTest {
//
//	@Test
//	public void getTest() {
//		Repository<Integer, Customer> repo = new UserRepository();
//		Customer client = new Customer("username", "email@email", "passwd");
//		Assertions.assertEquals(UserRepository.class, repo.getClass());
//		Assertions.assertNotNull(client);
//		Assertions.assertEquals(repo.getLenght(), 0);
//	}
//
//	@Test
//	public void insertTest() throws Exception {
//		Repository<Integer, Customer> repo = new UserRepository();
//		Customer client = new Customer("user1", "asd", "passwd");
//		Customer client1 = new Customer("user2", "asdwdadw", "passwd");
//
//		Assertions.assertEquals(client, repo.insert(1, client));
//		Assertions.assertEquals(client, repo.get(1));
//		Assertions.assertThrows(Exception.class, () -> repo.get(2));
//		Assertions.assertEquals(1, repo.getLenght());
//
//		Assertions.assertEquals(client1, repo.insert(1, client1));
//		Assertions.assertNotEquals(client, repo.get(1));
//
//		repo.insert(2, client);
//		Assertions.assertEquals(client, repo.get(2));
//		Assertions.assertEquals(client1, repo.get(1));
//		Assertions.assertEquals(2, repo.getLenght());
//	}
//
//	@Test
//	public void deleteTest() throws Exception {
//		Repository<Integer, Customer> repo = new UserRepository();
//		Customer client = new Customer("user1", "asd", "passwd");
//		Customer client1 = new Customer("user2", "asdwdadw", "passwd");
//
//		Assertions.assertEquals(client, repo.insert(1, client));
//		Assertions.assertEquals(client1, repo.insert(2, client1));
//		Assertions.assertEquals(2, repo.getLenght());
//		Assertions.assertEquals(client1, repo.get(2));
//		Assertions.assertEquals(client, repo.get(1));
//
//		repo.delete(1);
//		Assertions.assertEquals(1, repo.getLenght());
//		Assertions.assertEquals(client1, repo.get(2));
//		Assertions.assertThrows(Exception.class, () -> repo.get(1));
//
//		Assertions.assertEquals(client, repo.insert(1, client));
//		Assertions.assertEquals(2, repo.getLenght());
//		Assertions.assertEquals(client1, repo.get(2));
//		Assertions.assertEquals(client, repo.get(1));
//	}
//
//
//	@Test
//	public void modifyTest() throws Exception {
//		Repository<Integer, Customer> repo = new UserRepository();
//		Customer client = new Customer("user1", "asd", "passwd");
//		Customer client1 = new Customer("user2", "asdwdadw", "passwd");
//
//		Assertions.assertEquals(client, repo.insert(1, client));
//		Assertions.assertEquals(client1, repo.insert(2, client1));
//		Assertions.assertEquals(2, repo.getLenght());
//		Assertions.assertEquals(client1, repo.get(2));
//		Assertions.assertEquals(client, repo.get(1));
//
//		Assertions.assertDoesNotThrow(() -> {
//				repo.modify(1, (c) -> {
//						c.setEmail("asdawe");
//						return c;
//					});
//				});
//
//		Customer m1 = repo.modify(1, (c) -> {
//			c.setUsername("username");
//			return c;
//		});
//
//		Assertions.assertEquals(2, repo.getLenght());
//		Assertions.assertEquals(m1, repo.get(1));
//		Assertions.assertEquals(client1, repo.get(2));
//		Assertions.assertEquals("username", repo.get(1).getUsername());
//		Assertions.assertEquals("username", m1.getUsername());
//
//	}
//}
