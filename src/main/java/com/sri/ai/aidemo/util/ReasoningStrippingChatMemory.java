package com.sri.ai.aidemo.util;

import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.chat.messages.AssistantMessage;
import org.springframework.ai.chat.messages.Message;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ReasoningStrippingChatMemory implements ChatMemory {

    private final ChatMemory delegate;

    public ReasoningStrippingChatMemory(ChatMemory delegate) {
        this.delegate = delegate;
    }

    @Override
    public void add(String conversationId, List<Message> messages) {
        List<Message> sanitized = messages.stream()
                .map(this::stripReasoning)
                .toList();
        delegate.add(conversationId, sanitized);
    }

    private Message stripReasoning(Message message) {
        if (message instanceof AssistantMessage am
                && am.getMetadata() != null
                && am.getMetadata().containsKey("reasoningContent")) {
            Map<String, Object> cleanMeta = new HashMap<>(am.getMetadata());
            cleanMeta.remove("reasoningContent");

            return AssistantMessage.builder()
                    .content(am.getText())
                    .properties(cleanMeta)
                    .toolCalls(am.getToolCalls())
                    .media(am.getMedia())
                    .build();
        }
        return message;
    }

    @Override
    public List<Message> get(String conversationId) {
        return delegate.get(conversationId);
    }

    @Override
    public void clear(String conversationId) {
        delegate.clear(conversationId);
    }
}