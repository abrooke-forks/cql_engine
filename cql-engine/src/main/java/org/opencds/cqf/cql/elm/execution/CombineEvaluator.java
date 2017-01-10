package org.opencds.cqf.cql.elm.execution;

import org.opencds.cqf.cql.execution.Context;
import java.util.Iterator;

/*
Combine(source List<String>) String
Combine(source List<String>, separator String) String

The Combine operator combines a list of strings, optionally separating each string with the given separator.
If either argument is null, or any element in the source list of strings is null, the result is null.
*/

/**
 * Created by Bryn on 5/25/2016.
 */
public class CombineEvaluator extends org.cqframework.cql.elm.execution.Combine {

    @Override
    public Object doOperation(Iterable<Object> leftOperand, String rightOperand) {
        StringBuffer buffer = new StringBuffer("");
        Iterator iterator = ((Iterable) leftOperand).iterator();
        boolean first = true;
        while (iterator.hasNext()) {
            Object item = iterator.next();
            if (item == null) { return null; }
            if (!first) { buffer.append(rightOperand); }
            else {  first = false; }
            buffer.append((String)item);
        }
        return buffer.toString();
    }

    @Override
    public Object evaluate(Context context) {
        Object left = this.getSource().evaluate(context);
        Object right = this.getSeparator() == null ? "" : this.getSeparator().evaluate(context);

        if (left == null || right == null) { return null; }

        return Execution.resolveStringDoOperation(this, left, right);
    }
}
