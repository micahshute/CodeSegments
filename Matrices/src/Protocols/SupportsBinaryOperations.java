package Protocols;

//import java.util.function.*;
import java.util.Optional;

public interface SupportsBinaryOperations<T extends SupportsBinaryOperations<T>> extends SupportsUniversalMultiplication, CanClone<T> {


    public abstract T unityValueInstance();

    public abstract T zeroValueInstance();


    Optional<T> performBinary(FailableBinaryOperator<T> operation, T onSelfTo);

    Optional<T> performBinary(FailableBinaryOperator<T> operation, T lhs, T rhs);

    Optional<T> performBinary(SupportedOperationsEnumeration<FailableBinaryOperator<T>> supportedOperation, T onSelfTo);

    Optional<T> performBinary(SupportedOperationsEnumeration<FailableBinaryOperator<T>> supportedOperation, T lhs, T rhs);

//    FailableBinaryOperator<T> getSupportedBinaryOperation(SupportedOperationsEnumeration<FailableBinaryOperator<T>> supportedOperation);

    Optional<T> add(T toSelf);
    Optional<T> subtract(T toSelf);
    Optional<T> multiply(T toSelf);
    Optional<T> multiply(UniversalMultiplier toSelf);
    Optional<T> divide(T toSelf);

    public String toString();
}
