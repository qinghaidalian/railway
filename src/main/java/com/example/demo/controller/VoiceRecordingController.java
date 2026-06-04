package com.example.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import com.example.demo.dto.PageResult;
import com.example.demo.entity.VoiceRecording;
import com.example.demo.service.VoiceRecordingService;

import jakarta.servlet.http.HttpServletRequest;

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
    public PageResult<Map<String, Object>> listRecordings(
            @RequestParam(value = "page", defaultValue = "1") int page,
            @RequestParam(value = "size", defaultValue = "5") int size) {
        PageResult<VoiceRecording> pageResult = voiceRecordingService.getRecordingsPage(page, size);
        List<Map<String, Object>> items = pageResult.getItems().stream().map(r -> {
            Map<String, Object> item = new HashMap<>();
            item.put("id", r.getId());
            item.put("createdAt", r.getCreatedAt().toString());
            item.put("duration", r.getDuration());
            return item;
        }).toList();

        PageResult<Map<String, Object>> result = new PageResult<>();
        result.setItems(items);
        result.setTotal(pageResult.getTotal());
        result.setPage(pageResult.getPage());
        result.setSize(pageResult.getSize());
        return result;
    }

    @GetMapping("/api/voice/audio/{id}")
    @ResponseBody
    public ResponseEntity<byte[]> getAudio(@PathVariable Long id, HttpServletRequest request) {
        VoiceRecording recording = voiceRecordingService.getRecordingAudio(id);
        if (recording == null || recording.getAudioData() == null) {
            return ResponseEntity.notFound().build();
        }

        byte[] audioData = recording.getAudioData();
        long fileLength = audioData.length;

        // Handle HTTP Range requests — required for HTML5 audio seeking
        String rangeHeader = request.getHeader("Range");
        if (rangeHeader != null && rangeHeader.startsWith("bytes=")) {
            String[] parts = rangeHeader.substring(6).split("-");
            long start = Long.parseLong(parts[0]);
            long end = (parts.length > 1 && !parts[1].isEmpty())
                    ? Long.parseLong(parts[1])
                    : fileLength - 1;

            if (start >= fileLength) {
                return ResponseEntity.status(HttpStatus.REQUESTED_RANGE_NOT_SATISFIABLE)
                        .header("Content-Range", "bytes */" + fileLength)
                        .build();
            }

            end = Math.min(end, fileLength - 1);
            long contentLength = end - start + 1;

            byte[] partialData = new byte[(int) contentLength];
            System.arraycopy(audioData, (int) start, partialData, 0, (int) contentLength);

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.parseMediaType("audio/webm"));
            headers.setContentLength(contentLength);
            headers.set("Content-Range", "bytes " + start + "-" + end + "/" + fileLength);
            headers.set("Accept-Ranges", "bytes");
            headers.set("Cache-Control", "no-cache");

            return new ResponseEntity<>(partialData, headers, HttpStatus.PARTIAL_CONTENT);
        }

        // Full response (no Range header)
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.parseMediaType("audio/webm"));
        headers.setContentLength(fileLength);
        headers.set("Accept-Ranges", "bytes");
        headers.set("Cache-Control", "no-cache");
        return new ResponseEntity<>(audioData, headers, HttpStatus.OK);
    }

    @DeleteMapping("/api/voice/{id}")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> deleteRecording(@PathVariable Long id) {
        boolean deleted = voiceRecordingService.deleteRecording(id);
        Map<String, Object> result = new HashMap<>();
        result.put("success", deleted);
        if (!deleted) {
            result.put("error", "录音不存在或已删除");
        }
        return ResponseEntity.ok(result);
    }
}
