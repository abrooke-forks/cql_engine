package org.opencds.cqf.cql.elm.execution;

import org.opencds.cqf.cql.execution.Context;

/*
*** STRING NOTES ***
[](argument String, index Integer) String

The indexer ([]) operator returns the character at the indexth position in a string.
Indexes in strings are defined to be 0-based.
If either argument is null, the result is null.
If the index is greater than the length of the string being indexed, the result is null.

*** LIST NOTES ***
[](argument List<T>, index Integer) T

The indexer ([]) operator returns the element at the indexth position in a list.
Indexes in lists are defined to be 0-based.
If the index is greater than the number of elements in the list, the result is null.
If either argument is null, the result is null.
*/

/**
 * Created by Bryn on 5/25/2016.
 */
public class IndexerEvaluator extends org.cqframework.cql.elm.execution.Indexer {

    @Override
    public Object doOperation(String leftOperand, Integer rightOperand) {
        if(rightOperand < 0 || rightOperand >= leftOperand.length()){
            return null;
        }

        return "" + leftOperand.charAt(rightOperand);
    }

    @Override
    public Object doOperation(Iterable<Object> leftOperand, Integer rightOperand) {
        int index = -1;
        for (Object element : leftOperand) {
            index++;
            if (rightOperand == index) {
                return element;
            }
        }
        return null;
    }

    @Override
    public Object evaluate(Context context) {
        Object left = getOperand().get(0).evaluate(context);
        Object right = getOperand().get(1).evaluate(context);

        if (left == null || right == null) { return null; }

        return Execution.resolveSharedDoOperation(this, left, right);
    }
}
