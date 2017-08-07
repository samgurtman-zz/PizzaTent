package co.nz.pizzatent.deliverymanagementsystem.pubsub;

import java.util.function.Consumer;

/**
 * Consumer that exposes the type it expects to consume
 * @param <T> type expected to consume
 */
public interface TypedConsumer <T> extends Consumer<T> {
    Class<T> getType();
}
