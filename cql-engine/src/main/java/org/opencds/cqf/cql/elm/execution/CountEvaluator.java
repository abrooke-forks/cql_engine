package org.opencds.cqf.cql.elm.execution;

import org.opencds.cqf.cql.execution.Context;

/*
Count(argument List<T>) Integer

* The Count operator returns the number of non-null elements in the source.
* If the list contains no non-null elements, the result is 0.
* If the list is null, the result is null.
* Always returns Integer
*/

/**
* Created by Chris Schuler on 6/13/2016
*/
public class CountEvaluator extends org.cqframework.cql.elm.execution.Count {

  @Override
  public Object doOperation(Iterable<Object> operand) {
    int count = 0;

    for (Object value : operand) {
      if (value != null) { ++count; } // skip null
    }

    return count;
  }

  @Override
  public Object evaluate(Context context) {
    Object operand = getSource().evaluate(context);

    // The list may contain all nulls for this list operator
    if (operand == null) { return null; }

    return Execution.resolveAggregateDoOperation(this, operand);
  }
}
