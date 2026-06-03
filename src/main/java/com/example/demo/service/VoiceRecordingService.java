package com.example.demo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.example.demo.dto.PageResult;
import com.example.demo.entity.VoiceRecording;
import com.example.demo.mapper.VoiceRecordingMapper;
import java.util.List;

@Service
public class VoiceRecordingService {

    @Autowired
    private VoiceRecordingMapper voiceRecordingMapper;

    public VoiceRecording saveRecording(byte[] audioData, Integer duration) {
        VoiceRecording recording = new VoiceRecording();
        recording.setAudioData(audioData);
        recording.setDuration(duration);
        voiceRecordingMapper.insert(recording);
        // 重新查询以获取数据库自动生成的 created_at
        return voiceRecordingMapper.findById(recording.getId());
    }

    public List<VoiceRecording> getAllRecordings() {
        return voiceRecordingMapper.findAllWithoutAudio();
    }

    public PageResult<VoiceRecording> getRecordingsPage(int page, int size) {
        if (page < 1) {
            page = 1;
        }
        if (size < 1) {
            size = 5;
        }
        int offset = (page - 1) * size;
        List<VoiceRecording> items = voiceRecordingMapper.findByPage(offset, size);
        long total = voiceRecordingMapper.countAll();
        return new PageResult<>(items, total, page, size);
    }

    public VoiceRecording getRecordingAudio(Long id) {
        return voiceRecordingMapper.findById(id);
    }

    public boolean deleteRecording(Long id) {
        return voiceRecordingMapper.deleteById(id) > 0;
    }
}
