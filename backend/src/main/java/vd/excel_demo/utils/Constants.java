package vd.excel_demo.utils;

import org.springframework.http.MediaType;

public class Constants {

    public static final String SPACE = " ";
    public static final String APPLICATION_JSON = "application/json;charset=UTF-8";
    public static final String ERROR_CODE = "Error code: ";
    public static final String ACCESS_DENIED = "Access denied! ";
    public static final String UNAUTHORISED = "Unauthorised! ";
    public static final String AUTHORITY_ID = "authority_id";

    public static final String TYPE = "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet";
    public static final MediaType APPLICATION_ATOM_XML = MediaType.parseMediaType(TYPE);
}
