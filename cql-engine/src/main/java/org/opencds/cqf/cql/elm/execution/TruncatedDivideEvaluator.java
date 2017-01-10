package org.opencds.cqf.cql.elm.execution;

import org.opencds.cqf.cql.execution.Context;
import org.opencds.cqf.cql.runtime.Interval;
import org.opencds.cqf.cql.runtime.Uncertainty;

import java.math.BigDecimal;

/*
div(left Integer, right Integer) Integer
div(left Decimal, right Decimal) Decimal

The div operator performs truncated division of its arguments.
When invoked with mixed Integer and Decimal arguments, the Integer argument will be implicitly converted to Decimal.
If either argument is null, the result is null.
*/

/**
 * Created by Bryn on 5/25/2016.
 */
public class TruncatedDivideEvaluator extends org.cqframework.cql.elm.execution.TruncatedDivide {

  @Override
  public Object doOperation(Integer leftOperand, Integer rightOperand) {
    if (rightOperand == 0) { return null; }
    return leftOperand / rightOperand;
  }

  @Override
  public Object doOperation(BigDecimal leftOperand, BigDecimal rightOperand) {
    if (rightOperand.compareTo(new BigDecimal("0")) == 0) { return null; }
    return leftOperand.divideAndRemainder(rightOperand)[0];
  }

  // NOTE: this operation signature is not specified in the spec.
  // It is being used for uncertainty arithmetic
  @Override
  public Object doOperation(Uncertainty leftOperand, Uncertainty rightOperand) {
    Interval leftInterval = leftOperand.getUncertaintyInterval();
    Interval rightInterval = rightOperand.getUncertaintyInterval();

    if ((Boolean) Execution.resolveComparisonDoOperation(new EqualEvaluator(), rightInterval.getStart(), 0)
            || (Boolean) Execution.resolveComparisonDoOperation(new EqualEvaluator(), rightInterval.getEnd(), 0))
    {
      return null;
    }

    return new Uncertainty().withUncertaintyInterval(new Interval(
            Execution.resolveSharedDoOperation(this, leftInterval.getStart(), rightInterval.getStart()), true,
            Execution.resolveSharedDoOperation(this, leftInterval.getEnd(), rightInterval.getEnd()), true));
  }

    @Override
    public Object evaluate(Context context) {
        Object left = getOperand().get(0).evaluate(context);
        Object right = getOperand().get(1).evaluate(context);

        if (left == null || right == null) { return null; }

        return Execution.resolveArithmeticDoOperation(this, left, right);
    }
}
