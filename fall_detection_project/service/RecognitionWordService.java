package com.fall_detection_project.function_implement.service;

import com.fall_detection_project.function_implement.domain.RecognitionWord;
import com.fall_detection_project.function_implement.repository.RecognitionWordRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RecognitionWordService {
    @Autowired
    private RecognitionWordRepository recognitionWordRepository;

    public RecognitionWord saveWord(RecognitionWord word) {
        return recognitionWordRepository.save(word);
    }

    public List<RecognitionWord> getAllWords() {
        return recognitionWordRepository.findAll();
    }
}
