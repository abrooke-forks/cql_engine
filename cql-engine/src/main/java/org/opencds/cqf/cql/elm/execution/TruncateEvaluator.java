package org.opencds.cqf.cql.elm.execution;

import org.opencds.cqf.cql.execution.Context;

import java.math.BigDecimal;

/*
Truncate(argument Decimal) Integer

The Truncate operator returns the integer component of its argument.
When invoked with an Integer argument, the argument will be implicitly converted to Decimal.
If the argument is null, the result is null.
*/

/**
 * Created by Bryn on 5/25/2016.
 */
public class TruncateEvaluator extends org.cqframework.cql.elm.execution.Truncate {

    @Override
    public Object doOperation(BigDecimal operand) {
        return operand.compareTo(new BigDecimal("0")) < 0
                ? operand.setScale(0, BigDecimal.ROUND_CEILING).intValue()
                : operand.setScale(0, BigDecimal.ROUND_FLOOR).intValue();
    }

    @Override
    public Object evaluate(Context context) {
        Object operand = getOperand().evaluate(context);

        if (operand == null) { return null; }

        return Execution.resolveArithmeticDoOperation(this, operand);
    }
}
