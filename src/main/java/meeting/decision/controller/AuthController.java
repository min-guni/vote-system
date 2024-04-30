package meeting.decision.controller;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import meeting.decision.jwt.JwtTokenProvider;
import meeting.decision.service.UserService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@Slf4j
public class AuthController {


    private final UserService userService;
    private final JwtTokenProvider jwtTokenProvider;

    @PostMapping("/token")
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
}
