package Protocols;

import java.util.Optional;

public interface SupportsUnaryOperations<T extends SupportsBinaryOperations<T>> {

    Optional<T> performUnary(FailableUnaryOperator<T> operation);

    Optional<T> performUnary(FailableUnaryOperator<T> operation, T onObject);

    Optional<T> performUnary(SupportedOperationsEnumeration<FailableUnaryOperator<T>> operation);

    Optional<T> performUnary(SupportedOperationsEnumeration<FailableUnaryOperator<T>> operation, T onObject);

    FailableUnaryOperator getSupportedUnaryOperation(SupportedOperationsEnumeration<FailableUnaryOperator<T>> supportedOperation);
}
