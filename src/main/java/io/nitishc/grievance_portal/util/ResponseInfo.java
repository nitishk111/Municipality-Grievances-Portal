package io.nitishc.grievance_portal.util;


import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ResponseInfo<T> {

    private int statusCode;

    private String status;

    private T response;

    private String path;


}
