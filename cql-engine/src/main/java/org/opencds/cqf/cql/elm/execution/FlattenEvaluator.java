package org.opencds.cqf.cql.elm.execution;

import org.opencds.cqf.cql.execution.Context;

import java.util.ArrayList;

/*
flatten(argument List<List<T>>) List<T>

The flatten operator flattens a list of lists into a single list.
*/

/**
 * Created by Bryn on 5/25/2016.
 */
public class FlattenEvaluator extends org.cqframework.cql.elm.execution.Flatten {

    @Override
    public Object doOperation(Iterable<Object> operand) {
        ArrayList resultList = new ArrayList();
        for (Object element : (Iterable)operand) {
            for (Object subElement : (Iterable)element) {
                resultList.add(subElement);
            }
        }

        return resultList;
    }

    @Override
    public Object evaluate(Context context) {
        Object operand = getOperand().evaluate(context);

        if (operand == null) { return null; }

        return Execution.resolveListDoOperation(this, operand);
    }
}
