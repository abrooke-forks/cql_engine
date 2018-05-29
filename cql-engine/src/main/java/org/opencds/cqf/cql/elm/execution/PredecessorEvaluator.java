package org.opencds.cqf.cql.elm.execution;

import org.opencds.cqf.cql.execution.Context;
import org.opencds.cqf.cql.runtime.Value;

/*
predecessor of<T>(argument T) T

The predecessor operator returns the predecessor of the argument.
  For example, the predecessor of 2 is 1. If the argument is already the minimum value for the type, a run-time error is thrown.
The predecessor operator is defined for the Integer, Decimal, DateTime, and Time types.
For Integer, predecessor is equivalent to subtracting 1.
For Decimal, predecessor is equivalent to subtracting the minimum precision value for the Decimal type, or 10^-08.
For DateTime and Time values, predecessor is equivalent to subtracting a time-unit quantity for the lowest specified precision of the value.
  For example, if the DateTime is fully specified, predecessor is equivalent to subtracting 1 millisecond;
    if the DateTime is specified to the second, predecessor is equivalent to subtracting one second, etc.
If the argument is null, the result is null.
*/

public class PredecessorEvaluator extends org.cqframework.cql.elm.execution.Predecessor {

    public static Object predecessor(Object operand) {
        if (operand == null) {
            return null;
        }

        return Value.predecessor(operand);
    }

    @Override
    public Object evaluate(Context context) {
        Object operand = getOperand().evaluate(context);
        return context.logTrace(this.getClass(), predecessor(operand), operand);
    }
}
