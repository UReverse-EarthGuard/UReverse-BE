package com.earth.ureverse.global.common.controller;

import com.earth.ureverse.global.common.response.CommonResponseEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/example")
public class CommonController {
    //성공
    @GetMapping("/success")
    public CommonResponseEntity<String> getSuccessExample() {
        String data = "This is a successful response";
        return CommonResponseEntity.success(data);
    }

    //에러
    @GetMapping("/error")
    public ResponseEntity<CommonResponseEntity<Object>> getErrorExample() {
        CommonResponseEntity<Object> body =
                CommonResponseEntity.error(HttpStatus.INTERNAL_SERVER_ERROR, "An error occurred");
        return new ResponseEntity<>(body, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
