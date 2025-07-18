package com.bortolettotech.shortner_link.service;

import com.bortolettotech.shortner_link.dto.LinkDTO;
import com.bortolettotech.shortner_link.model.Link;
import com.bortolettotech.shortner_link.repository.LinkRepositoryI;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.time.LocalDateTime;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class LinkService {

    private final LinkRepositoryI repository;

    public Link createShortLink(LinkDTO dto) {
        String salt = dto.getUsername() + LocalDateTime.now().toString() + dto.getNotificationType();
        String shortCode = generateShortCode(salt);

        Link link = Link.builder()
                .id(null)
                .originalUrl(dto.getOriginalUrl())
                .shortCode(shortCode)
                .salt(salt)
                .createdAt(LocalDateTime.now())
                .build();

        log.info("Creating short link with shortCode: {}", shortCode);

        return repository.save(link);
    }

    public Optional<Link> getByShortCode(String shortCode) {
        log.info("Searching for link with shortCode: {}", shortCode);

        return repository.findByShortCode(shortCode);
    }

    public Optional<Link> findByOriginalUrl(String originalUrl) {
        log.info("Searching for link with originalUrl: {}", originalUrl);

        return repository.findAll().stream()
                .filter(link -> link.getOriginalUrl().equals(originalUrl))
                .findFirst();
    }

    public void deleteLink(Link link) {
        if (link != null) {
            log.info("Deleting link with shortCode: {}", link.getShortCode());
            repository.delete(link);
        } else {
            throw new RuntimeException("Link not found!");
        }
    }

    private String generateShortCode(String salt) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(salt.getBytes(StandardCharsets.UTF_8));
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < 6; i++) {
                sb.append(String.format("%02x", hash[i]));
            }
            return sb.toString();
        } catch (Exception e) {
            throw new RuntimeException("Error during shortCode generation! Details: ", e);
        }
    }
}