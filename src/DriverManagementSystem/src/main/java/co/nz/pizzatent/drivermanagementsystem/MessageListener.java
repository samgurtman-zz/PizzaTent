package co.nz.pizzatent.drivermanagementsystem;


import com.google.api.gax.core.ExecutorProvider;
import com.google.api.gax.core.InstantiatingExecutorProvider;
import com.google.cloud.pubsub.v1.AckReplyConsumer;
import com.google.cloud.pubsub.v1.MessageReceiver;
import com.google.cloud.pubsub.v1.Subscriber;
import com.google.common.util.concurrent.MoreExecutors;
import com.google.gson.Gson;
import com.google.pubsub.v1.PubsubMessage;
import com.google.pubsub.v1.SubscriptionName;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

/**
 * Listens for messages on a specific google PubSub topic and then processes each one received.
 */
public class MessageListener {
    private static final Logger logger = LogManager.getLogger();

    private final Subscriber subscriber;

    public <T> MessageListener(String gceProjectId, String topic, TypedConsumer<T> handler) {
        String pubsubId = gceProjectId + "/" + topic;

        MessageReceiver receiver = new StandardReceiver<>(pubsubId, handler);

        // Limit processing to one message at a time
        ExecutorProvider executorProvider =
                InstantiatingExecutorProvider.newBuilder()
                        .setExecutorThreadCount(1)
                        .build();

        SubscriptionName name = SubscriptionName.create(gceProjectId, topic);


        subscriber = Subscriber.defaultBuilder(name, receiver).setExecutorProvider(executorProvider).build();
        subscriber.addListener(new StandardStateListener(pubsubId), MoreExecutors.directExecutor());

    }


    public void startListening() {
        subscriber.startAsync().awaitRunning();
    }

    private static class StandardStateListener extends Subscriber.Listener{

        private String id;

        private StandardStateListener(String id){
            this.id = id;
        }

        @Override
        public void failed(Subscriber.State from, Throwable failure) {
            logger.error("Pubsub " + id + "failure :  " + failure);
        }
    }

    private static class StandardReceiver <T> implements MessageReceiver {
        private final Gson gson = new Gson();
        private final String id;
        private final TypedConsumer<T> handler;

        private StandardReceiver(String id, TypedConsumer<T> handler){
            this.handler = handler;
            this.id = id;
        }

        @Override
        public void receiveMessage(PubsubMessage pubsubMessage, AckReplyConsumer ackReplyConsumer) {
            byte[] bytes = pubsubMessage.getData().toByteArray();
            T entity = null;
            try{
                entity = convertToEntity(bytes, handler.getType());
            }catch(Exception e){
                logger.error("PubSub " + id + " message " + pubsubMessage.getMessageId() + " malformed: " + e);
                ackReplyConsumer.nack();
            }
            if(entity != null) {
                try {
                    handler.accept(entity);
                    ackReplyConsumer.ack();
                } catch (Exception e) {
                    logger.error("PubSub " + id + " message " + pubsubMessage.getMessageId() + " could not be consumed: " + e);
                    ackReplyConsumer.nack();
                }
            }
        }

        private T convertToEntity(byte[] bytes, Class<T> clazz){
            String json = new String(Base64.getDecoder().decode(bytes), StandardCharsets.UTF_8);
            return gson.fromJson(json, clazz);
        }

    }
}

