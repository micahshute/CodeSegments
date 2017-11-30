package Matrices;

import Protocols.*;
import java.util.*;

public class MDMatrix<T extends SupportsBinaryOperations> implements Equatable, Invertable, Displayable, SupportsBinaryOperations<MDMatrix<T>>, SupportsUnaryOperations<MDMatrix<T>>, SupportsUniversalMultiplication {


    //MARK: Properties

    private List<List<T>> values;
    private int numberOfRows;
    private int numberOfColumns;
    private int rank;



    //MARK: Unary Operations Enum

    public enum SupportedUnaryOperations implements SupportedOperationsEnumeration<FailableUnaryOperator<MDMatrix<SupportsBinaryOperations>>>{



        DETERMINANT("det",(matrix) -> {

            if(matrix.getNumberOfRows() != matrix.getNumberOfColumns()){
                return Optional.empty();
            }

            return determinantFinder(matrix);

        }),

        TRANSPOSE("T",(matrix) -> {

            SupportsBinaryOperations[][] transposeValues = new SupportsBinaryOperations[matrix.getNumberOfRows()][matrix.getNumberOfColumns()];

            for(int row = 0; row< matrix.getNumberOfRows(); row++){
                for(int col = 0; col < matrix.getNumberOfColumns(); col++){
                    transposeValues[row][col] = matrix.getValues().get(col).get(row);
                }
            }

            return Optional.of(new MDMatrix<SupportsBinaryOperations>(transposeValues));
        }),


        MINOR("minor", (matrix) -> {
            if(matrix.getNumberOfColumns() != matrix.getNumberOfRows()){
                return Optional.empty();
            }

            SupportsBinaryOperations[][] minorMatrixValues = new SupportsBinaryOperations[matrix.getNumberOfRows()][matrix.getNumberOfColumns()];

            for(int row = 0; row < matrix.getNumberOfRows(); row++){
                for(int col = 0; col<matrix.getNumberOfColumns(); col++){
                    minorMatrixValues[row][col] = determinantFinder(minorMatrixForLocation(row,col,matrix).get()).get().getValues().get(0).get(0);
                }
            }

            return (Optional.of(new MDMatrix<SupportsBinaryOperations>(minorMatrixValues)));

        }),

        COFACTOR("cofactor", (matrix) -> {
            if(matrix.getNumberOfColumns() != matrix.getNumberOfRows()){
                return Optional.empty();
            }

            List<List<SupportsBinaryOperations>> cofactorMatrixValues = new ArrayList<List<SupportsBinaryOperations>>(); //([matrix.getNumberOfRows()],[matrix.getNumberOfColumns()];
            List<List<SupportsBinaryOperations>> minorMatrixValues = SupportedUnaryOperations.MINOR.getOperationFunction().apply(matrix).get().getValues();

            for(int row = 0; row < matrix.getNumberOfRows(); row++){
                for(int col = 0; col < matrix.getNumberOfColumns(); col++){

                    MatrixDouble negateOrNot = ((row + col) % 2 == 0) ? new MatrixDouble(1) : new MatrixDouble(-1);
                    minorMatrixValues.get(row).get(col).multiplySelfBy(negateOrNot.getRawValues());
                    cofactorMatrixValues.get(row).add(col, minorMatrixValues.get(row).get(col)); //= minorMatrixValues[row][col];
                }
            }

            return Optional.of(new MDMatrix<SupportsBinaryOperations>(cofactorMatrixValues));

        }),

        ADJOINT("adj", (matrix) -> {

            if(matrix.getNumberOfColumns() != matrix.getNumberOfRows()){
                return Optional.empty();
            }

            return SupportedUnaryOperations.TRANSPOSE.getOperationFunction().apply(SupportedUnaryOperations.COFACTOR.getOperationFunction().apply(matrix).get());

        }),



