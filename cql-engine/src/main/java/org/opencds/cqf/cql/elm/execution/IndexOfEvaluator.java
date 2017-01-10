package org.opencds.cqf.cql.elm.execution;

import org.opencds.cqf.cql.execution.Context;

/*
IndexOf(argument List<T>, element T) Integer

The IndexOf operator returns the 0-based index of the given element in the given source list.
The operator uses the notion of equivalence to determine the index. The search is linear,
  and returns the index of the first element that is equivalent to the element being searched for.
If the list is empty, or no element is found, the result is -1.
If the list argument is null, the result is null.
*/

/**
 * Created by Bryn on 5/25/2016.
 */
public class IndexOfEvaluator extends org.cqframework.cql.elm.execution.IndexOf {

    @Override
    public Object doOperation(Iterable<Object> leftOperand, Object rightOperand) {
        int index = -1;
        boolean nullSwitch = false;
        for (Object element : leftOperand) {
            index++;
            Boolean equiv = (Boolean) Execution.resolveComparisonDoOperation(new EquivalentEvaluator(), element, rightOperand);
            if (equiv == null) { nullSwitch = true; }
            else if (equiv) {
                return index;
            }
        }
        if (nullSwitch) { return null; }
        return -1;
    }

    @Override
    public Object doOperation(Iterable<Object> leftOperand, Integer rightOperand) {
        int index = -1;
        boolean nullSwitch = false;
        for (Object element : leftOperand) {
            index++;
            Boolean equiv = (Boolean) Execution.resolveComparisonDoOperation(new EquivalentEvaluator(), element, rightOperand);
            if (equiv == null) { nullSwitch = true; }
            else if (equiv) {
                return index;
            }
        }
        if (nullSwitch) { return null; }
        return -1;
    }

    @Override
    public Object evaluate(Context context) {
        Object left = getSource().evaluate(context);
        Object right = getElement().evaluate(context);

        if (left == null) { return null; }

        return Execution.resolveListDoOperation(this, left, right);
    }
}
