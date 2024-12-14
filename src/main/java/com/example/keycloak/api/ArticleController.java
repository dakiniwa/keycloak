package com.example.keycloak.api;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/articles")
public class ArticleController {

    private static final String ROLE_PREMIUM = "ROLE_premium_access";

    private final Map<Long, Article> articles = createArticles();

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

    // In-Method Authorization
    @GetMapping("/all/{id}")
    public ResponseEntity<?> getArticleById(@PathVariable Long id, Authentication authentication) {
        if (!articles.containsKey(id)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("This content may have already deleted.");
        }

        var article = articles.get(id);
        if (article.isPremium()) {
            boolean isPremiumUser = authentication.getAuthorities().contains(new SimpleGrantedAuthority(ROLE_PREMIUM));
            if (isPremiumUser) {
                return ResponseEntity.ok(article);
            }

            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("This content is only available for premium members.");
        }

        return ResponseEntity.ok(article);
    }


    private Map<Long, Article> createArticles() {

        return Map.of(1L, new Article(1L, "Free Article", "This is a free article", false), 2L, new Article(2L, "Premium Article", "This is a premium article", true));
    }

}
