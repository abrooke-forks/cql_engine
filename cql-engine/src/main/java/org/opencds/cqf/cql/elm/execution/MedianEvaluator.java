package org.opencds.cqf.cql.elm.execution;

import org.opencds.cqf.cql.execution.Context;
import org.opencds.cqf.cql.runtime.Quantity;
import org.opencds.cqf.cql.runtime.CqlList;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Iterator;

/*
Median(argument List<Decimal>) Decimal
Median(argument List<Quantity>) Quantity

The Median operator returns the median of the elements in source.
If the source contains no non-null elements, null is returned.
If the source is null, the result is null.
*/

/**
* Created by Chris Schuler on 6/13/2016
*/
public class MedianEvaluator extends org.cqframework.cql.elm.execution.Median {

  @Override
  public Object doOperation(Iterable<Object> operand) {
    Iterator<Object> itr = operand.iterator();

    if (!itr.hasNext()) { return null; } // empty list

    ArrayList<Object> values = new ArrayList<>();
    while (itr.hasNext()) {
      Object value = itr.next();
      if (value != null) { values.add(value); }
    }

    if (values.isEmpty()) { return null; } // all null

    values = CqlList.sortList(values);

    if (values.size() % 2 != 0) {
      return values.get(values.size() / 2);
    }

    else {
      Object divisor = values.get(0) instanceof BigDecimal ? new BigDecimal("2.0")
              : new Quantity().withValue(new BigDecimal("2.0"));
      return Execution.resolveArithmeticDoOperation(
              new DivideEvaluator(), Execution.resolveSharedDoOperation(
                      new AddEvaluator(), values.get(values.size()/2), values.get((values.size()/2)-1)), divisor);
    }
  }

  @Override
  public Object evaluate(Context context) {
    Object operand = getSource().evaluate(context);

    if ((Boolean) new IsNullEvaluator().doOperation((Iterable<Object>) operand)) { return null; }

    return Execution.resolveAggregateDoOperation(this, operand);
  }
}
