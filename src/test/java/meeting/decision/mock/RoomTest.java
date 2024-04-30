package meeting.decision.mock;


import jakarta.servlet.http.Cookie;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class RoomTest {
    @Autowired
    private MockMvc mockMvc;



    @Test
    void testAddRoom() throws Exception {
        for(int i = 0; i < 10; i ++){
            signup("user" + i,"password" + i);
        }



    }


    private void signup(String username, String password) throws Exception{
        mockMvc.perform(MockMvcRequestBuilders.post("/user/")
                        .param("username", username)
                        .param("password", password))
                .andExpect(status().isOk());
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
