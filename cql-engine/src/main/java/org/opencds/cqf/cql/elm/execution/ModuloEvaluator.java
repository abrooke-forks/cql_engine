package org.opencds.cqf.cql.elm.execution;

import org.opencds.cqf.cql.execution.Context;
import java.math.BigDecimal;
import java.math.RoundingMode;

/*
mod(left Integer, right Integer) Integer
mod(left Decimal, right Decimal) Decimal

The mod operator computes the remainder of the division of its arguments.
When invoked with mixed Integer and Decimal arguments, the Integer argument will be implicitly converted to Decimal.
If either argument is null, the result is null.
*/

/**
 * Created by Bryn on 5/25/2016.
 */
public class ModuloEvaluator extends org.cqframework.cql.elm.execution.Modulo {

    private BigDecimal verifyPrecision(BigDecimal operand) {
        if (operand.precision() > 8) {
            return operand.setScale(8, RoundingMode.FLOOR);
        }
        return operand;
    }

    @Override
    public Object doOperation(Integer leftOperand, Integer rightOperand) {
        return rightOperand == 0 ? null : leftOperand % rightOperand;
    }

    @Override
    public Object doOperation(BigDecimal leftOperand, BigDecimal rightOperand) {
        return rightOperand.equals(new BigDecimal("0")) ? null :
                    verifyPrecision(leftOperand.remainder(rightOperand));
    }

    @Override
    public Object evaluate(Context context) {
        Object left = getOperand().get(0).evaluate(context);
        Object right = getOperand().get(1).evaluate(context);

        if (left == null || right == null) { return null; }

        return Execution.resolveArithmeticDoOperation(this, left, right);
    }
}
