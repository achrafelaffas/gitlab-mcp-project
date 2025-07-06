package com.achrafelaffas.mcpserver.tools;


import com.achrafelaffas.mcpserver.entity.GitLabProject;
import lombok.Builder;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;

@Service
@Builder
public class GitlabTools {

    private final String token =System.getenv("GITLAB_TOKEN");

    public GitlabTools() {}

    @Tool(description = "This function list all the project inside gitlab account")
    public List<GitLabProject> getOwnedProjects() {
        RestTemplate restTemplate = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(token);
        headers.setAccept(List.of(MediaType.APPLICATION_JSON));

        HttpEntity<String> entity = new HttpEntity<>(headers);

        ResponseEntity<GitLabProject[]> response = restTemplate.exchange(
                "https://gitlab.com/api/v4/projects?owned=true",
                HttpMethod.GET,
                entity,
                GitLabProject[].class
        );

        assert response.getBody() != null;
        return Arrays.asList(response.getBody());
    }

    @Tool(description = "Get all pipeline information for a GitLab project by its ID")
    public List<Object> getPipelinesForProject(Long projectId) {
        RestTemplate restTemplate = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(token);
        headers.setAccept(List.of(MediaType.APPLICATION_JSON));
        HttpEntity<String> entity = new HttpEntity<>(headers);

        String url = "https://gitlab.com/api/v4/projects/" + projectId + "/pipelines";

        ResponseEntity<Object[]> response = restTemplate.exchange(
                url,
                HttpMethod.GET,
                entity,
                Object[].class
        );

        assert response.getBody() != null;
        return Arrays.asList(response.getBody());
    }

    @Tool(description = "Get all open issues for a GitLab project by its ID")
    public List<Object> getOpenIssuesForProject(Long projectId) {
        RestTemplate restTemplate = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(token);
        headers.setAccept(List.of(MediaType.APPLICATION_JSON));
        headers.set("User-Agent", "JavaApp");

        HttpEntity<String> entity = new HttpEntity<>(headers);

        String url = "https://gitlab.com/api/v4/projects/" + projectId + "/issues";

        ResponseEntity<Object[]> response = restTemplate.exchange(
                url,
                HttpMethod.GET,
                entity,
                Object[].class
        );

        assert response.getBody() != null;
        return Arrays.asList(response.getBody());
    }

    @Tool(description = "List all branches for a GitLab project by its ID")
    public List<Object> getBranches(Long projectId) {
        RestTemplate restTemplate = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(token);
        headers.setAccept(List.of(MediaType.APPLICATION_JSON));
        headers.set("User-Agent", "JavaApp");

        HttpEntity<String> entity = new HttpEntity<>(headers);

        String url = "https://gitlab.com/api/v4/projects/" + projectId + "/repository/branches";

        ResponseEntity<Object[]> response = restTemplate.exchange(
                url,
                HttpMethod.GET,
                entity,
                Object[].class
        );

        assert response.getBody() != null;
        return Arrays.asList(response.getBody());
    }

    @Tool(description = "Retry GitLab pipeline by project ID and pipeline ID")
    public void retryPipeline(Long projectId, Long pipelineId) {
        RestTemplate restTemplate = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(token);
        headers.setAccept(List.of(MediaType.APPLICATION_JSON));
        headers.set("User-Agent", "JavaApp");

        HttpEntity<String> entity = new HttpEntity<>(headers);

        String url = "https://gitlab.com/api/v4/projects/" + projectId + "/pipelines/" + pipelineId + "/retry";

        ResponseEntity<Object> response = restTemplate.exchange(
                url,
                HttpMethod.POST,
                entity,
                Object.class
        );
    }

}
