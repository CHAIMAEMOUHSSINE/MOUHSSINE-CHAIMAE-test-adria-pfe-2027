package net.chaimae.ebankbot.agent;



import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.tool.ToolCallbackProvider;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import reactor.core.publisher.Flux;
@Service
public class EbankAIAgent {

    ChatClient chatClient;

    public EbankAIAgent(ChatClient.Builder chatClient, ChatMemory chatMemory, ToolCallbackProvider tools) {
        this.chatClient = chatClient
                .defaultSystem("""
   
    """)
                .defaultAdvisors(MessageChatMemoryAdvisor.builder(chatMemory)
                        .build())
                .defaultAdvisors(a -> a.param(ChatMemory.CONVERSATION_ID, "default"))
                .defaultTools(tools)
                .build();
    }


    public String chat(Prompt prompt) {

        return chatClient.prompt(prompt).call().content();
    }

    public Flux<String> chatStream(Prompt prompt) {
        return chatClient.prompt(prompt).stream().content();
    }


}