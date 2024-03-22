package meeting.decision.argumentresolver;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import meeting.decision.domain.User;
import meeting.decision.exception.UserNotFoundErrorException;
import meeting.decision.jwt.JwtTokenProvider;
import org.springframework.core.MethodParameter;
import org.springframework.security.access.AuthorizationServiceException;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

@Component
@RequiredArgsConstructor
public class LoginUserArgumentResolver implements HandlerMethodArgumentResolver {
    private final JwtTokenProvider jwtTokenProvider;

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        boolean hasLoginAnnotation = parameter.hasParameterAnnotation(Login.class);
        boolean hasMemberType = Long.class.isAssignableFrom(parameter.getParameterType());
        return hasLoginAnnotation && hasMemberType;
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
//        HttpServletRequest request = (HttpServletRequest) webRequest.getNativeRequest();
//        HttpSession session = request.getSession(false);
//        if(session == null){
//            throw new UserNotFoundErrorException();
//        }
//        return session.getAttribute("loginUserId");
        String token = null;
        HttpServletRequest request = (HttpServletRequest) webRequest.getNativeRequest();
        if (request.getCookies() != null) {
            for (Cookie cookie : request.getCookies()) {
                if ("JWT_TOKEN".equals(cookie.getName())) {
                    token = cookie.getValue();
                    break;
                }
            }
        }
        if(token != null){
            try{
                return jwtTokenProvider.getUserIdFromToken(token);
            }
            catch (Exception e){
                throw new AuthorizationServiceException("Not Authorized", e);
            }
        }
        throw new AuthorizationServiceException("Token Not Found");
    }
}
