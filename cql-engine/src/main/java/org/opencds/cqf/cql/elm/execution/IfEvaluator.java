package org.opencds.cqf.cql.elm.execution;

import org.opencds.cqf.cql.execution.Context;

public class IfEvaluator extends org.cqframework.cql.elm.execution.If {

    @Override
    public Object evaluate(Context context) {
        Object condition = getCondition().evaluate(context);

        if (condition == null) {
            condition = false;
        }

        return context.logTrace(this.getClass(), (Boolean)condition ? getThen().evaluate(context) : getElse().evaluate(context), condition);
    }
}
