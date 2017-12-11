package MDEntities;

import Protocols.*;
import java.util.*;

public abstract class Vector implements Displayable, Equatable, Invertable{

    int numOfDimensions;
    double[] dimensions;


    public Vector(int numOfDimensions, double... dimensions){

        if(numOfDimensions < 1){
            this.numOfDimensions = 1;
        }

        this.numOfDimensions = numOfDimensions;
        this.dimensions = new double[dimensions.length]
        ;
        for(int i = 0; i < dimensions.length; i++){
            this.dimensions[i] = dimensions[i];
        }
    }

    public double[] getDimensions(){
        return dimensions;
    }


    public double getMagnitude(){
        double sumOfSquares = 0;
        for(double dimension : dimensions){
            sumOfSquares += (dimension * dimension);
        }
        return round(Math.sqrt(sumOfSquares));
    }



    private double round(double number){
        return (double)Math.round(number * 100000) / 100000.0;
    }
//    protected enum BinaryOperation implements SupportedOperationsEnumeration<FailableBinaryOperator<MDEntities.Vector>>{
//
//        ADD("+", (lhs, rhs) -> {
//
//            if(lhs.numOfDimensions != rhs.numOfDimensions){
//                return Optional.empty();
//            }
//
//            double[] newDimensions = new double[lhs.numOfDimensions];
//
//            for(int i = 0; i < lhs.numOfDimensions; i++){
//                newDimensions[i] = lhs.dimensions[i] + rhs.dimensions[i];
//            }
//
//            return ;
//
//        }),
//
//        SUBTRACT("-", (lhs, rhs) -> {
//            double real = lhs.getReal() - rhs.getReal();
//            double imaginary = lhs.getImaginary() - rhs.getImaginary();
//            return Optional.of(new MDEntities.ComplexNumber(real, imaginary));
//
//        });
//
//        private final String symbol;
//        private final FailableBinaryOperator<MDEntities.Vector> operation;
//        private BinaryOperation(String symbol, FailableBinaryOperator<MDEntities.Vector> operation){
//            this.symbol = symbol;
//            this.operation = operation;
//        }
//
//        @Override
//        public FailableBinaryOperator<MDEntities.ComplexNumber> getOperationFunction(){
//            return operation;
//        }
//
//        @Override
//        public String getSymbol() {
//            return symbol;
//        }
//    }









//    public double getAngle(boolean degreeMode){
//
//        MDEntities.Angle angle = new MDEntities.Angle(degreeMode);
//
//        if(real == 0.0){
//            if(imaginary > 0.0){
//                angle.setDegree(90.0);
//                return angle.getAngle();
//            }else if(imaginary < 0.0){
//                angle.setDegree(-90.0);
//                return angle.getAngle();
//            }
//        }else if(imaginary == 0.0){
//            if(real > 0.0){
//                angle.setDegree(0.0);
//                return angle.getAngle();
//            }else{
//                angle.setDegree(180.0);
//                return angle.getAngle();
//            }
//        }
//
//        if(real > 0){
//            return Math.tan(imaginary/real);
//        }else{
//            angle.setDegree(180 - Math.tan(imaginary/real));
//            return angle.getAngle();
//        }
//    }
//

//
//    public double getAngle(){
//        return getAngle(true);
//    }
//
//    public double getAngleInDegrees(){
//        return getAngle(true);
//    }
//
//    public double getAngleInRadians(){
//        return getAngle(false);
//    }
//


//
//    public Optional<MDEntities.ComplexNumber> getInverse(){
//
//        return Optional.of((new MDEntities.ComplexNumber(-1 * real, -1 * imaginary)));
//    }

//
//    @Override
//    public boolean equals(Equatable complexNumberObject){
//
//        MDEntities.ComplexNumber complexNumber;
//
//        if(complexNumber instanceof MDEntities.ComplexNumber){
//            complexNumber = (MDEntities.ComplexNumber)complexNumberObject;
//        }else{
//            return false;
//        }
//
//        return ((this.real == complexNumber.real) && (this.imaginary == complexNumber.imaginary);
//    }


}
