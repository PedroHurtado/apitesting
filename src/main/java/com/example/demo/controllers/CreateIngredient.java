package com.example.demo.controllers;

import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


@Component
public class CreateIngredient {
    
    public record Request(String name,Double cost) {
    }
    public record Response(UUID id,String name,Double cost){

    }

    @RestController    
    public class Controler{
        @PostMapping("/api/ingredients")
        public ResponseEntity<?> handler(@RequestBody Request request) {
            
            var response = new Response(UUID.randomUUID(), request.name(), request.cost());
            return ResponseEntity.status(201).body(response);
        }
        
    }   
}
