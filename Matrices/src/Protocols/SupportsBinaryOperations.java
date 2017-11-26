package Protocols;

//import java.util.function.*;
import java.util.Optional;

public interface SupportsBinaryOperations<T> {

    Optional<T> performBinary(FailableBinaryOperator<T> operation, T onSelfTo);

    Optional<T> performBinary(FailableBinaryOperator<T> operation, T lhs, T rhs);

    Optional<T> performBinary(SupportedOperationsEnumeration<FailableBinaryOperator<T>> supportedOperation, T onSelfTo);

    Optional<T> performBinary(SupportedOperationsEnumeration<FailableBinaryOperator<T>> supportedOperation, T lhs, T rhs);

//    FailableBinaryOperator<T> getSupportedBinaryOperation(SupportedOperationsEnumeration<FailableBinaryOperator<T>> supportedOperation);

}
