package com.nhnacademy.examdooraymessagesender.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nhnacademy.examdooraymessagesender.domain.SearchResult;
import com.nhnacademy.examdooraymessagesender.formatter.Formatter;
import com.nhnacademy.examdooraymessagesender.mapper.IdMapper;
import com.nhnacademy.examdooraymessagesender.parser.CSVParser;
import com.nhnacademy.examdooraymessagesender.repo.MessageRepository;
import com.nhnacademy.examdooraymessagesender.sender.MessageSender;
import jakarta.servlet.http.HttpSession;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.client.RestTemplate;

/**
 * @author : 이성준
 * @since : 1.0
 */

@Controller
@RequestMapping("/")
@RequiredArgsConstructor
@Slf4j
public class ExplorerController {

    private final CSVParser csvParser;
    private final Formatter formatter;
    private final IdMapper idMapper;
    private final HttpSession httpSession;
    private final MessageRepository<String, Map<Map.Entry<String, String>, String>> messageRepository;
    private final MessageSender messageSender;

    @GetMapping
    public String explore(Model model) {
        Map<Map.Entry<String, String>, String> idAndNameEntryToFormattedMessage =
                messageRepository.findById(httpSession.getId());

        model.addAttribute("messages", idAndNameEntryToFormattedMessage);
        model.addAttribute("token", httpSession.getAttribute("token"));
        return "explorer";
    }

    @PostMapping("/send-message")
    public String sendMessage(@RequestParam String token) {
        messageSender.send(messageRepository.findById(httpSession.getId()), token);
        return "redirect:/";
    }


    @PostMapping("/preview-message")
    public String sendViaCsv(@RequestPart(name = "csv") String csv,
                             @RequestParam String token) {
        Map<String, Map<String, String>> map = csvParser.parse(csv);
        Map<String, String> nameToFormattedMessage = formatter.format(map);
        Map<Map.Entry<String, String>, String> idAndNameEntryToFormattedMessage =
                idMapper.mapId(nameToFormattedMessage, token);

        httpSession.setAttribute("token", token);
        messageRepository.save(httpSession.getId(), idAndNameEntryToFormattedMessage);

        return "redirect:/";
    }
}

