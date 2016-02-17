/**
 * Models a loan which can be used to produced an Amortization Schedule.
 */
package exercises;

import java.util.ArrayList;
import java.util.List;
import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;

public class Loan {
	
	// Loan limits.
	// These are different from the original program because at this time, 
	// these were the only range values I felt comfortable validating.
	public static final double[] borrowAmountRange = new double[] { 1d, 9999999d };
	public static final double[] aprRange = new double[] { 1d, 50d };
	public static final int[] termRange = new int[] { 1, 50 };

	
    // the amount he or she is borrowing,    
    private long originalLoanAmountInCents = 0;
    private BigDecimal currentBalanceInCents;    
    // the annual percentage rate used to repay the loan,  
    private BigDecimal annualPercentageRate;
    // the term, in years, over which the loan is repaid.    
    private int termInYears = 0;

    
    /**
     * Establish the loan.
     * @param loanAmount
     * @param apr
     * @param term
     */
    public Loan(long loanAmount, double apr, int term) {
        this.originalLoanAmountInCents = loanAmount;        
        this.annualPercentageRate = new BigDecimal(apr);            // I
        this.termInYears = term;                    
        this.currentBalanceInCents = new BigDecimal(loanAmount);    // P
    }
    
    /**
     * @return the loanAmountInCents
     */
    public long getOriginalLoanAmountInCents() {
        return originalLoanAmountInCents;
    }
    /**
     * @return the annualPercentageRate
     */
    public BigDecimal getAnnualPercentageRate() {
        return annualPercentageRate;
    }
    /**
     * @return the termInYears
     */
    public int getTermInYears() {
        return termInYears;
    }
    /**
     * @return the currentBalance
     */
    public BigDecimal getCurrentBalanceInCents() {
        return currentBalanceInCents;
    }
    
    /**
     * Make a payment and reduce the current loan balance.  
     * The payment summary is returned for possible output.
     * @param monthlyPayment
     * @return MonthlyPaymentSummary
     */
    public MonthlyPaymentSummary makePayment(BigDecimal monthlyPayment) {
        MonthlyPaymentSummary monthlyPaymentSummary = new MonthlyPaymentSummary();    
        monthlyPaymentSummary.setPaymentAmountInCents(monthlyPayment);
        
        // I = Interest
        // J = Monthly Interest in decimal form:  I / (12 * 100)        
        // Calculate H = P x J, this is your current monthly interest
        // Note that "P" in this context means current principle.
        MathContext mc = new MathContext(32, RoundingMode.HALF_UP);
        BigDecimal I = this.annualPercentageRate;
        BigDecimal J = I.divide(new BigDecimal(1200d), mc);        
        BigDecimal P = this.currentBalanceInCents;
        BigDecimal H = P.multiply(J);
        monthlyPaymentSummary.setPaymentInterest(H);
        
        // Calculate C = M - H, this is your monthly payment minus your monthly interest, so it is the amount of principal you pay for that month
        BigDecimal C = monthlyPaymentSummary.calculatePrinciplePaidInCents();
        // Calculate Q = P - C, this is the new balance of your principal of your loan.        
        BigDecimal Q = P.subtract(C);
        if (Q.doubleValue() > 0) {
        	monthlyPaymentSummary.setEndingBalanceInCents(Q);
        	this.currentBalanceInCents = Q;        	
        } else {
        	// Handle the case where the last payment would be more than 
        	// the loan amount.  This really shouldn't happen, but just in case.
        	BigDecimal zero = new BigDecimal(0);
        	monthlyPaymentSummary.setEndingBalanceInCents(zero);
        	monthlyPayment = P.add(H);
        	monthlyPaymentSummary.setPaymentAmountInCents(monthlyPayment);
        	this.currentBalanceInCents = zero;
        }
        return monthlyPaymentSummary;
    }
    
    /**
     * 
    // This will get you your monthly payment. 
    // M = P * (J / (1 - (Math.pow(1/(1 + J), N))));
    // 
    // Where:
    // P = Principal
    // I = Interest
    // J = Monthly Interest in decimal form:  I / (12 * 100)
    // N = Number of months of loan
    // M = Monthly Payment Amount    
     * 
     * @return BigDecimal monthly payment value
     */
    public BigDecimal calculateMonthlyPayment() {
        // Note that "P" in this context refers to the original loan amount.
    	MathContext mc = new MathContext(32, RoundingMode.HALF_UP);
        BigDecimal P = new BigDecimal(this.originalLoanAmountInCents);
        BigDecimal I = this.annualPercentageRate;
        BigDecimal J = I.divide(new BigDecimal(12*100), mc);
        int N = this.termInYears * 12; 
        BigDecimal one = new BigDecimal(1);
        BigDecimal power = one.divide(one.add(J), mc).pow(N);
        BigDecimal M = P.multiply(J.divide(one.subtract(power), mc));
        return M;
    }
    
    /**
     * Amortize the loan and create a schedule to be stored in a List.
     * @return List<MonthlyPaymentSummary>
     */
    public List<MonthlyPaymentSummary> amortizeLoan() {
        List<MonthlyPaymentSummary> monthlyPayments = new ArrayList<MonthlyPaymentSummary>();
        int N = this.termInYears * 12;
        BigDecimal monthlyPayment = this.calculateMonthlyPayment();
        for (int i=0; i < N; i++) {
            MonthlyPaymentSummary mps = this.makePayment(monthlyPayment);
            monthlyPayments.add(mps);
        }
        
        // Handle the situation where the loan is still not fully paid off.
        // This shouldn't happen, but if it does, we are going to have an
        // extra payment of all principle.
        if (this.currentBalanceInCents.doubleValue() > 0) {
        	BigDecimal zero = new BigDecimal(0);
	        MonthlyPaymentSummary mps = new MonthlyPaymentSummary();
	        mps.setEndingBalanceInCents(zero);
	        mps.setPaymentAmountInCents(currentBalanceInCents);
	        mps.setPaymentInterest(zero);
	        this.currentBalanceInCents = zero;
        }
        return monthlyPayments;
    }
}
