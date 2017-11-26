
package Matrices;

import java.util.*;
import Protocols.*;



class MatrixDouble implements Equatable, SupportsBinaryOperations<MatrixDouble>, UniversalMultiplier{

    private Double value;

    public MatrixDouble(double value){
        this.value = value;
    }

    public MatrixDouble(Double value){
        this.value = value;
    }

    public MatrixDouble(int value){
        this((double)value);
    }

    public MatrixDouble(Integer value){
        this(value.intValue());

    }

    public Double getValue() {
        return value;
    }

    public void setValue(Double value){
        this.value = value;
    }

    private enum SupportedBinaryOperations implements SupportedOperationsEnumeration<FailableBinaryOperator<MatrixDouble>>{

        ADD("+", (lhs,rhs) -> {
            return Optional.of((new MatrixDouble((lhs.getValue().doubleValue() + rhs.getValue().doubleValue()))));
        }),
        SUBTRACT("-", (lhs,rhs) ->{
            return Optional.of(new MatrixDouble(lhs.getValue().doubleValue() - rhs.getValue().doubleValue()));
        }),
        MULTIPLY("*", (lhs,rhs) -> {
            return Optional.of(new MatrixDouble(lhs.getValue().doubleValue() * rhs.getValue().doubleValue()));
        }),
        DIVIDE("/", (lhs,rhs) -> {
            return Optional.of(new MatrixDouble(lhs.getValue().doubleValue() / rhs.getValue().doubleValue()));
        });


        private final String symbol;
        private final FailableBinaryOperator<MatrixDouble> operator;

        private SupportedBinaryOperations(String symbol, FailableBinaryOperator<MatrixDouble> operator){
            this.symbol = symbol;
            this.operator = operator;
        }

        public FailableBinaryOperator<MatrixDouble> getOperationFunction(){
            return operator;
        }

        @Override
        public String getSymbol() {
            return symbol;
        }
    }


    @Override
    public boolean equals(Equatable otherObj){

        MatrixDouble equalTo;
        if(!(otherObj instanceof MatrixDouble)){
            return false;
        }else{
            equalTo = (MatrixDouble)otherObj;
        }

        return (this.getValue().doubleValue() == equalTo.getValue().doubleValue());
    }

    @Override
    public Optional<MatrixDouble> performBinary(SupportedOperationsEnumeration<FailableBinaryOperator<MatrixDouble>> supportedOperation, MatrixDouble onSelfTo) {
        return supportedOperation.getOperationFunction().apply(this,onSelfTo);
    }

    @Override
    public Optional<MatrixDouble> performBinary(FailableBinaryOperator<MatrixDouble> operation, MatrixDouble onSelfTo) {
        return operation.apply(this,onSelfTo);
    }

    @Override
    public Optional<MatrixDouble> performBinary(FailableBinaryOperator<MatrixDouble> operation, MatrixDouble lhs, MatrixDouble rhs) {
        return operation.apply(lhs,rhs);
    }

    @Override
    public Optional<MatrixDouble> performBinary(SupportedOperationsEnumeration<FailableBinaryOperator<MatrixDouble>> supportedOperation, MatrixDouble lhs, MatrixDouble rhs) {
        return supportedOperation.getOperationFunction().apply(lhs,rhs);
    }

    @Override
    public void multiplyWith(SupportsUniversalMultiplication target) {
        target.multiplySelfBy(this);
    }

}
