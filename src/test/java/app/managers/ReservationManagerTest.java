package app.managers;

import app.managers.a.CustomerManager;
import app.managers.a.ProductManager;
import app.managers.a.ReservationManager;
import app.model.a.Customer;
import app.model.a.Product;
import app.repositories.CustomerRepository;
import app.repositories.ProductRepository;
import app.repositories.ReservationRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

@ExtendWith(MockitoExtension.class)
public class ReservationManagerTest {

	@Mock
	ProductRepository prepository;
	@Mock
	CustomerRepository crepository;
	@Mock
	ReservationRepository rrepository;

	@InjectMocks
	ProductManager pm = new ProductManager();
	@InjectMocks
	CustomerManager cm = new CustomerManager();
	@InjectMocks
	ReservationManager rm = new ReservationManager();


	@Test
	public void sanityTest()  {
		ProductManager pm = new ProductManager();
		CustomerManager cm = new CustomerManager();
		ReservationManager rm = new ReservationManager();
		Product p0 = pm.create(100);
		Product p1 = pm.create(200);

		Customer c0 = cm.create("ptomczyk", "p@email.com");
		Customer c1 = cm.create("msochacki", "m@");

		Assertions.assertDoesNotThrow(() -> rm.create(LocalDateTime.now(), c0, p0));
		Assertions.assertDoesNotThrow(() -> rm.create(LocalDateTime.now(), c1, p1));
	}

	@Test
	public void smokeTest() throws Exception {
		ProductManager pm = new ProductManager();
		CustomerManager cm = new CustomerManager();
		Product p0 = pm.create(100);
		Product p1 = pm.create(200);

		Customer c0 = cm.create("ptomczyk", "p@email.com");
		Customer c1 = cm.create("msochacki", "m@");

		Assertions.assertEquals(100, p0.getPrice());
		Assertions.assertEquals(200, p1.getPrice());
		Assertions.assertEquals("ptomczyk", c0.getUsername());
		Assertions.assertEquals("m@", c1.getEmail());
		Assertions.assertEquals(2, pm.getLength());
		Assertions.assertEquals(2, cm.getLength());

		Assertions.assertEquals(100, pm.get(0).getPrice());
		Assertions.assertEquals(200, pm.get(1).getPrice());
		Assertions.assertEquals("ptomczyk", cm.get(0).getUsername());
		Assertions.assertEquals("m@", cm.get(1).getEmail());

		Assertions.assertEquals(c0, cm.get(0));
		Assertions.assertEquals(c1, cm.get(1));
		Assertions.assertEquals(p0, pm.get(0));
		Assertions.assertEquals(p1, pm.get(1));

		pm.delete(0);
		cm.delete(1);

		Assertions.assertEquals(1, pm.getLength());
		Assertions.assertEquals(1, cm.getLength());
		Assertions.assertNull(pm.get(0));
		Assertions.assertNull(cm.get(1));

		Assertions.assertEquals(200, pm.get(1).getPrice());
		Assertions.assertEquals("ptomczyk", cm.get(0).getUsername());

		List<Customer> l = cm.get((c) -> Objects.equals(c.getUsername(), "ptomczyk"));
		Assertions.assertEquals(c0, l.get(0));
		Assertions.assertEquals(1, l.size());

		Product pmod = pm.modify(1, (p) -> p.setPrice(300));
		Assertions.assertEquals(300, pmod.getPrice());
		Assertions.assertEquals(300, pm.get(1).getPrice());
		Assertions.assertEquals(300, pm.get(1).getPrice());
	}

	@Test
	public void regressionTest() {
		ProductManager pm = new ProductManager();
		CustomerManager cm = new CustomerManager();
		ReservationManager rm = new ReservationManager();
		Product p0 = pm.create(100);
		Product p1 = pm.create(200);

		Customer c0 = cm.create("ptomczyk", "p@email.com");
		Customer c1 = cm.create("msochacki", "m@");

		Assertions.assertDoesNotThrow(() -> rm.create(LocalDateTime.now().plusHours(2), c0, p0));
		Assertions.assertThrows(Exception.class, () -> rm.create(LocalDateTime.now().plusHours(2), c0, p0));
		Assertions.assertDoesNotThrow(() -> rm.create(LocalDateTime.now().plusHours(2), c0, p1));
		Assertions.assertEquals(2, c0.getReservations().size());
		Assertions.assertThrows(Exception.class, () -> rm.create(LocalDateTime.now().plusHours(2), c1, p0));
		Assertions.assertEquals(2, c0.getReservations().size());
		Assertions.assertEquals(1, p0.getReservations().size());
		Assertions.assertEquals(1, p1.getReservations().size());
		Assertions.assertEquals(0, c1.getReservations().size());

		try {
			rm.delete(0);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		Assertions.assertEquals(1, c0.getReservations().size());
		Assertions.assertEquals(0, p0.getReservations().size());
		Assertions.assertEquals(1, p1.getReservations().size());
		Assertions.assertEquals(0, c1.getReservations().size());
	}

}
