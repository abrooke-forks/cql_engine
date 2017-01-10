package org.opencds.cqf.cql.elm.execution;

import org.opencds.cqf.cql.execution.Context;
import org.opencds.cqf.cql.runtime.CqlList;

import java.util.ArrayList;
import java.util.Iterator;

/*
Mode(argument List<T>) T

The Mode operator returns the statistical mode of the elements in source.
If the source contains no non-null elements, null is returned.
If the source is null, the result is null.
*/

/**
* Created by Chris Schuler on 6/13/2016
*/
public class ModeEvaluator extends org.cqframework.cql.elm.execution.Mode {

  @Override
  public Object doOperation(Iterable<Object> operand) {
    Iterator<Object> itr = operand.iterator();

    if (!itr.hasNext()) { return null; } // empty list

    Object mode = new Object();
    ArrayList<Object> values = new ArrayList<>();
    while (itr.hasNext()) {
      Object value = itr.next();
      if (value != null) { values.add(value); }
    }

    if (values.isEmpty()) { return null; } // all null
    values = CqlList.sortList(values);

    int max = 0;
    for (int i = 0; i < values.size(); ++i) {
      int count = (values.lastIndexOf(values.get(i)) - i) + 1;
      if (count > max) {
        mode = values.get(i);
        max = count;
      }
    }
    return mode;
  }

  @Override
  public Object evaluate(Context context) {
    Object operand = getSource().evaluate(context);

    if ((Boolean) new IsNullEvaluator().doOperation((Iterable<Object>) operand)) { return null; }

    return Execution.resolveAggregateDoOperation(this, operand);
  }
}
