package com.example.yousignproto;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class YousignEventController {
    @PostMapping("/api/events")
    public void listeners(@RequestBody String event) {
        System.out.println("Event received: " + event);
    }
}
