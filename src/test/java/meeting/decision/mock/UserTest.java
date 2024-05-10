package meeting.decision.mock;

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
                        .param("password", "123123123"))
                .andExpect(status().isCreated())
                .andReturn();

        MvcResult result2 = mockMvc.perform(MockMvcRequestBuilders.post("/user/")
                        .param("username" , "minguni123")
                        .param("password", "123dsadasd4"))
                .andExpect(status().isBadRequest())
                .andReturn();
    }

    @Test
    @DisplayName("닉네임 validation 테스트1 숫자, 영어 이외의 것이 있으면 안됨")
    public void validationTest1() throws Exception {
        //공백이 있으면 안됨
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post("/user/")
                        .param("username", "min   i3")
                        .param("password", "1234567"))
                .andExpect(status().isBadRequest())
                .andReturn();

        MvcResult result1 = mockMvc.perform(MockMvcRequestBuilders.post("/user/")
                        .param("username", "윤민균입니다")
                        .param("password", "1234567"))
                .andExpect(status().isBadRequest())
                .andReturn();

        MvcResult result2 = mockMvc.perform(MockMvcRequestBuilders.post("/user/")
                        .param("username", "minguniii")
                        .param("password", "123     4"))
                .andExpect(status().isBadRequest())
                .andReturn();

        MvcResult result3 = mockMvc.perform(MockMvcRequestBuilders.post("/user/")
                        .param("username", "mingguniii")
                        .param("password", "비밀번호123"))
                .andExpect(status().isBadRequest())
                .andReturn();


    }

    @Test
    @DisplayName("닉네임 validation 테스트2 비어 있으면 안됨")
    public void validationTest2() throws Exception {
        //공백이 있으면 안됨
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post("/user/")
                        .param("username", "")
                        .param("password", "12345678"))
                .andExpect(status().isBadRequest())
                .andReturn();

        MvcResult result1 = mockMvc.perform(MockMvcRequestBuilders.post("/user/")
                        .param("username", "masdasdasd")
                        .param("password", ""))
                .andExpect(status().isBadRequest())
                .andReturn();
    }

    @Test
    @DisplayName("닉네임 validation 테스트3 안보내면 안됨")
    public void validationTest3() throws Exception {
        //공백이 있으면 안됨
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post("/user/"))
                .andExpect(status().isBadRequest())
                .andReturn();

        MvcResult result1 = mockMvc.perform(MockMvcRequestBuilders.post("/user/")
                        .param("username", "masdasdasd"))
                .andExpect(status().isBadRequest())
                .andReturn();

        MvcResult result2 = mockMvc.perform(MockMvcRequestBuilders.post("/user/")
                        .param("password", "asdasdasd"))
                .andExpect(status().isBadRequest())
                .andReturn();
    }

    @Test
    @DisplayName("닉네임 validation 테스트4 5자리 이하거나 16자리 이상이면 안됨")
    public void validationTest4() throws Exception {
        //공백이 있으면 안됨
        MvcResult result1 = mockMvc.perform(MockMvcRequestBuilders.post("/user/")
                        .param("username", "masda")
                        .param("password", "asdasdasd"))
                .andExpect(status().isBadRequest())
                .andReturn();

        MvcResult result2 = mockMvc.perform(MockMvcRequestBuilders.post("/user/")
                        .param("username", "masdaasdasd")
                        .param("password", "asdas"))
                .andExpect(status().isBadRequest())
                .andReturn();

        MvcResult result3 = mockMvc.perform(MockMvcRequestBuilders.post("/user/")
                        .param("username", "masdaasdfasdfasasa")
                        .param("password", "asdasdasd"))
                .andExpect(status().isBadRequest())
                .andReturn();

        MvcResult result4 = mockMvc.perform(MockMvcRequestBuilders.post("/user/")
                        .param("username", "masdaasdasd")
                        .param("password", "masdaasdfasdfasasa"))
                .andExpect(status().isBadRequest())
                .andReturn();

    }

    
    @Test
    @DisplayName("로그인 테스트")
    void testLogin() throws Exception{
        signup("1234567", "1234567");
        mockMvc.perform(MockMvcRequestBuilders.post("/auth/token")
                        .param("username", "1")
                        .param("password", "1234567"))
                .andExpect(status().isUnauthorized());

        mockMvc.perform(MockMvcRequestBuilders.post("/auth/token")
                        .param("username", "1234567")
                        .param("password", "1"))
                .andExpect(status().isUnauthorized());

        mockMvc.perform(MockMvcRequestBuilders.post("/auth/token")
                        .param("username", "1234567")
                        .param("password", "1234567"))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("유저 업데이트 테스트")
    public void testUserUpdate() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/user/")
                        .param("username", "dupulicatename")
                        .param("password", "1234567"))
                .andExpect(status().isCreated());
        Cookie cookie = signupAndLogin("username", "1234567");

        //no cookie
        mockMvc.perform(MockMvcRequestBuilders.put("/user/")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"username\": \"newusername\", \"password\" :  \"123123123\"}"))
                .andExpect(status().isUnauthorized());


        //already username
        mockMvc.perform(MockMvcRequestBuilders.put("/user/")
                        .cookie(cookie)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"username\": \"dupulicatename\", \"password\" :  \"123123123\"}"))
                .andExpect(status().isBadRequest());



        mockMvc.perform(MockMvcRequestBuilders.put("/user/")
                        .cookie(cookie)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"username\": \"newusername\", \"password\" :  \"123123123\"}"))
                .andExpect(status().isOk());

        //같은 아이디로 하면 bad request
        mockMvc.perform(MockMvcRequestBuilders.post("/user/")
                        .param("username", "newusername")
                        .param("password", "123123123"))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("자기 자신 정보 확인 테스트")
    void testUserMe() throws Exception {
        Cookie cookie = signupAndLogin("user1234567", "pass1234567");
        mockMvc.perform(MockMvcRequestBuilders.get("/user/me")
                        .cookie(cookie))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value("user1234567"));
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
                .andExpect(status().isCreated())
                .andReturn();
        MvcResult result1 = mockMvc.perform(MockMvcRequestBuilders.post("/auth/token")
                .param("username", username)
                .param("password", password))
                .andExpect(status().isOk())
                .andReturn();

        return result1.getResponse().getCookie("JWT_TOKEN");

    }
}
