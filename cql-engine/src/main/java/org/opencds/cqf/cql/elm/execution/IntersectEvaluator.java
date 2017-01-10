package org.opencds.cqf.cql.elm.execution;

import org.opencds.cqf.cql.execution.Context;
import org.opencds.cqf.cql.runtime.Interval;

import java.util.ArrayList;
import java.util.List;

/*
*** NOTES FOR INTERVAL ***
intersect(left Interval<T>, right Interval<T>) Interval<T>

The intersect operator for intervals returns the intersection of two intervals.
  More precisely, the operator returns the interval that defines the overlapping portion of both arguments.
If the arguments do not overlap, this operator returns null.
If either argument is null, the result is null.

*** NOTES FOR LIST ***
intersect(left List<T>, right List<T>) List<T>

The intersect operator for lists returns the intersection of two lists.
  More precisely, the operator returns a list containing only the elements that appear in both lists.
This operator uses the notion of equivalence to determine whether or not two elements are the same.
If either argument is null, the result is null.
*/

/**
 * Created by Bryn on 5/25/2016.
 */
public class IntersectEvaluator extends org.cqframework.cql.elm.execution.Intersect {

  @Override
  public Object doOperation(Interval leftOperand, Interval rightOperand) {
    if (!(Boolean) new OverlapsEvaluator().doOperation(leftOperand, rightOperand)) {
      return null;
    }

    Object leftStart = leftOperand.getStart();
    Object leftEnd = leftOperand.getEnd();
    Object rightStart = rightOperand.getStart();
    Object rightEnd = rightOperand.getEnd();

    if (leftStart == null || leftEnd == null || rightStart == null || rightEnd == null) { return null; }

    Object max = (Boolean) Execution.resolveComparisonDoOperation(
            new GreaterEvaluator(), leftStart, rightStart) ? leftStart : rightStart;
    Object min = (Boolean) Execution.resolveComparisonDoOperation(
            new LessEvaluator(), leftEnd, rightEnd) ? leftEnd : rightEnd;

    return new Interval(max, true, min, true);
  }

  @Override
  public Object doOperation(Iterable<Object> leftOperand, Iterable<Object> rightOperand) {
    List<Object> result = new ArrayList<>();
    for (Object leftItem : leftOperand) {
      if ((Boolean) Execution.resolveSharedDoOperation(new InEvaluator(), leftItem, rightOperand)) {
        result.add(leftItem);
      }
    }
    return result;
  }

  @Override
  public Object evaluate(Context context) {
    Object left = getOperand().get(0).evaluate(context);
    Object right = getOperand().get(1).evaluate(context);

    if (left == null || right == null) { return null; }

    return Execution.resolveSharedDoOperation(this, left, right);
  }
}
