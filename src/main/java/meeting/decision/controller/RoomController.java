package meeting.decision.controller;

import lombok.RequiredArgsConstructor;
import meeting.decision.argumentresolver.Login;
import meeting.decision.domain.User;
import meeting.decision.dto.room.RoomOutDTO;
import meeting.decision.dto.room.RoomUpdateDTO;
import meeting.decision.dto.user.UserOutDTO;
import meeting.decision.service.RoomService;
import meeting.decision.service.UserService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
    public List<RoomOutDTO> findRoom(@Login Long userId){
        return roomService.findAllRoomByUserId(userId);
//
//        User user = userService.findById(userId);
//        return user.getRoomList().stream()
//                .map((participant)-> new RoomOutDTO(participant.getRoom().getId(),
//                        participant.getRoom().getRoomName(),
//                        participant.getRoom().getOwner().getId(),
//                        participant.getRoom().getUserList().size()))
//                .collect(Collectors.toList());
    }

    @GetMapping("/{roomId}")
    public List<UserOutDTO> findUser(@Login Long userId, @PathVariable Long roomId){
        return roomService.findAllUserByRoomId(userId, roomId);
    }


    //인원추가(방장권한)없으면 403
    @PutMapping("/{roomId}/{invitedId}")
    public String addUser(@Login Long userId, @PathVariable Long invitedId, @PathVariable Long roomId ){
        roomService.addUserToRoom(userId, roomId, invitedId);
        return "success";
    }

    //인원 삭제(방장권한)없으면 403
    @DeleteMapping("/{roomId}/{deleteId}")
    public String deleteUser(@Login Long userId,@PathVariable Long roomId, @PathVariable Long deleteId){
        roomService.deleteUserFromRoom(userId, roomId, deleteId);
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
}
