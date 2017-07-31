package co.nz.pizzatent.drivermanagementsystem.api;


import co.nz.pizzatent.drivermanagementsystem.orders.OrderMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import java.util.Set;

@RestController
@RequestMapping("/api")
public class RestInteface {

    private final Set<OrderMessage> ordersReceived;

    @Autowired
    public RestInteface(Set<OrderMessage> ordersReceived){
        this.ordersReceived = ordersReceived;
    }

    @RequestMapping(value = "/orders", method = RequestMethod.GET)
    @ResponseBody
    public Set<OrderMessage> listReceivedOrders() {
        return ordersReceived;
    }

    @RequestMapping(value = "/auth", method = RequestMethod.GET)
    @ResponseBody
    public String getCurrentAuth(Authentication authentication) {
        return authentication.getName();
    }

}