        INVERSE("-1", (matrix) -> {
            if(matrix.getNumberOfRows() != matrix.getNumberOfColumns()){
                return Optional.empty();
            }
            //TODO: Check isPresent() prior to .get() commands
            SupportsBinaryOperations determinant = SupportedUnaryOperations.DETERMINANT.getOperationFunction().apply(matrix).get().getValues().get(0).get(0);
            List<List<SupportsBinaryOperations>> adjointToInverse = SupportedUnaryOperations.ADJOINT.getOperationFunction().apply(matrix).get().getValues();

            for(int row = 0; row < matrix.getNumberOfRows(); row++){
                for(int col = 0; col < matrix.getNumberOfColumns(); col++){
                    adjointToInverse.get(row).get(col).divide(determinant).ifPresent(x -> System.out.println(x));
                    //adjointToInverse[row][col] = adjointToInverse[row][col].divide(determinant);
                    //adjointToInverse.get(row).add(col, adjointToInverse.get(row).get(col).divide(determinant));
                }
            }
            return Optional.empty();
            //return new MDMatrix<SupportsBinaryOperations>(adjointToInverse);


        });




        private final String symbol;
        private final FailableUnaryOperator<MDMatrix<SupportsBinaryOperations>> operator;

        private SupportedUnaryOperations(String symbol, FailableUnaryOperator<MDMatrix<SupportsBinaryOperations>> operator){

            this.symbol = symbol;
            this.operator = operator;

        }

        //private enum methods

        private static Optional<MDMatrix<SupportsBinaryOperations>> minorMatrixForLocation(int row, int col, MDMatrix<SupportsBinaryOperations> matrix){
            if((row > matrix.getNumberOfRows() - 1) || (col > matrix.getNumberOfColumns() - 1)){
                return Optional.empty();
            }

            //SupportsBinaryOperations[][] minorValues = new SupportsBinaryOperations[matrix.getNumberOfRows() -1][matrix.getNumberOfColumns() - 1];
            List<List<SupportsBinaryOperations>> minorValues = new ArrayList<List<SupportsBinaryOperations>>();
            boolean rowPassed = false;
            boolean colPassed = false;

            for(int minorRow = 0; minorRow < matrix.getNumberOfRows() - 1; minorRow++){
                for(int minorCol = 0; minorCol < matrix.getNumberOfColumns() - 1; minorCol++){
                    int parentRow = 0;
                    int parentCol = 0;

                    if((minorRow == row) || (rowPassed)){
                        parentRow = minorRow + 1;
                        rowPassed = true;
                    }else{
                        parentRow = minorRow;
                    }

                    if((minorCol == col) || (colPassed)){
                        parentCol = minorCol + 1;
                        colPassed = true;
                    }else{
                        parentCol = minorCol;
                    }

                    minorValues.get(minorRow).add(minorCol, matrix.getValues().get(parentRow).get(parentCol));
                    //minorValues[minorRow][minorCol] = matrix.getValues()[parentRow][parentCol];

                }
                colPassed = false;
            }

            return matrix.createMatrixOfSameType(new MDMatrix<SupportsBinaryOperations>(minorValues));
        }


        private static Optional<MDMatrix<SupportsBinaryOperations>> determinantFinder(MDMatrix<SupportsBinaryOperations> matrix){


            if(matrix.getNumberOfColumns() == 2){
//                SupportsBinaryOperations[][] answer = new SupportsBinaryOperations[1][1];
//                Optional<SupportsBinaryOperations<? extends SupportsBinaryOperations>> multipleOne = matrix.getValues()[0][0].multiply(matrix.getValues()[1][1]);
//                Optional<SupportsBinaryOperations<? extends SupportsBinaryOperations>> multipleTwo = matrix.getValues()[0][1].multiply(matrix.getValues()[1][0]);

                List<List<SupportsBinaryOperations>> answer = new ArrayList<>();
                Optional<SupportsBinaryOperations> multipleOne = matrix.getValues().get(0).get(0).multiply(matrix.getValues().get(1).get(1));
                Optional<SupportsBinaryOperations> multipleTwo = matrix.getValues().get(0).get(1).multiply(matrix.getValues().get(1).get(0));
                if(multipleOne.isPresent()  && multipleTwo.isPresent() && multipleOne.get().subtract(multipleTwo.get()).isPresent()) {
                    answer.get(0).add(0,(SupportsBinaryOperations) multipleOne.get().subtract(multipleTwo.get()).get());
                    return (Optional.of(new MDMatrix<SupportsBinaryOperations>(answer)));
                }else{
                    return Optional.empty();
                }
            }else{

                //Not very elegant, but ensure that whatever object is in getValues initializes determinant with a "zero value instance" of that object type
                //Would be nice to implement a static method, but not sure how to do that using interfaces and generics in this situation.

                //SupportsBinaryOperations determinant = matrix.getValues()[0][0].zeroValueInstance();
                SupportsBinaryOperations determinant = matrix.getValues().get(0).get(0).zeroValueInstance();
                for(int column = 0; column < matrix.getNumberOfColumns(); column++){
                    MDMatrix<SupportsBinaryOperations> minorMatrix = minorMatrixForLocation(0,column,matrix).get();
                    MatrixDouble matrixDouble = new MatrixDouble(Math.pow(-1,(column)));
                    Optional<SupportsBinaryOperations> multipliedValues;
                    //multipliedValues = matrix.getValues()[0][column].multiply(determinantFinder(minorMatrix).get().getValues()[0][0]);
                    multipliedValues = matrix.getValues().get(0).get(column).multiply(determinantFinder(minorMatrix).get().getValues().get(0).get(0));
                    if(!multipliedValues.isPresent()){ return Optional.empty(); }

                    //Also not very elegant or consistent with other interface methods ie .add or .multiply, but in order to return the type I would not be able to
                    //not have UniversalMultiplier be without generics use. Can use .get because add
                    multipliedValues.get().multiplySelfBy(matrixDouble.getRawValues());
                    if(!determinant.add(multipliedValues.get()).isPresent()){return Optional.empty();}
                    determinant = (SupportsBinaryOperations)determinant.add(multipliedValues.get()).get();
                }

                SupportsBinaryOperations[][] determinantValue = new SupportsBinaryOperations[1][1];
                determinantValue[0][0] =  determinant;
                return matrix.createMatrixOfSameType(new MDMatrix<SupportsBinaryOperations>(determinantValue));

            }


        }


