
package Matrices;

import java.util.*;
import Protocols.*;



public class MatrixDouble implements Equatable, SupportsBinaryOperations<MatrixDouble>, UniversalMultiplier, SupportsUniversalMultiplication{

    public static MatrixDouble getZeroValueInstance(){
        return new MatrixDouble(0);
    }

    public static MatrixDouble getUnityValueInstance(){
        return new MatrixDouble(1);
    }

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


    public static MatrixDouble[][] convertToMatrixDoubleArray(double[][] data){
        MatrixDouble[][] mdArray = new MatrixDouble[data.length][];
        for(int i = 0; i < data.length; i++){
            for(int j = 0; j < data[i].length; j++){
                mdArray[i][j] = new MatrixDouble(data[i][j]);
            }
        }

        return mdArray;
    }

    public static MatrixDouble[][] convertToMatrixDoubleArray(Double[][] data){
        MatrixDouble[][] mdArray = new MatrixDouble[data.length][];
        for(int i = 0; i < data.length; i++){
            for(int j = 0; j < data[i].length; j++){
                mdArray[i][j] = new MatrixDouble(data[i][j]);
            }
        }

        return mdArray;
    }

    public static MatrixDouble[][] convertToMatrixDoubleArray(int[][] data){
        MatrixDouble[][] mdArray = new MatrixDouble[data.length][];
        for(int i = 0; i < data.length; i++){
            for(int j = 0; j < data.length; j++){
                mdArray[i][j] = new MatrixDouble(data[i][j]);
            }
        }
        return mdArray;
    }

    public static MatrixDouble[][] convertToMatrixDoubleArray(Integer[][] data){
        MatrixDouble[][] mdArray = new MatrixDouble[data.length][];
        for(int i = 0; i < data.length; i++){
            for(int j = 0; j < data.length; j++){
                mdArray[i][j] = new MatrixDouble(data[i][j]);
            }
        }
        return mdArray;
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
        target.multiplySelfBy(this.getRawValues());
    }

    @Override
    public Optional<MatrixDouble> add(MatrixDouble toSelf) {
        MatrixDouble mDouble;
        if(!(toSelf instanceof MatrixDouble)){
            return Optional.empty();
        }
        mDouble = (MatrixDouble)toSelf;
        return Optional.of(new MatrixDouble(this.getValue() + mDouble.getValue()));
    }

    @Override
    public Optional<MatrixDouble> subtract(MatrixDouble toSelf) {
        MatrixDouble mDouble;
        if(!(toSelf instanceof MatrixDouble)){
            return Optional.empty();
        }
        mDouble = (MatrixDouble)toSelf;
        return Optional.of(new MatrixDouble(this.getValue() - mDouble.getValue()));
    }

    @Override
    public Optional<MatrixDouble> multiply(MatrixDouble toSelf) {
        MatrixDouble mDouble;
        if(!(toSelf instanceof MatrixDouble)){
            return Optional.empty();
        }
        mDouble = (MatrixDouble)toSelf;
        return Optional.of(new MatrixDouble(this.getValue() * mDouble.getValue()));
    }

    @Override
    public Optional<MatrixDouble> multiply(UniversalMultiplier toSelf) {
        if(toSelf.getRawValues().length > 1){
            return Optional.empty();
        }else{
            return Optional.of(new MatrixDouble(toSelf.getRawValues()[0] * this.getValue().doubleValue()));
        }
    }

    @Override
    public Optional<MatrixDouble> divide(MatrixDouble toSelf) {
        MatrixDouble mDouble;
        if(!(toSelf instanceof MatrixDouble)){
            return Optional.empty();
        }
        mDouble = (MatrixDouble)toSelf;
        return Optional.of(new MatrixDouble(this.getValue() / mDouble.getValue()));
    }

    @Override
    public double[] getRawValues() {
        double[] value = new double[0];
        value[0] = getValue();
        return value;
    }

    @Override
    public void multiplySelfBy(double[] multiplier) {
        for(double val : multiplier){
            value *= val;
        }
    }


    @Override
    public MatrixDouble zeroValueInstance() {
        return new MatrixDouble(0);
    }

    @Override
    public MatrixDouble unityValueInstance() {
        return new MatrixDouble(1);
    }
}
