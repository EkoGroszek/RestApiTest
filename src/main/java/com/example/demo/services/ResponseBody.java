package com.example.demo.services;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class ResponseBody {
    String timestamp;
    String status;
    String error;
    String message;
    String path;
}
