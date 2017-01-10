package org.opencds.cqf.cql.elm.execution;

import org.opencds.cqf.cql.execution.Context;
import java.math.BigDecimal;
import java.math.RoundingMode;

/*
Log(argument Decimal, base Decimal) Decimal

The Log operator computes the logarithm of its first argument, using the second argument as the base.
When invoked with Integer arguments, the arguments will be implicitly converted to Decimal.
If either argument is null, the result is null.
*/

/**
 * Created by Bryn on 5/25/2016.
 */
public class LogEvaluator extends org.cqframework.cql.elm.execution.Log {

    private BigDecimal verifyPrecision(BigDecimal operand) {
        if (operand.precision() > 8) {
            return operand.setScale(8, RoundingMode.FLOOR);
        }
        return operand;
    }

    @Override
    public Object doOperation(BigDecimal leftOperand, BigDecimal rightOperand) {
        Double base = Math.log(rightOperand.doubleValue());
        Double value = Math.log(leftOperand.doubleValue());

        if (base == 0d) {
            return verifyPrecision(new BigDecimal(value));
        }

        return verifyPrecision(new BigDecimal(value / base));
    }

    @Override
    public Object evaluate(Context context) {
        Object left = getOperand().get(0).evaluate(context);
        Object right = getOperand().get(1).evaluate(context);

        if (left == null || right == null) { return null; }

        return Execution.resolveArithmeticDoOperation(this, left, right);
    }
}
