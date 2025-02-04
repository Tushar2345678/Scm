package com.scm.scm20.forms;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class UserForm {

    @NotBlank(message = "Name is required")
    @Size(min =6, message = "Min 5 character required")
    private String name;

    @Email(message = "Invalid Email address. Please enter your email address")
    private String email;

    @NotBlank(message = "Password is required")
    @Size(min = 6, message = "Min 6 character is required")
    private String password;

    @Size(min = 10, max = 10, message = "Phone number must be exactly 10 digits")
    private String phoneNumber;

    @NotBlank(message = "Field cannot empty")
    private String about;
}
