package ru.pasha.opencode.controllers;

import java.util.HashMap;
import java.util.Map;

abstract class Commons {
    static Map<String, Object> errorResponse(String errorMessage){
        Map<String, Object> response = new HashMap<>();
        response.put("status", "error");
        response.put("error_message", errorMessage);
        return response;
    }
}
