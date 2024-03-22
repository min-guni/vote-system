//package meeting.decision.service;
//
//import meeting.decision.domain.Room;
//import meeting.decision.domain.User;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.transaction.annotation.Transactional;
//
//import static org.assertj.core.api.Assertions.assertThat;
//
//@SpringBootTest
//@Transactional
//class RoomServiceTest {
//
//    private final RoomService roomService;
//    private final UserService userService;
//
//    @Autowired
//    RoomServiceTest(RoomService roomService, UserService userService) {
//        this.roomService = roomService;
//        this.userService = userService;
//    }
//
//
//    @Test
//    void ownerCheck(){
//        //save update delete
//        User user = userService.create("hello", "1234");
//
//        User user1 = userService.create("hello1", "1234");
//
//        User user2 = userService.create("hello2", "1234");
//
//        Room room = roomService.create("hi", user.getId());
//
//        assertThat(room.getUserList().stream().anyMatch(someUser->someUser.getUsername().equals("hello"))).isTrue();
//        roomService.addUserToRoom(room.getId(), user1.getId());
//        roomService.addUserToRoom(room.getId(), user2.getId());
//        roomService.addUserToRoom(room.getId(), user2.getId());
//        assertThat(room.getUserList().contains(user)).isTrue();
//        assertThat(room.getUserList().contains(user1)).isTrue();
//        assertThat(room.getUserList().contains(user2)).isTrue();
//        roomService.deleteUserFromRoom(room.getId(), user1.getId());
//        assertThat(room.getUserList().contains(user1)).isFalse();
//        assertThat(roomService.isUserInRoom(room.getId(), user.getId())).isTrue();
//        assertThat(roomService.isUserInRoom(room.getId(), user1.getId())).isFalse();
//
//    }
//
//    @Test
//    void roomTest(){
//        User user = userService.create("hello", "1234");
//
//        userService.create("hello1", "1234");
//
//        userService.create("hello2", "1234");
//
//        Room room = roomService.create("hi", user.getId());
//
//        Room room1 = roomService.create("hi2", user.getId());
//
//        assertThat(roomService.findAll().contains(room)).isTrue();
//        assertThat(roomService.findAll().contains(room1)).isTrue();
//    }
//
//
//
//}