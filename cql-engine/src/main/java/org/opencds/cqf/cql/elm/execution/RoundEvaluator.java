package org.opencds.cqf.cql.elm.execution;

import org.opencds.cqf.cql.execution.Context;
import java.math.BigDecimal;
import java.math.RoundingMode;

/*
Round(argument Decimal) Decimal
Round(argument Decimal, precision Integer) Decimal

The Round operator returns the nearest whole number to its argument. The semantics of round are defined as a traditional
  round, meaning that a decimal value of 0.5 or higher will round to 1.
When invoked with an Integer argument, the argument will be implicitly converted to Decimal.
If the argument is null, the result is null.
Precision determines the decimal place at which the rounding will occur.
If precision is not specified or null, 0 is assumed.
*/

/**
 * Created by Bryn on 5/25/2016.
 */
public class RoundEvaluator extends org.cqframework.cql.elm.execution.Round {

    @Override
    public Object doOperation(BigDecimal operand) {
        RoundingMode rm = operand.compareTo(new BigDecimal("0")) < 0 ? RoundingMode.HALF_DOWN : RoundingMode.HALF_UP;
        return operand.setScale(0, rm);
    }

    @Override
    public Object doOperation(BigDecimal leftOperand, Integer rightOperand) {
        RoundingMode rm = leftOperand.compareTo(new BigDecimal("0")) < 0 ? RoundingMode.HALF_DOWN : RoundingMode.HALF_UP;
        return leftOperand.setScale(rightOperand, rm);
    }

    @Override
    public Object evaluate(Context context) {
        Object left = getOperand().evaluate(context);
        Object right = getPrecision() == null ? null : getPrecision().evaluate(context);

        if (left == null) { return null; }

        return Execution.resolveArithmeticDoOperation(this, left, right);
    }
}
