package com.bortolettotech.shortner_link.dto;

import com.bortolettotech.shortner_link.type.NotificationType;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class LinkDTO {
    private String originalUrl;
    private String username;
    private NotificationType notificationType;
}
