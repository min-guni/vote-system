package meeting.decision.controller;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import meeting.decision.argumentresolver.Login;
import meeting.decision.dto.user.UserUpdateDTO;
import meeting.decision.jwt.JwtTokenProvider;
import meeting.decision.service.UserService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
@Slf4j
public class UserController {

    private final UserService userService;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;


    @PostMapping("/")
    public String signUp(@RequestParam("username") String username, @RequestParam("password") String password){
        //암호화
        userService.create(username, passwordEncoder.encode(password));
        return "success";
    }


    @PutMapping("/")
    public String updateUser(@Login Long userId, @RequestBody UserUpdateDTO updateParam){
        log.info(updateParam.getUsername());
        log.info(updateParam.getPassword());
        updateParam.setPassword(passwordEncoder.encode(updateParam.getPassword()));
        userService.update(userId, updateParam);
        return "success";
    }



}
