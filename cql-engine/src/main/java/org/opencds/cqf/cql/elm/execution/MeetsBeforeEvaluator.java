package org.opencds.cqf.cql.elm.execution;

import org.opencds.cqf.cql.execution.Context;
import org.opencds.cqf.cql.runtime.Interval;

/*
meets before(left Interval<T>, right Interval<T>) Boolean

The meets before operator returns true if the first interval ends immediately before the second interval starts.
If either argument is null, the result is null.
*/

/**
* Created by Chris Schuler on 6/8/2016
*/
public class MeetsBeforeEvaluator extends org.cqframework.cql.elm.execution.MeetsBefore {

  @Override
  public Object doOperation(Interval leftOperand, Interval rightOperand) {
    Object leftEnd = leftOperand.getEnd();
    Object rightStart = rightOperand.getStart();

    if (leftEnd == null || rightStart == null) { return null; }

    return Execution.resolveComparisonDoOperation(new EqualEvaluator(), rightStart, Interval.successor(leftEnd));
  }

  @Override
  public Object evaluate(Context context) {
    Object left = getOperand().get(0).evaluate(context);
    Object right = getOperand().get(1).evaluate(context);

    if (left == null || right == null) { return null; }

    return Execution.resolveIntervalDoOperation(this, left, right);
  }
}
