package org.opencds.cqf.cql.elm.execution;

import org.cqframework.cql.elm.execution.Expression;
import org.opencds.cqf.cql.execution.Context;

import java.util.ArrayList;
import java.util.List;

/*
Coalesce<T>(argument1 T, argument2 T) T
Coalesce<T>(argument1 T, argument2 T, argument3 T) T
Coalesce<T>(argument1 T, argument2 T, argument3 T, argument4 T) T
Coalesce<T>(argument1 T, argument2 T, argument3 T, argument4 T, argument5 T) T
Coalesce<T>(arguments List<T>) T

The Coalesce operator returns the first non-null result in a list of arguments.
If all arguments evaluate to null, the result is null.
The static type of the first argument determines the type of the result, and all subsequent arguments must be of that same type.
*/

/**
 * Created by Bryn on 5/25/2016.
 */
public class CoalesceEvaluator extends org.cqframework.cql.elm.execution.Coalesce {

    private Context context;
    public void setContext(Context context) {
        this.context = context;
    }

    @Override
    public Object doOperation(Iterable<Object> operand) {
        List<Expression> operands = new ArrayList<>();
        operand.forEach(ae -> operands.add((Expression) ae));

        for (Expression expression : operands) {
            Object tmpVal = expression.evaluate(context);
            if (tmpVal != null) {
                if (tmpVal instanceof Iterable && operands.size() == 1) {
                    for (Object obj : ((Iterable) tmpVal)) {
                        if (obj != null) {
                            return obj;
                        }
                    }
                    return null;
                }
                return tmpVal;
            }
        }
        return null;
    }

    @Override
    public Object evaluate(Context context) {
        Object operand = getOperand();
        this.context = context;

        return Execution.resolveNullogicalDoOperation(this, operand);
    }
}
