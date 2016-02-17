/**
 * Summarizes a payment made to a loan.
 * This can also be used to build a line item in an Amortization Schedule.
 */
package exercises;

import java.math.BigDecimal;

public class MonthlyPaymentSummary {
	private BigDecimal paymentAmountInCents;	// C
	private BigDecimal paymentInterest;			// H
	private BigDecimal endingBalanceInCents;	

	
	/**
	 * @return the paymentAmountInCents
	 */
	public BigDecimal getPaymentAmountInCents() {
		return paymentAmountInCents;
	}
	/**
	 * @param paymentAmountInCents the paymentAmountInCents to set
	 */
	public void setPaymentAmountInCents(BigDecimal paymentAmountInCents) {
		this.paymentAmountInCents = paymentAmountInCents;
	}
	/**
	 * @return the paymentInterest
	 */
	public BigDecimal getPaymentInterest() {
		return paymentInterest;
	}
	/**
	 * @param paymentInterest the paymentInterest to set
	 */
	public void setPaymentInterest(BigDecimal paymentInterest) {
		this.paymentInterest = paymentInterest;
	}

	/**
	 * @return the principlePaidInCents
	 */
	// Calculate C = M - H, this is your monthly payment minus your monthly interest, so it is the amount of principal you pay for that month
	public BigDecimal calculatePrinciplePaidInCents() {
		BigDecimal C = paymentAmountInCents.subtract(paymentInterest);
		return C;
	}
	/**
	 * @return the endingBalanceInCents
	 */
	public BigDecimal getEndingBalanceInCents() {
		return endingBalanceInCents;
	}
	/**
	 * @param endingBalanceInCents the endingBalanceInCents to set
	 */
	public void setEndingBalanceInCents(BigDecimal endingBalanceInCents) {
		this.endingBalanceInCents = endingBalanceInCents;
	}

}
