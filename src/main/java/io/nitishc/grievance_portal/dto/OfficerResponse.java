package io.nitishc.grievance_portal.dto;

import io.nitishc.grievance_portal.enums.Department;
import io.nitishc.grievance_portal.enums.Role;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OfficerResponse {

    private String fullName;

    private String email;

    private String phone;

    private Role role;

    private Department department;
}
