package sample.cafekiosk.spring.api;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class ApiResponse<T> {

    private int code;
    private HttpStatus status;
    private String message;
    private T data;

    public ApiResponse( HttpStatus status, String message, T data) {
        this.data = data;
        this.code = status.value();
        this.message = message;
        this.status = status;
    }


    public static <T> ApiResponse<T> of(HttpStatus httpStatus,String message,T data) {
        return new ApiResponse<>(httpStatus,message,data);
    } //메세지 추가

    public static <T> ApiResponse<T> of(HttpStatus httpStatus,T data) {
        return of(httpStatus,httpStatus.name(),data);
    } //기본형

    public static <T> ApiResponse<T> ok(T data) {
        return of(HttpStatus.OK,HttpStatus.OK.name(), data);
    } //OK 

}
