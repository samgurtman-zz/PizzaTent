package co.nz.pizzatent.deliverymanagementsystem.service.routing;

public class RoutingException extends Exception {
    private String code;

    RoutingException(String code){
        super("Received bad code: " + code);
        this.code = code;
    }

    public String getCode(){
        return code;
    }


}
