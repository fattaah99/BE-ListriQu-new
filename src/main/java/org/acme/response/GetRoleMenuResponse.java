package org.acme.response;

import java.util.List;

public class GetRoleMenuResponse {
    public String status;
    // public String message;
    public List<MenuDto> data;

    public static class MenuDto {
        public Long menu_id;
        public String menu_name;
        public String menu_url;
        public String menu_icon;
        public Integer menu_order;
        public Long parent_id;
    }
}
