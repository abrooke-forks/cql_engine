package org.opencds.cqf.cql.elm.execution;

import org.opencds.cqf.cql.execution.Context;
import org.opencds.cqf.cql.runtime.Quantity;
import org.opencds.cqf.cql.runtime.Interval;
import org.opencds.cqf.cql.runtime.Uncertainty;
import java.math.BigDecimal;
import java.math.RoundingMode;

/*
*(left Integer, right Integer) Integer
*(left Decimal, right Decimal) Decimal
*(left Decimal, right Quantity) Quantity
*(left Quantity, right Decimal) Quantity
*(left Quantity, right Quantity) Quantity

The multiply (*) operator performs numeric multiplication of its arguments.
When invoked with mixed Integer and Decimal arguments, the Integer argument will be implicitly converted to Decimal.
TODO: For multiplication operations involving quantities, the resulting quantity will have the appropriate unit. For example:
12 'cm' * 3 'cm'
3 'cm' * 12 'cm2'
In this example, the first result will have a unit of 'cm2', and the second result will have a unit of 'cm3'.
If either argument is null, the result is null.
*/

/**
 * Created by Bryn on 5/25/2016.
 */
public class MultiplyEvaluator extends org.cqframework.cql.elm.execution.Multiply {

    private BigDecimal verifyPrecision(BigDecimal operand) {
      if (operand.precision() > 8) {
        return operand.setScale(8, RoundingMode.FLOOR);
      }
      return operand;
    }

    @Override
    public Object doOperation(Integer leftOperand, Integer rightOperand) {
      return leftOperand * rightOperand;
    }

    @Override
    public Object doOperation(BigDecimal leftOperand, BigDecimal rightOperand) {
      return verifyPrecision(leftOperand.multiply(rightOperand));
    }

    @Override
    public Object doOperation(Quantity leftOperand, Quantity rightOperand) {
      return leftOperand.withValue(
              verifyPrecision(leftOperand.getValue().multiply(rightOperand.getValue())));
    }

    @Override
    public Object doOperation(BigDecimal leftOperand, Quantity rightOperand) {
      return rightOperand.withValue(
              verifyPrecision(leftOperand.multiply(rightOperand.getValue())));
    }

    @Override
    public Object doOperation(Quantity leftOperand, BigDecimal rightOperand) {
      return leftOperand.withValue(
              verifyPrecision(leftOperand.getValue().multiply(rightOperand)));
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

      return Execution.resolveArithmeticDoOperation(this, left, right);
    }
}
