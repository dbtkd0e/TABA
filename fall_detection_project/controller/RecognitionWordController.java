package com.fall_detection_project.function_implement.controller;

import com.fall_detection_project.function_implement.domain.RecognitionWord;
import com.fall_detection_project.function_implement.service.RecognitionWordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.List;

@RestController
@RequestMapping("/api/words")
public class RecognitionWordController {
    @Autowired
    private RecognitionWordService recognitionWordService;

    @PostMapping
    public RecognitionWord saveWord(@RequestBody RecognitionWord word) {
        return recognitionWordService.saveWord(word);
    }
    @GetMapping
    public String sendWordsToArduino() {
        List<RecognitionWord> words = recognitionWordService.getAllWords();
        String wordList = String.join(",", words.stream().map(RecognitionWord::getWord).toList());

        // Arduino IP와 통신
        String arduinoIp = "192.168.0.100"; // 실제 Arduino의 IP 주소
        try {
            URL url = new URL("http://" + arduinoIp + "/receive-words?words=" + URLEncoder.encode(wordList, "UTF-8"));
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            int responseCode = connection.getResponseCode();
            if (responseCode == 200) {
                return "Words sent to Arduino successfully";
            } else {
                return "Failed to send words to Arduino";
            }
        } catch (Exception e) {
            e.printStackTrace();
            return "Error occurred: " + e.getMessage();
        }
    }
    @GetMapping("/allWords")
    public List<RecognitionWord> getAllWords() {
        return recognitionWordService.getAllWords();
    }

}
