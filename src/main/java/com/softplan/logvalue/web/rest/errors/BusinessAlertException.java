package com.softplan.logvalue.web.rest.errors;

import org.zalando.problem.AbstractThrowableProblem;
import org.zalando.problem.Status;

import static org.zalando.problem.Status.BAD_REQUEST;

import java.net.URI;
import java.util.HashMap;
import java.util.Map;

public class BusinessAlertException extends AbstractThrowableProblem {

    private static final long serialVersionUID = 1L;

    private static final String PARAM = "param";

    public BusinessAlertException(String message, String... params) {
        this(message, toParamMap(params));
    }

    public BusinessAlertException(String message, Map<String, Object> paramMap) {
        super(ErrorConstants.PARAMETERIZED_TYPE, "Parameterized Exception", BAD_REQUEST, null, null, null, toProblemParameters(message, paramMap));
    }


    public static Map<String, Object> toParamMap(String... params) {
        Map<String, Object> paramMap = new HashMap<>();
        if (params != null && params.length > 0) {
            for (int i = 0; i < params.length; i++) {
                paramMap.put(PARAM + i, params[i]);
            }
        }
        return paramMap;
    }

    public static Map<String, Object> toProblemParameters(String message, Map<String, Object> paramMap) {
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("message", message);
        parameters.put("params", paramMap);
        return parameters;
    }

    // public BadRequestAlertException(URI type, String defaultMessage, String entityName, String errorKey) {
    //     super(type, defaultMessage, Status.BAD_REQUEST, null, null, null, getAlertParameters(entityName, errorKey));
    //     this.entityName = entityName;
    //     this.errorKey = errorKey;
    // }

    // public String getEntityName() {
    //     return entityName;
    // }

    // public String getErrorKey() {
    //     return errorKey;
    // }

    // private static Map<String, Object> getAlertParameters(String entityName, String errorKey) {
    //     Map<String, Object> parameters = new HashMap<>();
    //     parameters.put("message", "error." + errorKey);
    //     parameters.put("params", entityName);
    //     return parameters;
    // }
}
