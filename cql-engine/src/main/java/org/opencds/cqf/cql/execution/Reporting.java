package org.opencds.cqf.cql.execution;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

/**
 * Created by Christopher on 12/28/2016.
 */
public class Reporting {

    private JSONArray report;
    public JSONArray getReport() {
        return report;
    }
    public String getReportAsString() { return report.toJSONString(); }
    public void clearReport() { report.clear(); }

    public Reporting() {
        report = new JSONArray();
    }

    public void reportOperation(String evaluator, Object result) {
        JSONObject info = new JSONObject();
        info.put("Operation", evaluator);
        info.put("Result", result);
        report.add(info);
    }

    public void reportOperation(String evaluator, Object operand, Object result) {
        JSONObject info = new JSONObject();
        info.put("Operation", evaluator);
        info.put("Operand", operand);
        info.put("Result", result);
        report.add(info);
    }

    public void reportOperation(String evaluator, Object operand1, Object operand2, Object result) {
        JSONObject info = new JSONObject();
        JSONArray operands = new JSONArray();
        info.put("Operation", evaluator);
        operands.add(operand1);
        operands.add(operand2);
        info.put("Operands", operands);
        info.put("Result", result);
        report.add(info);
    }

    public void reportOperation(String evaluator, Object operand1, Object operand2, Object operand3, Object result) {
        JSONObject info = new JSONObject();
        JSONArray operands = new JSONArray();
        info.put("Operation", evaluator);
        operands.add(operand1);
        operands.add(operand2);
        operands.add(operand3);
        info.put("Operands", operands);
        info.put("Result", result);
        report.add(info);
    }

    public void reportOperation(String evaluator, Object operand1, Object operand2,
                                        Object operand3, Object operand4, Object result) {
        JSONObject info = new JSONObject();
        JSONArray operands = new JSONArray();
        info.put("Operation", evaluator);
        operands.add(operand1);
        operands.add(operand2);
        operands.add(operand3);
        operands.add(operand4);
        info.put("Operands", operands);
        info.put("Result", result);
        report.add(info);
    }

    public void reportOperation(String evaluator, Object operand1, Object operand2,
                                Object operand3, Object operand4, Object operand5, Object result) {
        JSONObject info = new JSONObject();
        JSONArray operands = new JSONArray();
        info.put("Operation", evaluator);
        operands.add(operand1);
        operands.add(operand2);
        operands.add(operand3);
        operands.add(operand4);
        operands.add(operand5);
        info.put("Operands", operands);
        info.put("Result", result);
        report.add(info);
    }

    public void reportOperation(String evaluator, Object operand1, Object operand2,
                                Object operand3, Object operand4, Object operand5,
                                Object operand6, Object result) {
        JSONObject info = new JSONObject();
        JSONArray operands = new JSONArray();
        info.put("Operation", evaluator);
        operands.add(operand1);
        operands.add(operand2);
        operands.add(operand3);
        operands.add(operand4);
        operands.add(operand5);
        operands.add(operand6);
        info.put("Operands", operands);
        info.put("Result", result);
        report.add(info);
    }

    public void reportOperation(String evaluator, Object operand1, Object operand2,
                                Object operand3, Object operand4, Object operand5,
                                Object operand6, Object operand7, Object result) {
        JSONObject info = new JSONObject();
        JSONArray operands = new JSONArray();
        info.put("Operation", evaluator);
        operands.add(operand1);
        operands.add(operand2);
        operands.add(operand3);
        operands.add(operand4);
        operands.add(operand5);
        operands.add(operand6);
        operands.add(operand7);
        info.put("Operands", operands);
        info.put("Result", result);
        report.add(info);
    }

    public void reportOperation(String evaluator, Object operand1, Object operand2,
                                Object operand3, Object operand4, Object operand5,
                                Object operand6, Object operand7, Object operand8, Object result) {
        JSONObject info = new JSONObject();
        JSONArray operands = new JSONArray();
        info.put("Operation", evaluator);
        operands.add(operand1);
        operands.add(operand2);
        operands.add(operand3);
        operands.add(operand4);
        operands.add(operand5);
        operands.add(operand6);
        operands.add(operand7);
        operands.add(operand8);
        info.put("Operands", operands);
        info.put("Result", result);
        report.add(info);
    }
}
