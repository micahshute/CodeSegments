package MDEntities;

import Protocols.*;
import java.util.*;

public class ComplexNumber extends Vector implements SupportsBinaryOperations<ComplexNumber>{

    public static ComplexNumber getZeroValueInstance(){
        return new ComplexNumber(0,0);
    }

    public static ComplexNumber getUnityValueInstance(){
        return new ComplexNumber(1,0);
    }


    //MARK: Private methods

    private double real;
    private double imaginary;

    public ComplexNumber(double real, double imaginary){
        super(2, real, imaginary);
        this.real = round(real);
        this.imaginary = round(imaginary);
    }

    public ComplexNumber(double magnitude, Angle angle) throws IllegalArgumentException{
        super(2,(double) Math.round(Math.cos(angle.getRadian()) * magnitude * 100000) / 100000.0, (double) Math.round(Math.sin(angle.getRadian()) * magnitude * 100000) / 100000.0);

        if(magnitude < 0){
            throw new IllegalArgumentException("Magnitude cannot be negative");
        }

        real = dimensions[0];
        imaginary = dimensions[1];

    }




    //MARK: ENUMS

    public enum BinaryOperation implements SupportedOperationsEnumeration<FailableBinaryOperator<ComplexNumber>>{

        ADD("+", (lhs, rhs) -> {
            double real = round(lhs.getReal() + rhs.getReal());
            double imaginary = round(lhs.getImaginary() + rhs.getImaginary());
            return Optional.of(new ComplexNumber(real, imaginary));
        }),

        SUBTRACT("-", (lhs, rhs) -> {
            double real = round(lhs.getReal() - rhs.getReal());
            double imaginary = round(lhs.getImaginary() - rhs.getImaginary());
            return Optional.of(new ComplexNumber(real, imaginary));

        }),

        MULTIPLY("*", (lhs, rhs) -> {
            double magnitude = round(lhs.getMagnitude() * rhs.getMagnitude());
            double angle = round(lhs.getAngleInDegrees() + rhs.getAngleInDegrees());
            return Optional.of(new ComplexNumber(magnitude, new Angle(angle, true)));
        }),

        DIVIDE("/", (lhs, rhs) -> {
            double magnitude = round(lhs.getMagnitude() / rhs.getMagnitude());
            double angle = round(lhs.getAngleInDegrees() - rhs.getAngleInDegrees());
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

        private static double round(double number){
            return (double)Math.round(number * 100000) / 100000.0;
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
            angle.setRadian(Math.PI - Math.atan(imaginary/real));
            return angle.getAngle();
        }
    }


    //TODO: make abstract superclass MDEntities.Vector implement these methods instead

//    public double getMagnitude(){
//        return Math.sqrt((real*real) + (imaginary * imaginary));
//    }

    public double getAngle(){
        return round(getAngle(true));
    }

    public double getAngleInDegrees(){
        return round(getAngle(true));
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

    @Override
    public Optional<ComplexNumber> multiply(ComplexNumber toSelf) {

        Optional<ComplexNumber> complexNumber = (toSelf instanceof ComplexNumber) ? Optional.of((ComplexNumber)toSelf) : Optional.empty();

        if(complexNumber.isPresent()) {
            complexNumber = ComplexNumber.BinaryOperation.MULTIPLY.getOperationFunction().apply(this, complexNumber.get());
        }

        return complexNumber;
    }

    @Override
    public Optional<ComplexNumber> multiply(UniversalMultiplier toSelf) {

        Optional<ComplexNumber> complexNumber;
        double magnitude = this.getMagnitude();
        Angle angle = new Angle(this.getAngleInDegrees(), true);
        for(double rawValue : toSelf.getRawValues()){
            magnitude = magnitude * rawValue;
        }

        complexNumber = Optional.of(new ComplexNumber(magnitude, angle));

        return complexNumber;
    }

    @Override
    public Optional<ComplexNumber> add(ComplexNumber toSelf) {

        Optional<ComplexNumber> complexNumber = (toSelf instanceof ComplexNumber) ? Optional.of((ComplexNumber) toSelf) : Optional.empty();

        if(complexNumber.isPresent()){
            complexNumber = ComplexNumber.BinaryOperation.ADD.getOperationFunction().apply(this,complexNumber.get());
        }

        return complexNumber;
    }

    @Override
    public Optional<ComplexNumber> subtract(ComplexNumber toSelf) {
        Optional<ComplexNumber> complexNumber = (toSelf instanceof ComplexNumber) ? Optional.of((ComplexNumber)toSelf) : Optional.empty();

        if(complexNumber.isPresent()){
            complexNumber = BinaryOperation.SUBTRACT.getOperationFunction().apply(this, complexNumber.get());
        }

        return complexNumber;
    }

    @Override
    public Optional<ComplexNumber> divide(ComplexNumber toSelf) {

        Optional<ComplexNumber> complexNumber = (toSelf instanceof ComplexNumber) ? Optional.of((ComplexNumber) toSelf) : Optional.empty();

        if(complexNumber.isPresent()){
            complexNumber = BinaryOperation.DIVIDE.getOperationFunction().apply(this, complexNumber.get());
        }

        return complexNumber;
    }

    @Override
    public void multiplySelfBy(double[] multiplier) {

        Optional<ComplexNumber> complexNumber;
        double magnitude = this.getMagnitude();
        Angle angle = new Angle(this.getAngleInDegrees(), true);

        for(double rawValue : multiplier){
            if(rawValue < 0){
                angle.setDegree(angle.getDegree() + 180);
                rawValue = -1 * rawValue;
            }
            magnitude = magnitude * rawValue;
        }

        complexNumber = Optional.of(new ComplexNumber(magnitude, angle));
        this.setReal(complexNumber.get().getReal());
        this.setImaginary(complexNumber.get().getImaginary());


    }

    @Override
    public ComplexNumber zeroValueInstance() {
        return new ComplexNumber(0,0);
    }

    @Override
    public ComplexNumber unityValueInstance() {
        return new ComplexNumber(1,0);
    }

    private double round(double number){
        return (double)Math.round(number * 100000) / 100000.0;
    }
}
