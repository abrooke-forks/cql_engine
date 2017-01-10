package org.opencds.cqf.cql.elm.execution;

import org.opencds.cqf.cql.execution.Context;
import org.opencds.cqf.cql.runtime.Interval;

/*
width of(argument Interval<T>) T

The width operator returns the width of an interval.
The result of this operator is equivalent to invoking: (start of argument – end of argument) + point-size.
Note that because CQL defines duration and difference operations for date/time and time valued intervals,
  width is not defined for intervals of these types.
If the argument is null, the result is null.
*/

/**
* Created by Chris Schuler 6/8/2016
*/
public class WidthEvaluator extends org.cqframework.cql.elm.execution.Width {

  @Override
  public Object doOperation(Interval operand) {
    return Interval.getSize(operand.getStart(), operand.getEnd());
  }

  @Override
  public Object evaluate(Context context) {
    Object operand = getOperand().evaluate(context);

    if (operand == null) { return null; }

    return Execution.resolveIntervalDoOperation(this, operand);
  }
}
