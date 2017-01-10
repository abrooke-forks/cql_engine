package org.opencds.cqf.cql.elm.execution;

import org.cqframework.cql.elm.execution.DateTimePrecision;
import org.opencds.cqf.cql.execution.Context;
import org.opencds.cqf.cql.runtime.*;

/*
*** NOTES FOR INTERVAL ***
after(left Interval<T>, right Interval<T>) Boolean
after(left T, right Interval<T>) Boolean
after(left Interval<T>, right T) Boolean

The after operator for intervals returns true if the first interval starts after the second one ends.
  In other words, if the starting point of the first interval is greater than the ending point of the second interval.
For the point-interval overload, the operator returns true if the given point is greater than the end of the interval.
For the interval-point overload, the operator returns true if the given interval starts after the given point.
This operator uses the semantics described in the Start and End operators to determine interval boundaries.
If either argument is null, the result is null.


*** NOTES FOR DATETIME ***
after precision of(left DateTime, right DateTime) Boolean
after precision of(left Time, right Time) Boolean

The after-precision-of operator compares two date/time values to the specified precision to determine whether the
  first argument is the after the second argument. Precision must be one of: year, month, day, hour, minute, second, or millisecond.
For comparisons involving date/time or time values with imprecision, note that the result of the comparison may be null,
  depending on whether the values involved are specified to the level of precision used for the comparison.
If either or both arguments are null, the result is null.
*/

/**
* Created by Chris Schuler on 6/7/2016
*/
public class AfterEvaluator extends org.cqframework.cql.elm.execution.After {

  @Override
  public void setPrecision(DateTimePrecision value) {
    super.setPrecision(value);
  }

  public AfterEvaluator withPrecision(DateTimePrecision value) {
    setPrecision(value);
    return this;
  }

  @Override
  public Object doOperation(Interval leftOperand, Interval rightOperand) {
    return Execution.resolveComparisonDoOperation(new GreaterEvaluator(), leftOperand.getStart(), rightOperand.getEnd());
  }

  @Override
  public Object doOperation(Interval leftOperand, Object rightOperand) {
    return Execution.resolveComparisonDoOperation(new GreaterEvaluator(), leftOperand.getStart(), rightOperand);
  }

  @Override
  public Object doOperation(Object leftOperand, Interval rightOperand) {
    return Execution.resolveComparisonDoOperation(new GreaterEvaluator(), leftOperand, rightOperand.getEnd());
  }

  @Override
  public Object doOperation(DateTime leftOperand, DateTime rightOperand) {
    int idx = DateTime.getFieldIndex(precision.value());

    if (idx != -1) {
      if (idx + 1 > leftOperand.getPartial().size() || idx + 1 > rightOperand.getPartial().size()) {
        if (Uncertainty.isUncertain(leftOperand, precision.value())) {
          return Execution.resolveComparisonDoOperation(new GreaterEvaluator(),
                  Uncertainty.getHighLowList(leftOperand, precision.value()).get(0), rightOperand);
        }

        else if (Uncertainty.isUncertain(rightOperand, precision.value())) {
          return Execution.resolveComparisonDoOperation(new GreaterEvaluator(),
                  leftOperand, Uncertainty.getHighLowList(rightOperand, precision.value()).get(1));
        }
        return null;
      }
      return leftOperand.getPartial().getValue(idx) > rightOperand.getPartial().getValue(idx);
    }

    throw new IllegalArgumentException(String.format("Invalid duration precision: %s", precision.value()));
  }

  @Override
  public Object doOperation(Time leftOperand, Time rightOperand) {
    int idx = Time.getFieldIndex(precision.value());

    if (idx != -1) {
      if (idx + 1 > leftOperand.getPartial().size() || idx + 1 > rightOperand.getPartial().size()) {
        if (Uncertainty.isUncertain(leftOperand, precision.value())) {
          return Execution.resolveComparisonDoOperation(new GreaterEvaluator(),
                  Uncertainty.getHighLowList(leftOperand, precision.value()).get(0), rightOperand);
        }

        else if (Uncertainty.isUncertain(rightOperand, precision.value())) {
          return Execution.resolveComparisonDoOperation(new GreaterEvaluator(),
                  leftOperand, Uncertainty.getHighLowList(rightOperand, precision.value()).get(1));
        }
        return null;
      }
      return leftOperand.getPartial().getValue(idx) > rightOperand.getPartial().getValue(idx);
    }

    throw new IllegalArgumentException(String.format("Invalid duration precision: %s", precision.value()));
  }

  @Override
  public Object evaluate(Context context) {
    Object left = getOperand().get(0).evaluate(context);
    Object right = getOperand().get(1).evaluate(context);

    if (left == null || right == null) { return null; }

    return Execution.resolveSharedDoOperation(this, left, right);
  }
}
