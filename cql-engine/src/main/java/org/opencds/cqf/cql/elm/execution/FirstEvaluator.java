package org.opencds.cqf.cql.elm.execution;

import org.opencds.cqf.cql.execution.Context;

/*
First(argument List<T>) T

The First operator returns the first element in a list. The operator is equivalent to invoking the indexer with an index of 0.
If the argument is null, the result is null.
*/

/**
 * Created by Bryn on 5/25/2016.
 */
public class FirstEvaluator extends org.cqframework.cql.elm.execution.First {

    @Override
    public Object doOperation(Iterable<Object> operand) {
        for (Object element : (Iterable)operand) {
            return element;
        }

        return null;
    }

    @Override
    public Object evaluate(Context context) {
        Object operand = getSource().evaluate(context);

        if (operand == null) { return null; }

        return Execution.resolveListDoOperation(this, operand);
    }
}