        @Override
        public FailableUnaryOperator<MDMatrix<SupportsBinaryOperations>> getOperationFunction() {
            return operator;
        }

        @Override
        public String getSymbol() {
            return symbol;
        }
    }


    //MARK: Binary Operations Enum

    public enum SupportedBinaryOperations implements SupportedOperationsEnumeration<FailableBinaryOperator<MDMatrix<? extends SupportsBinaryOperations>>>{

        ADD("+", (lhs, rhs) -> {

            if(!lhs.dimensionsAreEqual(rhs)){
                return Optional.empty();
            }

            List<List<SupportsBinaryOperations>> newArrayValues = new ArrayList<>();

            for(int rowNumber = 0; rowNumber < lhs.getNumberOfRows(); rowNumber++){
                for(int columnNumber = 0; columnNumber < lhs.getNumberOfColumns(); columnNumber++){
                    if(lhs.getValues().get(rowNumber).get(columnNumber).add(rhs.getValues().get(rowNumber).get(columnNumber)).isPresent()){
                        newArrayValues.get(rowNumber).add(columnNumber, (SupportsBinaryOperations) lhs.getValues().get(rowNumber).get(columnNumber).add(rhs.getValues().get(rowNumber).get(columnNumber)).get());
                    }else{
                        return Optional.empty();
                    }

                }
            }

//            Class<? extends SupportsBinaryOperations[][]> valueType = lhs.getValues().getClass();
//            valueType.cast(newArrayValues);
            //Class<? extends SupportsBinaryOperations> singleClassType = lhs.getValues()[0][0].getClass();

            return Optional.of(new MDMatrix<SupportsBinaryOperations>(newArrayValues));
        }),

        SUBTRACT("-", (lhs, rhs) -> {

            if(!lhs.dimensionsAreEqual(rhs)){
                return Optional.empty();
            }

            List<List<SupportsBinaryOperations>> newArrayValues = new ArrayList<>();

            for(int rowNumber = 0; rowNumber < lhs.getNumberOfRows(); rowNumber++){
                for(int columnNumber = 0; columnNumber < lhs.getNumberOfColumns(); columnNumber++){
                    if(lhs.getValues().get(rowNumber).get(columnNumber).add(rhs.getValues().get(rowNumber).get(columnNumber)).isPresent()){
                        newArrayValues.get(rowNumber).add(columnNumber, (SupportsBinaryOperations) lhs.getValues().get(rowNumber).get(columnNumber).subtract(rhs.getValues().get(rowNumber).get(columnNumber)).get());
                    }else{
                        return Optional.empty();
                    }
                }
            }
            return Optional.of(new MDMatrix<SupportsBinaryOperations>(newArrayValues));

            //TODO: take away ? extends SupportsBinaryOperators for this entire enum & replace with just SupportsBinaryOperations and try it with this line underneath
            //return lhs.createMatrixOfSameType(new MDMatrix<SupportsBinaryOperations>(newArrayValues));

        }),

