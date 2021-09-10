package com.apps.controllers;


import com.apps.domain.entity.City;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.RequiredArgsConstructor;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/")
@Api(value = "City APIs")
public class CityController {



//    @ApiOperation(value = "Xem thong tin City", response = City.class)
//    @ApiResponses(value = {
//            @ApiResponse(code = 200, message = "Thành công"),
//            @ApiResponse(code = 401, message = "Chưa xác thực"),
//            @ApiResponse(code = 403, message = "Truy cập bị cấm"),
//            @ApiResponse(code = 404, message = "Không tìm thấy")
//    })
//
}
