package org.opencds.cqf.cql.elm.execution;

import org.opencds.cqf.cql.execution.Context;
import org.opencds.cqf.cql.runtime.Quantity;
import java.math.BigDecimal;

/*
-(argument Integer) Integer
-(argument Decimal) Decimal
-(argument Quantity) Quantity

The negate (-) operator returns the negative of its argument.
When negating quantities, the unit is unchanged.
If the argument is null, the result is null.
*/

/**
 * Created by Bryn on 5/25/2016.
 */
public class NegateEvaluator extends org.cqframework.cql.elm.execution.Negate {

    @Override
    public Object doOperation(Integer operand) {
        return -operand;
    }

    @Override
    public Object doOperation(BigDecimal operand) {
        return operand.negate();
    }

    @Override
    public Object doOperation(Quantity operand) {
        return operand.withValue(operand.getValue().negate());
    }

    @Override
    public Object evaluate(Context context) {
        Object operand = getOperand().evaluate(context);

        if (operand == null) { return null; }

        return Execution.resolveArithmeticDoOperation(this, operand);
    }
}
