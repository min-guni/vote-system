package meeting.decision.controller;

import jakarta.websocket.server.PathParam;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import meeting.decision.argumentresolver.Login;
import meeting.decision.domain.Vote;
import meeting.decision.domain.VotePaper;
import meeting.decision.domain.VoteResultType;
import meeting.decision.dto.vote.VoteInDTO;
import meeting.decision.dto.vote.VoteOutDTO;
import meeting.decision.dto.vote.VoteUpdateDTO;
import meeting.decision.service.RoomService;
import meeting.decision.service.VoteService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/vote")
@Slf4j
@RequiredArgsConstructor
public class VoteController {

    private final RoomService roomService;
    private final VoteService voteService;
    //투표생성(방장권한)

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/{roomId}")
    public VoteOutDTO createVote(@Login Long userId, @PathVariable Long roomId, @RequestBody VoteInDTO voteDTO){
        return voteService.create(userId, roomId, voteDTO);
    }

    @PatchMapping("/{voteId}")
    public String updateVote(@Login Long userId, @PathVariable Long voteId, @RequestBody VoteUpdateDTO voteUpdateDTO){
        voteService.update(userId, voteId, voteUpdateDTO);
        return "success";
    }

    //투표 넣기(방에 있는 사람 권한)
    @PutMapping("/{voteId}")
    public String addVotePaper(@Login Long userId, @PathVariable Long voteId, @RequestBody VoteResultType voteResultType){
        voteService.addVotePaper(userId, voteId, voteResultType);
        return "success";
    }

    //투표 삭제
    @DeleteMapping("/{voteId}")
    public String deleteVote(@Login Long userId, @PathVariable Long voteId){
        voteService.delete(userId, voteId);
        return "success";
    }

    @PostMapping("/{voteId}/reset")
    public String clearVote(@Login Long userId, @PathVariable Long voteId){
        voteService.resetVote(userId, voteId);
        return "success";
    }

    @GetMapping("/{voteId}")
    public VoteOutDTO findVoteResult(@Login Long userId, @PathVariable Long voteId){
        return voteService.getResult(userId, voteId);
    }

    @GetMapping("/")
    public List<VoteOutDTO> findAllVote(@Login Long userId, @RequestParam("roomId") Long roomId){
        return voteService.getVotes(userId, roomId);
    }
}
