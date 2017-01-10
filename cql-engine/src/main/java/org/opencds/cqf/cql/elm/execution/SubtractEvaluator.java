package org.opencds.cqf.cql.elm.execution;

import org.opencds.cqf.cql.execution.Context;
import org.opencds.cqf.cql.runtime.*;

import java.math.BigDecimal;

/*
*** NOTES FOR ARITHMETIC OPERATOR ***
-(left Integer, right Integer) Integer
-(left Decimal, right Decimal) Decimal
-(left Quantity, right Quantity) Quantity

The subtract (-) operator performs numeric subtraction of its arguments.
When invoked with mixed Integer and Decimal arguments, the Integer argument will be implicitly converted to Decimal.
When subtracting quantities, the dimensions of each quantity must be the same, but not necessarily the unit.
  For example, units of 'cm' and 'm' can be subtracted, but units of 'cm2' and  'cm' cannot.
    The unit of the result will be the most granular unit of either input.
If either argument is null, the result is null.

*** NOTES FOR DATETIME ***
-(left DateTime, right Quantity) DateTime
-(left Time, right Quantity) Time

The subtract (-) operator returns the value of the given date/time, decremented by the time-valued quantity,
  respecting variable length periods for calendar years and months.
For DateTime values, the quantity unit must be one of: years, months, days, hours, minutes, seconds, or milliseconds.
For Time values, the quantity unit must be one of: hours, minutes, seconds, or milliseconds.
The operation is performed by attempting to derive the highest granularity precision first, working down successive
  granularities to the granularity of the time-valued quantity. For example, the following subtraction:
    DateTime(2014) - 24 months
    This example results in the value DateTime(2012) even though the date/time value is not specified to the level of precision of the time-valued quantity.
If either argument is null, the result is null.
NOTE: see note in AddEvaluator
*/

/**
 * Created by Bryn on 5/25/2016 
 */
public class SubtractEvaluator extends org.cqframework.cql.elm.execution.Subtract {

  private static final int YEAR_RANGE_MIN = 0001;

  @Override
  public Object doOperation(Integer leftOperand, Integer rightOperand) {
    return leftOperand - rightOperand;
  }

  @Override
  public Object doOperation(BigDecimal leftOperand, BigDecimal rightOperand) {
    return leftOperand.subtract(rightOperand);
  }

  @Override
  public Object doOperation(Quantity leftOperand, Quantity rightOperand) {
    return new Quantity().withValue(leftOperand.getValue().subtract(rightOperand.getValue())).withUnit(leftOperand.getUnit());
  }

  @Override
  public Object doOperation(DateTime leftOperand, Quantity rightOperand) {
    return leftOperand.getPartial().getValue(0) < YEAR_RANGE_MIN ?
            new ArithmeticException("The date time subtraction results in a year less than the accepted range.") :
            new AddEvaluator().doOperation(leftOperand, rightOperand.withValue(rightOperand.getValue().negate()));
  }

  @Override
  public Object doOperation(Time leftOperand, Quantity rightOperand) {
    return new AddEvaluator().doOperation(leftOperand, rightOperand.withValue(rightOperand.getValue().negate()));
  }

  // NOTE: this operation signature is not specified in the spec.
  // It is being used for uncertainty arithmetic
  @Override
  public Object doOperation(Uncertainty leftOperand, Uncertainty rightOperand) {
    Interval leftInterval = leftOperand.getUncertaintyInterval();
    Interval rightInterval = rightOperand.getUncertaintyInterval();
    return new Uncertainty().withUncertaintyInterval(new Interval(
            Execution.resolveSharedDoOperation(this, leftInterval.getStart(), rightInterval.getStart()), true,
            Execution.resolveSharedDoOperation(this, leftInterval.getEnd(), rightInterval.getEnd()), true));
  }

    @Override
    public Object evaluate(Context context) {
        Object left = getOperand().get(0).evaluate(context);
        Object right = getOperand().get(1).evaluate(context);

        if (left == null || right == null) { return null; }

        return Execution.resolveSharedDoOperation(this, left, right);
    }
}
