package co.nz.pizzatent.deliverymanagementsystem.service.routing;

import co.nz.pizzatent.deliverymanagementsystem.util.Location;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class RoutingService {

    private WebTarget tripsApi;

    @Autowired
    public RoutingService(@Value("${osrm.endpoint}") String osrmEndpoint) {
        Client client = ClientBuilder.newClient();
        tripsApi = client.target(osrmEndpoint).path("trip/v1/driving/{locations}").queryParam("roundtrip", true);
    }


    /**
     * Get optimised route for given delivery locations
     * @param storeLocation originating store location
     * @param deliveryLocations locations where deliveries will occur
     * @return list of locations to visit in order
     * @throws RoutingException
     */
    public List<Location> getRoute(Location storeLocation, Set<Location> deliveryLocations) throws RoutingException {
        List<Location> allLocations = new ArrayList<>();
        allLocations.add(storeLocation);
        allLocations.addAll(deliveryLocations);
        String encodedLocations = allLocations.stream()
                .map((Location location) -> location.getLatitude() + "," + location.getLongitude())
                .collect(Collectors.joining(";"));
        OsrmTripResponse response = tripsApi.
                resolveTemplate("locations", encodedLocations)
                .request()
                .buildGet()
                .invoke(OsrmTripResponse.class);
        return processResponse(response);
    }

    private List<Location> processResponse(OsrmTripResponse tripResponse) throws RoutingException {
        if(!tripResponse.getCode().equals("Ok")){
            throw new RoutingException(tripResponse.getCode());
        }
        ArrayList<OsrmTripResponse.Waypoint> waypoints = new ArrayList<>(tripResponse.getWaypoints());
        waypoints.sort(Comparator.comparingInt(OsrmTripResponse.Waypoint::getOrder));
        return waypoints.stream().map(OsrmTripResponse.Waypoint::getLocation).collect(Collectors.toList());
    }
}