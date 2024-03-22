//package meeting.decision.service;
//
//import meeting.decision.domain.Room;
//import meeting.decision.domain.User;
//import meeting.decision.dto.UserUpdateDTO;
//import meeting.decision.exception.UsernameAlreadyExistsException;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.transaction.annotation.Transactional;
//
//import static org.assertj.core.api.Assertions.assertThat;
//import static org.assertj.core.api.Assertions.assertThatThrownBy;
//
//@SpringBootTest
//@Transactional
//class UserServiceTest {
//
//    private final UserService userService;
//    private final RoomService roomService;
//
//    @Autowired
//    UserServiceTest(UserService userService, RoomService roomService) {
//        this.userService = userService;
//        this.roomService = roomService;
//    }
//
//    @Test
//    void create(){
//        User user = userService.create("hello", "1234");
//        UserUpdateDTO updateParam = new UserUpdateDTO();
//        updateParam.setUsername("minguni");
//        updateParam.setHashedPassword("123456");
//        userService.update(user.getId(), updateParam);
//        assertThat(user.getUsername()).isEqualTo("minguni");
//        assertThat(user.getHashedPassword()).isEqualTo("123456");
//    }
//
//    @Test
//    void saveDuplicateUsername(){
//        User user1 = userService.create("hello", "1234" );;
//        User user2 = userService.create("hello1", "1234" );;
//        assertThatThrownBy(()->userService.create("hello1", "1234")).isInstanceOf(UsernameAlreadyExistsException.class);
//    }
//
//    @Test
//    void userListTest(){
//        User user = userService.create("hello", "1234");
//        Room room = roomService.create("room1", user.getId());
//        Room room1 = roomService.create("room2", user.getId());
//
//        assertThat(userService.findAllRoomsById(user.getId()).contains(room)).isTrue();
//        assertThat(userService.findAllRoomsById(user.getId()).contains(room1)).isTrue();
//    }
//
//
//}