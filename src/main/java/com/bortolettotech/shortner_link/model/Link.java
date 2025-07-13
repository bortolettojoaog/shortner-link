package com.bortolettotech.shortner_link.model;

import lombok.*;
import org.springframework.data.annotation.Id;
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
    private LocalDateTime createdAt;
}