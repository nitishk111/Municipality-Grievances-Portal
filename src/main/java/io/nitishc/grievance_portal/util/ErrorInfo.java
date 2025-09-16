package io.nitishc.grievance_portal.util;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ErrorInfo {

    private int statusCode;
    private String status;
    private String errors;
    private String path;
}
