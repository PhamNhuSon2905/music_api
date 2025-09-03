package com.music_backend_api.music_api.controller.admin;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/admin/dashboard")
public class DashboardAdminController {

    @GetMapping("/index")
    public String dashboard() {
        return "admin/dashboard/index";
    }
}
