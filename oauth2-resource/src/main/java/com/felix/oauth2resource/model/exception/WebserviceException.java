package com.felix.oauth2resource.model.exception;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

@Getter
@Slf4j
public class WebserviceException extends RuntimeException {
    private static final long serialVersionUID = -3549694939635244564L;
    protected int code;
    protected String errorBody;

    public WebserviceException(int code, String message) {
        super(message);
        this.code = code;
        this.errorBody = null;
    }

    public WebserviceException(int code, String message, String errorBody) {
        super(message);
        this.code = code;
        this.errorBody = errorBody;
    }

    public static WebserviceException of(int code) {
        return of(code, null, null);
    }

    public static WebserviceException of(int code, String message) {
        return of(code, message, null);
    }

    public static WebserviceException of(int code, String message, String errorBody) {
        switch (code) {
        case 400:
            throw new BadRequestException(message, errorBody);
        case 401:
            throw new UnauthorizedException(message, errorBody);
        case 402:
            throw new ClientException(code, message == null ? "402 Payment Required" : message, errorBody);
        case 403:
            throw new ForbiddenException(message, errorBody);
        case 404:
            throw new NotFoundException(message, errorBody);
        case 405:
            throw new MethodNotAllowedException(message, errorBody);
        case 406:
            throw new NotAcceptableException(message, errorBody);
        case 407:
            throw new ClientException(code, message == null ? "407 Proxy Authentication Required" : message, errorBody);
        case 408:
            throw new RequestTimeoutException(message, errorBody);
        case 409:
            throw new ConflictException(message, errorBody);
        case 410:
            throw new GoneException(message, errorBody);
        case 411:
            throw new LengthRequiredException(message, errorBody);
        case 412:
            throw new ClientException(code, message == null ? "412 Precondition Failed" : message, errorBody);
        case 413:
            throw new PayloadTooLargeException(message, errorBody);
        case 414:
            throw new UriTooLongException(message, errorBody);
        case 415:
            throw new UnsupportedMediaTypeException(message, errorBody);
        case 416:
            throw new ClientException(code, message == null ? "416 Range Not Satisfiable" : message, errorBody);
        case 417:
            throw new ClientException(code, message == null ? "417 Expectation Failed" : message, errorBody);
        case 418:
            throw new ClientException(code, message == null ? "418 I'm a teapot" : message, errorBody);
        case 421:
            throw new ClientException(code, message == null ? "421 Misdirected Request" : message, errorBody);
        case 422:
            throw new ClientException(code, message == null ? "422 Unprocessable Entity" : message, errorBody);
        case 423:
            throw new ClientException(code, message == null ? "423 Locked" : message, errorBody);
        case 424:
            throw new ClientException(code, message == null ? "424 Failed Dependency" : message, errorBody);
        case 425:
            throw new ClientException(code, message == null ? "425 Too Early" : message, errorBody);
        case 426:
            throw new ClientException(code, message == null ? "426 Update Required" : message, errorBody);
        case 428:
            throw new ClientException(code, message == null ? "428 Precondition Required" : message, errorBody);
        case 429:
            throw new TooManyRequestsException(message, errorBody);
        case 431:
            throw new RequestHeaderFieldsTooLargeException(message, errorBody);
        case 500:
            throw new InternalServerErrorException(message, errorBody);
        case 501:
            throw new NotImplementedException(message, errorBody);
        case 502:
            throw new BadGatewayException(message, errorBody);
        case 503:
            throw new ServiceUnavailableException(message, errorBody);
        case 504:
            throw new GatewayTimeoutException(message, errorBody);
        case 505:
            throw new HttpVersionNotSupportedException(message, errorBody);
        case 506:
            throw new ServerException(code, message == null ? "506 Variant Also Negotiates" : message, errorBody);
        case 507:
            throw new ServerException(code, message == null ? "507 Insufficient Storage" : message, errorBody);
        case 508:
            throw new ServerException(code, message == null ? "508 Loop Detected" : message, errorBody);
        case 510:
            throw new ServerException(code, message == null ? "510 Not Extended" : message, errorBody);
        case 511:
            throw new ServerException(code, message == null ? "511 Network Authentication Required" : message,
                    errorBody);

        default:
            log.warn("Unexpected HTTP error code {}." + code);
            if (code >= 400 && code < 500) {
                throw new ClientException(code, message);
            } else if (code >= 500 && code < 600) {
                throw new ServerException(code, message);
            } else
                throw new IllegalArgumentException("HTTP status " + code + " is not an error.");
        }
    }
}
