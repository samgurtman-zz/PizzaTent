package co.nz.pizzatent.deliverymanagementsystem.service.routing;

/**
 * Represents an exception invoking OSRM routing engine
 */
public class RoutingException extends Exception {
    private String code;

    public RoutingException(String code){
        super("Received bad code: " + code);
        this.code = code;
    }

    public String getCode(){
        return code;
    }


}
