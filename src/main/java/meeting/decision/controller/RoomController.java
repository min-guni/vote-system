package meeting.decision.controller;

import lombok.RequiredArgsConstructor;
import meeting.decision.argumentresolver.Login;
import meeting.decision.domain.Room;
import meeting.decision.domain.User;
import meeting.decision.dto.RoomUpdateDTO;
import meeting.decision.service.RoomService;
import meeting.decision.service.UserService;
import org.springframework.web.bind.annotation.*;

import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/room")
@RequiredArgsConstructor
public class RoomController {

    private final RoomService roomService;
    private final UserService userService;
    //방생성
    @PostMapping("/")
    public Long createRoom(@Login Long userId, @RequestBody String roomName){
        return roomService.create(roomName, userId).getId();
    }

    //모든 방 찾기
    @GetMapping("/")
    public Set<Long> findRoom(@Login Long userId){
        User user = userService.findById(userId);
        return user.getRoomSet().stream()
                .map(Room::getId)
                .collect(Collectors.toSet());
    }


    //인원추가(방장권한)없으면 403
    @PutMapping("/{roomId}/{username}")
    public String addUser(@Login Long userId, @PathVariable String username, @PathVariable Long roomId ){
        roomService.addUserToRoom(roomId, userId, username);
        return "success";
    }

    //인원 삭제(방장권한)없으면 403
    @DeleteMapping("/{roomId}/{username}")
    public String deleteUser(@Login Long userId,@PathVariable Long roomId, @PathVariable String username){
        roomService.deleteUserFromRoom(roomId, userId, username);
        return "success";
    }

    @PostMapping("/{roomId}")
    public String update(@Login Long userId, @PathVariable Long roomId, @RequestBody RoomUpdateDTO roomUpdateDTO) {
        roomService.update(userId, roomId, roomUpdateDTO);
        return "success";
    }

    @DeleteMapping("/{roomId}")
    public String deleteRoom(@Login Long userId, @PathVariable Long roomId){
        roomService.delete(userId, roomId);
        return "delete success";
    }





    //



}
