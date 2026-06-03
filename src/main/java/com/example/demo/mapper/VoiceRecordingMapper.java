package com.example.demo.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import com.example.demo.entity.VoiceRecording;
import java.util.List;

@Mapper
public interface VoiceRecordingMapper {
    int insert(VoiceRecording recording);
    List<VoiceRecording> findAllWithoutAudio();
    List<VoiceRecording> findByPage(@Param("offset") int offset, @Param("size") int size);
    long countAll();
    VoiceRecording findById(Long id);
    int deleteById(Long id);
}
