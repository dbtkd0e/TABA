package com.fall_detection_project.function_implement.domain;

import jakarta.persistence.*;

@Entity
@Table(name = "recognition_word")
public class RecognitionWord {

    @Id
    @Column(name = "recognition_word_id")
    private long recognitionWordId;

    @Column(name = "wordText")
    private String wordText;

    public Long getId() {
        return recognitionWordId;
    }

    public void setId(Long id) {
        this.recognitionWordId = id;
    }

    public String getWord() {
        return wordText;
    }

    public void setWord(String word) {
        this.wordText = word;
    }

}