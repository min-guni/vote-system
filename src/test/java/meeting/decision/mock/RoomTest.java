package meeting.decision.mock;


import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityManager;
import jakarta.servlet.http.Cookie;
import meeting.decision.dto.room.RoomOutDTO;
import meeting.decision.dto.user.UserOutDTO;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class RoomTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    EntityManager em;
    
    
    @Test
    void testCreateRoom() throws Exception{
        Cookie cookie = signupAndLogin("user", "user");
        makeRoom("room", cookie);
    }


    @Test
    @DisplayName("Room에 사용자를 추가하는 테스트")
    void testAddRoom() throws Exception {
        Map<Long, UserOutDTO> dtoList = new HashMap<>();
        for (int i = 0; i < 10; i++) {
            UserOutDTO user = signup("user" + i, "password" + i);
            dtoList.put(user.getId(), user);
        }
        UserOutDTO user1 = signup("user", "password");
        Cookie cookie = login("user", "password");

        MvcResult result = makeRoom("room1", cookie);
        String content = result.getResponse().getContentAsString();
        RoomOutDTO roomOutDTO = objectMapper.readValue(content, RoomOutDTO.class);





        for(Long id : dtoList.keySet()){
            mockMvc.perform(MockMvcRequestBuilders.put("/room/" + roomOutDTO.getRoomId() + "/user/" + id)
                    .cookie(cookie))
                    .andExpect(status().isOk());
        }

        MvcResult result1 = mockMvc.perform(MockMvcRequestBuilders.get("/room/" + roomOutDTO.getRoomId() + "/users")
                        .cookie(cookie))
                .andExpect(status().isOk())
                .andReturn();

        String content1 = result1.getResponse().getContentAsString();
        List<UserOutDTO> roomResult = objectMapper.readValue(content1, new TypeReference<List<UserOutDTO>>(){});
        assertThat(roomResult.size()).isEqualTo(11);
        dtoList.put(user1.getId(), user1);
        for(int i = 0; i < 11; i ++){
            assertThat(roomResult.get(i)).isEqualTo(dtoList.get(roomResult.get(i).getId()));
        }

    }

    @Test
    @DisplayName("채팅방 인원 삭제 테스트")
    void deleteTest() throws Exception {
        int num = 10;

        Map<Long, UserOutDTO> addDTOList = new HashMap<>();
        for (int i = 0; i < num; i++) {
            UserOutDTO user = signup("adduser" + i, "password" + i);
            addDTOList.put(user.getId(), user);
        }
        Map<Long, UserOutDTO> delDTOList = new HashMap<>();
        for (int i = 0; i < num; i++) {
            UserOutDTO user = signup("deluser" + i, "password" + i);
            delDTOList.put(user.getId(), user);
        }
        UserOutDTO user1 = signup("user", "password");
        Cookie cookie = login("user", "password");

        MvcResult result = makeRoom("room1", cookie);
        String content = result.getResponse().getContentAsString();
        RoomOutDTO roomOutDTO = objectMapper.readValue(content, RoomOutDTO.class);

        for(Long id : addDTOList.keySet()){
            mockMvc.perform(MockMvcRequestBuilders.put("/room/" + roomOutDTO.getRoomId() + "/user/" + id)
                            .cookie(cookie))
                    .andExpect(status().isOk());
        }
        for(Long id : delDTOList.keySet()){
            mockMvc.perform(MockMvcRequestBuilders.put("/room/" + roomOutDTO.getRoomId() + "/user/" + id)
                            .cookie(cookie))
                    .andExpect(status().isOk());
        }
        MvcResult result1 = mockMvc.perform(MockMvcRequestBuilders.get("/room/" + roomOutDTO.getRoomId() + "/users")
                        .cookie(cookie))
                .andExpect(status().isOk())
                .andReturn();

        String content1 = result1.getResponse().getContentAsString();
        List<UserOutDTO> roomResult = objectMapper.readValue(content1, new TypeReference<List<UserOutDTO>>(){});
        assertThat(roomResult.size()).isEqualTo(num * 2 + 1);
        em.clear();


        //삭제 기능
        for(Long id : delDTOList.keySet()){
            mockMvc.perform(MockMvcRequestBuilders.delete("/room/" + roomOutDTO.getRoomId() + "/user/" + id)
                            .cookie(cookie))
                    .andExpect(status().isOk());
        }

        MvcResult result2 = mockMvc.perform(MockMvcRequestBuilders.get("/room/" + roomOutDTO.getRoomId() + "/users")
                        .cookie(cookie))
                .andExpect(status().isOk())
                .andReturn();

        String content2 = result2.getResponse().getContentAsString();
        System.out.println(content2);
        List<UserOutDTO> roomResult1 = objectMapper.readValue(content2, new TypeReference<List<UserOutDTO>>(){});
        assertThat(roomResult1.size()).isEqualTo(num + 1);
        addDTOList.put(user1.getId(), user1);
        for(int i = 0; i < addDTOList.size(); i ++){
            assertThat(roomResult1.get(i)).isEqualTo(addDTOList.get(roomResult1.get(i).getId()));
        }

    }
    @Test
    @DisplayName("ROOM 삭제 TEST")
    void roomDeleteTest() throws Exception {
        int num = 10;
        Map<Long, UserOutDTO> addDTOList = new HashMap<>();
        for (int i = 0; i < num; i++) {
            UserOutDTO user = signup("adduser" + i, "password" + i);
            addDTOList.put(user.getId(), user);
        }

        UserOutDTO user1 = signup("user", "password");
        Cookie cookie = login("user", "password");

        MvcResult result = makeRoom("room1", cookie);
        String content = result.getResponse().getContentAsString();
        RoomOutDTO roomOutDTO = objectMapper.readValue(content, RoomOutDTO.class);

        for(Long id : addDTOList.keySet()){
            mockMvc.perform(MockMvcRequestBuilders.put("/room/" + roomOutDTO.getRoomId() + "/user/" + id)
                            .cookie(cookie))
                    .andExpect(status().isOk());
        }
        em.clear();
        long start = System.currentTimeMillis();

        mockMvc.perform(MockMvcRequestBuilders.delete("/room/" + roomOutDTO.getRoomId())
                .cookie(cookie))
                .andExpect(status().isOk());
        ;
        long end = System.currentTimeMillis();

        mockMvc.perform(MockMvcRequestBuilders.get("/room/" + roomOutDTO.getRoomId())
                .cookie(cookie))
                .andExpect(status().isNotFound());
        System.out.println("실행시간 : " + (end - start) + "ms");
    }

    @Test
    @DisplayName("방의 주인만이 초대 삭제할 수 있음")
    void AuthTest() throws Exception {
        UserOutDTO user1 = signup("user1", "password");
        Cookie cookie1 = login("user1", "password");

        UserOutDTO user2 = signup("user2", "password");

        MvcResult result = makeRoom("room1", cookie1);
        String content = result.getResponse().getContentAsString();
        RoomOutDTO roomOutDTO = objectMapper.readValue(content, RoomOutDTO.class);

        em.clear();

        mockMvc.perform(MockMvcRequestBuilders.put("/room/" + roomOutDTO.getRoomId() + "/user/" + user2.getId()))
                .andExpect(status().isForbidden());

        mockMvc.perform(MockMvcRequestBuilders.put("/room/" + roomOutDTO.getRoomId() + "/user/" + user2.getId())
                        .cookie(cookie1))
                .andExpect(status().isOk());

        UserOutDTO user3 = signup("user3", "password");


        Cookie cookie2 = login("user2", "password");


        //방장 아닌 사람이 방에 추가하기
        mockMvc.perform(MockMvcRequestBuilders.put("/room/" + roomOutDTO.getRoomId() + "/user/" + user3.getId())
                        .cookie(cookie2))
                .andExpect(status().isForbidden());

        //방장 아닌 사람이 방에 없는 사람 방에서 삭제하기
        mockMvc.perform(MockMvcRequestBuilders.delete("/room/" + roomOutDTO.getRoomId() + "/user/" + user3.getId())
                        .cookie(cookie2))
                .andExpect(status().isForbidden());

        //방장 아닌 사람이 방장 추방
        mockMvc.perform(MockMvcRequestBuilders.delete("/room/" + roomOutDTO.getRoomId() + "/user/" + user1.getId())
                        .cookie(cookie2))
                .andExpect(status().isForbidden());

        //방장 아닌 사람이 원래 있는 사람 다시 초대
        mockMvc.perform(MockMvcRequestBuilders.put("/room/" + roomOutDTO.getRoomId() + "/user/" + user1.getId())
                        .cookie(cookie2))
                .andExpect(status().isForbidden());

        //방장 아닌 사람이 없는 사람 초대
        mockMvc.perform(MockMvcRequestBuilders.put("/room/" + roomOutDTO.getRoomId() + "/user/" + user1.getId() + 5000)
                        .cookie(cookie2))
                .andExpect(status().isForbidden());


    }


    private MvcResult makeRoom(String roomName, Cookie cookie) throws Exception {
        return mockMvc.perform(MockMvcRequestBuilders.post("/room/")
                        .cookie(cookie)
                .content(roomName))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.roomName").value(roomName))
                .andReturn();
    }


    private UserOutDTO signup(String username, String password) throws Exception {
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post("/user/")
                        .param("username", username)
                        .param("password", password))
                .andExpect(status().isCreated())
                .andReturn();

        String res = result.getResponse().getContentAsString();
        return objectMapper.readValue(res, UserOutDTO.class);

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

    private Cookie login(String username, String password) throws Exception {
        MvcResult result1 = mockMvc.perform(MockMvcRequestBuilders.post("/auth/token")
                        .param("username", username)
                        .param("password", password))
                .andExpect(status().isOk())
                .andReturn();

        return result1.getResponse().getCookie("JWT_TOKEN");
    }
}