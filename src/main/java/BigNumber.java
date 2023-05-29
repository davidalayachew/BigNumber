import java.math.BigInteger;
import java.util.Objects;

/** If you know BigInteger and BigDecimal, think of this as BigFraction. This class is immutable. */
public final class BigNumber
{

   /**
    *
    * The numerator.
    *
    * Can be any whole number.
    * Must always contain the negative symbol if the whole "BigNumber" is negative.
    * Cannot be null;
    *
    */
   private final BigInteger numerator;

   /**
    *
    * The denominator.
    *
    * Can be any whole number except for zero.
    * Must be strictly positive.
    * Cannot be null.
    *
    */
   private final BigInteger denominator;

   /**
    *
    * Constructor.
    *
    * @param param the new number.
    *
    */
   public BigNumber(long param)
   {
   
      this(BigInteger.valueOf(param), BigInteger.ONE);
   
   }

   /**
    *
    * Constructor.
    *
    * @param param the new number.
    * @throws NullPointerException if parameter is null
    *
    */
   public BigNumber(BigInteger param)
   {
   
      Objects.requireNonNull(param, "parameter cannot be null");
   
      this.numerator = param;
      this.denominator = BigInteger.ONE;
   
   }

   /**
    *
    * Constructor.
    *
    * @param numerator        the numerator.
    * @param denominator      the denominator.
    * @throws NullPointerException        if numerator or denominator is null
    * @throws IllegalArgumentException    if denominator is 0
    *
    */
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

   /**
    *
    * Constructor.
    *
    * @param param the new number.
    * @throws NullPointerException if parameter is null
    *
    */
   public BigNumber(BigNumber param)
   {
   
      this(Objects.requireNonNull(param, "BigNumber cannot be null").numerator, param.denominator);
   
   }

   /**
    *
    * Returns the (signed) numerator.
    *
    * @return     the numerator.
    *
    */
   public BigInteger getNumerator()
   {
   
      return this.numerator;
   
   }

   /**
    *
    * Returns the (unsigned) denominator.
    *
    * @return     the denominator.
    *
    */
   public BigInteger getDenominator()
   {
   
      return this.denominator;
   
   }

   /**
    *
    * Returns an equivalent BigNumber, but with the sign changed from positive to negative, or vice versa.
    *
    * @return     a BigNumber that has had its sign flipped
    *
    */
   public BigNumber negate()
   {
   
      return new BigNumber(this.numerator.negate(), this.denominator);
   
   }

   /**
    *
    * Returns true if positive.
    *
    * @return     the result
    *
    */
   public boolean isPositive()
   {
   
      return isPositive(this.numerator);
   
   }

   /**
    *
    * Returns true if num is >0.
    *
    * @param num  the number we are checking the sign of
    * @return     boolean result
    *
    */
   private static boolean isPositive(BigInteger num)
   {
   
      return num.signum() > 0;
   
   }

   /**
    *
    * Method to simplify the numerator and denominator before creating a BigNumber from them.
    *
    * Numerator cannot be null.
    * Denominator cannot be null.
    * Denominator cannot be 0.
    *
    * @param numerator     the numerator
    * @param denominator   the denominator
    * @return              the simplified BigNumber
    *
    */
   private static BigNumber simplify(BigInteger numerator, BigInteger denominator)
   {
   
      final var gcd = numerator.gcd(denominator);
   
      return new BigNumber(numerator.divide(gcd), denominator.divide(gcd));
   
   }

   /**
    *
    * Standard add function.
    *
    * @param param      the number to add.
    * @return           The answer.
    *
    */
   public BigNumber add(long param)
   {
   
      return this.add(new BigNumber(param));
   
   }

   /**
    *
    * Standard add function.
    *
    * @param param      the number to add.
    * @return           The answer.
    * @throws NullPointerException if parameter is null
    *
    */
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

   /**
    *
    * Standard subtract function.
    *
    * @param param      the number to subtract from this.
    * @return           The answer.
    *
    */
   public BigNumber subtract(long param)
   {
   
      return this.add(new BigNumber(param).negate());
   
   }

   /**
    *
    * Standard subtract function.
    *
    * @param param      the number to subtract from this.
    * @return           The answer.
    *
    */
   public BigNumber subtract(BigNumber param)
   {
   
      Objects.requireNonNull(param, "BigNumber cannot be null");
   
      return this.add(param.negate());
   
   }

   /**
    *
    * Standard multiply function.
    *
    * @param param      the number to multiply.
    * @return           The answer.
    *
    */
   public BigNumber multiply(long param)
   {
   
      return this.multiply(new BigNumber(param));
   
   }

   /**
    *
    * Standard multiply function.
    *
    * @param param      the number to multiply.
    * @return           The answer.
    * @throws NullPointerException if parameter is null
    *
    */
   public BigNumber multiply(BigNumber param)
   {
   
      Objects.requireNonNull(param, "parameter cannot be null");
   
      final BigInteger resultNumerator = this.numerator.multiply(param.numerator);
      final BigInteger resultDenominator = this.denominator.multiply(param.denominator);
   
      return simplify(resultNumerator, resultDenominator);
   
   }

   /**
    *
    * Standard divide function.
    *
    * @param param      the number to divide this by.
    * @return           The answer.
    * @throws IllegalArgumentException if param == 0
    *
    */
   public BigNumber divide(long param)
   {
   
      if (param == 0) {
         throw new IllegalArgumentException("param cannot be 0"); }
   
      return this.divide(new BigNumber(param));
   
   }

   /**
    *
    * Standard divide function.
    *
    * @param param      the number to divide this by.
    * @return           The answer.
    * @throws NullPointerException if parameter is null
    * @throws IllegalArgumentException if param == 0
    *
    */
   public BigNumber divide(BigNumber param)
   {
   
      Objects.requireNonNull(param, "parameter cannot be null");
   
      if (param.numerator.equals(BigInteger.ZERO)) {
         throw new IllegalArgumentException("param cannot be 0"); }
   
      return this.multiply(new BigNumber(param.denominator, param.numerator));
   
   }

   /** {@inheritDoc} */
   public String toString()
   {
   
      return this.numerator + " / " + this.denominator;
   
   }

}