package org.opencds.cqf.cql.elm.execution;

import org.opencds.cqf.cql.execution.Context;

import java.math.BigDecimal;

/*
div(left Integer, right Integer) Integer
div(left Decimal, right Decimal) Decimal

The div operator performs truncated division of its arguments.
When invoked with mixed Integer and Decimal arguments, the Integer argument will be implicitly converted to Decimal.
If either argument is null, the result is null.
*/

public class TruncatedDivideEvaluator extends org.cqframework.cql.elm.execution.TruncatedDivide {

    public static Object div(Object left, Object right) {
        if (left == null || right == null) {
            return null;
        }

        if (left instanceof Integer) {
            if ((Integer)right == 0) {
                return null;
            }

            return (Integer)left / (Integer)right;
        }

        else if (left instanceof BigDecimal) {
            if (EqualEvaluator.equal(right, new BigDecimal("0.0"))) {
                return null;
            }

            return ((BigDecimal)left).divideAndRemainder((BigDecimal)right)[0];
        }

//        else if (left instanceof Uncertainty && right instanceof Uncertainty) {
//            Interval leftInterval = ((Uncertainty)left).getUncertaintyInterval();
//            Interval rightInterval = ((Uncertainty)right).getUncertaintyInterval();
//
//            if (EqualEvaluator.equal(rightInterval.getStart(), 0)
//                    || EqualEvaluator.equal(rightInterval.getEnd(), 0))
//            {
//                return null;
//            }
//
//            return new Uncertainty().withUncertaintyInterval(new Interval(div(leftInterval.getStart(), rightInterval.getStart()), true, div(leftInterval.getEnd(), rightInterval.getEnd()), true));
//        }

        throw new IllegalArgumentException(String.format("Cannot Div arguments of type '%s' and '%s'.", left.getClass().getName(), right.getClass().getName()));
    }

    @Override
    public Object evaluate(Context context) {
        Object left = getOperand().get(0).evaluate(context);
        Object right = getOperand().get(1).evaluate(context);

        return context.logTrace(this.getClass(), div(left, right), left, right);
    }
}
