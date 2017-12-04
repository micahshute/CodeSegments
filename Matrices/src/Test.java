import MDEntities.ComplexNumber;
import Matrices.*;
import Protocols.*;
import java.util.*;

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
        System.out.println("Magnitude " + mult.getMagnitude() + " MDEntities.Angle " + mult.getAngleInDegrees() + " Real " + mult.getReal() + " Imaginary " + mult.getImaginary());
        System.out.println(ComplexNumber.BinaryOperation.DIVIDE.getOperationFunction().apply(num,num2).get());

        System.out.println(mult);
        double[][] values = {{1,2,3},
                            {4,5,6},
                            {7,8,9}};
        double[][] values1 = {{2,4,6},
                              {8,2,4},
                                {6,8,10}};
        Matrix matrix = new Matrix(values);
        Matrix matrix1 = new Matrix(values1);
        Matrix sum = Matrix.SupportedBinaryOperations.ADD.getOperationFunction().apply(matrix,matrix1).get();
        sum.displaySelf(2);


        List<List<ComplexNumber>> complexValues= new ArrayList<>();
        List<List<ComplexNumber>> complexValues1 = new ArrayList<>();
        List<ComplexNumber> valueHelper = new ArrayList<>();
        List<ComplexNumber> valueHelper1 = new ArrayList();

        valueHelper.add(new ComplexNumber(1,0));
        valueHelper.add(new ComplexNumber(1,1));
        valueHelper.add(new ComplexNumber(2,1));
        System.out.println(valueHelper);
        complexValues.add(new ArrayList<>(valueHelper));

        for(List<ComplexNumber> row : complexValues){
            for(ComplexNumber value : row){
                System.out.print("  ");
                value.displaySelf();
                System.out.print("  ");
            }
            System.out.println();
        }
        clear(valueHelper);
        System.out.println(valueHelper);

        valueHelper.add(new ComplexNumber(3,1));
        valueHelper.add(new ComplexNumber(2,3));
        valueHelper.add(new ComplexNumber(3,2));
        complexValues.add(new ArrayList<>(valueHelper));
        System.out.println(valueHelper);


        for(List<ComplexNumber> row : complexValues){
            for(ComplexNumber value : row){
                System.out.print("  ");
                value.displaySelf();
                System.out.print("  ");
            }
            System.out.println();
        }

        clear(valueHelper);
        System.out.println(valueHelper);

        valueHelper.add(new ComplexNumber(2,4));
        valueHelper.add(new ComplexNumber(4,6));
        valueHelper.add(new ComplexNumber(6,8));
        System.out.println(valueHelper);

        complexValues.add(new ArrayList<>(valueHelper));

        for(List<ComplexNumber> row : complexValues){
            for(ComplexNumber value : row){
                System.out.print("  ");
                value.displaySelf();
                System.out.print("  ");
            }
            System.out.println();
        }
        MDMatrix<ComplexNumber> complexMatrix = new MDMatrix<>(complexValues);
        complexMatrix.displaySelf();

        //Optional<MDMatrix<MDEntities.ComplexNumber>> summation = MDMatrix.SupportedBinaryOperations.ADD.getOperationFunction().apply(complexMatrix,complexMatrix);
        MDMatrix<ComplexNumber> summ = complexMatrix.add(complexMatrix).get();
        summ.displaySelf();
        Optional<MDMatrix<ComplexNumber>> difference = complexMatrix.subtract((complexMatrix));
        difference.ifPresent(MDMatrix::displaySelf);
        Optional<MDMatrix<ComplexNumber>> product = complexMatrix.multiply(complexMatrix);
        product.ifPresent(MDMatrix::displaySelf);
        MDMatrix<ComplexNumber> addTest = (MDMatrix<ComplexNumber>) MDMatrix.SupportedBinaryOperations.ADD.getOperationFunction().apply(complexMatrix,complexMatrix).get();
        addTest.displaySelf();
        MDMatrix<SupportsBinaryOperations> notherTest = (MDMatrix<SupportsBinaryOperations>) MDMatrix.SupportedBinaryOperations.MULTIPLY.getOperationFunction().apply(complexMatrix,complexMatrix).get();
        notherTest.displaySelf();
        MDMatrix<ComplexNumber> againTest = complexMatrix.createMatrixOfSameType(MDMatrix.SupportedBinaryOperations.ADD.getOperationFunction().apply(complexMatrix,complexMatrix).get()).get();

        MDMatrix<ComplexNumber> unaryTest = (MDMatrix<ComplexNumber>) MDMatrix.SupportedUnaryOperations.DETERMINANT.getOperationFunction().apply(complexMatrix).get();
        System.out.println(unaryTest);


    }

    static void clear(List list){
        int size = list.size();
        System.out.println("List size:  " + list.size());
        for(int i = 0; i < size; i++){
            System.out.println("i = " + i);
            list.remove(0);
            System.out.println("removed");
        }
    }
}
