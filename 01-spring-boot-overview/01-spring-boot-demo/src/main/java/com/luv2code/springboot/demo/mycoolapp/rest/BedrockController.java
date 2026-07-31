package com.luv2code.springboot.demo.mycoolapp.rest;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.bedrockruntime.BedrockRuntimeClient;
import software.amazon.awssdk.services.bedrockruntime.model.ContentBlock;
import software.amazon.awssdk.services.bedrockruntime.model.ConversationRole;
import software.amazon.awssdk.services.bedrockruntime.model.ConverseRequest;
import software.amazon.awssdk.services.bedrockruntime.model.ConverseResponse;
import software.amazon.awssdk.services.bedrockruntime.model.InferenceConfiguration;
import software.amazon.awssdk.services.bedrockruntime.model.Message;

@RestController
public class BedrockController {

    private final BedrockRuntimeClient bedrock = BedrockRuntimeClient.builder()
            .region(Region.US_EAST_1)
            .build();

    @GetMapping("/api/ask")
    public String ask(@RequestParam String prompt) {
        Message message = Message.builder()
                .role(ConversationRole.USER)
                .content(ContentBlock.fromText(prompt))
                .build();

        ConverseResponse response = bedrock.converse(ConverseRequest.builder()
                .modelId("amazon.nova-micro-v1:0")
                .messages(message)
                .inferenceConfig(InferenceConfiguration.builder()
                        .maxTokens(200)
                        .build())
                .build());

        return response.output()
                .message()
                .content()
                .get(0)
                .text();
    }
}