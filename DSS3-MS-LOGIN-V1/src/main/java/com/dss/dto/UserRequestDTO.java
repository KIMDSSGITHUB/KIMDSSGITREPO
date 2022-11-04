package com.dss.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserRequestDTO {

    @NotBlank
    @Pattern(regexp = "^[a-zA-Z]+$", message = "must contain letters only")
    private String firstName;

    @NotBlank
    @Pattern(regexp = "^[a-zA-Z]+$", message = "must contain letters only")
    private String lastName;

    @NotBlank
    @Email(message = "must be a valid email")
    private String email;

    @NotBlank
    private String phone;

    @NotBlank
    @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%_*?&])[A-Za-z\\d@$!%_*?&]+$",
            message = "must contain uppercase, lowercase, number, and special character")
    private String password;
}
