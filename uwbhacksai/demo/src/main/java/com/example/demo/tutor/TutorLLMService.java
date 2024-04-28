package com.example.demo.tutor;

import com.azure.ai.openai.OpenAIClient;
import com.azure.ai.openai.models.*;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Service layer class that manages conversational interactions with an AI model via the OpenAIClient.
 * This service is designed to facilitate a dialogue-based interaction using a GPT model, allowing
 * customization of responses based on the content and predefined instructions.
 * <p>
 * The service maintains a list of chat messages to keep track of the conversation history,
 * which is used to generate context-aware responses from the AI.
 *
 * @author Andres Lince
 */
@Service
public class TutorLLMService {

    private final String model = "gpt-3.5-turbo";
    private final OpenAIClient openAIClient;
    private final List<ChatRequestMessage> chatMessages = new ArrayList<>();

    /**
     * Constructs a TutorLLMService with a specified OpenAIClient.
     * The OpenAIClient is used to interact with the LLM to generate chat completions
     * based on the provided conversation history.
     *
     * @param openAIClient the client used to interact with OpenAI services.
     */
    public TutorLLMService(OpenAIClient openAIClient) {
        this.openAIClient = openAIClient;
    }

    private ChatCompletionsOptions getChatCompletionOptions() {
        ChatCompletionsOptions chatCompletionsOptions = new ChatCompletionsOptions(chatMessages);
        ChatCompletionsFunctionToolDefinition tools = new ChatCompletionsFunctionToolDefinition(
                new FunctionDefinition("startGrading")
                        .setDescription("Call this function when you finish asking all the questions to the student.")
        );
        chatCompletionsOptions.setTools(Collections.singletonList(tools));
        return chatCompletionsOptions;
    }

    /**
     * Initiates a conversation with the AI model using the provided content and tutor personality.
     * This method adds the initial user message and predefined instructions to the chat history,
     * then generates an AI response which is also added to the chat history.
     *
     * @param content the initial content of the conversation provided by the user, setting the context of the dialogue.
     * @param tutorPersonality a string representing the desired personality traits of the tutor, which may influence
     *        the AI's response style or content.
     * @return the AI-generated response as a string.
     */
    public String startConversation(String content, String tutorPersonality) {
        // Reset the chat messages to start a new conversation
        chatMessages.clear();

        // Add the initial system message with predefined instructions
        chatMessages.add(new ChatRequestSystemMessage(content + "\n---\n" + PREDIFINED_INSTRUCTIONS));

        // Generate the first AI response based on the system instructions
        ChatCompletions chatCompletions = openAIClient.getChatCompletions(model, getChatCompletionOptions());
        String tutorResponse = chatCompletions.getChoices().get(0).getMessage().getContent();

        // Add the AI-generated response to the chat history
        chatMessages.add(new ChatRequestAssistantMessage(tutorResponse));

        return tutorResponse;
    }

    /**
     * Processes a user response by adding it to the conversation history and requesting the AI to generate a follow-up response.
     * This method facilitates an ongoing dialogue, ensuring that each user input is contextually considered
     * by the AI in subsequent responses.
     *
     * @param response the user's response to the AI's previous message.
     * @return the AI-generated follow-up response as a string.
     */
    public TutorResponse processResponse(String response) {
        chatMessages.add(new ChatRequestUserMessage(response));

        // Generate the next AI response based on the user input
        ChatCompletions chatCompletions = openAIClient.getChatCompletions(model, getChatCompletionOptions());
        ChatChoice choice = chatCompletions.getChoices().get(0);

        // Get the AI-generated response and add it to the chat history
        String tutorResponse = choice.getMessage().getContent();
        boolean startGrading = false;
        chatMessages.add(new ChatRequestAssistantMessage(tutorResponse));

        // Check if the AI has requested to call a tool
        if (choice.getFinishReason().equals(CompletionsFinishReason.TOOL_CALLS)) {
            // The AI has requested to call a tool, handle this case
            ChatCompletionsFunctionToolCall toolCall = (ChatCompletionsFunctionToolCall) choice.getMessage().getToolCalls().get(0);
            String function = toolCall.getFunction().getName();
            if (function.equals("startGrading")) {
                startGrading = true;
                // TODO: Implement grading logic

            }
        }

        return new TutorResponse(tutorResponse, startGrading);
    }

    //public String

    private static final String PREDIFINED_INSTRUCTIONS =
            """
            You are an expert tutor when it comes to the content provided above. Your task is to test a student's understanding of the content by asking detailed questions that challenge their knowledge. You should be friendly and treat this as a casual conversation between you and your student, while at the same time testing them on ensuring that the interaction remains focused on extracting a clear and thorough understanding of the subject matter. Remember, your goal is to evaluate the student's comprehension and not to instruct or guide them towards the answers. Your questions should be open-ended to allow the student to express their understanding in their own words.
            Begin by greeting the student and explaining what the test is going to be about. Follow this by probing deeper with questions that require the student to apply concepts, analyze information, and express reasoning. Encourage them to elaborate on their responses and to provide examples where applicable.
            Throughout the session, maintain a supportive tone and be receptive to their explanations, using their responses to craft follow-up questions that further challenge their understanding. This process will not only help in assessing their grasp of the material but also in identifying any misconceptions or gaps in knowledge that may need attention in future assessments.
            Make sure to enumerate the questions.
            Every time the student responds a question, provide very detailed feedback on what they did well and areas where they can improve. Make sure to be very kind and not give all the answers right away to let the student rationalize. Then, proceed to the next question to continue the evaluation process. After the second question, start the grading process.
            Right now, start with the greeting message and then proceed with only asking the first question. Remember to keep the conversation engaging and focused on the subject matter.
            """;
}
