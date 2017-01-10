package org.opencds.cqf.cql.runtime;

import java.math.BigDecimal;
import java.util.*;
/**
 * Created by Bryn on 5/2/2016.
 */
public class CqlList {

    public static Comparator<Object> valueSort = new Comparator<Object>() {
        public int compare(Object comparandOne, Object comparandTwo) {
          if (comparandOne instanceof Integer) {
            return (Integer)comparandOne - (Integer)comparandTwo;
          }

          else if (comparandOne instanceof BigDecimal) {
            return ((BigDecimal)comparandOne).compareTo((BigDecimal)comparandOne);
          }

          else if (comparandOne instanceof Quantity) {
            return ((Quantity)comparandOne).getValue().compareTo(((Quantity)comparandTwo).getValue());
          }

          else if (comparandOne instanceof DateTime) {
            return ((DateTime)comparandOne).compareTo(((DateTime)comparandTwo));
          }

          else if (comparandOne instanceof Time) {
            return ((Time)comparandOne).compareTo(((Time)comparandTwo));
          }

          throw new IllegalArgumentException("Type is not comparable");
        }
    };

    public static ArrayList<Object> sortList(ArrayList<Object> values) {
        Collections.sort(values, CqlList.valueSort);
        return values;
    }

    public static Iterable<Object> ensureIterable(Object source) {
        if (source instanceof Iterable) {
            return (Iterable<Object>)source;
        }
        else {
            ArrayList sourceList = new ArrayList();
            if (source != null)
                sourceList.add(source);
            return sourceList;
        }
    }
}
