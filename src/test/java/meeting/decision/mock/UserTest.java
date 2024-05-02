package meeting.decision.mock;

import com.fasterxml.jackson.databind.ObjectMapper;
import meeting.decision.argumentresolver.Login;
import meeting.decision.dto.user.UserOutDTO;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import jakarta.servlet.http.Cookie;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class UserTest {

    @Autowired
    private MockMvc mockMvc;


    @Test
    @DisplayName("중복 닉네임 확인 테스트")
    public void testDuplicateUsername() throws Exception {
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post("/user/")
                        .param("username", "minguni123")
                        .param("password", "1234"))
                .andExpect(status().isCreated())
                .andReturn();

        MvcResult result2 = mockMvc.perform(MockMvcRequestBuilders.post("/user/")
                        .param("username" , "minguni123")
                        .param("password", "123dsadasd4"))
                .andExpect(status().isBadRequest())
                .andReturn();
    }

    
    @Test
    @DisplayName("로그인 테스트")
    void testLogin() throws Exception{
        signup("1", "1");
        mockMvc.perform(MockMvcRequestBuilders.post("/auth/token")
                        .param("username", "1")
                        .param("password", "12"))
                .andExpect(status().isUnauthorized());

        mockMvc.perform(MockMvcRequestBuilders.post("/auth/token")
                        .param("username", "21")
                        .param("password", "1"))
                .andExpect(status().isUnauthorized());

        mockMvc.perform(MockMvcRequestBuilders.post("/auth/token")
                        .param("username", "1")
                        .param("password", "1"))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("유저 업데이트 테스트")
    public void testUserUpdate() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/user/")
                        .param("username", "myid")
                        .param("password", "1234"))
                .andExpect(status().isCreated());
        Cookie cookie = signupAndLogin("ming", "1234");

        //no cookie
        mockMvc.perform(MockMvcRequestBuilders.put("/user/")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"username\": \"myid\", \"password\" :  \"123123123\"}"))
                .andExpect(status().isForbidden());


        //already username
        mockMvc.perform(MockMvcRequestBuilders.put("/user/")
                        .cookie(cookie)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"username\": \"myid\", \"password\" :  \"123123123\"}"))
                .andExpect(status().isBadRequest());



        mockMvc.perform(MockMvcRequestBuilders.put("/user/")
                        .cookie(cookie)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"username\": \"myid123\", \"password\" :  \"123123123\"}"))
                .andExpect(status().isOk());

        //같은 아이디로 하면 bad request
        mockMvc.perform(MockMvcRequestBuilders.post("/user/")
                        .param("username", "myid123")
                        .param("password", "1234"))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("자기 자신 정보 확인 테스트")
    void testUserMe() throws Exception {
        Cookie cookie = signupAndLogin("user1", "pass");
        mockMvc.perform(MockMvcRequestBuilders.get("/user/me")
                        .cookie(cookie))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value("user1"));
    }

    private void signup(String username, String password) throws Exception{
        mockMvc.perform(MockMvcRequestBuilders.post("/user/")
                        .param("username", username)
                        .param("password", password))
                .andExpect(status().isCreated());
    }


    private Cookie signupAndLogin(String username, String password) throws Exception {
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post("/user/")
                .param("username", username)
                .param("password", password))
                .andReturn();
        MvcResult result1 = mockMvc.perform(MockMvcRequestBuilders.post("/auth/token")
                .param("username", username)
                .param("password", password))
                .andReturn();

        return result1.getResponse().getCookie("JWT_TOKEN");

    }
}
