package co.nz.pizzatent.deliverymanagementsystem.service.routing;

import co.nz.pizzatent.deliverymanagementsystem.util.Location;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@ActiveProfiles("development")
@RunWith(SpringRunner.class)
@SpringBootTest()
public class RoutingServiceIT {

    @Autowired
    private RoutingService routingService;

    @Test
    public void osrmReturnsValidRoute() throws RoutingException {
        Location startLocation = new Location(-36.850443, 174.764724);
        Location stopLocation1 = new Location(-36.8502608,174.7616317);
        Location stopLocation2 = new Location(-36.846501, 174.766069);
        Set<Location> locations = new HashSet<>();
        locations.add(stopLocation1);
        locations.add(stopLocation2);
        List<Location> route = routingService.getRoute(startLocation, locations);
        Assert.assertEquals(4, route.size());
    }
}
