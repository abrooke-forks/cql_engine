package org.opencds.cqf.cql.elm.execution;

import org.opencds.cqf.cql.execution.Context;
import org.opencds.cqf.cql.runtime.Interval;

/*
starts(left Interval<T>, right Interval<T>) Boolean

The starts operator returns true if the first interval starts the second.
  More precisely, if the starting point of the first is equal to the starting point of the second interval and the
    ending point of the first interval is less than or equal to the ending point of the second interval.
This operator uses the semantics described in the Start and End operators to determine interval boundaries.
If either argument is null, the result is null.
*/

/**
* Created by Chris Schuler on 6/8/2016
*/
public class StartsEvaluator extends org.cqframework.cql.elm.execution.Starts {

  @Override
  public Object doOperation(Interval leftOperand, Interval rightOperand) {
    Object leftStart = leftOperand.getStart();
    Object leftEnd = leftOperand.getEnd();
    Object rightStart = rightOperand.getStart();
    Object rightEnd = rightOperand.getEnd();

    if (leftStart == null || leftEnd == null || rightStart == null || rightEnd == null) { return null; }

    return ((Boolean) Execution.resolveComparisonDoOperation(new EqualEvaluator(), leftStart, rightStart)
              && (Boolean) Execution.resolveComparisonDoOperation(new LessOrEqualEvaluator(), leftEnd, rightEnd));
  }

  @Override
  public Object evaluate(Context context) {
    Object left = getOperand().get(0).evaluate(context);
    Object right = getOperand().get(1).evaluate(context);

    if (left == null || right == null) { return null; }

    return Execution.resolveIntervalDoOperation(this, left, right);
  }
}
