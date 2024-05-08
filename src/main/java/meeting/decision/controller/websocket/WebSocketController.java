package meeting.decision.controller.websocket;

import lombok.RequiredArgsConstructor;
import meeting.decision.argumentresolver.Login;
import meeting.decision.domain.VoteResultType;
import meeting.decision.dto.user.UserOutDTO;
import meeting.decision.dto.vote.VoteInDTO;
import meeting.decision.dto.vote.VoteOutDTO;
import meeting.decision.service.RoomService;
import meeting.decision.service.VoteService;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;


@Controller
@RequiredArgsConstructor
public class WebSocketController {
    private final VoteService voteService;
    private final RoomService roomService;

    @MessageMapping("/room/{roomId}/add")
    @SendTo("/topic/room/{roomId}")
    public List<UserOutDTO> addUser(@Login Long userId, @DestinationVariable Long roomId, @RequestBody Long addUserId){
        roomService.addUserToRoom(userId, roomId, addUserId);
        return roomService.findAllUserByRoomId(userId, roomId);
    }

    @MessageMapping("/room/{roomId}/delete")
    @SendTo("/topic/room/{roomId}")
    public List<UserOutDTO> deleteUser(@Login Long userId, @DestinationVariable Long roomId, @RequestBody Long addUserId){
        roomService.deleteUserFromRoom(userId, roomId, addUserId);
        return roomService.findAllUserByRoomId(userId, roomId);
    }


    //투표생성(방장권한)
    @MessageMapping("/room/{roomId}")
    @SendTo("/topic/room/{roomId}")
    public VoteOutDTO createVote(@Login Long userId, @DestinationVariable Long roomId, @RequestBody VoteInDTO voteDTO){
        return voteService.create(userId, roomId, voteDTO);
    }

    @MessageMapping("/vote/{voteId}")
    @SendTo("/topic/vote/{voteId}")
    public VoteOutDTO addVotePaper(@Login Long userId, @DestinationVariable Long voteId, @RequestBody VoteResultType voteResultType){
        voteService.addVotePaper(userId, voteId, voteResultType);
        return voteService.getResult(userId, voteId);
    }


}
