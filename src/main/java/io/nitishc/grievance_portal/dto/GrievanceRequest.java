package io.nitishc.grievance_portal.dto;

import io.nitishc.grievance_portal.model.Address;
import io.nitishc.grievance_portal.enums.Department;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class GrievanceRequest {

    private Department grievanceType;

    private String complaintTitle;

    private String complaintDescription;

    private Address address;

}
