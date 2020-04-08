/*
 * Purpose: Design and Analysis of Algorithms Assignment 3
 * Status: Complete and thoroughly tested
 * Last update: 03/11/19
 * Submitted:  03/11/19
 * Comment: test suite and sample run attached
 * @author: Jeffrey Wang
 * @version: 2019.03.11
 */
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class Driver {
    static BufferedReader stdin = new BufferedReader (new InputStreamReader(System.in));
    static int solution = 0;
    static int partial = 0;
    static int jobs =0;
    public static void main(String[] args) throws IOException {
        System.out.println("Select from the following menu:");
        System.out.println("1.All Solution");
        System.out.println("2.Quit");
        boolean quit = false;
        while(!quit) {
            int user = -1;
            try {
                String inputString = stdin.readLine().trim();
                user = Integer.parseInt(inputString);
            }
            catch(Exception e) {
                System.err.println("Enter a valid number please!");
            }
            switch(user) {
            case 1:
                allSolutions();
                break;
            case 2:
                System.out.println("GoodBye");
                System.exit(0);
                break;
            default:
                System.out.println("Pick again!");
                break;
            }

        }
    }
    /*
    Matching the person with the most effcient job 
    Enter Matrix size if 3x3 -> Enter 9 numbers
    */
    public static void allSolutions() {
        int matrixN = 0;
        int position = 0;
        int counter =0;
        int max=0;
        String finalAssignment = "";
        //inner
        boolean stopPlacement = true;
        //outter
        boolean stp = false;
        int keepIndex = -1;
        try {
            System.out.println("Enter Matrix Size: ");
            String matrixSize = stdin.readLine().trim();
            matrixN = Integer.parseInt(matrixSize);
        } catch(Exception e) {
            System.err.println("Enter a valid number please!");
        }
        int board[] = new int[matrixN * matrixN];
        int checkBoard[] = new int[matrixN * matrixN];
        //goes through and store values in board
        for(int i = 0; i<board.length; i++) {
            try {
                String pos = stdin.readLine().trim();
                position = Integer.parseInt(pos);
            } catch(Exception e) {
                System.err.println("Enter a valid number please!");
            }
            //store values in board
            board[i] = position;
        }
        //start of each row
        for(int i =0; i<=board.length && !(stp); i+=matrixN) {
            stopPlacement=true;
            for(int j = i; j <= board.length && stopPlacement; j++) {

                if(keepIndex >= 0) {
                    j = keepIndex;
                    keepIndex = -1;
                }
                if(counter == matrixN) {
                    i =i-matrixN;
                    //backTracking
                    for(int posFinder = i; posFinder<(i+matrixN); posFinder++) {
                        if(checkBoard[posFinder] == 1) {
                            jobs = jobs - board[posFinder];
                            checkBoard[posFinder] = 0;
                            j = posFinder + 1;
                            counter --;
                        }
                    }
                }
                //backTracking
                while(j==i+matrixN) {
                    if(j == (i+matrixN)) {
                        i = i-matrixN;
                        if(i>=0) {
                            for(int posFinder = i; posFinder<(i+matrixN); posFinder++) {
                                if(checkBoard[posFinder] == 1) {
                                    jobs = jobs - board[posFinder];
                                    checkBoard[posFinder] = 0;
                                    j = posFinder + 1;
                                    counter --;
                                }
                            }
                        } else {
                            stp = true;
                        }
                    }
                }
                if(i>=0 && i<checkBoard.length) {
                    if(columnAttack(checkBoard, matrixN, j) == true) {
                        jobs = jobs + board[j];
                        checkBoard[j] = 1;
                        counter ++;
                        stopPlacement = false;
                        if(findBestJob(board, checkBoard, i, matrixN)<=max) {

                            for(int posFinder = i; posFinder<(i+matrixN); posFinder++) {
                                if(checkBoard[posFinder] == 1) {
                                    jobs = jobs - board[posFinder];
                                    checkBoard[posFinder] = 0;
                                    j = posFinder + 1;
                                    counter --;

                                    i =i-matrixN;
                                }
                            }
                            keepIndex = j;

                        }

                    }
                } else {
                    stp = true;
                }
                if(j+i >= checkBoard.length ) {
                    solution++;
                    if(max<jobs) {
                        max = jobs;
                        finalAssignment = saveString(checkBoard,matrixN);
                    }
                }

            }//inner for
        }//outter for
        System.out.println("Number of job assignments explored:" + solution);
        print(board,matrixN);
        System.out.println("Best job assignment is:");
        System.out.print(finalAssignment);
        System.out.println("Partial Solutions found: " + partial);
        System.out.println("Best job assignment cost " + max);
        solution = 0;
        max = 0;
        partial=0;
    }

    /*
    finding the best job placement 
    */
    public static int findBestJob (int board[],int checkBoard[],int i, int matrixN) {
        int possiblyBestSum=jobs;
        int possibleBiggestInRow = 0;
        for(int b = i+matrixN; b < checkBoard.length; b += matrixN) {
            for(int index=b; index < b+matrixN; index++) {
                if(columnAttack(checkBoard, matrixN, index) == false) {

                } else {
                    if(possibleBiggestInRow < board[index]) {
                        possibleBiggestInRow = board[index];
                    }
                }
            }//inner for
            possiblyBestSum = possiblyBestSum + possibleBiggestInRow;
            possibleBiggestInRow = 0;
        }//outter for
        partial++;
        return possiblyBestSum;
    }

    /*
    building a string
    */
    public static String saveString(int checkBoard[], int matrixN) {
        String save = "";
        int employee =-1;
        for(int index = 0; index < checkBoard.length; index++) {
            if(checkBoard[index] == 1) {
                employee++;
                save = save + "Person " + employee +  " assigned job "+ (index%matrixN) + "\n";
            }
        }
        return save;
    }
    /*
    boolean check to place marker
    */
    public static boolean columnAttack(int board[], int numberOfQueen, int indexPlaced) {
        boolean queenPlaced = true;
        for(int i = indexPlaced; i>=0; i-=numberOfQueen) {
            if(board[i] == 1) {
                queenPlaced = false;
            }
        }
        return queenPlaced;
    }
    /*
    print method
    */
    public static void print(int board[], int n) {
        for(int i =0; i<board.length; i++) {
            if(i%n == 0) {
                System.out.println("");
            }
            System.out.print(board[i] +"  ");

        }
        System.out.println("");
    }
}
