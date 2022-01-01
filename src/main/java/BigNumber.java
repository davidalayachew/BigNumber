import java.lang.ArithmeticException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/** Let's improve BigInteger/BigDecimal. This class is immutable. */
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
    * Must always be unsigned.
    * Cannot be null.
    * 
    */
   private final BigInteger denominator;
   
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
      
      if (denominator.intValue() == 0) { throw new IllegalArgumentException("denominator cannot be 0"); }
      
      this.numerator = isPositive(denominator) ? numerator : numerator.negate();
      this.denominator = denominator.abs();
      
   }
   
   /**
    * 
    * Constructor.
    * 
    * @param param the new number.
    * 
    */
   public BigNumber(long param)
   {
   
      this(BigInteger.valueOf(param));
   
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
    * Returns a BigNumber equivalent to this, but with sign changed from positive to negative, or vice versa.
    * 
    * @return     a BigNumber that has had its sign flipped
    * 
    */
   public BigNumber negate()
   {
   
      return this.multiply(-1);
   
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
    * Returns the sign of the BigNumber, assuming that it is given the following numerator and denominator.
    * 
    * @param numerator     the numerator we are checking the sign of
    * @param denominator   the denominator we are checking the sign of
    * @return              boolean result
    * 
    */
   private static boolean isPositive(BigInteger numerator, BigInteger denominator)
   {
   
      return isPositive(numerator) == isPositive(denominator);
      
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
   
      BigInteger finalNumerator = numerator;
      BigInteger finalDenominator = denominator;
      
      BigInteger i = BigInteger.valueOf(2);
      
      while (i.compareTo(finalNumerator.abs()) <= 0)
      {
      
         if (isDivisible(finalNumerator, i) && isDivisible(finalDenominator, i))
         {
         
            finalNumerator = finalNumerator.divide(i);
            finalDenominator = finalDenominator.divide(i);
         
         }
         
         else
         {
         
            i = i.add(BigInteger.ONE);
         
         }
      
      }
      
      return new BigNumber(finalNumerator, finalDenominator);
   
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
   
      BigInteger finalNumerator;
      BigInteger finalDenominator;
   
      if (this.denominator.equals(param.getDenominator()))
      {
      
         finalNumerator = this.numerator.add(param.getNumerator());
         finalDenominator = BigInteger.ONE;
      
      }
      
      else
      {
      
         //our goal here is to end up with a shared denominator
         //So, in order to accomplish that, we will need to do
         //that cross multiplication thing that you do to end
         //up with the same denominator
      
         BigInteger numerator1 = this.numerator.multiply(param.denominator);
         BigInteger numerator2 = param.numerator.multiply(this.denominator);
         
         finalNumerator = numerator1.add(numerator2);
         finalDenominator = param.getDenominator().multiply(this.getDenominator());
         
      }
      
      //simplify before we return. We don't want to waste memory when the number can be simplified into something smaller.   
      return simplify(finalNumerator, finalDenominator);
   
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
   
      return this.add(new BigNumber(param * -1));
   
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
      
      BigInteger finalNumerator = this.numerator.multiply(param.numerator);
      BigInteger finalDenominator = this.denominator.multiply(param.denominator);
      
      return simplify(finalNumerator, finalDenominator);
   
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
   
      if (param == 0) { throw new IllegalArgumentException("param cannot be 0"); }
      
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
      
      if (param.numerator.equals(BigInteger.ZERO)) { throw new IllegalArgumentException("param cannot be 0"); }
      
      return this.multiply(new BigNumber(param.denominator, param.numerator));
   
   }
   
   /**
    * 
    * Method to tell if num can be cleanly divided (divide with no remainder) by the divider.
    * 
    * @param num        number to be divided.
    * @param divider    divider that the num will be divided by.
    * @return           boolean result
    * @throws NullPointerException if num is null
    * @throws IllegalArgumentException if divider == 0
    * 
    */
   public static boolean isDivisible(BigInteger num, long divider)
   {
   
      Objects.requireNonNull(num, "num cannot be null");
   
      if (divider == 0) { throw new IllegalArgumentException("divider cannot be 0"); }
      
      return isDivisible(num, BigInteger.valueOf(divider));
   
   }
   
   /**
    * 
    * Method to tell if num can be cleanly divided (divide with no remainder) by the divider.
    * 
    * @param num        number to be divided.
    * @param divider    divider that the num will be divided by.
    * @return           boolean result
    * @throws NullPointerException if num or divider is null
    * @throws IllegalArgumentException if divider == 0
    * 
    */
   public static boolean isDivisible(BigInteger num, BigInteger divider)
   {
   
      Objects.requireNonNull(num, "num cannot be null");
      Objects.requireNonNull(num, "divider cannot be null");
   
      if (divider.equals(BigInteger.ZERO)) { throw new IllegalArgumentException("divider cannot be 0"); }
      
      return num.remainder(divider).equals(BigInteger.ZERO);
   
   }
   
   /** {@inheritDoc} */
   public String toString()
   {
   
      return this.numerator + " / " + this.denominator;
   
   }
   
   /**
    * 
    * Main method, useful for figuring out basic functionality.
    * 
    * @param args    commandline arguments.
    * 
    */
   public static void main(String[] args)
   {
   
      doMain(3, 3);
   
   }
   
   /**
    * 
    * Method to do the main demo.
    * 
    * @param a    first
    * @param b    second
    * 
    */
   public static void doMain(int a, int b)
   {
   
      System.out.println(a + ",\t" + b + "\t\t" + simplify(BigInteger.valueOf(a), BigInteger.valueOf(b)));
      
   }

}