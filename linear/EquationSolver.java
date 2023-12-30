package linear;
import linear.algebra.GaussianElimination;

public class EquationSolver
{
     public static class HelperMethods{ // Made it static so I can use the method without creating an instance 
    // Create stringsToDoubles: class-level helper method which converts an array of String to an array of double.
        public static double[] stringsToDoubles(String[] elements){
            double[] convertedElements = new double[elements.length]; 
            for(int i = 0; i < elements.length; i++){
                convertedElements[i] = Double.parseDouble(elements[i]);
            }
            return convertedElements;
        }
    }

    public static void main(String[] args){
        // Implement an linear.EquationSolver class with a main which takes a command line argument in form 2,1,-1,8 -3,-1,2,-11 -2,1,2,-3 if corresponding to the previous example of equations.
        int row = 3;
        int cols = 4;

        if (args.length == 0) {
            System.out.println("Please provide equations as command line arguments.");
        return;
        }

        double [][] matrix = new double[row][cols];
        String[] stringElem = new String[row*cols]; 

        for (int i = 0; i < args.length; i++) {
            String[] elements = args[i].split(" ");
            for (int j = 0; j < elements.length; j++) {
                String[] values = elements[j].split(",");
                double[] doubleElem = HelperMethods.stringsToDoubles(values);
                for (int k = 0; k < doubleElem.length; k++) {
                    matrix[i][k] = doubleElem[k];   
                }
            }
        }

        GaussianElimination newMatrix = new GaussianElimination(row, cols, matrix); 
        newMatrix.print();

        newMatrix.rowEchelonForm();
        newMatrix.print();

        newMatrix.backSubstitution(); 
        newMatrix.print();
    }
}
    