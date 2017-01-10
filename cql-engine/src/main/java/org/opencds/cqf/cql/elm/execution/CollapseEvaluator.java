package org.opencds.cqf.cql.elm.execution;

import org.opencds.cqf.cql.execution.Context;
import org.opencds.cqf.cql.runtime.Interval;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/*
collapse(argument List<Interval<T>>) List<Interval<T>>

The collapse operator returns the unique set of intervals that completely covers the ranges present in the given list of intervals.
If the list of intervals is empty, the result is empty.
If the list of intervals contains a single interval, the result is a list with that interval.
If the list of intervals contains nulls, they will be excluded from the resulting list.
If the argument is null, the result is null.
*/

/**
* Created by Chris Schuler on 6/8/2016
*/
public class CollapseEvaluator extends org.cqframework.cql.elm.execution.Collapse {

  @Override
  public Object doOperation(Iterable<Object> operand) {
    List<Interval> list = new ArrayList<>();
    operand.forEach(ae -> list.add((Interval) ae));

    if (list.size() == 1 && list.get(0) != null) { return list; }
    else if (list.size() == 1) { return null; }
    else if (list.size() == 0) { return null; }

    Collections.sort(list, Interval.sortInterval);

    for (int i = 0; i < list.size(); ++i) {
      if (list.get(i).getStart() == null || list.get(i).getEnd() == null) { continue; }
      if ((i+1) < list.size()) {
        if ((Boolean) new OverlapsEvaluator().doOperation(list.get(i), list.get(i+1))) {
          list.set(i, new Interval((list.get(i)).getStart(), true, (list.get(i+1)).getEnd(), true));
          list.remove(i+1);
          i -= 1;
        }
      }
    }
    return list.isEmpty() ? null : list;
  }

  @Override
  public Object evaluate(Context context) {
    Object operand = getOperand().evaluate(context);

    if (operand == null) { return null; }

    return Execution.resolveIntervalDoOperation(this, operand);
  }
}
