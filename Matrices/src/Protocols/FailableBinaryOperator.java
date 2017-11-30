package Protocols;

import java.util.Optional;

public interface FailableBinaryOperator<T extends SupportsBinaryOperations<? extends SupportsBinaryOperations>> extends BinaryOperator{
    //TODO: Change name to indicate optional return
    Optional<T> apply(T lhs, T rhs);



}
