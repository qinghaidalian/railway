package com.example.demo.entity;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class VoiceRecording {
    private Long id;
    private LocalDateTime createdAt;
    private byte[] audioData;
    private Integer duration;
}