        MULTIPLY("*", (lhs, rhs) -> {


            if(lhs.getNumberOfColumns() != rhs.getNumberOfRows()){
                return Optional.empty();
            }

            List<List<SupportsBinaryOperations>> newArrayValues = new ArrayList<>();

            for(int lhsAnsRow = 0; lhsAnsRow < lhs.getNumberOfRows(); lhsAnsRow++) {
                for (int rhsAnsColumn = 0; rhsAnsColumn < rhs.getNumberOfColumns(); rhsAnsColumn++){
                    for (int lhsColumnRhsRow = 0; lhsColumnRhsRow < lhs.getNumberOfColumns(); lhsColumnRhsRow++) {
                        newArrayValues.get(lhsAnsRow).add(rhsAnsColumn, (SupportsBinaryOperations) newArrayValues.get(lhsAnsRow).get(rhsAnsColumn).add((SupportsBinaryOperations) lhs.getValues().get(lhsAnsRow).get(lhsColumnRhsRow).multiply(rhs.getValues().get(lhsColumnRhsRow).get(rhsAnsColumn)).get()).get());
                    }
                }
            }

            return Optional.of(new MDMatrix<SupportsBinaryOperations>(newArrayValues));
        });


        private final String symbol;
        private final FailableBinaryOperator<MDMatrix<? extends SupportsBinaryOperations>> operation;
        private SupportedBinaryOperations(String symbol, FailableBinaryOperator<MDMatrix<? extends SupportsBinaryOperations>> operation){
            this.symbol = symbol;
            this.operation = operation;

        }

        @Override
        public String getSymbol() {
            return symbol;
        }

        @Override
        public FailableBinaryOperator<MDMatrix<? extends SupportsBinaryOperations>> getOperationFunction() {
            return operation;
        }
    }





    /* MARK: Constructor */

    public MDMatrix(T[][] twoDArray) throws IllegalArgumentException{

        int rowSize = 0;
        boolean firstRow = true;

        for(T[] row : twoDArray){

            if(firstRow){
                rowSize = row.length;
                firstRow = false;
                continue;
            }

            if(row.length != rowSize){
                throw new IllegalArgumentException();
            }
        }

        List<T> rowValues = new ArrayList<>();
        List<List<T>> rows = new ArrayList<>();

        for(T[] row : twoDArray){
            for(T value : row){
                rowValues.add(value);
            }
            rows.add(rowValues);
            for(int i = 0; i< rowValues.size(); i++){
                rowValues.remove(0);
            }
        }
        this.values = rows;
        this.numberOfRows = twoDArray.length;
        this.numberOfColumns = rowSize;
        this.rank = 0; //TODO: Fix this line

    }

    public MDMatrix(T... array) throws IllegalArgumentException{
        List<List<T>> row = new ArrayList<>();
        List<T> list = new ArrayList<>();

        for(T value : array){
            list.add(value);
        }

        row.add(list);

        this.values = row;
        this.numberOfRows = 1;
        this.numberOfColumns = list.size();
        this.rank = 0;
    }

    public MDMatrix(List<List<T>> twoDList) throws IllegalArgumentException{
        int rowSize = 0;
        boolean firstRow = true;

        for(List<T> row : twoDList){
            if(firstRow){
                rowSize = row.size();
                firstRow = false;
                continue;
            }

            if(row.size() != rowSize){
                throw new IllegalArgumentException();
            }
        }

        this.values = twoDList;
        this.numberOfRows = twoDList.size();
        this.numberOfColumns = rowSize;
        this.rank = 0;

    }





    //MARK: Getters

    public int getNumberOfColumns() {
        return numberOfColumns;
    }


    public int getNumberOfRows(){
        return numberOfRows;
    }


    public List<List<T>> getValues(){
        return values;
    }


    public Optional<List<T>> getValuesForRow(int row){

        Optional<List<T>> rowValues;

        try{
            rowValues = Optional.of(values.get(row));
        }catch(ArrayIndexOutOfBoundsException outOfBoundsException){
            rowValues = Optional.empty();
        }
        return rowValues;
    }




