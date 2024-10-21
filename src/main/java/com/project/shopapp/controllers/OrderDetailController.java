package com.project.shopapp.controllers;


import com.project.shopapp.dtos.OrderDetailDTO;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("${api.prefix}/order_details")
public class OrderDetailController {
    @PostMapping("")
    public ResponseEntity<?> createOrderDetail(@RequestBody @Valid OrderDetailDTO orderDetailDTO, BindingResult bindingResult){
        try {
            if(bindingResult.hasErrors()){
                List<String> errorMessage = bindingResult.getFieldErrors()
                        .stream()
                        .map(FieldError::getDefaultMessage)
                        .toList();
                return ResponseEntity.badRequest().body(errorMessage);
            }
            return ResponseEntity.ok("Create order detail successfully");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/{orderId}")
    public ResponseEntity<?> getOrderDetail(@PathVariable long orderId){
        return ResponseEntity.ok("Get order detail successfully" + orderId);
    }

    @PutMapping("/{orderId}")
    public ResponseEntity<?> updateOrderDetail(@PathVariable long orderId, @RequestBody @Valid OrderDetailDTO orderDetailDTO){
        return ResponseEntity.ok("Update order detail successfully" + orderId + "new: " + orderDetailDTO);
    }

    @DeleteMapping("/{orderId}")
    public ResponseEntity<?> deleteOrderDetail(@PathVariable long orderId){
        return ResponseEntity.ok("Delete order detail successfully" + orderId);
    }
}
