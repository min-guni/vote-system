//package meeting.decision.service;
//
//import meeting.decision.domain.*;
//import meeting.decision.exception.UserNotInVoteRoomException;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.transaction.annotation.Transactional;
//
//import java.util.Set;
//
//import static org.assertj.core.api.Assertions.assertThat;
//import static org.assertj.core.api.Assertions.assertThatThrownBy;
//
//@SpringBootTest
//@Transactional
//class VoteServiceTest {
//
//    private final VoteService voteService;
//    private final RoomService roomService;
//    private final UserService userService;
//
//    @Autowired
//    VoteServiceTest(VoteService voteService, RoomService roomService, UserService userService) {
//        this.voteService = voteService;
//        this.roomService = roomService;
//        this.userService = userService;
//    }
//    //리셋하면 못찾는거확인
//    @Test
//    void testRest(){
//        User user = userService.create("minguni", "1234");
//        User user1 = userService.create("minguni1", "1234");
//        User user2 = userService.create("minguni2", "1234");
//        User user3 = userService.create("minguni3", "1234");
//
//        Room room = roomService.create("roomname",user.getId());
//        roomService.addUserToRoom(room.getId(), user1.getId());
//        roomService.addUserToRoom(room.getId(), user2.getId());
//        Vote vote = voteService.create("tupyo", room.getId());
//        voteService.addVotePaper(vote.getId(), user.getId(), VoteResultType.YES);
//        assertThat(voteService.addVotePaper(vote.getId(), user.getId(), VoteResultType.YES)).isFalse();
//        assertThatThrownBy(()->voteService.addVotePaper(vote.getId(), user3.getId(), VoteResultType.YES)).isInstanceOf(UserNotInVoteRoomException.class);
//        assertThat(voteService.getResult(vote.getId()).stream().anyMatch(votePaper -> votePaper.getUser().getId() == user.getId())).isTrue();
//
//    }
//
//
//    //투표 결과 확인
//
//
//}