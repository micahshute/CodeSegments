public class Test {


    public static void main(String[] args){
        ComplexNumber num = new ComplexNumber(3,5);
        System.out.println(num);
        ComplexNumber num2 = new ComplexNumber(4,2);
        System.out.println(num2);
        System.out.println(ComplexNumber.BinaryOperation.ADD.getOperationFunction().apply(num,num2).get());
        System.out.println(ComplexNumber.BinaryOperation.SUBTRACT.getOperationFunction().apply(num,num2).get());
        System.out.println("num Magnitude");
        System.out.println(num.getMagnitude());
        System.out.println("num angle degress, radians");
        System.out.println(num.getAngle(true));
        System.out.println(num.getAngle(false));
        System.out.println("num2 Magnitude");
        System.out.println(num2.getMagnitude());
        System.out.println("num2 angle degress, radians");
        System.out.println(num2.getAngle(true));
        System.out.println(num2.getAngle(false));

        ComplexNumber mult = ComplexNumber.BinaryOperation.MULTIPLY.getOperationFunction().apply(num,num2).get();
        System.out.println("Magnitude " + mult.getMagnitude() + " Angle " + mult.getAngleInDegrees() + " Real " + mult.getReal() + " Imaginary " + mult.getImaginary());
        System.out.println(ComplexNumber.BinaryOperation.DIVIDE.getOperationFunction().apply(num,num2).get());

        System.out.println(mult);
    }
}
