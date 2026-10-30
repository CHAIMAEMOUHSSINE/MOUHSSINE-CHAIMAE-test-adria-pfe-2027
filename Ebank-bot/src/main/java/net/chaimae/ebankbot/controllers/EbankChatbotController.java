package net.chaimae.ebankbot.controllers;


import net.chaimae.ebankbot.agent.EbankAIAgent;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import reactor.core.publisher.Flux;

public class EbankChatbotController {
    private final EbankAIAgent ebankAIAgent;

    public EbankChatbotController(EbankAIAgent ebankAIAgent) {
        this.ebankAIAgent = ebankAIAgent;
    }

    @GetMapping("/chat")
    public String chat(
            @RequestParam(defaultValue = "bonjour") String query,
            @RequestParam(defaultValue = "default-conversation")
            String conversationId) {

        return ebankAIAgent.chat(new Prompt(query));
    }


    @GetMapping(value = "/chatStream", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<String> chatStream(@RequestParam(defaultValue = "bonjour") String query) {
        return ebankAIAgent.chatStream(new Prompt(query));
    }
}
