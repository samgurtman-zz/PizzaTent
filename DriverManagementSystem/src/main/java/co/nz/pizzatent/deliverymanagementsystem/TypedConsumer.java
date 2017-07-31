package co.nz.pizzatent.deliverymanagementsystem;

import java.util.function.Consumer;

public interface TypedConsumer <T> extends Consumer<T> {
    Class<T> getType();
}
