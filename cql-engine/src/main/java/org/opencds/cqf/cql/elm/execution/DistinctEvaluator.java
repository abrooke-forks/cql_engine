package org.opencds.cqf.cql.elm.execution;

import org.opencds.cqf.cql.execution.Context;

import java.util.ArrayList;
import java.util.List;

/*
distinct(argument List<T>) List<T>

The distinct operator returns the given list with duplicates eliminated.
This operator uses the notion of equivalence to determine whether two elements in the list are the same
  for the purposes of duplicate elimination.
    In particular this means that if the list contains multiple null elements, the result will only contain one null element.
If the argument is null, the result is null.
*/

/**
 * Created by Bryn on 5/25/2016.
 */
public class DistinctEvaluator extends org.cqframework.cql.elm.execution.Distinct {

    @Override
    public Object doOperation(Iterable<Object> operand) {
        List<Object> result = new ArrayList<>();
        for (Object element : operand) {
            if (!(Boolean) Execution.resolveSharedDoOperation(new InEvaluator(), element, result)) {
                result.add(element);
            }
        }
        return result;
    }

    @Override
    public Object evaluate(Context context) {
        Object operand = this.getOperand().evaluate(context);

        if (operand == null) { return null; }

        return Execution.resolveListDoOperation(this, operand);
    }
}
