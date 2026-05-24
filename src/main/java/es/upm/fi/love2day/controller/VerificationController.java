package es.upm.fi.love2day.controller;

import es.upm.fi.love2day.model.VerificationInquiry;
import es.upm.fi.love2day.model.VerificationStatus;
import es.upm.fi.love2day.service.VerificationService;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/verification")
public class VerificationController {
    private final VerificationService verificationService;

    public VerificationController(VerificationService verificationService) {
        this.verificationService = verificationService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public VerificationInquiry start(@RequestParam Long userId) {
        return verificationService.startVerification(userId);
    }

    @GetMapping
    public VerificationStatus getStatus(@RequestParam Long userId) {
        return verificationService.getVerificationStatus(userId);
    }
}
