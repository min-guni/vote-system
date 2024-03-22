package meeting.decision.controller;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import meeting.decision.argumentresolver.Login;
import meeting.decision.argumentresolver.SessionConst;
import meeting.decision.domain.User;
import meeting.decision.dto.UserUpdateDTO;
import meeting.decision.exception.LoginFailedException;
import meeting.decision.exception.UsernameAlreadyExistsException;
import meeting.decision.jwt.JwtTokenProvider;
import meeting.decision.service.RoomService;
import meeting.decision.service.UserService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.security.auth.login.LoginException;
import java.time.LocalDateTime;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
@Slf4j
public class UserController {

    private final UserService userService;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;


    @PostMapping("/signup")
    public String signUp(@RequestParam("username") String username, @RequestParam("password") String password){
        //암호화
        userService.create(username, passwordEncoder.encode(password));
        return "success";
    }

    @PostMapping("/login")
    public ResponseEntity<String> logIn(@RequestParam("username") String username, @RequestParam("password") String password, HttpServletRequest request){
//        //간단하게 토큰기반이 아닌 세션 기반의 로그인 활용
//        Long userId = userService.checkUser(username, password);
//        HttpSession session = request.getSession();
//        session.setAttribute(SessionConst.LOGIN_USER_ID, userId);
        Long userId = userService.checkUser(username, password);
        String jwt = jwtTokenProvider.createToken(userId);
        ResponseCookie jwtCookie = ResponseCookie.from("JWT_TOKEN", jwt)
                .httpOnly(true)
                .secure(true)
                .path("/")
                .maxAge(3600)
                .build();
        return ResponseEntity.ok().header(HttpHeaders.SET_COOKIE, jwtCookie.toString()).body("login success");

    }

    @PostMapping("/")
    public UserUpdateDTO updateUser(@Login Long userId, @ModelAttribute UserUpdateDTO updateParam){
        //update
        userService.update(userId, updateParam);
        return updateParam;
    }


}
