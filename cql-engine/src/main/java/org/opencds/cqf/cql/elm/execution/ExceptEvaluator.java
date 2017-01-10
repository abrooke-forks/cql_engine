package org.opencds.cqf.cql.elm.execution;

import org.opencds.cqf.cql.execution.Context;
import org.opencds.cqf.cql.runtime.Interval;

import java.util.ArrayList;
import java.util.List;

/*
except(left List<T>, right List<T>) List<T>

The except operator returns the set difference of two lists.
  More precisely, the operator returns a list with the elements that appear in the first operand that do not appear
    in the second operand.
This operator uses the notion of equivalence to determine whether two elements are the same for the purposes of
  computing the difference.
If either argument is null, the result is null.

except(left Interval<T>, right Interval<T>) Interval<T>

The except operator for intervals returns the set difference of two intervals.
  More precisely, this operator returns the portion of the first interval that does not overlap with the second.
Note that to avoid returning an improper interval, if the second argument is properly contained within the first and
  does not start or end it, this operator returns null.
If either argument is null, the result is null.

*/

/**
 * Created by Bryn on 5/25/2016.
 */
public class ExceptEvaluator extends org.cqframework.cql.elm.execution.Except {

  @Override
  public Object doOperation(Interval leftOperand, Interval rightOperand) {
    Object leftStart = leftOperand.getStart();
    Object leftEnd = leftOperand.getEnd();
    Object rightStart = rightOperand.getStart();
    Object rightEnd = rightOperand.getEnd();

    if (leftStart == null || leftEnd == null || rightStart == null || rightEnd == null) {
      return null;
    }

    if ((Boolean) Execution.resolveComparisonDoOperation(new GreaterEvaluator(), rightStart, leftEnd)) { return leftOperand; }
    else if ((Boolean) Execution.resolveComparisonDoOperation(new LessEvaluator(), leftStart, rightStart)
                && (Boolean) Execution.resolveComparisonDoOperation(new GreaterEvaluator(), leftEnd, rightEnd)) {
      return null;
    }

    // left interval starts before right interval
    if (((Boolean) Execution.resolveComparisonDoOperation(new LessEvaluator(), leftStart, rightStart)
                && (Boolean) Execution.resolveComparisonDoOperation(new LessOrEqualEvaluator(), leftEnd, rightEnd))) {
      Object min = (Boolean) Execution.resolveComparisonDoOperation(new LessEvaluator(), Interval.predecessor(rightStart), leftEnd)
                        ? Interval.predecessor(rightStart) : leftEnd;
      return new Interval(leftStart, true, min, true);
    }
    // right interval starts before left interval
    else if ((Boolean) Execution.resolveComparisonDoOperation(new GreaterOrEqualEvaluator(), leftStart, rightStart)
                && (Boolean) Execution.resolveComparisonDoOperation(new GreaterEvaluator(), leftEnd, rightEnd)) {
      Object max = (Boolean) Execution.resolveComparisonDoOperation(new GreaterEvaluator(), Interval.successor(rightEnd), leftStart)
                        ? Interval.successor(rightEnd) : leftStart;
      return new Interval(max, true, leftEnd, true);
    }
    throw new IllegalArgumentException(String.format("The following interval values led to an undefined Except result: leftStart: %s, leftEnd: %s, rightStart: %s, rightEnd: %s", leftStart.toString(), leftEnd.toString(), rightStart.toString(), rightEnd.toString()));
  }

  @Override
  public Object doOperation(Iterable<Object> leftOperand, Iterable<Object> rightOperand) {
    List<Object> result = new ArrayList<>();
    for (Object element : leftOperand) {
      if (!(Boolean) Execution.resolveSharedDoOperation(new InEvaluator(), element, rightOperand)) {
        result.add(element);
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
