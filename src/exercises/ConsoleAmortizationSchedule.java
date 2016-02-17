/**
 * This is a console based input and output program used to 
 * input loan values and calculate and output an Amortization Schedule.
 * It is the main entry point for our application.
 */
//
// Exercise Details:
// Build an amortization schedule program using Java. 
// 
// The program should prompt the user for
//        the amount he or she is borrowing,
//        the annual percentage rate used to repay the loan,
//        the term, in years, over which the loan is repaid.  
// 
// The output should include:
//        The first column identifies the payment number.
//        The second column contains the amount of the payment.
//        The third column shows the amount paid to interest.
//        The fourth column has the current balance.  The total payment amount and the interest paid fields.
// 
// Use appropriate variable names and comments.  You choose how to display the output (i.e. Web, console).  
// Amortization Formula
// This will get you your monthly payment.  Will need to update to Java.
// M = P * (J / (1 - (Math.pow(1/(1 + J), N))));
// 
// Where:
// P = Principal
// I = Interest
// J = Monthly Interest in decimal form:  I / (12 * 100)
// N = Number of months of loan
// M = Monthly Payment Amount
// 
// To create the amortization table, create a loop in your program and follow these steps:
// 1.      Calculate H = P x J, this is your current monthly interest
// 2.      Calculate C = M - H, this is your monthly payment minus your monthly interest, so it is the amount of principal you pay for that month
// 3.      Calculate Q = P - C, this is the new balance of your principal of your loan.
// 4.      Set P equal to Q and go back to Step 1: You thusly loop around until the value Q (and hence P) goes to zero.
// 

package exercises;

import java.io.Console;
import java.lang.Math;
import java.io.InputStreamReader;
import java.io.BufferedReader;
import java.io.IOException;
import java.util.IllegalFormatException;
import java.util.List;

public class ConsoleAmortizationSchedule {
    private Loan loan;
    
    private static Console console = System.console();

    
    /**
     * Allow the user to input values for the loan and then create the loan.
     */
    public void inputAndCreateLoan() {
        double borrowAmount = this.inputBorrowAmount();
        double apr = this.inputApr();
        int term = this.inputTerm();
        loan = new Loan(Math.round(borrowAmount*100), apr, term);
    }
    
    /**
     * Original loan amount.
     * @return
     */
    private double inputBorrowAmount() {
        double range[] = Loan.borrowAmountRange;
        String prompt = "Please enter the amount you would like to borrow: ";
        double amount = this.inputDoubleValue(prompt, range);
        return amount;
    }
    
    /**
     * Annual percentage rate.
     * @return
     */
    private double inputApr() {
        double range[] = Loan.aprRange;
        String prompt = "Please enter the annual percentage rate used to repay the loan: ";
        double amount = this.inputDoubleValue(prompt, range);
        return amount;        
    }
    
    /**
     * Term of the loan in years.
     * @return
     */
    private int inputTerm() {
        String prompt = "Please enter the term, in years, over which the loan is repaid: ";
        int range[] = Loan.termRange;
        int amount = 0;
        boolean isValidValue = false;
        while (! isValidValue) {
            try {
                String line = readLine(prompt);
                amount = Integer.parseInt(line);
                
                if ((range[0] <= amount) && (amount <= range[1])) {
                    isValidValue = true;
                } else {
                    print("Please enter a positive value between " + range[0] + " and " + range[1] + ". ");
                    print("An invalid value was entered.\n");
                }                
                
            } catch (Exception e) {
                print("An invalid value was entered.\n");
            }
        }
        return amount;
    }    
    
    
    private double inputDoubleValue(String prompt, double range[]) {
        double amount = 0;
        boolean isValidValue = false;
        while (! isValidValue) {
            try {
                String line = readLine(prompt);
                amount = Double.parseDouble(line);
                
                if ((range[0] <= amount) && (amount <= range[1])) {
                    isValidValue = true;
                } else {
                    print("Please enter a positive value between " + range[0] + " and " + range[1] + ". ");
                    print("An invalid value was entered.\n");
                }                
                
            } catch (Exception e) {
                print("An invalid value was entered.\n");
            }
        }
        return amount;
    }    
    
    
    
    /**
     * The output should include:
     *    The first column identifies the payment number.
     *    The second column contains the amount of the payment.
     *    The third column shows the amount paid to interest.
     *    The fourth column has the current balance.  The total payment amount and the interest paid fields.
     */
    public void outputAmortizationSchedule() {
        String formatString = "%1$-20s%2$-20s%3$-20s%4$s\n";
        printf(formatString,
                "Payment Number", "Payment Amount", "Interest Paid",
                "Current Balance");

        List<MonthlyPaymentSummary> monthlySummaries = loan.amortizeLoan();
        for (int i=0; i< monthlySummaries.size(); i++) {
            this.outputMonthlyPaymentSummary(i+1, monthlySummaries.get(i));
        }
    }
    
    /**
     * Output a line of the Amortization Schedule.
     * @param paymentNumber
     * @param monthlyPayment
     */
    private void outputMonthlyPaymentSummary(int paymentNumber, MonthlyPaymentSummary monthlyPayment) {
        // output is in dollars
        String formatString = "%1$-20d%2$-20.2f%3$-20.2f%4$.2f\n";
        printf(formatString, paymentNumber, 
                monthlyPayment.getPaymentAmountInCents().doubleValue() / 100d,
                monthlyPayment.getPaymentInterest().doubleValue() / 100d,
                monthlyPayment.getEndingBalanceInCents().doubleValue() / 100d);        
    }
    
    /**
     * Reads a line of input from the user.
     * @param userPrompt
     * @return String
     * @throws IOException
     */
    private  String readLine(String userPrompt) throws IOException {
        String line = "";
        
        if (console != null) {
            line = console.readLine(userPrompt);
        } else {
            // print("console is null\n");
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));

            print(userPrompt);
            line = bufferedReader.readLine();
        }
        line.trim();
        return line;
    }
    
    
    private static void printf(String formatString, Object... args) {        
        try {
            if (console != null) {
                console.printf(formatString, args);
            } else {
                System.out.print(String.format(formatString, args));
            }
        } catch (IllegalFormatException e) {
            System.err.print("Error printing...\n");
        }
    }
    
    private static void print(String s) {
        printf("%s", s);
    }

    
    /**
     * Uses the console to input the loan values and then
     * calculates the amortization schedule and outputs it to the screen.
     * @param args
     */
    public static void main(String [] args) {
        ConsoleAmortizationSchedule loanConsole = new ConsoleAmortizationSchedule(); 
        loanConsole.inputAndCreateLoan();
        loanConsole.outputAmortizationSchedule();
    }
    
}
