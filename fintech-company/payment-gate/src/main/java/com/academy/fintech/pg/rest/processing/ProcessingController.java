package com.academy.fintech.pg.rest.processing;

import com.academy.fintech.pg.core.service.processing.ProcessingService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/payment-gate/payment")
@RequiredArgsConstructor
public class ProcessingController {

    private final ProcessingService processingService;

    @PostMapping
    public ResponseEntity<Void> processing(@RequestBody ProcessingRequest request) {
        boolean resultProcessing = processingService.processing(request);
        if (resultProcessing) {
            return ResponseEntity.status(HttpStatus.OK).build();
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
    }

}
