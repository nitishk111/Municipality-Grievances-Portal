package io.nitishc.grievance_portal.dto;

import io.nitishc.grievance_portal.model.Address;
import io.nitishc.grievance_portal.model.Comment;
import io.nitishc.grievance_portal.enums.Priority;
import io.nitishc.grievance_portal.enums.Status;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Data
@NoArgsConstructor
public class GrievanceResponse {

    private long grievanceId;

    private String complaintTitle;

    private String complaintDescription;

    private Address address;

    private Status status;

    private Priority priority;

    private LocalDate createdAt;

    private LocalDate lastUpdate;

    private List<Comment> comments;

    private List<String> imageUrls;
}
