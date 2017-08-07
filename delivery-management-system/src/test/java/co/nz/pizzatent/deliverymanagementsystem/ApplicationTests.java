package co.nz.pizzatent.deliverymanagementsystem;

import co.nz.pizzatent.deliverymanagementsystem.pubsub.MessageListener;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
@TestPropertySource(properties = { "GCE_PROJECT_ID = test" })
public class ApplicationTests {

    @MockBean
	private MessageListener messageListener;

	@Test
	public void contextLoads() {
	}

}
