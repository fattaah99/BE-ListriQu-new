package org.acme.dto;

import java.util.List;
import java.util.Map;

public class LoginResponseDto {
    public String status;
    public String message;
    public Data data;

    public static class Data {
        public int user_id;
        public String username;
        public String full_name;
        public int role_id;
        public String token;
        public List<Map<String, Object>> menu;
    }
}
