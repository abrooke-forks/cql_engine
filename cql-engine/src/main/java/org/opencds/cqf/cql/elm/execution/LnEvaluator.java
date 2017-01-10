package org.opencds.cqf.cql.elm.execution;

import org.opencds.cqf.cql.execution.Context;

import java.math.BigDecimal;
import java.math.RoundingMode;

/*
Ln(argument Decimal) Decimal

The Ln operator computes the natural logarithm of its argument.
When invoked with an Integer argument, the argument will be implicitly converted to Decimal.
If the argument is null, the result is null.
*/

/**
 * Created by Bryn on 5/25/2016.
 */
public class LnEvaluator extends org.cqframework.cql.elm.execution.Ln {

    private BigDecimal verifyPrecision(BigDecimal operand) {
        if (operand.precision() > 8) {
            return operand.setScale(8, RoundingMode.FLOOR);
        }
        return operand;
    }

    @Override
    public Object doOperation(BigDecimal operand) {
        BigDecimal retVal;
        try {
            retVal = verifyPrecision(new BigDecimal(Math.log(operand.doubleValue())));
        } catch (NumberFormatException nfe){
            if (operand.compareTo(new BigDecimal(0)) < 0) {
                return null;
            }
            else if (operand.compareTo(new BigDecimal(0)) == 0) {
                throw new ArithmeticException("Results in negative infinity");
            }
            else { throw new NumberFormatException(); }
        }
        return retVal;
    }

    @Override
    public Object evaluate(Context context) {
        Object operand = getOperand().evaluate(context);

        if (operand == null) { return null; }

        return Execution.resolveArithmeticDoOperation(this, operand);
    }
}
