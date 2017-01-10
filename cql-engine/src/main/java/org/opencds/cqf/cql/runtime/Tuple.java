package org.opencds.cqf.cql.runtime;

import org.opencds.cqf.cql.elm.execution.EqualEvaluator;
import org.opencds.cqf.cql.elm.execution.Execution;

import java.util.HashMap;

/**
* Created by Chris Schuler on 6/15/2016
*/
public class Tuple {

  protected HashMap<String, Object> elements;

  public Object getElement(String key) {
    return elements.get(key);
  }

  public HashMap<String, Object> getElements() {
    if (elements == null) { return new HashMap<String, Object>(); }
    return elements;
  }

  public void setElements(HashMap<String, Object> elements) {
    this.elements = elements;
  }

  public Tuple withElements(HashMap<String, Object> elements) {
    setElements(elements);
    return this;
  }

  public Boolean equal(Tuple other) {
    HashMap<String, Object> leftMap = getElements();
    HashMap<String, Object> rightMap = other.getElements();
    for (String key : rightMap.keySet()) {
      if (leftMap.containsKey(key)) {
        Boolean equal = (Boolean) Execution.resolveComparisonDoOperation(new EqualEvaluator(), rightMap.get(key), leftMap.get(key));
        if (equal == null) { return null; }
        else if (!equal) { return false; }
      }
      else { return false; }
    }
    return true;
  }
}
