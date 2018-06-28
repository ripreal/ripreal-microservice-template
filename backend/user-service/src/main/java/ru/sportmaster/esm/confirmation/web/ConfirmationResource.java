package ru.sportmaster.esm.confirmation.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.sportmaster.esm.confirmation.configuration.SMSConfirmationProperties;
import ru.sportmaster.esm.confirmation.service.GenerateCodeException;
import ru.sportmaster.esm.confirmation.service.SMSCodeService;

import java.util.Objects;

@RestController
@RequestMapping("/api/v2/confirmation")
public class ConfirmationResource {

    private final SMSCodeService codeService;
    private final SMSConfirmationProperties properties;

    @Autowired
    public ConfirmationResource(SMSCodeService codeService, SMSConfirmationProperties properties) {
        this.codeService = Objects.requireNonNull(codeService);
        this.properties = Objects.requireNonNull(properties);
    }

    @PostMapping("/{key}")
    public ResponseEntity<?> sendConfirmationCode(@PathVariable String key) {
        try {
            return ResponseEntity.ok(codeService.generate(key));
        } catch (GenerateCodeException ex) {
            return ResponseEntity.badRequest().build();
        }
    }

    @DeleteMapping("/{key}")
    public void invalidateCode(@PathVariable String key) {
        codeService.invalidate(key);
    }

    @GetMapping("/{key}/{code}")
    public boolean validateCode(@PathVariable String key, @PathVariable String code) {
        return codeService.validate(key, code);
    }
}
