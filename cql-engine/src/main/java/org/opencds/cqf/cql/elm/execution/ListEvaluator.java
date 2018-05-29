package org.opencds.cqf.cql.elm.execution;

import org.opencds.cqf.cql.execution.Context;

import java.util.ArrayList;

public class ListEvaluator extends org.cqframework.cql.elm.execution.List {

    @Override
    public Object evaluate(Context context) {
        ArrayList<Object> result = new ArrayList<>();
        for (org.cqframework.cql.elm.execution.Expression element : this.getElement()) {
            result.add(element.evaluate(context));
        }
        return result;
    }
}
