package Protocols;

import java.util.Optional;

public interface FailableBinaryOperator<T> {

    Optional<T> apply(T lhs, T rhs);


}
