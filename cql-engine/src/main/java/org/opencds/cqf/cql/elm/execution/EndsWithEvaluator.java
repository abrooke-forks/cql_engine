package org.opencds.cqf.cql.elm.execution;

import org.opencds.cqf.cql.execution.Context;

/*
* EndsWith(argument String, suffix String) Boolean
*
* The EndsWith operator returns true if the given string starts with the given suffix.
    If the suffix is the empty string, the result is true.
    If either argument is null, the result is null.
*
* */

public class EndsWithEvaluator extends org.cqframework.cql.elm.execution.EndsWith {

    public static Object endsWith(Object argument, Object suffix) {
        if (argument == null || suffix == null) {
            return null;
        }

        if (argument instanceof String && suffix instanceof String) {
            return ((String) suffix).isEmpty() || ((String) argument).endsWith((String) suffix);
        }

        throw new IllegalArgumentException(
                String.format("Cannot perform EndsWith operator on types %s and %s",
                        argument.getClass().getSimpleName(), suffix.getClass().getSimpleName()));
    }

    @Override
    public Object evaluate(Context context) {
        Object argument = getOperand().get(0).evaluate(context);
        Object suffix = getOperand().get(1).evaluate(context);

        return context.logTrace(this.getClass(), endsWith(argument, suffix), argument, suffix);
    }
}
