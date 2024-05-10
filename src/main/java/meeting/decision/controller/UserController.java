package meeting.decision.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import meeting.decision.argumentresolver.Login;
import meeting.decision.dto.user.UserInDTO;
import meeting.decision.dto.user.UserOutDTO;
import meeting.decision.dto.user.UserUpdateDTO;
import meeting.decision.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
@Slf4j
public class UserController {

    private final UserService userService;
    private final PasswordEncoder passwordEncoder;


    @PostMapping("/")
    @ResponseStatus(HttpStatus.CREATED)
    public UserOutDTO signUp(@Validated UserInDTO userInDTO){
        //암호화
        return userService.create(userInDTO.getUsername(), passwordEncoder.encode(userInDTO.getPassword()));
    }

    @GetMapping("/me")
    public UserOutDTO whoAmI(@Login Long userId){
        return userService.findById(userId);
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
