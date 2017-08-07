package co.nz.pizzatent.deliverymanagementsystem.configuration;

import co.nz.pizzatent.deliverymanagementsystem.orders.OrderMessageProcessor;
import co.nz.pizzatent.deliverymanagementsystem.pubsub.MessageListener;
import co.nz.pizzatent.deliverymanagementsystem.pubsub.PubSubSettings;
import com.google.api.gax.core.CredentialsProvider;
import com.google.api.gax.core.NoCredentialsProvider;
import com.google.api.gax.grpc.ChannelProvider;
import com.google.api.gax.grpc.FixedChannelProvider;
import com.google.cloud.ServiceOptions;
import com.google.cloud.pubsub.v1.SubscriptionAdminClient;
import com.google.cloud.pubsub.v1.SubscriptionAdminSettings;
import com.google.cloud.pubsub.v1.TopicAdminClient;
import com.google.cloud.pubsub.v1.TopicAdminSettings;
import com.google.pubsub.v1.PushConfig;
import com.google.pubsub.v1.SubscriptionName;
import com.google.pubsub.v1.TopicName;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import java.io.IOException;

@Configuration
public class PubSubConfig {
    private static final Logger logger = LogManager.getLogger();

    /**
     * Set up the topics on the pubsub emulator
     */
    private void initDevTopics(ChannelProvider channelProvider, CredentialsProvider credentialsProvider) throws IOException {
        String projectId = ServiceOptions.getDefaultProjectId();

        //Very ugly but they've seem to have broken the easy way of setting this up
        TopicAdminSettings topicAdminSettings = TopicAdminSettings
                        .defaultBuilder()
                        .setTransportProvider(TopicAdminSettings.defaultGrpcTransportProviderBuilder().setChannelProvider(channelProvider).build())
                        .setCredentialsProvider(credentialsProvider)
                        .build();
        SubscriptionAdminSettings subscriptionAdminSettings = SubscriptionAdminSettings.defaultBuilder()
                .setCredentialsProvider(credentialsProvider)
                .setTransportProvider(SubscriptionAdminSettings.defaultGrpcTransportProviderBuilder().setChannelProvider(channelProvider).build())
                .build();

        String topic = "orders";
        String subscription = "orders-subscription";
        TopicName topicName = TopicName.create(projectId, topic);
        SubscriptionName subscriptionName = SubscriptionName.create(projectId, subscription);
        try (
                TopicAdminClient topicAdminClient = TopicAdminClient.create(topicAdminSettings);
                SubscriptionAdminClient client = SubscriptionAdminClient.create(subscriptionAdminSettings);
        ) {
            topicAdminClient.createTopic(topicName);
            client.createSubscription(subscriptionName, topicName, PushConfig.getDefaultInstance(), 60);
        }catch(Exception e){
            logger.warn(e);
        }

    }

    @Bean
    @Profile(Profiles.DEVELOPMENT)
    public PubSubSettings developmentSettings(
            @Value("${PUBSUB_EMULATOR_HOST}") String pubsubEmulatorPort) throws IOException {

        ManagedChannel channel = ManagedChannelBuilder.forTarget(pubsubEmulatorPort).usePlaintext(true).build();
        ChannelProvider channelProvider = FixedChannelProvider.create(channel);
        CredentialsProvider credentialsProvider = new NoCredentialsProvider();
        initDevTopics(channelProvider, credentialsProvider);
        return new PubSubSettings(channelProvider, new NoCredentialsProvider());
    }

    @Autowired(required = false)
    @Bean
    public MessageListener orderListener(OrderMessageProcessor orderMessageProcessor, PubSubSettings pubSubSettings) {
        if(pubSubSettings == null){
            return new MessageListener("orders-subscription", orderMessageProcessor);
        }else{
            return new MessageListener("orders-subscription", orderMessageProcessor, pubSubSettings);
        }
    }

}
