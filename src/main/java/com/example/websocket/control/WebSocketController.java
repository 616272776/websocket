package com.example.websocket.control;

import com.example.websocket.service.WebSocketService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class WebSocketController {

    @Autowired
    private WebSocketService webSocketService;

    @GetMapping("/test")
    public void test(){
        System.out.println(1);
    }

    @GetMapping("/sendMessage/{message}/toEveryone")
    public void sendAll(@PathVariable String message){
        webSocketService.sendMessageToEveryone(message);
    }

    @GetMapping("/sendMessage/{message}/toSomeOne/{SomeOne}")
    public void sendOne(@PathVariable String message, @PathVariable String SomeOne){
        webSocketService.sendMessageToSomeone(message,SomeOne);
    }

    @GetMapping("/getAllClient")
    public List<String> getAllClient(){
        return WebSocketService.getAllClient();
    }

}
