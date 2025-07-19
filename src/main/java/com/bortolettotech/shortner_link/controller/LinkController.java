package com.bortolettotech.shortner_link.controller;

import com.bortolettotech.shortner_link.dto.LinkDTO;
import com.bortolettotech.shortner_link.model.Link;
import com.bortolettotech.shortner_link.service.LinkService;
import com.bortolettotech.shortner_link.type.NotificationType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;
import java.util.Map;
import java.util.Optional;

@Controller
@RequiredArgsConstructor
@Slf4j
public class LinkController {

    private final LinkService service;

    @PostMapping("/shortner-link")
    @ResponseBody
    public ResponseEntity<?> generateLink(@RequestBody LinkDTO dto) {
        log.info("Received request to generate short link: {}", dto);

        if (dto.getOriginalUrl() == null || dto.getUsername() == null || dto.getNotificationType() == null) {
            return ResponseEntity.badRequest().body("Missing parameters: originalUrl, username, or notificationType");
        }

        if (!dto.getOriginalUrl().contains("http") && !dto.getOriginalUrl().contains("https")) {
            log.warn("The URL must contain 'http' or 'https'. Adding 'https://' to the URL.");
            dto.setOriginalUrl("https://" + dto.getOriginalUrl());
        }

        try {
            NotificationType notificationType = Arrays.stream(NotificationType.values())
                .filter(type -> type.name().equalsIgnoreCase(String.valueOf(dto.getNotificationType())))
                .findFirst()
                .orElseThrow();
            dto.setNotificationType(notificationType);
        } catch (Exception e) {
            log.error("Invalid notification type: {}", dto.getNotificationType(), e);
            return ResponseEntity.badRequest().body("Incorrect notification type: " + dto.getNotificationType());
        }

        Optional<Link> existing = service.findByOriginalUrl(dto.getOriginalUrl());
        if (existing.isPresent()) {
            return ResponseEntity.ok(Map.of("shortCode", existing.get().getShortCode()));
        }

        Link link = service.createShortLink(dto);
        log.info("Short link created successfully: {}", link);
        return ResponseEntity.ok(Map.of("shortCode", link.getShortCode()));
    }

    @GetMapping("/{shortCode}")
    public void redirect(@PathVariable String shortCode, HttpServletResponse response) throws IOException {
        log.info("Received request to redirect for short code: {}", shortCode);

        Optional<Link> linkOpt = service.getByShortCode(shortCode);

        if (linkOpt.isPresent()) {
            String url = linkOpt.get().getOriginalUrl();
            if (!url.startsWith("http://") && !url.startsWith("https://")) {
                url = "https://" + url;
            }
            response.sendRedirect(url);
        } else {
            response.sendError(HttpServletResponse.SC_NOT_FOUND, "Link not found");
        }
    }

    @DeleteMapping
    public ResponseEntity<?> deleteLink(@RequestParam String shortCode) {
        log.info("Received request to delete link with short code: {}", shortCode);

        Optional<Link> linkOpt = service.getByShortCode(shortCode);
        if (linkOpt.isPresent()) {
            service.deleteLink(linkOpt.get());
            return ResponseEntity.ok(Map.of("message", "Link deleted successfully"));
        } else {
            return ResponseEntity.status(404).body(Map.of("message", "Link not found"));
        }
    }
}