package com.enlink.es.controllers.responses;

public enum ResultCode {
    OK("200", "OK"),
    CREATED("201", "Created"),
    ACCEPTED("202", "Accepted"),
    NON_AUTHORITATIVE_INFORMATION("203", "Non-Authoritative Information"),
    NO_CONTENT("204", "No Content"),
    RESET_CONTENT("205", "Reset Content"),
    PARTIAL_CONTENT("206", "Partial Content"),
    MULTI_STATUS("207", "Multi Status"),
    IM_USED("226", "Im Used"),

    // redirection
    MULTIPLE_CHOICES("300", "Multiple Choices"),
    MOVED_PERMANENTLY("301", "Moved Permanently"),
    FOUND("302", "Found"),
    SEE_OTHER("303", "See Other"),
    NOT_MODIFIED("304", "Not Modified"),
    USE_PROXY("305", "Use Proxy"),
    TEMPORARY_REDIRECT("307", "Temporary Redirect"),

    // client error
    BAD_REQUEST("400", "Bad Request"),
    UNAUTHORIZED("401", "Unauthorized"),
    PAYMENT_REQUIRED("402", "Payment Required"),
    FORBIDDEN("403", "Forbidden"),
    NOT_FOUND("404", "Not Found"),
    METHOD_NOT_ALLOWED("405", "Method Not Allowed"),
    NOT_ACCEPTABLE("406", "Not Acceptable"),
    PROXY_AUTHENTICATION_REQUIRED("407", "Proxy Authentication Required"),
    REQUEST_TIMEOUT("408", "Request Timeout"),
    CONFLICT("409", "Conflict"),
    GONE("410", "Gone"),
    LENGTH_REQUIRED("411", "Length Required"),
    PRECONDITION_FAILED("412", "Precondition Failed"),
    REQUEST_ENTITY_TOO_LARGE("413", "Request Entity Too Large"),
    REQUEST_URI_TOO_LONG("414", "Request-URI Too Long"),
    UNSUPPORTED_MEDIA_TYPE("415", "Unsupported Media Type"),
    REQUESTED_RANGE_NOT_SATISFIABLE("416", "Requested Range Not Satisfiable"),
    EXPECTATION_FAILED("417", "Expectation Failed"),
    UNPROCESSABLE_ENTITY("422", "Unprocessable Entity"),
    LOCKED("423", "Locked"),
    FAILED_DEPENDENCY("424", "Failed Dependency"),
    UPGRADE_REQUIRED("426", "Upgrade Required"),
    PRECONDITION_REQUIRED("428", "Precondition Required"),
    TOO_MANY_REQUESTS("429", "Too Many Requests"),
    REQUEST_HEADER_FIELDS_TOO_LARGE("431", "Request Header Fields Too Large"),

    // server error
    INTERNAL_SERVER_ERROR("500", "Internal Server Error"),
    NOT_IMPLEMENTED("501", "Not Implemented"),
    BAD_GATEWAY("502", "Bad Gateway"),
    SERVICE_UNAVAILABLE("503", "Service Unavailable"),
    GATEWAY_TIMEOUT("504", "Gateway Timeout"),
    HTTP_VERSION_NOT_SUPPORTED("505", "HTTP Version Not Supported"),
    INSUFFICIENT_STORAGE("507", "Insufficient Storage"),
    NOT_EXTENDED("510", "Not Extended"),
    NETWORK_AUTHENTICATION_REQUIRED("511", "Network Authentication Required"),

    // system error
    SYSTEM_ERROR("900", "System Error"),
    THE_REQUIRED_PARAMETERS_ARE_MISSING("901", "The Required Parameters Are Missing"),
    PARTIAL_PARAMETER_ERROR("902", "Partial Parameter Error"),
    PARAMETER_TYPE_NOT_CONFORMITY("903", "Parameter Type Not Conformity"),
    NOT_RIGHT_ACCESS("904", "Not Right Access"),
    BODY_MISSING("905", "Body Missing"),
    PAGING_PARAMETER_ERROR("906", "Paging Parameter Error"),
    GETTING_INFORMATION_FAILURE("907", "Getting Information Failure"),
    LOG_SETTING_FAILED("908", "Log Setting Failed");

    private String code;
    private String description;

    private ResultCode(String code, String desc) {
        this.code = code;
        this.description = desc;
    }

    public String getCode() {
        return code;
    }

    public String getDescription() {
        return description;
    }
}

