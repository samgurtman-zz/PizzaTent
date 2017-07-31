package co.nz.pizzatent.drivermanagementsystem;

import java.util.function.Consumer;

public interface TypedConsumer <T> extends Consumer<T> {
    Class<T> getType();
}
