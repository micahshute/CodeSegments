package Protocols;


import java.util.Optional;


public interface FailableUnaryOperator<T> {

    Optional<T> apply(T onObject);
}
