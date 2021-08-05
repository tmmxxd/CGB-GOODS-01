package com.cy.pj.common.web;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import com.cy.pj.common.vo.JsonResult;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**@ControllerAdvice 描述的类为控制层全局异常处理类*/
//@ControllerAdvice
//@ResponseBody
@RestControllerAdvice
public class GlobalExceptionHandler {
      /**
       * @ExceptionHandler 描述的方法为异常处理方法,注解内部定义的
       * 异常类型为此方法可以处理的异常类型
       * @return
       */
	  @ExceptionHandler(RuntimeException.class)
//	  @ResponseBody
	  public JsonResult doHandleRuntimeException(
			  RuntimeException e) {
		  e.printStackTrace();
		  return new JsonResult(e);
	  }
}
