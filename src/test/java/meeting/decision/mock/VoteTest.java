package meeting.decision.mock;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityManager;
import jakarta.servlet.http.Cookie;
import meeting.decision.dto.room.RoomOutDTO;
import meeting.decision.dto.user.UserOutDTO;
import meeting.decision.dto.vote.VoteInDTO;
import meeting.decision.dto.vote.VoteOutDTO;
import meeting.decision.dto.vote.VoteResultDTO;
import meeting.decision.dto.vote.VoteUpdateDTO;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class VoteTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    EntityManager em;

    @Test
    @DisplayName("Vote 생성 테스트")
    void createVoteTest() throws Exception {
        UserOutDTO user1 = signup("userid1", "password");
        Cookie cookie = login("userid1", "password");


        RoomOutDTO roomOutDTO = makeRoom("room", cookie);
        VoteInDTO voteInput = new VoteInDTO("vote1", false);
        VoteOutDTO voteResult = createVote(voteInput, roomOutDTO.getRoomId(), cookie);
        assertThat(voteInput.getVoteName()).isEqualTo(voteResult.getVoteName());
        assertThat(voteInput.isAnonymous()).isEqualTo(voteResult.isAnonymous());
    }

    @Test
    @DisplayName("Vote 생성 테스트 validation : 투표 이름이 공백이면 안됨")
    void createVoteValidationTest() throws Exception {
        UserOutDTO user1 = signup("userid1", "password");
        Cookie cookie = login("userid1", "password");


        RoomOutDTO roomOutDTO = makeRoom("room", cookie);
        VoteInDTO voteInput = new VoteInDTO("    ", false);

        mockMvc.perform(MockMvcRequestBuilders.post("/vote/" + roomOutDTO.getRoomId())
                        .cookie(cookie)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(voteInput)))
                .andExpect(status().isBadRequest())
                .andReturn();


        VoteInDTO voteInput1 = new VoteInDTO("", false);

        mockMvc.perform(MockMvcRequestBuilders.post("/vote/" + roomOutDTO.getRoomId())
                        .cookie(cookie)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(voteInput1)))
                .andExpect(status().isBadRequest())
                .andReturn();

        mockMvc.perform(MockMvcRequestBuilders.post("/vote/" + roomOutDTO.getRoomId())
                        .cookie(cookie))
                .andExpect(status().isBadRequest())
                .andReturn();
    }

    @Test
    @DisplayName("Vote 생성 테스트 auth : 투표는 방장만이 생성할 수 있음")
    void createVoteAuthTest() throws Exception {
        UserOutDTO user1 = signup("userid1", "password");
        Cookie cookie1 = login("userid1", "password");


        UserOutDTO user2 = signup("userid2", "password");
        Cookie cookie2 = login("userid2", "password");

        RoomOutDTO roomOutDTO = makeRoom("room", cookie1);
        VoteInDTO voteInput = new VoteInDTO("vote", false);

        mockMvc.perform(MockMvcRequestBuilders.post("/vote/" + roomOutDTO.getRoomId())
                        .cookie(cookie2)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(voteInput)))
                .andExpect(status().isForbidden())
                .andReturn();
    }

    @Test
    @DisplayName("Vote 결과 테스트")
    void voteResultTest() throws Exception {
        UserOutDTO user1 = signup("userid1", "password");
        Cookie cookie = login("userid1", "password");


        RoomOutDTO roomOutDTO = makeRoom("room", cookie);
        VoteInDTO voteInput = new VoteInDTO("vote1", false);
        VoteOutDTO voteOutDTO = createVote(voteInput, roomOutDTO.getRoomId(), cookie);


        int num = 10;
        Map<Long, UserOutDTO> addDTOList = new HashMap<>();
        for (int i = 0; i < num; i++) {
            UserOutDTO user = signup("adduser" + i, "password" + i);
            addDTOList.put(user.getId(), user);
        }

        for(Long id : addDTOList.keySet()){
            mockMvc.perform(MockMvcRequestBuilders.put("/room/" + roomOutDTO.getRoomId() + "/user/" + id)
                            .cookie(cookie))
                    .andExpect(status().isOk());
        }


        int[] saveVoteList = new int[3];
        String[] voteVal = {"YES", "NO", "ABSTAIN"};
        Random random = new Random();

        for(int i = 0; i < num; i++){
            int randomNum = random.nextInt(3);
            Cookie myCookie = login("adduser" + i, "password" + i);
            mockMvc.perform(MockMvcRequestBuilders.put("/votePaper/" + voteOutDTO.getId())
                            .cookie(myCookie)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(("\"" + voteVal[randomNum] + "\"")))
                    .andExpect(status().isOk());
            saveVoteList[randomNum] ++;
        }

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/votePaper/" + voteOutDTO.getId())
                        .cookie(cookie))
                .andExpect(status().isOk())
                .andReturn();

        String content = result.getResponse().getContentAsString();
        VoteOutDTO voteResult = objectMapper.readValue(content, VoteOutDTO.class);

        assertThat(voteResult.getYesNum()).isEqualTo(saveVoteList[0]);
        assertThat(voteResult.getNoNum()).isEqualTo(saveVoteList[1]);
        assertThat(voteResult.getAbstentionNum()).isEqualTo(saveVoteList[2]);

        List<Long> voteResultIds = voteResult.getVoteResult().get().stream()
                .map(VoteResultDTO::getUserId).toList();


        System.out.println(voteResult);

        assertThat(addDTOList.keySet()).allMatch(voteResultIds::contains);
    }

    @Test
    @DisplayName("익명 테스트")
    void anonymousTest() throws Exception {
        UserOutDTO user1 = signup("userid1", "password");
        Cookie cookie = login("userid1", "password");


        RoomOutDTO roomOutDTO = makeRoom("room", cookie);
        VoteInDTO voteInput = new VoteInDTO("vote1", true);
        VoteOutDTO voteOutDTO = createVote(voteInput, roomOutDTO.getRoomId(), cookie);


        int num = 10;
        Map<Long, UserOutDTO> addDTOList = new HashMap<>();
        for (int i = 0; i < num; i++) {
            UserOutDTO user = signup("adduser" + i, "password" + i);
            addDTOList.put(user.getId(), user);
        }

        for(Long id : addDTOList.keySet()){
            mockMvc.perform(MockMvcRequestBuilders.put("/room/" + roomOutDTO.getRoomId() + "/user/" + id)
                            .cookie(cookie))
                    .andExpect(status().isOk());
        }


        int[] saveVoteList = new int[3];
        String[] voteVal = {"YES", "NO", "ABSTAIN"};
        Random random = new Random();

        for(int i = 0; i < num; i++){
            int randomNum = random.nextInt(3);
            Cookie myCookie = login("adduser" + i, "password" + i);
            mockMvc.perform(MockMvcRequestBuilders.put("/votePaper/" + voteOutDTO.getId())
                            .cookie(myCookie)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(("\"" + voteVal[randomNum] + "\"")))
                    .andExpect(status().isOk());
            saveVoteList[randomNum] ++;
        }

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/votePaper/" + voteOutDTO.getId())
                        .cookie(cookie))
                .andExpect(status().isOk())
                .andReturn();

        String content = result.getResponse().getContentAsString();
        VoteOutDTO voteResult = objectMapper.readValue(content, VoteOutDTO.class);

        assertThat(voteResult.getYesNum()).isEqualTo(saveVoteList[0]);
        assertThat(voteResult.getNoNum()).isEqualTo(saveVoteList[1]);
        assertThat(voteResult.getAbstentionNum()).isEqualTo(saveVoteList[2]);

        assertThat(voteResult.getVoteResult()).isEmpty();
        System.out.println(voteResult);

    }

    @Test
    @DisplayName("Vote update and inactivate Test")
    void voteInActivateTest() throws Exception {
        UserOutDTO user1 = signup("userid1", "password");
        Cookie cookie = login("userid1", "password");


        RoomOutDTO roomOutDTO = makeRoom("room", cookie);
        VoteInDTO voteInput = new VoteInDTO("vote1", true);
        VoteOutDTO voteOutDTO = createVote(voteInput, roomOutDTO.getRoomId(), cookie);


        int num = 10;
        Map<Long, UserOutDTO> addDTOList = new HashMap<>();
        for (int i = 0; i < num; i++) {
            UserOutDTO user = signup("adduser" + i, "password" + i);
            addDTOList.put(user.getId(), user);
        }



        for(Long id : addDTOList.keySet()){
            mockMvc.perform(MockMvcRequestBuilders.put("/room/" + roomOutDTO.getRoomId() + "/user/" + id)
                            .cookie(cookie))
                    .andExpect(status().isOk());
        }


        VoteUpdateDTO voteInput2 = new VoteUpdateDTO("updatedVote", false);
        mockMvc.perform(MockMvcRequestBuilders.patch("/vote/" + voteOutDTO.getId())
                .cookie(cookie)
                        .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(voteInput2)))
                .andExpect(status().isOk())
                .andReturn();

        //업데이트는 방장만 할 수 있음
        Cookie cookie1 = login("adduser0", "password0");
        mockMvc.perform(MockMvcRequestBuilders.patch("/vote/" + voteOutDTO.getId())
                        .cookie(cookie1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(voteInput2)))
                .andExpect(status().isForbidden())
                .andReturn();


        String[] voteVal = {"YES", "NO", "ABSTAIN"};
        Random random = new Random();

        for(int i = 0; i < num; i++){
            int randomNum = random.nextInt(3);
            Cookie myCookie = login("adduser" + i, "password" + i);
            mockMvc.perform(MockMvcRequestBuilders.put("/votePaper/" + voteOutDTO.getId())
                            .cookie(myCookie)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(("\"" + voteVal[randomNum] + "\"")))
                    .andExpect(status().isForbidden());
        }

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/votePaper/" + voteOutDTO.getId())
                        .cookie(cookie))
                .andExpect(status().isOk())
                .andReturn();

        String content = result.getResponse().getContentAsString();
        VoteOutDTO voteResult = objectMapper.readValue(content, VoteOutDTO.class);

        assertThat(voteResult.getYesNum()).isEqualTo(0);
        assertThat(voteResult.getNoNum()).isEqualTo(0);
        assertThat(voteResult.getAbstentionNum()).isEqualTo(0);

        assertThat(voteResult.getVoteName()).isEqualTo(voteInput2.getVoteName());
        assertThat(voteResult.isActivated()).isFalse();

        assertThat(voteResult.getVoteResult()).isEmpty();
        System.out.println(voteResult);
    }

    @Test
    @DisplayName("Vote Authorize test1 : 방안에 있는 사람만 투표 가능")
    void voteAuthTest1() throws Exception {
        UserOutDTO user1 = signup("userid1", "password");
        Cookie cookie = login("userid1", "password");


        RoomOutDTO roomOutDTO = makeRoom("room", cookie);
        VoteInDTO voteInput = new VoteInDTO("vote1", true);
        VoteOutDTO voteOutDTO = createVote(voteInput, roomOutDTO.getRoomId(), cookie);


        int num = 10;
        Map<Long, UserOutDTO> addDTOList = new HashMap<>();
        for (int i = 0; i < num; i++) {
            UserOutDTO user = signup("adduser" + i, "password" + i);
            addDTOList.put(user.getId(), user);
        }

        for(Long id : addDTOList.keySet()){
            mockMvc.perform(MockMvcRequestBuilders.put("/room/" + roomOutDTO.getRoomId() + "/user/" + id)
                            .cookie(cookie))
                    .andExpect(status().isOk());
        }
        Cookie cookie1 = signupAndLogin("notRoomUser", "password");

        mockMvc.perform(MockMvcRequestBuilders.put("/votePaper/" + voteOutDTO.getId())
                        .cookie(cookie1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(("\"YES\"")))
                .andExpect(status().isForbidden());

    }

    @Test
    @DisplayName("Vote List Test")
    void voteListTest() throws Exception {
        UserOutDTO user1 = signup("userid1", "password");
        Cookie cookie = login("userid1", "password");


        RoomOutDTO roomOutDTO = makeRoom("room", cookie);

        Map<Long, VoteOutDTO> voteMap = new HashMap<>();
        for(int i = 0; i < 10; i ++){
            VoteInDTO voteInput = new VoteInDTO("vote1", false);
            VoteOutDTO voteOutDTO = createVote(voteInput, roomOutDTO.getRoomId(), cookie);
            voteMap.put(voteOutDTO.getId(), voteOutDTO);
        }

        int num = 10;
        Map<Long, UserOutDTO> addDTOList = new HashMap<>();
        for (int i = 0; i < num; i++) {
            UserOutDTO user = signup("adduser" + i, "password" + i);
            addDTOList.put(user.getId(), user);
        }

        for(Long id : addDTOList.keySet()){
            mockMvc.perform(MockMvcRequestBuilders.put("/room/" + roomOutDTO.getRoomId() + "/user/" + id)
                            .cookie(cookie))
                    .andExpect(status().isOk());
        }


        Map<Long, int[]> voteResultMap = new HashMap<>();
        String[] voteVal = {"YES", "NO", "ABSTAIN"};
        Random random = new Random();


        for(Long voteId : voteMap.keySet()){
            int[] voteResultArr = new int[3];
            for (int i = 0; i < num; i++) {
                int randomNum = random.nextInt(3);
                Cookie myCookie = login("adduser" + i, "password" + i);
                mockMvc.perform(MockMvcRequestBuilders.put("/votePaper/" + voteId)
                                .cookie(myCookie)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(("\"" + voteVal[randomNum] + "\"")))
                        .andExpect(status().isOk());
                voteResultArr[randomNum] ++;
            }
            voteResultMap.put(voteId, voteResultArr);
        }

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/vote/" + roomOutDTO.getRoomId())
                        .cookie(cookie))
                .andExpect(status().isOk())
                .andReturn();

        String content = result.getResponse().getContentAsString();
        List<VoteOutDTO> voteOutDTOList = objectMapper.readValue(content, new TypeReference<List<VoteOutDTO>>() {});

        assertThat(voteOutDTOList.size()).isEqualTo(10);

        for(VoteOutDTO dto : voteOutDTOList){
            int[] result1 = voteResultMap.get(dto.getId());

            assertThat(result1[0]).isEqualTo(dto.getYesNum().intValue());
            assertThat(result1[1]).isEqualTo(dto.getNoNum().intValue());
            assertThat(result1[2]).isEqualTo(dto.getAbstentionNum().intValue());

        }

    }

    @Test
    @DisplayName("두번 투표를 해도 한번만 처리가 됨")
    void duplicateVoteTest() throws Exception {

        UserOutDTO user1 = signup("userid1", "password");
        Cookie cookie = login("userid1", "password");


        RoomOutDTO roomOutDTO = makeRoom("room", cookie);
        VoteInDTO voteInput = new VoteInDTO("vote1", false);
        VoteOutDTO voteOutDTO = createVote(voteInput, roomOutDTO.getRoomId(), cookie);


        int num = 10;
        Map<Long, UserOutDTO> addDTOList = new HashMap<>();
        for (int i = 0; i < num; i++) {
            UserOutDTO user = signup("adduser" + i, "password" + i);
            addDTOList.put(user.getId(), user);
        }

        for(Long id : addDTOList.keySet()){
            mockMvc.perform(MockMvcRequestBuilders.put("/room/" + roomOutDTO.getRoomId() + "/user/" + id)
                            .cookie(cookie))
                    .andExpect(status().isOk());
        }


        int[] saveVoteList = new int[3];
        String[] voteVal = {"YES", "NO", "ABSTAIN"};
        Random random = new Random();

        for(int i = 0; i < num; i++){
            int randomNum = random.nextInt(3);
            Cookie myCookie = login("adduser" + i, "password" + i);
            mockMvc.perform(MockMvcRequestBuilders.put("/votePaper/" + voteOutDTO.getId())
                            .cookie(myCookie)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(("\"" + voteVal[randomNum] + "\"")))
                    .andExpect(status().isOk());
            saveVoteList[randomNum] ++;
        }

        for(int i = 0; i < num; i++){
            int randomNum = random.nextInt(3);
            Cookie myCookie = login("adduser" + i, "password" + i);
            mockMvc.perform(MockMvcRequestBuilders.put("/votePaper/" + voteOutDTO.getId())
                            .cookie(myCookie)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(("\"" + voteVal[randomNum] + "\"")))
                    .andExpect(status().isOk());
        }

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/votePaper/" + voteOutDTO.getId())
                        .cookie(cookie))
                .andExpect(status().isOk())
                .andReturn();

        String content = result.getResponse().getContentAsString();
        VoteOutDTO voteResult = objectMapper.readValue(content, VoteOutDTO.class);

        assertThat(voteResult.getYesNum()).isEqualTo(saveVoteList[0]);
        assertThat(voteResult.getNoNum()).isEqualTo(saveVoteList[1]);
        assertThat(voteResult.getAbstentionNum()).isEqualTo(saveVoteList[2]);

        List<Long> voteResultIds = voteResult.getVoteResult().get().stream()
                .map(VoteResultDTO::getUserId).toList();


        System.out.println(voteResult);

        assertThat(addDTOList.keySet()).allMatch(voteResultIds::contains);
        assertThat(voteResultIds.size()).isEqualTo(10);
    }


    @Test
    @DisplayName("투표 삭제 테스트")
    void deleteVoteTest() throws Exception {
        UserOutDTO user1 = signup("userid1", "password");
        Cookie cookie = login("userid1", "password");


        RoomOutDTO roomOutDTO = makeRoom("room", cookie);
        VoteInDTO voteInput = new VoteInDTO("vote1", false);
        VoteOutDTO voteOutDTO = createVote(voteInput, roomOutDTO.getRoomId(), cookie);


        int num = 10;
        Map<Long, UserOutDTO> addDTOList = new HashMap<>();
        for (int i = 0; i < num; i++) {
            UserOutDTO user = signup("adduser" + i, "password" + i);
            addDTOList.put(user.getId(), user);
        }

        for(Long id : addDTOList.keySet()){
            mockMvc.perform(MockMvcRequestBuilders.put("/room/" + roomOutDTO.getRoomId() + "/user/" + id)
                            .cookie(cookie))
                    .andExpect(status().isOk());
        }


        int[] saveVoteList = new int[3];
        String[] voteVal = {"YES", "NO", "ABSTAIN"};
        Random random = new Random();

        for(int i = 0; i < num; i++){
            int randomNum = random.nextInt(3);
            Cookie myCookie = login("adduser" + i, "password" + i);
            mockMvc.perform(MockMvcRequestBuilders.put("/votePaper/" + voteOutDTO.getId())
                            .cookie(myCookie)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(("\"" + voteVal[randomNum] + "\"")))
                    .andExpect(status().isOk());
            saveVoteList[randomNum] ++;
        }

        mockMvc.perform(MockMvcRequestBuilders.delete("/vote/" + voteOutDTO.getId())
                .cookie(cookie))
                .andExpect(status().isOk());

        mockMvc.perform(MockMvcRequestBuilders.get("/votePaper/" + voteOutDTO.getId())
                        .cookie(cookie))
                .andExpect(status().isNotFound())
                .andReturn();
    }

    @Test
    @DisplayName("투표 리셋 테스트")
    void voteResetTest() throws Exception {
        UserOutDTO user1 = signup("userid1", "password");
        Cookie cookie = login("userid1", "password");


        RoomOutDTO roomOutDTO = makeRoom("room", cookie);
        VoteInDTO voteInput = new VoteInDTO("vote1", false);
        VoteOutDTO voteOutDTO = createVote(voteInput, roomOutDTO.getRoomId(), cookie);


        int num = 10;
        Map<Long, UserOutDTO> addDTOList = new HashMap<>();
        for (int i = 0; i < num; i++) {
            UserOutDTO user = signup("adduser" + i, "password" + i);
            addDTOList.put(user.getId(), user);
        }

        for(Long id : addDTOList.keySet()){
            mockMvc.perform(MockMvcRequestBuilders.put("/room/" + roomOutDTO.getRoomId() + "/user/" + id)
                            .cookie(cookie))
                    .andExpect(status().isOk());
        }


        int[] saveVoteList = new int[3];
        String[] voteVal = {"YES", "NO", "ABSTAIN"};
        Random random = new Random();

        for(int i = 0; i < num; i++){
            int randomNum = random.nextInt(3);
            Cookie myCookie = login("adduser" + i, "password" + i);
            mockMvc.perform(MockMvcRequestBuilders.put("/votePaper/" + voteOutDTO.getId())
                            .cookie(myCookie)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(("\"" + voteVal[randomNum] + "\"")))
                    .andExpect(status().isOk());
            saveVoteList[randomNum] ++;
        }

        em.clear();
        long start = System.currentTimeMillis();

        mockMvc.perform(MockMvcRequestBuilders.delete("/votePaper/" + voteOutDTO.getId())
                        .cookie(cookie))
                .andExpect(status().isOk());

        long end = System.currentTimeMillis();


        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/votePaper/" + voteOutDTO.getId())
                        .cookie(cookie))
                .andExpect(status().isOk())
                .andReturn();
        String content = result.getResponse().getContentAsString();
        VoteOutDTO voteOutDTO1 = objectMapper.readValue(content, VoteOutDTO.class);
        assertThat(voteOutDTO1.getVoteResult().get().size()).isEqualTo(0);
        assertThat(voteOutDTO1.getNoNum()).isEqualTo(0);
        assertThat(voteOutDTO1.getYesNum()).isEqualTo(0);
        assertThat(voteOutDTO1.getAbstentionNum()).isEqualTo(0);
        System.out.println(start - end);
    }

    private RoomOutDTO makeRoom(String roomName, Cookie cookie) throws Exception {
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post("/room/")
                        .cookie(cookie)
                        .content(roomName))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.roomName").value(roomName))
                .andReturn();
        String content = result.getResponse().getContentAsString();
        return objectMapper.readValue(content, RoomOutDTO.class);
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

    private VoteOutDTO createVote(VoteInDTO voteInDTO, Long roomId, Cookie cookie) throws Exception {
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post("/vote/" + roomId)
                        .cookie(cookie)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(voteInDTO)))
                .andExpect(status().isCreated())
                .andReturn();

        String content = result.getResponse().getContentAsString();
        return objectMapper.readValue(content, VoteOutDTO.class);

    }
}
