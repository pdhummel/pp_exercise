/**
 * Calculations were validated using:
 * http://www.myamortizationchart.com/ and
 * http://www.amortization-calc.com/
 * 
 */
package test.exercises;

import java.util.List;
import java.math.BigDecimal;

import org.junit.Test;
import junit.framework.TestCase;

import exercises.Loan;
import exercises.MonthlyPaymentSummary;


public class LoanTest {
	
    @Test
    public void testMakePayment() {
        Loan loan = new Loan(10000 * 100, 10, 1);	      // $10000, 10%, 1 year
        MonthlyPaymentSummary mps = loan.makePayment(new BigDecimal(87900));	// $879.00
    	TestCase.assertEquals(87900, Math.round(mps.getPaymentAmountInCents().doubleValue()));
    	TestCase.assertEquals(8333, Math.round(mps.getPaymentInterest().doubleValue()));
    	TestCase.assertEquals(79567, Math.round(mps.calculatePrinciplePaidInCents().doubleValue()));        
        TestCase.assertEquals(920433, Math.round(mps.getEndingBalanceInCents().doubleValue()));
        TestCase.assertEquals(920433, Math.round(loan.getCurrentBalanceInCents().doubleValue()));
    }
    

    @Test
    public void testCalculateMonthlyPayment() {
    	Loan loan = new Loan(10000 * 100, 10, 1);
    	double mp = loan.calculateMonthlyPayment().doubleValue();
    	TestCase.assertEquals(87916, Math.round(mp));
    }

    @Test
    public void testAmortizeLoan() {
    	Loan loan = new Loan(10000 * 100, 10, 1);        // $10000, 10%, 1 year
    	List<MonthlyPaymentSummary> monthlyPayments = loan.amortizeLoan();
    	MonthlyPaymentSummary mps = monthlyPayments.get(0);
    	TestCase.assertEquals(87916, Math.round(mps.getPaymentAmountInCents().doubleValue()));
    	TestCase.assertEquals(8333, Math.round(mps.getPaymentInterest().doubleValue()));
    	TestCase.assertEquals(79583, Math.round(mps.calculatePrinciplePaidInCents().doubleValue()));    	
    	mps = monthlyPayments.get(11);
    	TestCase.assertEquals(87916, Math.round(mps.getPaymentAmountInCents().doubleValue()));
    	TestCase.assertEquals(727, Math.round(mps.getPaymentInterest().doubleValue()));
    	TestCase.assertEquals(87189, Math.round(mps.calculatePrinciplePaidInCents().doubleValue()));  
    	TestCase.assertEquals(0, Math.round(mps.getEndingBalanceInCents().doubleValue()));
    	TestCase.assertEquals(0, Math.round(loan.getCurrentBalanceInCents().doubleValue()));
    }

    @Test
    // min limits from http://www.amortization-calc.com
    public void testAmortizeLoanLowerLimit() {
    	Loan loan = new Loan(1 * 100, 1, 1);		          // $1, 1%, 1 year
    	List<MonthlyPaymentSummary> monthlyPayments = loan.amortizeLoan();
    	MonthlyPaymentSummary mps = monthlyPayments.get(0);
    	TestCase.assertEquals(8, Math.round(mps.getPaymentAmountInCents().doubleValue()));
    	TestCase.assertEquals(0, Math.round(mps.getPaymentInterest().doubleValue()));
    	TestCase.assertEquals(8, Math.round(mps.calculatePrinciplePaidInCents().doubleValue()));    	
    	mps = monthlyPayments.get(11);
    	TestCase.assertEquals(8, Math.round(mps.getPaymentAmountInCents().doubleValue()));
    	TestCase.assertEquals(0, Math.round(mps.getPaymentInterest().doubleValue()));
    	TestCase.assertEquals(8, Math.round(mps.calculatePrinciplePaidInCents().doubleValue()));  
    	TestCase.assertEquals(0, Math.round(mps.getEndingBalanceInCents().doubleValue()));
    	TestCase.assertEquals(0, Math.round(loan.getCurrentBalanceInCents().doubleValue()));
    }        
    
    @Test
    // max limits from http://www.amortization-calc.com
    public void testAmortizeLoanUpperLimit() {
    	Loan loan = new Loan(9999999 * 100, 50, 50);   // $9999999, 50%, 50 years
    	List<MonthlyPaymentSummary> monthlyPayments = loan.amortizeLoan();
    	MonthlyPaymentSummary mps = monthlyPayments.get(0);
    	TestCase.assertEquals(41666663, Math.round(mps.getPaymentAmountInCents().doubleValue()));
    	TestCase.assertEquals(41666663, Math.round(mps.getPaymentInterest().doubleValue()));
    	TestCase.assertEquals(0, Math.round(mps.calculatePrinciplePaidInCents().doubleValue()));
    	mps = monthlyPayments.get(599);
    	TestCase.assertEquals(41666663, Math.round(mps.getPaymentAmountInCents().doubleValue()));
    	TestCase.assertEquals(1666667, Math.round(mps.getPaymentInterest().doubleValue()));
    	TestCase.assertEquals(39999996, Math.round(mps.calculatePrinciplePaidInCents().doubleValue()));  
    	TestCase.assertEquals(0, Math.round(mps.getEndingBalanceInCents().doubleValue()));
    	TestCase.assertEquals(0, Math.round(loan.getCurrentBalanceInCents().doubleValue()));
    }
    

    
}
