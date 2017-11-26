
import Protocols.*;
import java.util.*;

public class ComplexNumber extends Vector implements SupportsBinaryOperations<ComplexNumber>{


    //MARK: Private methods

    private double real;
    private double imaginary;

    public ComplexNumber(double real, double imaginary){
        super(2, real, imaginary);
        this.real = real;
        this.imaginary = imaginary;
    }

    public ComplexNumber(double magnitude, Angle angle) throws IllegalArgumentException{
        super(2,Math.cos(angle.getRadian()) * magnitude, Math.sin(angle.getRadian()) * magnitude);

        if(magnitude < 0){
            throw new IllegalArgumentException("Magnitude cannot be negative");
        }

        real = dimensions[0];
        imaginary = dimensions[1];

    }




    //MARK: ENUMS

    protected enum BinaryOperation implements SupportedOperationsEnumeration<FailableBinaryOperator<ComplexNumber>>{

        ADD("+", (lhs, rhs) -> {
            double real = lhs.getReal() + rhs.getReal();
            double imaginary = lhs.getImaginary() + rhs.getImaginary();
            return Optional.of(new ComplexNumber(real, imaginary));
        }),

        SUBTRACT("-", (lhs, rhs) -> {
            double real = lhs.getReal() - rhs.getReal();
            double imaginary = lhs.getImaginary() - rhs.getImaginary();
            return Optional.of(new ComplexNumber(real, imaginary));

        }),

        MULTIPLY("*", (lhs, rhs) -> {
            double magnitude = lhs.getMagnitude() * rhs.getMagnitude();
            double angle = lhs.getAngleInDegrees() + rhs.getAngleInDegrees();
            return Optional.of(new ComplexNumber(magnitude, new Angle(angle, true)));
        }),

        DIVIDE("/", (lhs, rhs) -> {
            double magnitude = lhs.getMagnitude() / rhs.getMagnitude();
            double angle = lhs.getAngleInDegrees() - rhs.getAngleInDegrees();
            return Optional.of(new ComplexNumber(magnitude, new Angle(angle, true)));
        });

        private final String symbol;
        private final FailableBinaryOperator<ComplexNumber> operation;
        private BinaryOperation(String symbol, FailableBinaryOperator<ComplexNumber> operation){
            this.symbol = symbol;
            this.operation = operation;
        }

        @Override
        public FailableBinaryOperator<ComplexNumber> getOperationFunction(){
            return operation;
        }

        @Override
        public String getSymbol() {
            return symbol;
        }
    }


    //MARK: Getters

    public double getReal(){
        return this.real;
    }

    public double getImaginary(){
        return this.imaginary;
    }

    public double getAngle(boolean degreeMode){

        Angle angle = new Angle(degreeMode);

        if(real == 0.0){
            if(imaginary > 0.0){
                angle.setDegree(90.0);
                return angle.getAngle();
            }else if(imaginary < 0.0){
                angle.setDegree(-90.0);
                return angle.getAngle();
            }
        }else if(imaginary == 0.0){
            if(real > 0.0){
                angle.setDegree(0.0);
                return angle.getAngle();
            }else{
                angle.setDegree(180.0);
                return angle.getAngle();
            }
        }

        if(real > 0){
            angle.setRadian(Math.atan(imaginary/real));
            return angle.getAngle();
        }else{
            angle.setRadian(180 - Math.atan(imaginary/real));
            return angle.getAngle();
        }
    }


    //TODO: make abstract superclass Vector implement these methods instead

//    public double getMagnitude(){
//        return Math.sqrt((real*real) + (imaginary * imaginary));
//    }

    public double getAngle(){
        return getAngle(true);
    }

    public double getAngleInDegrees(){
        return getAngle(true);
    }

    public double getAngleInRadians(){
        return getAngle(false);
    }

    //MARK: Setters

    public void setReal(double real){
        this.real = real;
    }

    public void setImaginary(double imaginary){
        this.imaginary = imaginary;
    }

    //MARK: interface methods




    @Override
    public Optional<ComplexNumber> getInverse(){
        return Optional.of((new ComplexNumber(-1 * real, -1 * imaginary)));
    }

    @Override
    public boolean equals(Equatable complexNumberObject){

        ComplexNumber complexNumber;

        if(complexNumberObject instanceof ComplexNumber){
            complexNumber = (ComplexNumber)complexNumberObject;
        }else{
            return false;
        }

        return ((this.real == complexNumber.real) && (this.imaginary == complexNumber.imaginary));
    }

    @Override
    public void displaySelf(){
        System.out.println(this.toString());
    }

    @Override
    public String toString(){
        if(imaginary >= 0){
            return (real + " + j" + imaginary);
        }else{
            return (real + " - j" + imaginary);
        }
    }


    @Override
    public Optional<ComplexNumber> performBinary(FailableBinaryOperator<ComplexNumber> operation, ComplexNumber onSelfTo){

        return operation.apply(this,onSelfTo);
    }

    @Override
    public Optional<ComplexNumber> performBinary(FailableBinaryOperator<ComplexNumber> operation, ComplexNumber lhs, ComplexNumber rhs) {
        return operation.apply(lhs, rhs);

    }


    @Override
    public Optional<ComplexNumber> performBinary(SupportedOperationsEnumeration<FailableBinaryOperator<ComplexNumber>> supportedOperation, ComplexNumber onSelfTo) {
        return supportedOperation.getOperationFunction().apply(this, onSelfTo);
    }

    @Override
    public Optional performBinary(SupportedOperationsEnumeration<FailableBinaryOperator<ComplexNumber>> supportedOperation, ComplexNumber lhs, ComplexNumber rhs) {
        return supportedOperation.getOperationFunction().apply(lhs,rhs);
    }

//    @Override
    public FailableBinaryOperator<ComplexNumber> getSupportedBinaryOperation(SupportedOperationsEnumeration<FailableBinaryOperator<ComplexNumber>> supportedOperation) {
        return supportedOperation.getOperationFunction();
    }
}
