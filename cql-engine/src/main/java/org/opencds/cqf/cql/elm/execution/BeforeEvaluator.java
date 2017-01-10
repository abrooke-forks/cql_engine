package org.opencds.cqf.cql.elm.execution;

import org.cqframework.cql.elm.execution.DateTimePrecision;
import org.opencds.cqf.cql.execution.Context;
import org.opencds.cqf.cql.runtime.*;

/*
*** NOTES FOR INTERVAL ***
before(left Interval<T>, right Interval<T>) Boolean
before(left T, right Interval<T>) Boolean
before(left interval<T>, right T) Boolean

The before operator for intervals returns true if the first interval ends before the second one starts.
  In other words, if the ending point of the first interval is less than the starting point of the second interval.
For the point-interval overload, the operator returns true if the given point is less than the start of the interval.
For the interval-point overload, the operator returns true if the given interval ends before the given point.
This operator uses the semantics described in the Start and End operators to determine interval boundaries.
If either argument is null, the result is null.


*** NOTES FOR DATETIME ***
before precision of(left DateTime, right DateTime) Boolean
before precision of(left Time, right Time) Boolean

The before-precision-of operator compares two date/time values to the specified precision to determine whether the
  first argument is the before the second argument. Precision must be one of: year, month, day, hour, minute, second, or millisecond.
For comparisons involving date/time or time values with imprecision, note that the result of the comparison may be null,
  depending on whether the values involved are specified to the level of precision used for the comparison.
If either or both arguments are null, the result is null.
*/

/**
* Created by Chris Schuler on 6/7/2016 
*/
public class BeforeEvaluator extends org.cqframework.cql.elm.execution.Before {

  @Override
  public void setPrecision(DateTimePrecision value) {
    super.setPrecision(value);
  }

  public BeforeEvaluator withPrecision(DateTimePrecision value) {
    setPrecision(value);
    return this;
  }

  @Override
  public Object doOperation(Interval leftOperand, Interval rightOperand) {
    return Execution.resolveComparisonDoOperation(new LessEvaluator(), leftOperand.getEnd(), rightOperand.getStart());
  }

  @Override
  public Object doOperation(Interval leftOperand, Object rightOperand) {
    return Execution.resolveComparisonDoOperation(new LessEvaluator(), leftOperand.getEnd(), rightOperand);
  }

  @Override
  public Object doOperation(Object leftOperand, Interval rightOperand) {
    return Execution.resolveComparisonDoOperation(new LessEvaluator(), leftOperand, rightOperand.getStart());
  }

  @Override
  public Object doOperation(DateTime leftOperand, DateTime rightOperand) {
    return new AfterEvaluator().withPrecision(this.getPrecision()).doOperation(rightOperand, leftOperand);
  }

  @Override
  public Object doOperation(Time leftOperand, Time rightOperand) {
    return new AfterEvaluator().withPrecision(this.getPrecision()).doOperation(rightOperand, leftOperand);
  }

  @Override
  public Object evaluate(Context context) {
    Object left = getOperand().get(0).evaluate(context);
    Object right = getOperand().get(1).evaluate(context);

    if (left == null || right == null) { return null; }

    return Execution.resolveSharedDoOperation(this, left, right);
  }
}
