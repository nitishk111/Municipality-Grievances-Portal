package io.nitishc.grievance_portal.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class UserSignupRequest {

    @NotBlank(message = "Name is Mandatory")
    @Size(min = 3, max = 30, message = "Name must be between 3-30 character")
    private String fullName;

    @NotBlank(message = "Password needed")
    @Size(min = 1, message = "Password should contain at least 6 character")
    private String password;

    @NotBlank(message = "Email is mandatory")
    @Email(message = "Email should be valid")
    private String email;

    @Size(min=1, max=10, message = "Phone no should be of 10 digits only")
    private String phone;

}
