package com.example.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import com.example.demo.entity.VoiceRecording;
import com.example.demo.service.VoiceRecordingService;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class VoiceRecordingController {

    @Autowired
    private VoiceRecordingService voiceRecordingService;

    @GetMapping("/voice")
    public String voicePage() {
        return "voice";
    }

    @PostMapping("/api/voice/save")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> saveRecording(
            @RequestParam("audio") MultipartFile audioFile,
            @RequestParam(value = "duration", defaultValue = "0") Integer duration) {
        try {
            byte[] audioData = audioFile.getBytes();
            VoiceRecording saved = voiceRecordingService.saveRecording(audioData, duration);

            Map<String, Object> result = new HashMap<>();
            result.put("success", true);
            result.put("id", saved.getId());
            result.put("createdAt", saved.getCreatedAt().toString());
            result.put("duration", saved.getDuration());
            return ResponseEntity.ok(result);
        } catch (IOException e) {
            Map<String, Object> error = new HashMap<>();
            error.put("success", false);
            error.put("error", "音频保存失败: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }

    @GetMapping("/api/voice/list")
    @ResponseBody
    public List<Map<String, Object>> listRecordings() {
        List<VoiceRecording> recordings = voiceRecordingService.getAllRecordings();
        return recordings.stream().map(r -> {
            Map<String, Object> item = new HashMap<>();
            item.put("id", r.getId());
            item.put("createdAt", r.getCreatedAt().toString());
            item.put("duration", r.getDuration());
            return item;
        }).toList();
    }

    @GetMapping("/api/voice/audio/{id}")
    @ResponseBody
    public ResponseEntity<byte[]> getAudio(@PathVariable Long id) {
        VoiceRecording recording = voiceRecordingService.getRecordingAudio(id);
        if (recording == null || recording.getAudioData() == null) {
            return ResponseEntity.notFound().build();
        }
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.parseMediaType("audio/webm"));
        headers.setContentLength(recording.getAudioData().length);
        return new ResponseEntity<>(recording.getAudioData(), headers, HttpStatus.OK);
    }
}
