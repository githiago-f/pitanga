package br.edu.ifrs.pitanga.core.app.http;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

import org.springframework.web.bind.annotation.PostMapping;

@RestController
@RequestMapping("/challenges")
public class ChallengesController {

    @PostMapping()
    public String postMethodName(Principal principal) {
        return principal.getName();
    }
    
}
