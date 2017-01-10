package org.opencds.cqf.cql.elm.execution;

import org.opencds.cqf.cql.execution.Context;
import org.opencds.cqf.cql.runtime.Interval;

/*
*** NOTES FOR INTERVAL ***
in(point T, argument Interval<T>) Boolean

The in operator for intervals returns true if the given point is greater than or equal to the starting point of the interval,
  and less than or equal to the ending point of the interval.
For open interval boundaries, exclusive comparison operators are used.
For closed interval boundaries, if the interval boundary is null, the result of the boundary comparison is considered true.
If either argument is null, the result is null.
*/

/*
*** NOTES FOR LIST ***
in(element T, argument List<T>) Boolean

The in operator for lists returns true if the given element is in the given list.
This operator uses the notion of equivalence to determine whether or not the element being searched for is
  equivalent to any element in the list.
  In particular this means that if the list contains a null, and the element being searched for is null, the result will be true.
If either argument is null, the result is null.
*/

/**
 * Created by Bryn on 5/25/2016.
 */
public class InEvaluator extends org.cqframework.cql.elm.execution.In {

  @Override
  public Object doOperation(Object leftOperand, Iterable<Object> rightOperand) {
    boolean nullSwitch = false;
    for (Object element : rightOperand) {
      Boolean equiv = (Boolean) Execution.resolveComparisonDoOperation(new EquivalentEvaluator(), leftOperand, element);
      if (equiv == null) { nullSwitch = true; }
      else if (equiv) { return true; }
    }

    if (nullSwitch) { return null; }
    return false;
  }

  @Override
  public Object doOperation(Object leftOperand, Interval rightOperand) {
    if (rightOperand.getStart() == null && rightOperand.getLowClosed()) { return true; }
    else if (rightOperand.getEnd() == null && rightOperand.getHighClosed()) { return true; }
    else if (rightOperand.getStart() == null || rightOperand.getEnd() == null) { return null; }
    else if (leftOperand == null && (rightOperand.getStart() == null || rightOperand.getEnd() == null)) {
      return true;
    }

    Boolean lowerBound = (Boolean) Execution.resolveComparisonDoOperation(
            new GreaterOrEqualEvaluator(), leftOperand, rightOperand.getStart());
    Boolean upperBound = (Boolean) Execution.resolveComparisonDoOperation(
            new LessOrEqualEvaluator(), leftOperand, rightOperand.getEnd());

    if (lowerBound == null || upperBound == null) { return null; }

    return lowerBound && upperBound;
  }

  @Override
  public Object evaluate(Context context) {
    Object left = getOperand().get(0).evaluate(context);
    Object right = getOperand().get(1).evaluate(context);

    if (right == null) { return null; }

    return Execution.resolveSharedDoOperation(this, left, right);
  }
}
