package org.opencds.cqf.cql.elm.execution;

import org.opencds.cqf.cql.execution.Context;
import org.opencds.cqf.cql.runtime.Interval;

/*
ends(left Interval<T>, right Interval<T>) Boolean

The ends operator returns true if the first interval ends the second.
  More precisely, if the starting point of the first interval is greater than or equal to the starting point of the second,
    and the ending point of the first interval is equal to the ending point of the second.
This operator uses the semantics described in the start and end operators to determine interval boundaries.
If either argument is null, the result is null.
*/

/**
* Created by Chris Schuler on 6/7/2016
*/
public class EndsEvaluator extends org.cqframework.cql.elm.execution.Ends {

  @Override
  public Object doOperation(Interval leftOperand, Interval rightOperand) {
    Object leftStart = leftOperand.getStart();
    Object leftEnd = leftOperand.getEnd();
    Object rightStart = rightOperand.getStart();
    Object rightEnd = rightOperand.getEnd();

    if (leftStart == null || leftEnd == null || rightStart == null || rightEnd == null) { return null; }

    Boolean within = (Boolean) Execution.resolveComparisonDoOperation(new GreaterOrEqualEvaluator(), leftStart, rightStart);
    Boolean equalEnd = (Boolean) Execution.resolveComparisonDoOperation(new EqualEvaluator(), leftEnd, rightEnd);

    return within == null || equalEnd == null ? null : within && equalEnd;
  }

  @Override
  public Object evaluate(Context context) {
    Object left = getOperand().get(0).evaluate(context);
    Object right = getOperand().get(1).evaluate(context);

    if (left == null || right == null) {
      return null;
    }

    return Execution.resolveIntervalDoOperation(this, left, right);
  }
}