    public Optional<List<T>> getValuesForColumn(int column){

        if(column >= numberOfColumns){
            return Optional.empty();
        }

        List<T> columnValues = new ArrayList<>();
        int counter = 0;

        for(List<T> rowValues : values){
            columnValues.add(counter, rowValues.get(column));
            counter += 1;
        }


        return Optional.of(columnValues);
    }




    //MARK: Interface Methods

//    public void displaySelf(){
//        displaySelf(1);
//    }

    public void displaySelfTest(){
        System.out.print("[  ");

        for(List<T> row : values){
            for(T value: row){
                System.out.print("   " + value + "   ");
            }
            System.out.println();
        }
    }

    public void displaySelf(){
        displaySelfTest();
    }
//    public void displaySelf(Integer numOfDecimalPlaces){
//
//        int largestValueLength = 0;
//        int smallestValueLength = 0;
//        boolean firstColumn = true;
//        String printString;
//
//        if(numOfDecimalPlaces >= 0){
//            printString = "%."+ numOfDecimalPlaces.toString() + "f";
//        }else{
//            printString = "%f";
//        }
//
//
//        for(T[] rows: this.values){
//            for(T value : rows) {
//
//                if(totalDisplayNumbers(value,numOfDecimalPlaces) > largestValueLength){
//                    largestValueLength = totalDisplayNumbers(value,numOfDecimalPlaces) ;
//                }
//                if(smallestValueLength == 0){
//                    smallestValueLength = largestValueLength;
//                }
//                if(totalDisplayNumbers(value,numOfDecimalPlaces) < smallestValueLength){
//                    smallestValueLength = totalDisplayNumbers(value,numOfDecimalPlaces);
//                }
//            }
//        }
//
//        for(int row = 0; row < this.numberOfRows; row++){
//
//            System.out.print("[  ");
//
//            for(int col = 0; col < this.numberOfColumns; col++){
//
//                if(!firstColumn){
//                    int biasLength = 0;
//                    if(values[row][col - 1] > smallestValueLength){
//                        biasLength = totalDisplayNumbers(values[row][col -1], numOfDecimalPlaces) - smallestValueLength;
//                    }
//                    for(int i = 0; i < (largestValueLength - biasLength )+ 2; i++){
//                        System.out.print(" ");
//                    }
//                }else {
//                    System.out.print(" ");
//                    firstColumn = false;
//                }
//                System.out.printf(printString , values[row][col]);
//            }
//            System.out.println("   ]");
//            firstColumn = true;
//        }
//    }
//
//    public boolean equals(Equatable equatableObject){
//
//        MDMatrix matrix;
//
//        if(equatableObject instanceof MDMatrix){
//            matrix = (MDMatrix)equatableObject;
//        }else{
//            return false;
//        }
//
//        if(!this.dimensionsAreEqual(matrix)){
//            return false;
//        }
//
//        for(int rowPosition = 0; rowPosition < this.getNumberOfRows(); rowPosition++){
//            for(int colPosition = 0; colPosition < this.getNumberOfColumns(); colPosition++){
//                if(this.getValues()[rowPosition][colPosition] != matrix.getValues()[rowPosition][colPosition]){
//                    return false;
//                }
//            }
//        }
//
//        return true;
//    }


    public Optional<MDMatrix<T>> getInverse(){
        SupportedOperationsEnumeration<FailableUnaryOperator<MDMatrix<SupportsBinaryOperations>>> requestedOperation = MDMatrix.SupportedUnaryOperations.INVERSE;
        //TODO: FIX THIS!!
        return Optional.empty();
        //return performUnary(requestedOperation, this);
    }

    @Override
    public Optional<MDMatrix<T>> performBinary(FailableBinaryOperator<MDMatrix<T>> operation, MDMatrix<T> onSelfTo) {
        //TODO: Fix this!
        return Optional.empty();
        //return operation.apply(this,onSelfTo);
    }

    @Override
    public Optional<MDMatrix<T>> performBinary(SupportedOperationsEnumeration<FailableBinaryOperator<MDMatrix<T>>> supportedOperation, MDMatrix<T> onSelfTo) {
        FailableBinaryOperator<MDMatrix<T>> operationToPerform = supportedOperation.getOperationFunction();
        return operationToPerform.apply(this,onSelfTo);
    }

