package com.example.keycloak.api;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/articles")
public class ArticleController {

    // Pre Authorization 1
    @GetMapping("/basic")
    @PreAuthorize("hasAnyRole('basic_access', 'premium_access')")
    public String getBasicArticle() {
        return "Free Article";
    }

    // Pre Authorization 2
    @GetMapping("/premium")
    @PreAuthorize("hasRole('premium_access')")
    public String getPremiumArticle() {
        return "Premium Article";
    }

}
