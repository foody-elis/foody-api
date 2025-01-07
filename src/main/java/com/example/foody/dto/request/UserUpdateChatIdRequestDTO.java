package com.example.foody.dto.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserUpdateChatIdRequestDTO {
    @NotNull(message = "chatId cannot be null")
    @Positive(message = "chatId cannot be a negative number or zero")
    private Long chatId;
}