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

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class RoomTest {
    @Autowired
    private MockMvc mockMvc;
    
    
    @Test
    void testCreateRoom() throws Exception{
        Cookie cookie = signupAndLogin("user", "user");
        makeRoom("room", cookie);
    }


    @Test
    void testAddRoom() throws Exception {
        for (int i = 0; i < 10; i++) {
            signup("user" + i, "password" + i);
        }
        signupAndLogin("user", "password");



//        for(int i = 0; i < 10; i ++){
//            mockMvc.perform(MockMvcRequestBuilders.put("/room/")
//                    .
//        }



    }

    private MvcResult makeRoom(String roomName, Cookie cookie) throws Exception {
        return mockMvc.perform(MockMvcRequestBuilders.post("/room/")
                        .cookie(cookie)
                .content(roomName))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.roomName").value(roomName))
                .andReturn();
    }


    private void signup(String username, String password) throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/user/")
                        .param("username", username)
                        .param("password", password))
                .andExpect(status().isOk());
    }

    private Cookie signupAndLogin(String username, String password) throws Exception {
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post("/user/")
                        .param("username", username)
                        .param("password", password))
                .andExpect(status().isOk())
                .andReturn();
        MvcResult result1 = mockMvc.perform(MockMvcRequestBuilders.post("/auth/token")
                        .param("username", username)
                        .param("password", password))
                .andExpect(status().isOk())
                .andReturn();

        return result1.getResponse().getCookie("JWT_TOKEN");

    }

    private Cookie login(String username, String password) throws Exception {
        MvcResult result1 = mockMvc.perform(MockMvcRequestBuilders.post("/auth/token")
                        .param("username", username)
                        .param("password", password))
                .andExpect(status().isOk())
                .andReturn();

        return result1.getResponse().getCookie("JWT_TOKEN");
    }
}