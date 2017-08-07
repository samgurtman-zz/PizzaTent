package co.nz.pizzatent.deliverymanagementsystem.service.orders;

import co.nz.pizzatent.deliverymanagementsystem.domain.order.OrderEntity;
import co.nz.pizzatent.deliverymanagementsystem.domain.order.OrderRepository;
import co.nz.pizzatent.deliverymanagementsystem.domain.store.StoreEntity;
import co.nz.pizzatent.deliverymanagementsystem.domain.store.StoreRepository;
import co.nz.pizzatent.deliverymanagementsystem.service.routes.RoutingException;
import co.nz.pizzatent.deliverymanagementsystem.service.routes.RoutingService;
import co.nz.pizzatent.deliverymanagementsystem.util.Location;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Answers;
import org.mockito.Matchers;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.internal.stubbing.answers.ReturnsArgumentAt;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.core.annotation.Order;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.*;

import static org.mockito.BDDMockito.given;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anySet;

@DataJpaTest
@ActiveProfiles("development")
@Import(OrdersService.class)
@RunWith(SpringRunner.class)
public class OrdersServiceTest {
    private static final double DELTA = 1e-15;
    private static final double STORE_LATITUDE = 10;
    private static final double STORE_LONGITUDE = 11;
    private static final String STORE_ID = "abc123";

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private StoreRepository storeRepository;

    @MockBean
    private RoutingService routingService;

    @Autowired
    private OrdersService ordersService;

    @Before
    public void setup() throws RoutingException {
        StoreEntity storeEntity = new StoreEntity();
        storeEntity.setStoreId(STORE_ID);
        storeEntity.setLatitude(STORE_LATITUDE);
        storeEntity.setLongitude(STORE_LONGITUDE);
        storeRepository.save(storeEntity);
        Assert.assertNotNull(storeRepository.findByStoreId(STORE_ID));

        given(routingService.getRoute(any(Location.class), Matchers.anySetOf(Location.class))).willAnswer(invocation -> {
            List<Location> locations = new ArrayList<>();
            locations.add((Location) invocation.getArguments()[0]);
            locations.addAll((Set<Location>) invocation.getArguments()[1]);
            return locations;
        });
    }

    @Test
    public void testAddOrder(){

        Date past = new Date(System.currentTimeMillis() - 1000 * 60);
        ordersService.addOrder(STORE_ID, "def456", new Location(2, 3), 5, past);
        OrderEntity orderEntity = orderRepository.findByOrderId("def456");
        Assert.assertNotNull(orderEntity);
        Assert.assertEquals(STORE_ID, orderEntity.getOriginStore().getStoreId());
        Assert.assertEquals("def456", orderEntity.getOrderId());
        Assert.assertEquals(2d, orderEntity.getDelvieryLatitude(), DELTA);
        Assert.assertEquals(3d, orderEntity.getDeliveryLongitude(), DELTA);
        Assert.assertEquals(5, (int)orderEntity.getSize());
        Assert.assertEquals(past, orderEntity.getReadyTime());

        List<OrderEntity> orders = ordersService.getPendingOrders(STORE_ID);
        Assert.assertEquals(1, orders.size());
        orderEntity = orders.get(0);
        Assert.assertNotNull(orderEntity);
        Assert.assertEquals(STORE_ID, orderEntity.getOriginStore().getStoreId());
        Assert.assertEquals("def456", orderEntity.getOrderId());
        Assert.assertEquals(2d, orderEntity.getDelvieryLatitude(), DELTA);
        Assert.assertEquals(3d, orderEntity.getDeliveryLongitude(), DELTA);
        Assert.assertEquals(5, (int)orderEntity.getSize());
        Assert.assertEquals(past, orderEntity.getReadyTime());

    }

    @Test
    public void testCompleteOrder() {

        Date past = new Date(System.currentTimeMillis() - 1000 * 60);
        ordersService.addOrder(STORE_ID, "def456", new Location(2, 3), 5, past);
        List<OrderEntity> pendingOrders = ordersService.getPendingOrders(STORE_ID);
        Assert.assertEquals(1, pendingOrders.size());
        ordersService.completeOrder("def456");
        pendingOrders = ordersService.getPendingOrders(STORE_ID);
        Assert.assertEquals(0, pendingOrders.size());

        try {
            ordersService.completeOrder("def456");
            Assert.fail("Should have thrown an IllegalArgumentException");
        }catch(IllegalArgumentException ignore){}

    }

