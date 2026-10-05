package com.bortolettotech.shortner_link.model;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Document(collection = "links")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Link {

    @Id
    private String id;

    private String originalUrl;
    private String shortCode;
    private String salt;

    @Indexed(name = "createdAt_ttl", expireAfterSeconds = 30 * 24 * 60 * 60)
    private LocalDateTime createdAt;
}