package org.opencds.cqf.cql.elm.execution;

import org.opencds.cqf.cql.execution.Context;
import org.opencds.cqf.cql.runtime.Quantity;

import java.math.BigDecimal;
import java.math.RoundingMode;

/*
/(left Decimal, right Decimal) Decimal
/(left Quantity, right Decimal) Quantity
/(left Quantity, right Quantity) Quantity

The divide (/) operator performs numeric division of its arguments.
Note that this operator is Decimal division; for Integer division, use the truncated divide (div) operator.
When invoked with Integer arguments, the arguments will be implicitly converted to Decimal.
TODO: For division operations involving quantities, the resulting quantity will have the appropriate unit. For example:
12 'cm2' / 3 'cm'
In this example, the result will have a unit of 'cm'.
If either argument is null, the result is null.
*/

/**
 * Created by Bryn on 5/25/2016.
 */
public class DivideEvaluator extends org.cqframework.cql.elm.execution.Divide {

  private Object verifyPrecision(BigDecimal operand) {
    if (operand.precision() > 8) {
      return operand.setScale(8, RoundingMode.FLOOR);
    }
    return operand;
  }

  private Object verifyPrecision(Quantity operand) {
    if (operand.getValue().precision() > 8) {
      return operand.getValue().setScale(8, RoundingMode.FLOOR);
    }
    return operand;
  }

  @Override
  public Object doOperation(BigDecimal leftOperand, BigDecimal rightOperand) {
    if (rightOperand.equals(new BigDecimal("0"))) { return null; }
    try {
      return verifyPrecision(leftOperand.divide(rightOperand));
    } catch (ArithmeticException ae) {
      return leftOperand.divide(rightOperand, 8, RoundingMode.FLOOR);
    }
  }

  @Override
  public Object doOperation(Quantity leftOperand, Quantity rightOperand) {
    if (rightOperand.getValue().equals(new BigDecimal("0"))) { return null; }
    try {
      return verifyPrecision(leftOperand.withValue(leftOperand.getValue().divide(rightOperand.getValue())));
    } catch (ArithmeticException ae) {
      return leftOperand.withValue(leftOperand.getValue().divide(rightOperand.getValue(), 8, RoundingMode.FLOOR));
    }
  }

  @Override
  public Object doOperation(Quantity leftOperand, BigDecimal rightOperand) {
    if (rightOperand.equals(new BigDecimal("0"))) { return null; }
    try {
      return verifyPrecision(leftOperand.withValue(leftOperand.getValue().divide(rightOperand)));
    } catch (ArithmeticException ae) {
      return leftOperand.withValue(leftOperand.getValue().divide(rightOperand, 8, RoundingMode.FLOOR));
    }
  }

    @Override
    public Object evaluate(Context context) {
        Object left = getOperand().get(0).evaluate(context);
        Object right = getOperand().get(1).evaluate(context);

      if (left == null || right == null) {
        return null;
      }

        return Execution.resolveArithmeticDoOperation(this, left, right);
    }
}
