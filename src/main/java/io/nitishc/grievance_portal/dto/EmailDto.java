package io.nitishc.grievance_portal.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class EmailDto {

    String to;
    String subject;
    String body;
}
