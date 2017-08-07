package co.nz.pizzatent.deliverymanagementsystem.pubsub;

import com.google.api.gax.core.CredentialsProvider;
import com.google.api.gax.grpc.ChannelProvider;

public class PubSubSettings {

    private final ChannelProvider channelProvider;
    private final CredentialsProvider credentialsProvider;

    public PubSubSettings(ChannelProvider channelProvider, CredentialsProvider credentialsProvider) {

        this.channelProvider = channelProvider;
        this.credentialsProvider = credentialsProvider;
    }



    public CredentialsProvider getCredentialsProvider() {
        return credentialsProvider;
    }

    public ChannelProvider getChannelProvider() {
        return channelProvider;
    }



}
