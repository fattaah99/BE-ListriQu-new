package org.acme.util;

import jakarta.ws.rs.core.Response;
import java.util.Map;

public class ErrorResponse {

    public static Response unauthorized(String message) {
        return Response.status(Response.Status.UNAUTHORIZED)
                .entity(Map.of(
                        "status", "error",
                        "message", message
                ))
                .build();
    }

    public static Response forbidden(String message) {
        return Response.status(Response.Status.FORBIDDEN)
                .entity(Map.of(
                        "status", "error",
                        "message", message
                ))
                .build();
    }

    public static Response badRequest(String message) {
        return Response.status(Response.Status.BAD_REQUEST)
                .entity(Map.of(
                        "status", "error",
                        "message", message
                ))
                .build();
    }

    public static Response serverError(String message, Exception e) {
        return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                .entity(Map.of(
                        "status", "error",
                        "message", message,
                        "details", e.getMessage()
                ))
                .build();
    }
    public static Response notFound(String message) {
    return Response.status(Response.Status.NOT_FOUND)
        .entity(Map.of("status", "error", "message", message))
        .build();
}


   
    
}
