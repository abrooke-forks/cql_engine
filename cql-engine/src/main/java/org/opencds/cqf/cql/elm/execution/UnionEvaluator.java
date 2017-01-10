package org.opencds.cqf.cql.elm.execution;

import org.opencds.cqf.cql.execution.Context;
import org.opencds.cqf.cql.runtime.Interval;

import java.util.ArrayList;
import java.util.List;

/*
*** NOTES FOR INTERVAL ***
union(left Interval<T>, right Interval<T>) Interval<T>

The union operator for intervals returns the union of the intervals.
  More precisely, the operator returns the interval that starts at the earliest starting point in either argument,
    and ends at the latest starting point in either argument.
If the arguments do not overlap or meet, this operator returns null.
If either argument is null, the result is null.

*** NOTES FOR LIST ***
union(left List<T>, right List<T>) List<T>

The union operator for lists returns a list with all elements from both arguments.
Note that duplicates are not eliminated during this process, if an element appears once in both sources,
  that element will be present twice in the resulting list.
If either argument is null, the result is null.
Note that the union operator can also be invoked with the symbolic operator (|).
*/

/**
 * Created by Bryn on 5/25/2016.
 */
public class UnionEvaluator extends org.cqframework.cql.elm.execution.Union {

  @Override
  public Object doOperation(Interval leftOperand, Interval rightOperand) {
    Object leftStart = leftOperand.getStart();
    Object leftEnd = leftOperand.getEnd();
    Object rightStart = rightOperand.getStart();
    Object rightEnd = rightOperand.getEnd();

    if (leftStart == null || leftEnd == null || rightStart == null || rightEnd == null) { return null; }

    if (!(Boolean) Execution.resolveIntervalDoOperation(new OverlapsEvaluator(), leftOperand, rightOperand)
            && !(Boolean) Execution.resolveIntervalDoOperation(new MeetsEvaluator(), leftOperand, rightOperand)) {
      return null;
    }

    Object min = (Boolean) Execution.resolveComparisonDoOperation(new LessEvaluator(), leftStart, rightStart)
                    ? leftStart : rightStart;
    Object max = (Boolean) Execution.resolveComparisonDoOperation(new GreaterEvaluator(), leftEnd, rightEnd)
                    ? leftEnd : rightEnd;

    return new Interval(min, true, max, true);
  }

  @Override
  public Object doOperation(Iterable<Object> leftOperand, Iterable<Object> rightOperand) {
    List<Object> union = new ArrayList<>();
    leftOperand.forEach(union::add);
    rightOperand.forEach(union::add);
    return union;
  }

  @Override
  public Object evaluate(Context context) {
    Object left = getOperand().get(0).evaluate(context);
    Object right = getOperand().get(1).evaluate(context);

    if (left == null || right == null) { return null; }

    return Execution.resolveSharedDoOperation(this, left, right);
  }
}
