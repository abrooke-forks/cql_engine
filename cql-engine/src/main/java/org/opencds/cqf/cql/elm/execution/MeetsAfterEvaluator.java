package org.opencds.cqf.cql.elm.execution;

import org.opencds.cqf.cql.execution.Context;
import org.opencds.cqf.cql.runtime.Interval;

/*
meets after(left Interval<T>, right Interval<T>) Boolean

The meets after operator returns true if the first interval starts immediately after the second interval ends.
If either argument is null, the result is null.
*/

/**
* Created by Chris Schuler on 6/8/2016
*/
public class MeetsAfterEvaluator extends org.cqframework.cql.elm.execution.MeetsAfter {

  @Override
  public Object doOperation(Interval leftOperand, Interval rightOperand) {
    Object leftStart = leftOperand.getStart();
    Object rightEnd = rightOperand.getEnd();

    if (leftStart == null || rightEnd == null) { return null; }

    return Execution.resolveComparisonDoOperation(new EqualEvaluator(), leftStart, Interval.successor(rightEnd));
  }

  @Override
  public Object evaluate(Context context) {
    Object left = getOperand().get(0).evaluate(context);
    Object right = getOperand().get(1).evaluate(context);

    if (left == null || right == null) { return null; }

    return Execution.resolveIntervalDoOperation(this, left, right);
  }
}
