//package Matrices;
//
//import Protocols.*;
//import sun.tools.tree.CastExpression;
//
//import java.util.Optional;
//
//public class MSMatrix implements Equatable, Invertable, Displayable, SupportsBinaryOperations<MSMatrix>, SupportsUnaryOperations<MSMatrix> {
//
//    //MARK: Properties
//
//    private SupportsBinaryOperations[][] values;
//    private int numberOfRows;
//    private int numberOfColumns;
//    private int rank;
//
//
//
//    //MARK: Unary Operations Enum
//
//    public enum SupportedUnaryOperations implements SupportedOperationsEnumeration<FailableUnaryOperator<Matrix>>{
//
//
//
//        DETERMINANT("det",(matrix) -> {
//
//            if(matrix.getNumberOfRows() != matrix.getNumberOfColumns()){
//                return Optional.empty();
//            }
//
//            return determinantFinder(matrix);
//
//        }),
//
//        TRANSPOSE("T",(matrix) -> {
//
//            double[][] transposeValues = new double[matrix.getNumberOfRows()][matrix.getNumberOfColumns()];
//
//            for(int row = 0; row< matrix.getNumberOfRows(); row++){
//                for(int col = 0; col < matrix.getNumberOfColumns(); col++){
//                    transposeValues[row][col] = matrix.getValues()[col][row];
//                }
//            }
//
//            return Optional.of(new Matrix(transposeValues));
//        }),
//
//
//        MINOR("minor", (matrix) -> {
//            if(matrix.getNumberOfColumns() != matrix.getNumberOfRows()){
//                return Optional.empty();
//            }
//
//            double[][] minorMatrixValues = new double[matrix.getNumberOfRows()][matrix.getNumberOfColumns()];
//
//            for(int row = 0; row < matrix.getNumberOfRows(); row++){
//                for(int col = 0; col<matrix.getNumberOfColumns(); col++){
//                    minorMatrixValues[row][col] = determinantFinder(minorMatrixForLocation(row,col,matrix).get()).get().getValues()[0][0];
//                }
//            }
//
//            return (Optional.of(new Matrix(minorMatrixValues)));
//
//        }),
//
//        COFACTOR("cofactor", (matrix) -> {
//            if(matrix.getNumberOfColumns() != matrix.getNumberOfRows()){
//                return Optional.empty();
//            }
//
//            double[][] cofactorMatrixValues = new double[matrix.getNumberOfRows()][matrix.getNumberOfColumns()];
//            double[][] minorMatrixValues = SupportedUnaryOperations.MINOR.getOperationFunction().apply(matrix).get().getValues();
//
//            for(int row = 0; row < matrix.getNumberOfRows(); row++){
//                for(int col = 0; col < matrix.getNumberOfColumns(); col++){
//                    cofactorMatrixValues[row][col] = minorMatrixValues[row][col] * Math.pow(-1,row+col);
//                }
//            }
//
//            return Optional.of(new Matrix(cofactorMatrixValues));
//
//        }),
//
//        ADJOINT("adj", (matrix) -> {
//
//            if(matrix.getNumberOfColumns() != matrix.getNumberOfRows()){
//                return Optional.empty();
//            }
//
//            return SupportedUnaryOperations.TRANSPOSE.getOperationFunction().apply(SupportedUnaryOperations.COFACTOR.getOperationFunction().apply(matrix).get());
//
//        }),
//
//
//
//        INVERSE("-1", (matrix) -> {
//            if(matrix.getNumberOfRows() != matrix.getNumberOfColumns()){
//                return Optional.empty();
//            }
//
//            double determinant = SupportedUnaryOperations.DETERMINANT.getOperationFunction().apply(matrix).get().getValues()[0][0];
//            double[][] adjointToInverse = SupportedUnaryOperations.ADJOINT.getOperationFunction().apply(matrix).get().getValues();
//
//            for(int row = 0; row < matrix.getNumberOfRows(); row++){
//                for(int col = 0; col < matrix.getNumberOfColumns(); col++){
//                    adjointToInverse[row][col] = adjointToInverse[row][col] / determinant;
//                }
//            }
//
//            return Optional.of(new Matrix(adjointToInverse));
//
//
//        });
//
//
//
//
//        private final String symbol;
//        private final FailableUnaryOperator<Matrix> operator;
//
//        private SupportedUnaryOperations(String symbol, FailableUnaryOperator<Matrix> operator){
//
//            this.symbol = symbol;
//            this.operator = operator;
//
//        }
//
//        //private enum methods
//
//        private static Optional<Matrix> minorMatrixForLocation(int row, int col, Matrix matrix){
//            if((row > matrix.getNumberOfRows() - 1) || (col > matrix.getNumberOfColumns() - 1)){
//                return Optional.empty();
//            }
//
//            double[][] minorValues = new double[matrix.getNumberOfRows() -1][matrix.getNumberOfColumns() - 1];
//            boolean rowPassed = false;
//            boolean colPassed = false;
//
//            for(int minorRow = 0; minorRow < matrix.getNumberOfRows() - 1; minorRow++){
//                for(int minorCol = 0; minorCol < matrix.getNumberOfColumns() - 1; minorCol++){
//                    int parentRow = 0;
//                    int parentCol = 0;
//
//                    if((minorRow == row) || (rowPassed)){
//                        parentRow = minorRow + 1;
//                        rowPassed = true;
//                    }else{
//                        parentRow = minorRow;
//                    }
//
//                    if((minorCol == col) || (colPassed)){
//                        parentCol = minorCol + 1;
//                        colPassed = true;
//                    }else{
//                        parentCol = minorCol;
//                    }
//
//                    minorValues[minorRow][minorCol] = matrix.getValues()[parentRow][parentCol];
//
//                }
//                colPassed = false;
//            }
//
//            return Optional.of(new Matrix(minorValues));
//        }
//
//
//        private static Optional<Matrix> determinantFinder(Matrix matrix){
//
//            if(matrix.getNumberOfColumns() == 2){
//                double[][] answer = new double[1][1];
//                answer[0][0] = (matrix.getValues()[0][0] * matrix.getValues()[1][1]) - (matrix.getValues()[0][1] * matrix.getValues()[1][0]);
//                return (Optional.of(new Matrix(answer)));
//            }else{
//                double determinant = 0;
//
//                for(int column = 0; column < matrix.getNumberOfColumns(); column++){
//                    Matrix minorMatrix = minorMatrixForLocation(0,column,matrix).get();
//                    determinant += Math.pow(-1,(column)) * matrix.getValues()[0][column] * determinantFinder(minorMatrix).get().getValues()[0][0];
//                }
//                double[][] determinantValue = new double[1][1];
//                determinantValue[0][0] =  determinant;
//                return Optional.of(new Matrix(determinantValue));
//
//            }
//        }
//
//
//        @Override
//        public FailableUnaryOperator<Matrix> getOperationFunction() {
//            return operator;
//        }
//
//        @Override
//        public String getSymbol() {
//            return symbol;
//        }
//    }
//
//
//    //MARK: Binary Operations Enum
//
//    public enum SupportedBinaryOperations implements SupportedOperationsEnumeration<FailableBinaryOperator<MSMatrix<SupportsBinaryOperations>>>{
//
//        ADD("+", (lhs, rhs) -> {
//
//
//            if(!lhs.dimensionsAreEqual(rhs)){
//                return Optional.empty();
//            }
//
//            SupportsBinaryOperations[][] newArrayValues = new SupportsBinaryOperations[lhs.getNumberOfRows()][lhs.getNumberOfColumns()];
//
//            for(int rowNumber = 0; rowNumber < lhs.getNumberOfRows(); rowNumber++){
//                for(int columnNumber = 0; columnNumber < lhs.getNumberOfColumns(); columnNumber++){
//                    if(lhs.getValues()[rowNumber][columnNumber].add(rhs.getValues()[rowNumber][columnNumber]).isPresent()) {
//                        newArrayValues[rowNumber][columnNumber] = (SupportsBinaryOperations)(lhs.getValues()[rowNumber][columnNumber]).add(rhs.getValues()[rowNumber][columnNumber]).get();
//                    }else{
//                        return Optional.empty();
//                    }
//                }
//            }
//
//            return Optional.of(new MSMatrix<SupportsBinaryOperations>(newArrayValues));
//        }),
//
//        SUBTRACT("-", (lhs, rhs) -> {
//
//            if(!lhs.dimensionsAreEqual(rhs)){
//                return Optional.empty();
//            }
//
//            double[][] newArrayValues = new double[lhs.getNumberOfRows()][lhs.getNumberOfColumns()];
//
//            for(int rowNumber = 0; rowNumber < lhs.getNumberOfRows(); rowNumber++){
//                for(int columnNumber = 0; columnNumber < lhs.getNumberOfColumns(); columnNumber++){
//                    newArrayValues[rowNumber][columnNumber] = lhs.getValues()[rowNumber][columnNumber] - rhs.getValues()[rowNumber][columnNumber];
//                }
//            }
//
//            return Optional.of(new Matrix(newArrayValues));
//
//        }),
//
//        MULTIPLY("*", (lhs, rhs) -> {
//
//
//            if(lhs.getNumberOfColumns() != rhs.getNumberOfRows()){
//                return Optional.empty();
//            }
//
//            double[][] newArrayValues = new double[lhs.getNumberOfRows()][rhs.getNumberOfColumns()];
//
//            for(int lhsAnsRow = 0; lhsAnsRow < lhs.getNumberOfRows(); lhsAnsRow++) {
//                for (int rhsAnsColumn = 0; rhsAnsColumn < rhs.getNumberOfColumns(); rhsAnsColumn++){
//                    for (int lhsColumnRhsRow = 0; lhsColumnRhsRow < lhs.getNumberOfColumns(); lhsColumnRhsRow++) {
//                        newArrayValues[lhsAnsRow][rhsAnsColumn] += (lhs.getValues()[lhsAnsRow][lhsColumnRhsRow] * rhs.getValues()[lhsColumnRhsRow][rhsAnsColumn]);
//                    }
//                }
//            }
//
//            return Optional.of(new Matrix(newArrayValues));
//        });
//
//
//        private final String symbol;
//        private final FailableBinaryOperator<MSMatrix<SupportsBinaryOperations>> operation;
//        private SupportedBinaryOperations(String symbol, FailableBinaryOperator<MSMatrix<SupportsBinaryOperations>> operation){
//            this.symbol = symbol;
//            this.operation = operation;
//
//        }
//
//        @Override
//        public String getSymbol() {
//            return symbol;
//        }
//
//        @Override
//        public FailableBinaryOperator<MSMatrix<SupportsBinaryOperations>> getOperationFunction() {
//            return operation;
//        }
//    }
//
//
//    /* MARK: Constructor */
//
//    public MSMatrix(SupportsBinaryOperations[][] twoDArray) throws IllegalArgumentException{
//
//        int rowSize = 0;
//        boolean firstRow = true;
//
//        for(T[] row : twoDArray){
//
//            if(firstRow){
//                rowSize = row.length;
//                firstRow = false;
//                continue;
//            }
//
//            if(row.length != rowSize){
//                throw new IllegalArgumentException();
//            }
//        }
//
//        this.values = twoDArray;
//        this.numberOfRows = twoDArray.length;
//        this.numberOfColumns = rowSize;
//        this.rank = 0; //TODO: Fix this line
//
//    }
//
//    public Matrix(){
//        this(new double[1][0]);
//    }
//
//
//
//    //MARK: Getters
//
//    public int getNumberOfColumns() {
//        return numberOfColumns;
//    }
//
//
//    public int getNumberOfRows(){
//        return numberOfRows;
//    }
//
//
//    public SupportsBinaryOperations[][] getValues(){
//
//        return (T[][]) values;
//    }
//
//    public Optional<T[]> getValuesForRow(int row){
//
//        Optional<T[]> rowValues;
//
//        try{
//            rowValues = Optional.of((T[]) values[row]);
//        }catch(ArrayIndexOutOfBoundsException outOfBoundsException){
//            rowValues = Optional.empty();
//        }catch(ClassCastException cast){
//            rowValues = Optional.empty();
//        }
//        return rowValues;
//    }
//
//
//
//
//    public Optional<T[]> getValuesForColumn(int column){
//
//        if(column >= numberOfColumns){
//            return Optional.empty();
//        }
//
//        SupportsBinaryOperations[] columnValues = new SupportsBinaryOperations[numberOfColumns];
//        int counter = 0;
//
//        for(SupportsBinaryOperations[] rowValues : values){
//            columnValues[counter] = rowValues[column];
//            counter += 1;
//        }
//
//        return Optional.of((T[])columnValues);
//    }
//
//
//
//
//    //MARK: Interface Methods
//
//    public void displaySelf(){
//        displaySelf(1);
//    }
//
//
//
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
//        for(SupportsBinaryOperations[] rows: this.values){
//            for(SupportsBinaryOperations value : rows) {
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
//        Matrix matrix;
//
//        if(equatableObject instanceof Matrix){
//            matrix = (Matrix)equatableObject;
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
//
//
//    public Optional<Matrix> getInverse(){
//
//        SupportedOperationsEnumeration<FailableUnaryOperator<Matrix>> requestedOperation = Matrix.SupportedUnaryOperations.INVERSE;
//        return performUnary(requestedOperation, this);
//
//    }
//
//    @Override
//    public Optional<Matrix> performBinary(FailableBinaryOperator<Matrix> operation, Matrix onSelfTo) {
//        return operation.apply(this,onSelfTo);
//    }
//
//    @Override
//    public Optional<Matrix> performBinary(SupportedOperationsEnumeration<FailableBinaryOperator<Matrix>> supportedOperation, Matrix onSelfTo) {
//        FailableBinaryOperator<Matrix> operationToPerform = supportedOperation.getOperationFunction();
//        return operationToPerform.apply(this,onSelfTo);
//    }
//
//    @Override
//    public Optional<Matrix> performBinary(FailableBinaryOperator<Matrix> operation, Matrix lhs, Matrix rhs) {
//        return operation.apply(lhs,rhs);
//    }
//
//    @Override
//    public Optional<Matrix> performBinary(SupportedOperationsEnumeration<FailableBinaryOperator<Matrix>> supportedOperation, Matrix lhs, Matrix rhs) {
//        FailableBinaryOperator<Matrix> operationToPerform = supportedOperation.getOperationFunction();
//        return operationToPerform.apply(lhs,rhs);
//    }
//
//    //@Override
//    public FailableBinaryOperator<Matrix> getSupportedBinaryOperation(SupportedOperationsEnumeration<FailableBinaryOperator<Matrix>> supportedOperation) {
//        return supportedOperation.getOperationFunction();
//    }
//
//    @Override
//    public Optional<Matrix> performUnary(FailableUnaryOperator<Matrix> operation) {
//
//        return operation.apply(this);
//    }
//
//    @Override
//    public Optional<Matrix> performUnary(SupportedOperationsEnumeration<FailableUnaryOperator<Matrix>> operation) {
//        return operation.getOperationFunction().apply(this);
//    }
//
//    @Override
//    public Optional<Matrix> performUnary(FailableUnaryOperator operation, Matrix onObject) {
//        return operation.apply(onObject);
//    }
//
//    @Override
//    public Optional<Matrix> performUnary(SupportedOperationsEnumeration<FailableUnaryOperator<Matrix>> operation, Matrix onObject) {
//        return operation.getOperationFunction().apply(onObject);
//    }
//
//    @Override
//    public FailableUnaryOperator getSupportedUnaryOperation(SupportedOperationsEnumeration<FailableUnaryOperator<Matrix>> supportedOperation) {
//        return supportedOperation.getOperationFunction();
//    }
//
//    //MARK: Public methods
//
//    public boolean dimensionsAreEqual(MSMatrix comparedTo){
//        if((this.getNumberOfRows() != comparedTo.getNumberOfRows()) || (this.getNumberOfColumns() != comparedTo.getNumberOfColumns())){
//            return false;
//        }else{
//            return true;
//        }
//    }
//
//
//    public boolean canMultiplyWith(Matrix multiplierMatrix) {
//        if (this.getNumberOfColumns() != multiplierMatrix.getNumberOfRows()) {
//            return false;
//        } else {
//            return true;
//        }
//    }
//
//
//
//
//
//    //MARK: Private methods
//
//
//    private int totalDisplayNumbers(double value){
//        return numsBeforeDecimal(value) + numsAfterDecimal(value);
//    }
//
//    private int totalDisplayNumbers(double value, int withMaximumAfterDecimal){
//        int correctedDecimalCount;
//        if(numsAfterDecimal(value) > withMaximumAfterDecimal){
//            correctedDecimalCount = withMaximumAfterDecimal;
//        }else{
//            correctedDecimalCount = withMaximumAfterDecimal;
//        }
//
//        if(withMaximumAfterDecimal < 0){
//            return totalDisplayNumbers(value);
//        }
//
//        return numsBeforeDecimal(value) + correctedDecimalCount;
//    }
//
//    private int numsBeforeDecimal(double value){
//        Double objectValue = value;
//        String[] valueString = objectValue.toString().split("\\.");
//        int lengthInteger = valueString[0].length();
//        return lengthInteger;
//    }
//
//    private int numsAfterDecimal(double value){
//        Double objectValue = value;
//        String[] valueString = objectValue.toString().split("\\.");
//        int lengthDecimal = valueString[1].length();
//        return lengthDecimal;
//    }
//
//
//
//}