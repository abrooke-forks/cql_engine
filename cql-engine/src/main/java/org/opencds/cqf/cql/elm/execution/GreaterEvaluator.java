package org.opencds.cqf.cql.elm.execution;

import org.opencds.cqf.cql.execution.Context;
import org.opencds.cqf.cql.runtime.DateTime;
import org.opencds.cqf.cql.runtime.Quantity;
import org.opencds.cqf.cql.runtime.Time;
import org.opencds.cqf.cql.runtime.Uncertainty;

import java.math.BigDecimal;

/*
>(left Integer, right Integer) Boolean
>(left Decimal, right Decimal) Boolean
>(left Quantity, right Quantity) Boolean
>(left DateTime, right DateTime) Boolean
>(left Time, right Time) Boolean
>(left String, right String) Boolean

The greater (>) operator returns true if the first argument is greater than the second argument.
For comparisons involving quantities, the dimensions of each quantity must be the same, but not necessarily the unit.
  For example, units of 'cm' and 'm' are comparable, but units of 'cm2' and  'cm' are not.
For comparisons involving date/time or time values with imprecision, note that the result of the comparison may be null,
  depending on whether the values involved are specified to the level of precision used for the comparison.
If either argument is null, the result is null.
*/

/**
 * Created by Bryn on 5/25/2016.
 */
public class GreaterEvaluator extends org.cqframework.cql.elm.execution.Greater {

    @Override
    public Object doOperation(Integer leftOperand, Integer rightOperand) {
        return leftOperand.compareTo(rightOperand) > 0;
    }

    @Override
    public Object doOperation(BigDecimal leftOperand, BigDecimal rightOperand) {
        return leftOperand.compareTo(rightOperand) > 0;
    }

    @Override
    public Object doOperation(Quantity leftOperand, Quantity rightOperand) {
        return leftOperand.getValue().compareTo(rightOperand.getValue()) > 0;
    }

    @Override
    public Object doOperation(DateTime leftOperand, DateTime rightOperand) {
        return leftOperand.compareTo(rightOperand) > 0;
    }

    @Override
    public Object doOperation(Time leftOperand, Time rightOperand) {
        return leftOperand.compareTo(rightOperand) > 0;
    }

    @Override
    public Object doOperation(String leftOperand, String rightOperand) {
        return leftOperand.compareTo(rightOperand) > 0;
    }

    // NOTE: this operation signature is not specified in the spec.
    // It is being used for uncertainty comparisons
    @Override
    public Object doOperation(Uncertainty leftOperand, Integer rightOperand) {
        if ((Boolean) new InEvaluator().doOperation(rightOperand, leftOperand.getUncertaintyInterval())) {
            return null;
        }
        return ((Integer) leftOperand.getUncertaintyInterval().getStart()).compareTo(rightOperand) > 0;
    }

    @Override
    public Object evaluate(Context context) {
        Object left = getOperand().get(0).evaluate(context);
        Object right = getOperand().get(1).evaluate(context);

        if (left == null || right == null) { return null; }

        return Execution.resolveComparisonDoOperation(this, left, right);
    }
}
