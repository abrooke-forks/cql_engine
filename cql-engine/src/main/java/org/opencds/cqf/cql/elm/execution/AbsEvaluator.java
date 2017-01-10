package org.opencds.cqf.cql.elm.execution;

import org.opencds.cqf.cql.execution.Context;
import org.opencds.cqf.cql.runtime.Quantity;

import java.math.BigDecimal;

/*
Abs(argument Integer) Integer
Abs(argument Decimal) Decimal
Abs(argument Quantity) Quantity

The Abs operator returns the absolute value of its argument.
When taking the absolute value of a quantity, the unit is unchanged.
If the argument is null, the result is null.
*/

/**
 * Created by Bryn on 5/24/2016.
 */
public class AbsEvaluator extends org.cqframework.cql.elm.execution.Abs {

    @Override
    public Object doOperation(Integer operand) {
        return Math.abs(operand);
    }

    @Override
    public Object doOperation(BigDecimal operand) {
        return operand.abs();
    }

    @Override
    public Object doOperation(Quantity operand) {
        return operand.getValue().abs();
    }

    @Override
    public Object evaluate(Context context) {
        Object value = getOperand().evaluate(context);

        if (value == null) {
            return null;
        }

        return Execution.resolveArithmeticDoOperation(this, value);
    }
}
