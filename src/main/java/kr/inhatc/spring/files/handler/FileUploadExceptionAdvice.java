package kr.inhatc.spring.files.handler;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import kr.inhatc.spring.files.upload.message.ResponseMessage;

@ControllerAdvice	// 모든 @Controller 즉, 전역에서 발생할 수 있는 예외를 잡아 처리해주는 annotation이다.
public class FileUploadExceptionAdvice extends ResponseEntityExceptionHandler
{
	@ExceptionHandler(MaxUploadSizeExceededException.class)
	public ResponseEntity<ResponseMessage> handleMaxSizeException(MaxUploadSizeExceededException ex)
	{
		return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(new ResponseMessage("파일이 너무 큽니다!"));
	}
}
