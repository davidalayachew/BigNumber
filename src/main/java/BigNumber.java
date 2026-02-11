import java.math.BigInteger;
import java.util.Objects;

/** If you know BigInteger and BigDecimal, think of this as BigFraction. This class is immutable. */
public final class BigNumber
{

	private final BigInteger numerator;

	private final BigInteger denominator;

	public BigNumber()
	{
	
		this(BigInteger.ZERO);
	
	}

	public BigNumber(long param)
	{
	
		this(BigInteger.valueOf(param), BigInteger.ONE);
	
	}

	public BigNumber(BigInteger param)
	{
	
		Objects.requireNonNull(param, "parameter cannot be null");
	
		this.numerator = param;
		this.denominator = BigInteger.ONE;
	
	}

	public BigNumber(BigInteger numerator, BigInteger denominator)
	{
	
		Objects.requireNonNull(numerator, "numerator cannot be null");
		Objects.requireNonNull(denominator, "denominator cannot be null");
	
		if (denominator.equals(BigInteger.ZERO))
		{
		
			throw new IllegalArgumentException("denominator cannot be 0");
		
		}
	
		this.numerator = isPositive(denominator) ? numerator : numerator.negate();
		this.denominator = denominator.abs();
	
	}

	public BigNumber(BigNumber param)
	{
	
		this(Objects.requireNonNull(param, "BigNumber cannot be null").numerator, param.denominator);
	
	}

	public BigInteger getNumerator()
	{
	
		return this.numerator;
	
	}

	public BigInteger getDenominator()
	{
	
		return this.denominator;
	
	}

	public BigNumber negate()
	{
	
		return new BigNumber(this.numerator.negate(), this.denominator);
	
	}

	public boolean isPositive()
	{
	
		return isPositive(this.numerator);
	
	}

	private static boolean isPositive(BigInteger num)
	{
	
		return num.signum() > 0;
	
	}

	private static BigNumber simplify(BigInteger numerator, BigInteger denominator)
	{
	
		final var gcd = numerator.gcd(denominator);
	
		return new BigNumber(numerator.divide(gcd), denominator.divide(gcd));
	
	}

	public BigNumber add(long param)
	{
	
		return this.add(new BigNumber(param));
	
	}

	public BigNumber add(BigNumber param)
	{
	
		Objects.requireNonNull(param, "parameter cannot be null");
	
		BigInteger resultNumerator;
		BigInteger resultDenominator;
	
		if (this.denominator.equals(param.getDenominator()))
		{
		
			resultNumerator = this.numerator.add(param.getNumerator());
			resultDenominator = this.denominator;
		
		}
		
		else
		{
		
		   //our goal here is to end up with a shared denominator
		   //So, in order to accomplish that, we will need to do
		   //that cross multiplication thing that you do to end
		   //up with the same denominator
		
			BigInteger numerator1 = this.numerator.multiply(param.denominator);
			BigInteger numerator2 = param.numerator.multiply(this.denominator);
		
			resultNumerator = numerator1.add(numerator2);
			resultDenominator = param.getDenominator().multiply(this.getDenominator());
		
		}
	
	   //simplify before we return. We don't want to waste memory when the number can be simplified into something smaller.
		return simplify(resultNumerator, resultDenominator);
	
	}

	public BigNumber subtract(long param)
	{
	
		return this.add(new BigNumber(param).negate());
	
	}

	public BigNumber subtract(BigNumber param)
	{
	
		Objects.requireNonNull(param, "BigNumber cannot be null");
	
		return this.add(param.negate());
	
	}

	public BigNumber multiply(long param)
	{
	
		return this.multiply(new BigNumber(param));
	
	}

	public BigNumber multiply(BigNumber param)
	{
	
		Objects.requireNonNull(param, "parameter cannot be null");
	
		final BigInteger resultNumerator = this.numerator.multiply(param.numerator);
		final BigInteger resultDenominator = this.denominator.multiply(param.denominator);
	
		return simplify(resultNumerator, resultDenominator);
	
	}

	public BigNumber divide(long param)
	{
	
		if (param == 0) {
			throw new IllegalArgumentException("param cannot be 0"); }
	
		return this.divide(new BigNumber(param));
	
	}

	public BigNumber divide(BigNumber param)
	{
	
		Objects.requireNonNull(param, "parameter cannot be null");
	
		if (param.numerator.equals(BigInteger.ZERO)) {
			throw new IllegalArgumentException("param cannot be 0"); }
	
		return this.multiply(new BigNumber(param.denominator, param.numerator));
	
	}

	public String toString()
	{
	
		return this.numerator + " / " + this.denominator;
	
	}

	void main()
	{
	
		var num = new BigNumber();
	
		num
			.add(3)
			.multiply(2)
			.subtract(1)
			.divide(5)
			;
	
	}

   //code review additions

}