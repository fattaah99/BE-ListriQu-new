package org.acme.util;

import jakarta.ws.rs.core.Response;
import java.util.Map;

public class SuccessResponse {

    public static Response ok(String message) {
        return Response.ok(Map.of(
                "status", "success",
                "message", message
        )).build();
    }

    public static Response ok(String message, Object data) {
        return Response.ok(Map.of(
                "status", "success",
                "message", message,
                "data", data
        )).build();
    }
}
