package org.opencds.cqf.cql.elm.execution;

import org.opencds.cqf.cql.execution.Context;
import org.opencds.cqf.cql.runtime.Interval;

/*
meets(left Interval<T>, right Interval<T>) Boolean

The meets operator returns true if the first interval ends immediately before the second interval starts,
  or if the first interval starts immediately after the second interval ends.
In other words, if the ending point of the first interval is equal to the predecessor of the starting point of the second,
  or if the starting point of the first interval is equal to the successor of the ending point of the second.
If either argument is null, the result is null.
*/

/**
* Created by Chris Schuler on 6/8/2016
*/
public class MeetsEvaluator extends org.cqframework.cql.elm.execution.Meets {

  @Override
  public Object doOperation(Interval leftOperand, Interval rightOperand) {
    Object leftStart = leftOperand.getStart();
    Object leftEnd = leftOperand.getEnd();
    Object rightStart = rightOperand.getStart();
    Object rightEnd = rightOperand.getEnd();

    if (leftStart == null || leftEnd == null
            || rightStart == null || rightEnd == null) { return null; }

    return ((Boolean) Execution.resolveComparisonDoOperation(new GreaterEvaluator(), rightStart, leftEnd)) ?
            (Boolean) Execution.resolveComparisonDoOperation(new EqualEvaluator(), rightStart, Interval.successor(leftEnd)) :
            (Boolean) Execution.resolveComparisonDoOperation(new EqualEvaluator(), leftStart, Interval.successor(rightEnd));
  }

  @Override
  public Object evaluate(Context context) {
    Interval left = (Interval)getOperand().get(0).evaluate(context);
    Interval right = (Interval)getOperand().get(1).evaluate(context);

    if (left == null || right == null) { return null; }

    return Execution.resolveIntervalDoOperation(this, left, right);
  }
}
