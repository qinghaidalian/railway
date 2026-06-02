package com.example.demo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
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

    public VoiceRecording getRecordingAudio(Long id) {
        return voiceRecordingMapper.findById(id);
    }
}
