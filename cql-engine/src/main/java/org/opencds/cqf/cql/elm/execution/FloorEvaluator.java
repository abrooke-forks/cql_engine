package org.opencds.cqf.cql.elm.execution;

import org.opencds.cqf.cql.execution.Context;

import java.math.BigDecimal;

/*
Floor(argument Decimal) Integer

The Floor operator returns the first integer less than or equal to the argument.
When invoked with an Integer argument, the argument will be implicitly converted to Decimal.
If the argument is null, the result is null.
*/

/**
 * Created by Bryn on 5/25/2016.
 */
public class FloorEvaluator extends org.cqframework.cql.elm.execution.Floor {

    @Override
    public Object doOperation(BigDecimal operand) {
        return BigDecimal.valueOf(Math.floor(operand.doubleValue())).intValue();
    }


    @Override
    public Object evaluate(Context context) {
        Object operand = getOperand().evaluate(context);

        if (operand == null) { return null; }

        return Execution.resolveArithmeticDoOperation(this, operand);
    }
}
