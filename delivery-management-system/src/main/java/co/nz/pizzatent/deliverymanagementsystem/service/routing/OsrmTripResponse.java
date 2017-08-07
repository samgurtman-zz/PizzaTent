package co.nz.pizzatent.deliverymanagementsystem.service.routing;

import co.nz.pizzatent.deliverymanagementsystem.util.Location;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import java.util.List;

class OsrmTripResponse {
    private String code;

    private List<Waypoint> waypoints;

    String getCode() {
        return code;
    }


    List<Waypoint> getWaypoints() {
        return waypoints;
    }

    static class Waypoint {

        @JsonFormat(shape = JsonFormat.Shape.ARRAY)
        @JsonPropertyOrder({ "latitude", "longitude" })
        private Location location;

        @JsonProperty("waypoint_index")
        private int order;

        Location getLocation() {
            return location;
        }

        int getOrder() {
            return order;
        }
    }
}
