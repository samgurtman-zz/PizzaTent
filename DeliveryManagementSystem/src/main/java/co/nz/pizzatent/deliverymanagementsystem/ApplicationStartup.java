package co.nz.pizzatent.deliverymanagementsystem;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ApplicationStartup  implements ApplicationListener<ApplicationReadyEvent> {

    private List<MessageListener> listeners;

    @Autowired
    public ApplicationStartup(List<MessageListener> listeners){
        this.listeners = listeners;
    }

    @Override
    public void onApplicationEvent(final ApplicationReadyEvent event) {
        for(MessageListener listener : listeners){
            listener.startListening();
        }
    }

}
