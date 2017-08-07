package co.nz.pizzatent.deliverymanagementsystem.pubsub;


import co.nz.pizzatent.deliverymanagementsystem.TypedConsumer;
import com.google.api.gax.core.ExecutorProvider;
import com.google.api.gax.core.InstantiatingExecutorProvider;
import com.google.cloud.ServiceOptions;
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

    /***
     * @param handler function that processes received messages
     * @param <T> type of received messages
     */
    public <T> MessageListener(String subscription, TypedConsumer<T> handler) {
        this(subscription, handler, null);
    }

    public <T> MessageListener(String subscription, TypedConsumer<T> handler, PubSubSettings pubSubSettings) {
        String projectId = ServiceOptions.getDefaultProjectId();

        SubscriptionName subscriptionName = SubscriptionName.create(projectId, subscription);
        String pubsubId = subscriptionName.toString();
        MessageReceiver receiver = new StandardReceiver<>(pubsubId, handler);

        // Limit processing to one message at a time
        ExecutorProvider executorProvider =
                InstantiatingExecutorProvider.newBuilder()
                        .setExecutorThreadCount(1)
                        .build();

        Subscriber.Builder builder = Subscriber.defaultBuilder(subscriptionName, receiver);
        if(pubSubSettings != null) {
            if (pubSubSettings.getChannelProvider() != null) {
                builder.setChannelProvider(pubSubSettings.getChannelProvider());
            }
            if (pubSubSettings.getCredentialsProvider() != null) {
                builder.setCredentialsProvider(pubSubSettings.getCredentialsProvider());
            }
        }
        subscriber = builder.setExecutorProvider(executorProvider).build();
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
                ackReplyConsumer.ack();
            }
            if(entity != null) {
                try {
                    handler.accept(entity);
                    ackReplyConsumer.ack();
                } catch (Exception e) {
                    logger.error("PubSub " + id + " message " + pubsubMessage.getMessageId() +
                            " could not be consumed: " + e);
                    // nack only on internal consumer error
                    ackReplyConsumer.nack();
                }
            }
        }

        /**
         * PubSub messages should be base64 encoded. Thus we decode from base64 to a JSON string. Then we convert to a
         * POJO
         */
        private T convertToEntity(byte[] bytes, Class<T> clazz){
            String json = new String(Base64.getDecoder().decode(bytes), StandardCharsets.UTF_8);
            return gson.fromJson(json, clazz);
        }

    }
}

