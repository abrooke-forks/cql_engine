package org.opencds.cqf.cql.elm.execution;

import org.opencds.cqf.cql.execution.Context;
import org.opencds.cqf.cql.runtime.Interval;

/*
overlaps(left Interval<T>, right Interval<T>) Boolean

The overlaps operator returns true if the first interval overlaps the second.
  More precisely, if the ending point of the first interval is greater than or equal to the starting point of the second interval,
    and the starting point of the first interval is less than or equal to the ending point of the second interval.
If either argument is null, the result is null.
*/

/**
* Created by Chris Schuler on 6/8/2016
*/
public class OverlapsEvaluator extends org.cqframework.cql.elm.execution.Overlaps {

  @Override
  public Object doOperation(Interval leftOperand, Interval rightOperand) {
    if (leftOperand == null || rightOperand == null) { return null; }

    Object leftStart = leftOperand.getStart();
    Object leftEnd = leftOperand.getEnd();
    Object rightStart = rightOperand.getStart();
    Object rightEnd = rightOperand.getEnd();

    if (leftStart == null || leftEnd == null || rightStart == null || rightEnd == null) { return null; }

    return ((Boolean) Execution.resolveComparisonDoOperation(new LessOrEqualEvaluator(), leftStart, rightEnd)
                && (Boolean) Execution.resolveComparisonDoOperation(new LessOrEqualEvaluator(), rightStart, leftEnd));
  }

  @Override
  public Object evaluate(Context context) {
    Object left = getOperand().get(0).evaluate(context);
    Object right = getOperand().get(1).evaluate(context);

    if (left == null || right == null) { return null; }

    return Execution.resolveIntervalDoOperation(this, left, right);
  }
}
