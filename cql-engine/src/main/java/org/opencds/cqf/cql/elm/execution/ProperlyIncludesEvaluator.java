package org.opencds.cqf.cql.elm.execution;

import org.opencds.cqf.cql.execution.Context;
import org.opencds.cqf.cql.runtime.Interval;

/*
*** NOTES FOR INTERVAL ***
properly includes(left Interval<T>, right Interval<T>) Boolean

The properly includes operator for intervals returns true if the first interval completely includes the second and the
  first interval is strictly larger than the second.
  More precisely, if the starting point of the first interval is less than or equal to the starting point of the second interval,
    and the ending point of the first interval is greater than or equal to the ending point of the second interval,
      and they are not the same interval.
This operator uses the semantics described in the Start and End operators to determine interval boundaries.
If either argument is null, the result is null.

*** NOTES FOR LIST ***
properly includes(left List<T>, right List<T>) Boolean

The properly includes operator for lists returns true if the first list contains every element of the second list,
  and the first list is strictly larger than the second list.
This operator uses the notion of equivalence to determine whether or not two elements are the same.
If either argument is null, the result is null.
Note that the order of elements does not matter for the purposes of determining inclusion.
*/

/**
* Created by Chris Schuler on 6/8/2016
*/
public class ProperlyIncludesEvaluator extends org.cqframework.cql.elm.execution.ProperIncludes {

  @Override
  public Object doOperation(Interval leftOperand, Interval rightOperand) {
    Object leftStart = leftOperand.getStart();
    Object leftEnd = leftOperand.getEnd();
    Object rightStart = rightOperand.getStart();
    Object rightEnd = rightOperand.getEnd();

    return ((Boolean) Execution.resolveComparisonDoOperation(new GreaterEvaluator(), Interval.getSize(leftStart, leftEnd), Interval.getSize(rightStart, rightEnd))
            && (Boolean) Execution.resolveComparisonDoOperation(new LessOrEqualEvaluator(), leftStart, rightStart)
            && (Boolean) Execution.resolveComparisonDoOperation(new GreaterOrEqualEvaluator(), leftEnd, rightEnd));
  }

  @Override
  public Object doOperation(Iterable<Object> leftOperand, Iterable<Object> rightOperand) {
    return (Boolean) new IncludesEvaluator().doOperation(leftOperand, rightOperand)
                    && leftOperand.spliterator().getExactSizeIfKnown() >
                            rightOperand.spliterator().getExactSizeIfKnown();
  }

  @Override
  public Object evaluate(Context context) {
    Object left = getOperand().get(0).evaluate(context);
    Object right = getOperand().get(1).evaluate(context);

    if (left == null || right == null) { return null; }

    return Execution.resolveSharedDoOperation(this, left, right);
  }
}
