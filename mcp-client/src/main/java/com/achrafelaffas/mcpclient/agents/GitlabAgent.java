package com.achrafelaffas.mcpclient.agents;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor;
import org.springframework.ai.chat.memory.MessageWindowChatMemory;
import org.springframework.ai.tool.ToolCallbackProvider;
import org.springframework.stereotype.Service;

@Service
public class GitlabAgent {
   private final ChatClient client;

    public GitlabAgent(ChatClient.Builder clientBuilder, ToolCallbackProvider toolCallbackProvider) {
        this.client = clientBuilder
                .defaultSystem("You are an agent that helps users managed their gitlab projects")
                .defaultSystem("when asked that could include or be displayed as lists do somthing like this | Date    | Pipeline   | Branch            | Status | Commit Message    |\n" +
                        "|---------|------------|-------------------|--------|-------------------|\n" +
                        "| May 31  | #1846595292| feature/login-page| ✅     | UI login page2    |\n and dont answer like this Issue ID: #1 (170092697)\n" +
                        "Title: profile page issue\n" +
                        "Description: \"I cannot see my profile page?\"\n" +
                        "Created: July 5, 2025\n" +
                        "Author: Achraf EL AFFAS (you)\n" +
                        "Status: Open\n" +
                        "Assignee: None\n" +
                        "Labels: None\n" +
                        "Comments: 0")
                .defaultToolCallbacks(toolCallbackProvider)
                .defaultAdvisors(MessageChatMemoryAdvisor
                        .builder(MessageWindowChatMemory.builder().build()).build()
                ).build();
    }

    public String prompt(String question) {
        return this.client.prompt().user(question).call().content();
    }
}
