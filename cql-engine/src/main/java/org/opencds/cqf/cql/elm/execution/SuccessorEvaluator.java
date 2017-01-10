package org.opencds.cqf.cql.elm.execution;

import org.opencds.cqf.cql.execution.Context;
import org.opencds.cqf.cql.runtime.DateTime;
import org.opencds.cqf.cql.runtime.Interval;
import org.opencds.cqf.cql.runtime.Time;

import java.math.BigDecimal;

/*
successor of<T>(argument T) T

The successor operator returns the successor of the argument. For example, the successor of 1 is 2.
  If the argument is already the maximum value for the type, a run-time error is thrown.
The successor operator is defined for the Integer, Decimal, DateTime, and Time types.
For Integer, successor is equivalent to adding 1.
For Decimal, successor is equivalent to adding the minimum precision value for the Decimal type, or 10^-08.
For DateTime and Time values, successor is equivalent to adding a time-unit quantity for the lowest specified precision of the value.
  For example, if the DateTime is fully specified, successor is equivalent to adding 1 millisecond;
    if the DateTime is specified to the second, successor is equivalent to adding one second, etc.
If the argument is null, the result is null.
*/

/**
 * Created by Bryn on 5/25/2016.
 */
public class SuccessorEvaluator extends org.cqframework.cql.elm.execution.Successor {

    @Override
    public Object doOperation(Integer operand) {
        return Interval.successor(operand);
    }

    @Override
    public Object doOperation(BigDecimal operand) {
        return Interval.successor(operand);
    }

    @Override
    public Object doOperation(DateTime operand) {
        return Interval.successor(operand);
    }

    @Override
    public Object doOperation(Time operand) {
        return Interval.successor(operand);
    }

    @Override
    public Object evaluate(Context context) {
        Object operand = this.getOperand().evaluate(context);

        if (operand == null) { return null; }

        return Execution.resolveArithmeticDoOperation(this, operand);
    }
}
