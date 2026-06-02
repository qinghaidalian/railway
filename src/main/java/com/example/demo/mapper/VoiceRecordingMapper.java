package com.example.demo.mapper;

import org.apache.ibatis.annotations.Mapper;
import com.example.demo.entity.VoiceRecording;
import java.util.List;

@Mapper
public interface VoiceRecordingMapper {
    int insert(VoiceRecording recording);
    List<VoiceRecording> findAllWithoutAudio();
    VoiceRecording findById(Long id);
}
