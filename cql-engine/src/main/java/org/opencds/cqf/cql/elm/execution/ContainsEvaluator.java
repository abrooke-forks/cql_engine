package org.opencds.cqf.cql.elm.execution;

import org.opencds.cqf.cql.execution.Context;
import org.opencds.cqf.cql.runtime.Interval;

/*
*** NOTES FOR LIST ***
contains(argument List<T>, element T) Boolean

The contains operator for intervals returns true if the given point is greater than or equal to the starting point
  of the interval, and less than or equal to the ending point of the interval.
For open interval boundaries, exclusive comparison operators are used.
For closed interval boundaries, if the interval boundary is null, the result of the boundary comparison is considered true.
If either argument is null, the result is null.

*** NOTES FOR INTERVAL ***
contains(argument Interval<T>, point T) Boolean

The contains operator for intervals returns true if the given point is greater than or equal to the starting point
  of the interval, and less than or equal to the ending point of the interval.
For open interval boundaries, exclusive comparison operators are used.
For closed interval boundaries, if the interval boundary is null, the result of the boundary comparison is considered true.
If either argument is null, the result is null.

*/

/**
 * Created by Bryn on 5/25/2016 
 */
public class ContainsEvaluator extends org.cqframework.cql.elm.execution.Contains {

  @Override
  public Object doOperation(Interval leftOperand, Object rightOperand) {
    return Execution.resolveSharedDoOperation(new InEvaluator(), rightOperand, leftOperand);
  }

  @Override
  public Object doOperation(Iterable<Object> leftOperand, Object rightOperand) {
    return Execution.resolveSharedDoOperation(new InEvaluator(), rightOperand, leftOperand);
  }

  @Override
  public Object evaluate(Context context) {
    Object left = getOperand().get(0).evaluate(context);
    Object right = getOperand().get(1).evaluate(context);

    if (left == null || right == null) { return null; }

    return Execution.resolveSharedDoOperation(this, left, right);
  }
}
