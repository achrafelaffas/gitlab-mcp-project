package com.achrafelaffas.mcpclient.controller;

import com.achrafelaffas.mcpclient.agents.GitlabAgent;
import org.springframework.web.bind.annotation.*;

@RestController
public class GitlabAgentController {
    private final GitlabAgent agent;

    public GitlabAgentController(GitlabAgent agent) {
        this.agent = agent;
    }

    @PostMapping("/chat/send")
    @ResponseBody
    public String sendMessage(@RequestParam String message) {
        return agent.prompt(message);
    }

}
