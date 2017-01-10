package org.opencds.cqf.cql.elm.execution;

import org.opencds.cqf.cql.execution.Context;
import java.math.BigDecimal;
import java.math.RoundingMode;

/*
^(argument Integer, exponent Integer) Integer
^(argument Decimal, exponent Decimal) Decimal

The power (^) operator raises the first argument to the power given by the second argument.
When invoked with mixed Integer and Decimal arguments, the Integer argument will be implicitly converted to Decimal.
If either argument is null, the result is null.
*/

/**
 * Created by Bryn on 5/25/2016.
 */
public class PowerEvaluator extends org.cqframework.cql.elm.execution.Power {

    private BigDecimal verifyPrecision(BigDecimal operand) {
        if (operand.precision() > 8) {
            return operand.setScale(8, RoundingMode.FLOOR);
        }
        return operand;
    }

    @Override
    public Object doOperation(Integer leftOperand, Integer rightOperand) {
        if (rightOperand < 0) {
            return verifyPrecision(new BigDecimal(Math.pow((double) leftOperand, (double) rightOperand)));
        }
        return new BigDecimal(Math.pow((double) leftOperand, (double) rightOperand)).intValue();
    }

    @Override
    public Object doOperation(BigDecimal leftOperand, BigDecimal rightOperand) {
        return verifyPrecision(new BigDecimal(Math.pow((leftOperand.doubleValue()), rightOperand.doubleValue())));
    }

    @Override
    public Object evaluate(Context context) {
        Object left = getOperand().get(0).evaluate(context);
        Object right = getOperand().get(1).evaluate(context);

        if (left == null || right == null) { return null; }

        return Execution.resolveArithmeticDoOperation(this, left, right);
    }
}