    @Test
    public void testPendingOnlyAfterReadyTime(){
        Date future = new Date(System.currentTimeMillis() + 1000 * 60);
        ordersService.addOrder(STORE_ID, "def456", new Location(2, 3), 5, future);
        List<OrderEntity> orders = ordersService.getPendingOrders(STORE_ID);
        Assert.assertEquals(0, orders.size());
    }

    @Test
    public void testCreateTrip() throws RoutingException {

        Date past = new Date(System.currentTimeMillis() - 1000 * 60);
        ordersService.addOrder(STORE_ID, "def456", new Location(2, 3), 5, past);
        ordersService.addOrder(STORE_ID, "ghi789", new Location(7, 8), 5, past);

        List<OrderEntity> orders = ordersService.getPendingOrders(STORE_ID);
        Assert.assertEquals(2, orders.size());

        List<Location> locations = ordersService.createNextTrip(STORE_ID, 5);
        Assert.assertEquals(2, locations.size());
        Assert.assertEquals(STORE_LATITUDE, locations.get(0).getLatitude(), DELTA);
        Assert.assertEquals(STORE_LONGITUDE, locations.get(0).getLongitude(), DELTA);
        Assert.assertEquals(2, locations.get(1).getLatitude(), DELTA);
        Assert.assertEquals(3, locations.get(1).getLongitude(), DELTA);
        orders = ordersService.getPendingOrders(STORE_ID);
        Assert.assertEquals(1, orders.size());
    }

    @Test
    public void testPendingOrder() {
        Date early = new Date(System.currentTimeMillis() - 1000 * 30);

        Date earlier = new Date(System.currentTimeMillis() - 1000 * 60);
        Date earliest = new Date(System.currentTimeMillis() - 1000 * 120);

        ordersService.addOrder(STORE_ID, "def456", new Location(2, 3), 5, earlier);
        ordersService.addOrder(STORE_ID, "ghi789", new Location(7, 8), 5, early);
        ordersService.addOrder(STORE_ID, "jkl012", new Location(7, 8), 5, earliest);

        List<OrderEntity> orders = ordersService.getPendingOrders(STORE_ID);
        Assert.assertEquals(3, orders.size());
        Assert.assertEquals("jkl012", orders.get(0).getOrderId());
        Assert.assertEquals("def456", orders.get(1).getOrderId());
        Assert.assertEquals("ghi789", orders.get(2).getOrderId());

    }

    @Test
    public void testTripPacking() throws RoutingException {


        Date earlier = new Date(System.currentTimeMillis() - 1000 * 60);
        Date early = new Date(System.currentTimeMillis() - 1000 * 30);

        Date past = new Date(System.currentTimeMillis() - 1000 * 60);
        ordersService.addOrder(STORE_ID, "def456", new Location(2, 3), 5, earlier);
        ordersService.addOrder(STORE_ID, "ghi789", new Location(7, 8), 5, early);
        ordersService.addOrder(STORE_ID, "jkl012", new Location(9, 10), 3, early);

        List<OrderEntity> orders = ordersService.getPendingOrders(STORE_ID);
        Assert.assertEquals(3, orders.size());

        List<Location> locations = ordersService.createNextTrip(STORE_ID, 2);
        Assert.assertNull(locations);
        orders = ordersService.getPendingOrders(STORE_ID);
        Assert.assertEquals(3, orders.size());

        locations = ordersService.createNextTrip(STORE_ID, 4);
        Assert.assertEquals(2, locations.size());
        Assert.assertEquals(9, locations.get(1).getLatitude(), DELTA);
        Assert.assertEquals(10, locations.get(1).getLongitude(), DELTA);
        orders = ordersService.getPendingOrders(STORE_ID);
        Assert.assertEquals(2, orders.size());


        locations = ordersService.createNextTrip(STORE_ID, 11);
        Assert.assertEquals(3, locations.size());
        orders = ordersService.getPendingOrders(STORE_ID);
        Assert.assertEquals(0, orders.size());
    }


}
