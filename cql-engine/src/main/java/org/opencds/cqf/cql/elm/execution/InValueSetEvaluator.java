package org.opencds.cqf.cql.elm.execution;

import org.cqframework.cql.elm.execution.ValueSetRef;
import org.cqframework.cql.elm.execution.ValueSetDef;
import org.cqframework.cql.elm.execution.CodeSystemRef;
import org.cqframework.cql.elm.execution.CodeSystemDef;
import org.opencds.cqf.cql.execution.Context;
import org.opencds.cqf.cql.runtime.Code;
import org.opencds.cqf.cql.runtime.Concept;
import org.opencds.cqf.cql.terminology.CodeSystemInfo;
import org.opencds.cqf.cql.terminology.TerminologyProvider;
import org.opencds.cqf.cql.terminology.ValueSetInfo;
import java.util.List;
import java.util.ArrayList;

/*
in(code String, valueset ValueSetRef) Boolean
in(code Code, valueset ValueSetRef) Boolean
in(concept Concept, valueset ValueSetRef) Boolean

The in (Valueset) operators determine whether or not a given code is in a particular valueset.
  Note that these operators can only be invoked by referencing a defined valueset.
For the String overload, if the given valueset contains a code with an equivalent code element, the result is true.
For the Code overload, if the given valueset contains an equivalent code, the result is true.
For the Concept overload, if the given valueset contains a code equivalent to any code in the given concept, the result is true.
If the code argument is null, the result is null.
*/

/**
* Created by Chris Schuler on 7/13/2016
*/
public class InValueSetEvaluator extends org.cqframework.cql.elm.execution.InValueSet {

  private List<ValueSetInfo> valueSetInfos;
  private TerminologyProvider provider;
  private Context context;

  public void setContext(Context context) {
    this.context = context;
  }

  public InValueSetEvaluator withContext(Context context) {
    setContext(context);
    return this;
  }

  private ValueSetDef resolveVSR(Context context, ValueSetRef valueset) {
    return context.resolveValueSetRef(valueset.getLibraryName(), valueset.getName());
  }

  private CodeSystemDef resolveCSR(Context context, CodeSystemRef codesystem) {
    return context.resolveCodeSystemRef(codesystem.getLibraryName(), codesystem.getName());
  }

  public void setup(ValueSetRef rightOperand) {
    // Resolve ValueSetRef & CodeSystemRef -- Account for multiple codesystems represented within a valueset
    ValueSetDef vsd = resolveVSR(context, rightOperand);
    List<CodeSystemDef> codeSystemDefs = new ArrayList<>();
    for (CodeSystemRef csr : vsd.getCodeSystem()) {
      codeSystemDefs.add(resolveCSR(context, csr));
    }

    List<CodeSystemInfo> codeSystemInfos = new ArrayList<>();
    if (codeSystemDefs.size() > 0) {
      for (CodeSystemDef csd : codeSystemDefs) {
        codeSystemInfos.add(new CodeSystemInfo().withId(csd.getId()).withVersion(csd.getVersion()));
      }
    }
    // TODO: find better solution than this -- temporary solution
    else {
      codeSystemInfos.add(new CodeSystemInfo().withId(null).withVersion(null));
    }

    valueSetInfos = new ArrayList<>();
    for (CodeSystemInfo csi : codeSystemInfos) {
      valueSetInfos.add(new ValueSetInfo().withId(vsd.getId()).withVersion(vsd.getVersion()).withCodeSystem(csi));
    }

    provider = context.resolveTerminologyProvider();
  }

  @Override
  public Object doOperation(String leftOperand, ValueSetRef rightOperand) {
    setup(rightOperand);
    for (ValueSetInfo vsi : valueSetInfos) {
      if (provider.in(new Code().withCode(leftOperand), vsi)) { return true; }
    }
    return false;
  }

  @Override
  public Object doOperation(Code leftOperand, ValueSetRef rightOperand) {
    setup(rightOperand);
    for (ValueSetInfo vsi : valueSetInfos) {
      if (provider.in(leftOperand, vsi)) { return true; }
    }
    return false;
  }

  @Override
  public Object doOperation(Concept leftOperand, ValueSetRef rightOperand) {
    setup(rightOperand);
    for (ValueSetInfo vsi : valueSetInfos) {
      for (Code codes : leftOperand.getCodes()) {
        if (provider.in(codes, vsi)) { return true; }
      }
      // return false;
    }
    return false;
  }

  @Override
  public Object evaluate(Context context) {
    Object left = getCode().evaluate(context);
    Object right = getValueset();
    this.context = context;

    if (left == null) { return null; }

    return Execution.resolveSharedDoOperation(this, left, right);
  }
}
