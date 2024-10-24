package com.act.Gakos.controller;

import com.act.Gakos.entity.FatherDetails;
import com.act.Gakos.entity.User;
import com.act.Gakos.service.FatherService;
import com.act.Gakos.response.BaseResponse;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/fathers")
@RequiredArgsConstructor
public class FatherController {

    private final FatherService fatherService;
    private final static Logger logger = LoggerFactory.getLogger(FatherController.class);

    @PostMapping("/add")
    public ResponseEntity<BaseResponse<User>> addFather(@RequestParam String username, @RequestBody FatherDetails father) {
        try{
            BaseResponse<User> response = fatherService.addFatherToUser(username, father);
            logger.info("Father Response: {}", response);
            return ResponseEntity.ok(response);
        }catch(Exception e){
            logger.warn("Error: {}", e.getMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