    @Override
    public Optional<MDMatrix<T>> performBinary(FailableBinaryOperator<MDMatrix<T>> operation, MDMatrix<T> lhs, MDMatrix<T> rhs) {
       return operation.apply(lhs,rhs);

       //TODO: This line is for if the generic SupportsBinaryOperations type is used above
        // return lhs.createMatrixOfSameType(operation.apply(lhs,rhs));
    }

    @Override
    public Optional<MDMatrix<T>> performBinary(SupportedOperationsEnumeration<FailableBinaryOperator<MDMatrix<T>>> supportedOperation, MDMatrix<T> lhs, MDMatrix<T> rhs) {

        FailableBinaryOperator<MDMatrix<T>> operationToPerform = supportedOperation.getOperationFunction();
        return operationToPerform.apply(lhs,rhs);
        //return lhs.createMatrixOfSameType(operationToPerform.apply(lhs,rhs).get());
    }

    //@Override
    public FailableBinaryOperator<MDMatrix<T>> getSupportedBinaryOperation(SupportedOperationsEnumeration<FailableBinaryOperator<MDMatrix<T>>> supportedOperation) {
        return supportedOperation.getOperationFunction();
    }

    @Override
    public Optional<MDMatrix<T>> performUnary(FailableUnaryOperator<MDMatrix<T>> operation) {

        return operation.apply(this);
    }

    @Override
    public Optional<MDMatrix<T>> performUnary(SupportedOperationsEnumeration<FailableUnaryOperator<MDMatrix<T>>> operation) {
        return operation.getOperationFunction().apply(this);
    }

    @Override
    public Optional<MDMatrix<T>> performUnary(FailableUnaryOperator<MDMatrix<T>> operation, MDMatrix<T> onObject) {
        return operation.apply(onObject);
    }

    @Override
    public Optional<MDMatrix<T>> performUnary(SupportedOperationsEnumeration<FailableUnaryOperator<MDMatrix<T>>> operation, MDMatrix<T> onObject) {
        return operation.getOperationFunction().apply(onObject);
    }

    @Override
    public FailableUnaryOperator getSupportedUnaryOperation(SupportedOperationsEnumeration<FailableUnaryOperator<MDMatrix<T>>> supportedOperation) {
        return supportedOperation.getOperationFunction();
    }

    @Override
    public MDMatrix<T> unityValueInstance() {
        T unityValue = this.getValues().get(0).get(0);
        unityValue = (T) unityValue.unityValueInstance();
        return new MDMatrix<T>(unityValue);
    }

    @Override
    public MDMatrix<T> zeroValueInstance() {
        T zeroValue = this.getValues().get(0).get(0);
        zeroValue = (T) zeroValue.zeroValueInstance();
        return new MDMatrix<T>(zeroValue);
    }

    @Override
    public Optional<MDMatrix<T>> add(MDMatrix<T> toSelf) {
        Optional<MDMatrix<T>> sum;
        Optional<MDMatrix<? extends SupportsBinaryOperations>> sumToCheck = MDMatrix.SupportedBinaryOperations.ADD.getOperationFunction().apply(this,toSelf);
        if(sumToCheck.isPresent()){
            sum = this.createMatrixOfSameType(sumToCheck.get());
        }else{
            sum = Optional.empty();
        }

        return sum;
    }

    @Override
    public Optional<MDMatrix<T>> subtract(MDMatrix<T> toSelf) {
        Optional<MDMatrix<? extends SupportsBinaryOperations>> differenceToCheck = SupportedBinaryOperations.SUBTRACT.getOperationFunction().apply(this, toSelf);
        Optional<MDMatrix<T>> difference;

        if(differenceToCheck.isPresent()){
            difference = this.createMatrixOfSameType(differenceToCheck.get());
        }else{
            difference = Optional.empty();
        }

        return difference;
    }

    @Override
    public Optional<MDMatrix<T>> multiply(MDMatrix<T> toSelf) {
        Optional<MDMatrix<? extends SupportsBinaryOperations>> productToCheck = SupportedBinaryOperations.MULTIPLY.getOperationFunction().apply(this, toSelf);
        Optional<MDMatrix<T>> product;
        if(productToCheck.isPresent()){
            product = this.createMatrixOfSameType(productToCheck.get());
        }else{
            product = Optional.empty();
        }
        return product;
    }

