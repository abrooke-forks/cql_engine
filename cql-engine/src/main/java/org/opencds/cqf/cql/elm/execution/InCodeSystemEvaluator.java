package org.opencds.cqf.cql.elm.execution;

import org.cqframework.cql.elm.execution.CodeSystemRef;
import org.cqframework.cql.elm.execution.CodeSystemDef;
import org.opencds.cqf.cql.execution.Context;
import org.opencds.cqf.cql.runtime.Code;
import org.opencds.cqf.cql.runtime.Concept;
import org.opencds.cqf.cql.terminology.CodeSystemInfo;
import org.opencds.cqf.cql.terminology.TerminologyProvider;

/*
in(code String, codesystem CodeSystemRef) Boolean
in(code Code, codesystem CodeSystemRef) Boolean
in(concept Concept, codesystem CodeSystemRef) Boolean

The in (Codesystem) operators determine whether or not a given code is in a particular codesystem.
  Note that these operators can only be invoked by referencing a defined codesystem.
For the String overload, if the given code system contains a code with an equivalent code element, the result is true.
For the Code overload, if the given code system contains an equivalent code, the result is true.
For the Concept overload, if the given code system contains a code equivalent to any code in the given concept, the result is true.
If the code argument is null, the result is null.
*/

/**
* Created by Chris Schuler on 7/13/2016
*/
public class InCodeSystemEvaluator extends org.cqframework.cql.elm.execution.InCodeSystem {

  private Context context;

  public void setContext(Context context) {
    this.context = context;
  }

  public InCodeSystemEvaluator withContext(Context context) {
    setContext(context);
    return this;
  }

  private CodeSystemDef resolveCSR(Context context, CodeSystemRef codesystem) {
    return context.resolveCodeSystemRef(codesystem.getLibraryName(), codesystem.getName());
  }

  @Override
  public Object doOperation(String leftOperand, CodeSystemRef rightOperand) {
    CodeSystemDef csd = resolveCSR(context, rightOperand);
    CodeSystemInfo csi = new CodeSystemInfo().withId(csd.getId()).withVersion(csd.getVersion());
    return context.resolveTerminologyProvider().lookup(new Code().withCode(leftOperand), csi) != null;
  }

  @Override
  public Object doOperation(Code leftOperand, CodeSystemRef rightOperand) {
    CodeSystemDef csd = resolveCSR(context, rightOperand);
    CodeSystemInfo csi = new CodeSystemInfo().withId(csd.getId()).withVersion(csd.getVersion());
    return context.resolveTerminologyProvider().lookup(leftOperand, csi) != null;
  }

  @Override
  public Object doOperation(Concept leftOperand, CodeSystemRef rightOperand) {
    CodeSystemDef csd = resolveCSR(context, rightOperand);
    CodeSystemInfo csi = new CodeSystemInfo().withId(csd.getId()).withVersion(csd.getVersion());

    TerminologyProvider provider = context.resolveTerminologyProvider();
    for (Code code : leftOperand.getCodes()) {
      if (provider.lookup(code, csi) != null) { return true; }
    }
    return false;
  }

  @Override
  public Object evaluate(Context context) {
    Object left = getCode().evaluate(context);
    Object right = getCodesystem();
    this.context = context;

    if (left == null) { return null; }

    return Execution.resolveSharedDoOperation(this, left, right);
  }
}
