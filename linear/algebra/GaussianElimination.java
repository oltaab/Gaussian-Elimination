package linear.algebra;

// Compiling the test: javac -cp checkthat.jar:junit5all.jar:. linear/GaussianEliminationTest.java
// Executing the test: java -cp checkthat.jar:junit5all.jar:. org.junit.platform.console.ConsoleLauncher --class-path . --scan-class-path

public class GaussianElimination
{
    private double[][] matrix; // 2 dim array
    private int rows; // Storing the dim 
    private int cols; // Storing the dim

    // Constuctor 
    public GaussianElimination(int row, int column, double[][] inputMatrix){ 
        this.rows = row;
        this.cols = column; 

        // Creating a copy of the originial matrix
        this.matrix = new double[inputMatrix.length][]; 
        for(int i = 0; i < inputMatrix.length; i++){
            this.matrix[i] = inputMatrix[i].clone(); 
        }
    }

    // Getters
    public int getRows() {
        return this.rows;
    }

    public int getCols() {
        return this.cols;
    }

    public double[][] getMatrix() {
        return this.matrix;
    }   

    // Setter
    public void setMatrix(double[][] currentMatrix){
        int currentNumberOfRows = currentMatrix.length; 
        int currentNumberOfColumns = currentMatrix[0].length;

        if(currentNumberOfColumns != this.cols || currentNumberOfRows != this.rows){
            throw new IllegalArgumentException("Dimensions aren't the same");
        }
        this.matrix = currentMatrix; 
    }

    public void checkMatrixDimensions(double[][] currentMatrix){
        if(currentMatrix[0].length != this.cols || currentMatrix.length != this.rows){
            throw new IllegalArgumentException("Dimensions aren't the same");
        }
    }

    // ArgMax which takes a row index and column index, and returns the index of the row coming after row index, containing the highest absolute value (Math.abs) in the given column index.
    public int argMax(int rowIndex, int columnIndex){
        double maxValue = Math.abs(this.matrix[rowIndex][columnIndex]);
        int maxIndex = rowIndex;
        
        for(int i = rowIndex+1; i < this.rows; i++){
            if( maxValue < Math.abs(this.matrix[i][columnIndex])){
                maxValue = Math.abs(this.matrix[i][columnIndex]);
                maxIndex = i; 
            }
        } 
        return maxIndex; 
    }

    // SwapRows which takes two row indexes as arguments and swaps the rows.
    public void swapRows(int rowIndexSwap1, int rowIndexSwap2){
        for(int i = 0; i < this.cols; i++){
            double temp = this.matrix[rowIndexSwap2][i];
            this.matrix[rowIndexSwap2][i] = this.matrix[rowIndexSwap1][i];
            this.matrix[rowIndexSwap1][i] = temp;
        }
    }

    // MultiplyAndAddRow - to both multiply and add in a combined step, implement this method which takes an addRow index, a mulRow index and a colIndex. 
    // It subtracts from addRow, mulRow multiplied by the ratio of addRow and mulRow at colIndex as seen in the inner loop of the pseudo-code.
    public void multiplyAndAddRow(int addRowIndex, int mulRowIndex, int colIndex) {
        double ratio = (this.matrix[addRowIndex][colIndex]) / (this.matrix[mulRowIndex][colIndex]);

        for (int i = colIndex; i < this.cols; i++) {
            this.matrix[addRowIndex][i] = this.matrix[addRowIndex][i] - (ratio * this.matrix[mulRowIndex][i]);
        }
    }

    // MultiplyRow which takes a row index and column index and divides the row by the value at that row and column index. 
    // Unlike the pseudo-code which increments the row and column pivot indexes and continues, we add one line before incrementing, multiplyRow(rowIndex, colIndex) which will produce the reduced row echelon form useful for obtaining a solution.
    public void multiplyRow(int rowIndex, int colIndex){
        double div = this.matrix[rowIndex][colIndex];
            for(int j = colIndex; j < this.cols; j++){
                if (this.matrix[rowIndex][j] != 0 ){
                    this.matrix[rowIndex][j] = this.matrix[rowIndex][j]/div;
            }
        }
    }

    public void rowEchelonForm() {
        int currentRow = 0;
        int currentColumn = 0;

        while (currentRow < this.rows && currentColumn < this.cols) {
            int maxRowIndex = argMax(currentRow, currentColumn);

            if (maxRowIndex != currentRow) {
                swapRows(currentRow, maxRowIndex);
            }

            for (int i = currentRow + 1; i < this.rows; i++) {
                multiplyAndAddRow(i, currentRow, currentColumn);
            }

            multiplyRow(currentRow, currentColumn);

            currentRow++;
            currentColumn++;
        }
    }

    // Implement the backSubstitution method which goes through the rows in reverse order, then for every other row before the current row, uses multiplyAndAddRow to subtract the already solved variables. 
    // Before entering the inner loop, check that the diagonal element is non-zero, otherwise throw an IllegalArgumentException as it is a system of equations without a unique solution!
    public void backSubstitution() {
        for (int i = this.rows-1; i >= 0; i--) {
            double diagonalElement = this.matrix[i][i];
            if (diagonalElement == 0) {
                throw new IllegalArgumentException("The eq. system doesn't have a unique solution");
            }
            
            for (int j = i-1; j >= 0; j--) {
                multiplyAndAddRow(j, i, i);
            }
        }
    }

    // Printing
    public void printMatrix() {
    int numRows = this.matrix.length;
    int numCols = this.matrix[0].length;

    int maxDigits = 1;
    for (int i = 0; i < numRows; i++) {
        for (int j = 0; j < numCols; j++) {
            int numDigits = String.valueOf(this.matrix[i][j]).length();
            if (numDigits > maxDigits) {
                    maxDigits = numDigits;
            }
        }
    }

    for (int i = 0; i < numRows; i++) {
        for (int j = 0; j < numCols; j++) {
            int numDigits = String.valueOf(this.matrix[i][j]).length();
            int numSpaces = maxDigits - numDigits;
                for (int k = 0; k < numSpaces; k++) {
                    System.out.print(" ");
                }
                System.out.print(this.matrix[i][j] + " ");
            }
            System.out.println(); 
        }
    }

    // Printing in a human readable form
    public void print() {
        String[] variables = {"x", "y", "z"};
        for (int i = 0; i < this.rows; i++) {
            String theEquation = "";
            for (int j = 0; j < this.cols - 1; j++) {
                theEquation += (this.matrix[i][j] < 0 ? "-" : "+") + Math.abs(this.matrix[i][j]) + variables[j] + " ";
            }       
            System.out.println(theEquation + "= " + this.matrix[i][this.cols-1]); 
        }
        System.out.println();
    }
}