    @Override
    public Optional<MDMatrix<T>> multiply(UniversalMultiplier toSelf) {
        MDMatrix<T> tempMatrix = this;
        tempMatrix.multiplySelfBy(toSelf.getRawValues());
        return Optional.of(tempMatrix);
    }

    @Override
    public Optional<MDMatrix<T>> divide(MDMatrix<T> toSelf) {
        return Optional.empty();
    }

    @Override
    public boolean equals(Equatable toObject) {
        Optional<MDMatrix<SupportsBinaryOperations>> matrix = (toObject instanceof MDMatrix) ? Optional.of((MDMatrix<SupportsBinaryOperations>) toObject) : Optional.empty();
        if(matrix.isPresent()){
            if(matrix.get().getValues().get(0).get(0).getClass().equals(this.getValues().get(0).get(0).getClass())){
                Optional <MDMatrix<T>> sameTypeMatrix = this.createMatrixOfSameType(matrix.get());
                if(sameTypeMatrix.get().getValues().equals(this.getValues())){
                    return true;
                }
            }
        }

        return false;
    }


    //MARK: Public methods

    public boolean dimensionsAreEqual(MDMatrix comparedTo){
        if((this.getNumberOfRows() != comparedTo.getNumberOfRows()) || (this.getNumberOfColumns() != comparedTo.getNumberOfColumns())){
            return false;
        }else{
            return true;
        }
    }


    public boolean canMultiplyWith(Matrix multiplierMatrix) {
        if (this.getNumberOfColumns() != multiplierMatrix.getNumberOfRows()) {
            return false;
        } else {
            return true;
        }
    }


    public Optional<MDMatrix<T>> createMatrixOfSameType(MDMatrix<? extends SupportsBinaryOperations> genericMatrix){

        try{
            List<List<T>> newValues = new ArrayList<>();
            List<T> newRow = new ArrayList<>();
            for(List<? extends SupportsBinaryOperations> genericList : genericMatrix.getValues()){
                for(SupportsBinaryOperations genericValue : genericList){
                    newRow.add((T) genericValue);
                }
                newValues.add(newRow);
                for(int i = 0; i < newRow.size(); i++){
                    newRow.remove(0);
                }
            }

            return Optional.of(new MDMatrix<>(newValues));


        }catch(ClassCastException e){
            return Optional.empty();
        }

//        List<List<T>> newRows = new ArrayList<>();
//        List<T> newValues = new ArrayList<>();
//
//        for(SupportsBinaryOperations[] genericRow : genericMatrix.getValues()){
//            for(SupportsBinaryOperations genericValue : genericRow){
//
//                try {
//                    newValues.add((T) genericValue);
//                }catch (ClassCastException exception){
//                    return Optional.empty();
//                }
//            }
//
//            newRows.add(newValues);
//            for(int i = 0; i < newValues.size(); i++){
//                newValues.remove(0);
//            }
//        }
//
//        return new MDMatrix<T>(newRows);
    }


    //MARK: Private methods


    private int totalDisplayNumbers(double value){
        return numsBeforeDecimal(value) + numsAfterDecimal(value);
    }

    private int totalDisplayNumbers(double value, int withMaximumAfterDecimal){
        int correctedDecimalCount;
        if(numsAfterDecimal(value) > withMaximumAfterDecimal){
            correctedDecimalCount = withMaximumAfterDecimal;
        }else{
            correctedDecimalCount = withMaximumAfterDecimal;
        }

        if(withMaximumAfterDecimal < 0){
            return totalDisplayNumbers(value);
        }

        return numsBeforeDecimal(value) + correctedDecimalCount;
    }

    private int numsBeforeDecimal(double value){
        Double objectValue = value;
        String[] valueString = objectValue.toString().split("\\.");
        int lengthInteger = valueString[0].length();
        return lengthInteger;
    }

    private int numsAfterDecimal(double value){
        Double objectValue = value;
        String[] valueString = objectValue.toString().split("\\.");
        int lengthDecimal = valueString[1].length();
        return lengthDecimal;
    }


    @Override
    public void multiplySelfBy(double[] multiplier) {

        for(List<T> row : values){
            for(T value : row){
                value.multiplySelfBy(multiplier);
            }
        }

        return;
    }

}
