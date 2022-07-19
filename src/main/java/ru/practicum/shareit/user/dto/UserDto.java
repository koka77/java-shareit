package ru.practicum.shareit.user.dto;

import lombok.Data;
import lombok.NonNull;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@Data
public class UserDto {
    private Long id;
    private String name;
    
    @Email
    @NotBlank
    private String email;
}
