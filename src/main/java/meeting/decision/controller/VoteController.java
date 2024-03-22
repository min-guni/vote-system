package meeting.decision.controller;

import lombok.RequiredArgsConstructor;
import meeting.decision.argumentresolver.Login;
import meeting.decision.domain.User;
import meeting.decision.domain.Vote;
import meeting.decision.domain.VotePaper;
import meeting.decision.domain.VoteResultType;
import meeting.decision.service.RoomService;
import meeting.decision.service.VoteService;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

@RestController
@RequestMapping("/vote")
@RequiredArgsConstructor
public class VoteController {

    private final RoomService roomService;
    private final VoteService voteService;
    //투표생성(방장권한)
    @PostMapping("/")
    public Vote createVote(@Login Long userId, @RequestBody Long roomId,@RequestBody String voteName){
        return voteService.create(userId, voteName, roomId);
    }

    //투표 넣기(방에 있는 사람 권한)
    @PutMapping("/")
    public String addVotePaper(@Login Long userId, @RequestBody Long voteId, @RequestBody VoteResultType voteResultType){
        voteService.addVotePaper(voteId, userId, voteResultType);
        return "success";
    }

    //투표 리셋
    @DeleteMapping("/{voteId}")
    public String resetVote(@Login Long userId, @PathVariable Long voteId){
        voteService.resetVote(userId, voteId);
        return "success";
    }
    @GetMapping("/{voteId}")
    public Set<VotePaper> findVoteResult(@Login Long userId, @PathVariable Long voteId){
        return voteService.getResult(userId, voteId);
    }

    @GetMapping("/")
    public Set<Vote> findAllVote(@Login Long userId, @RequestParam("roomId") Long roomId){
        return voteService.getVotes(userId, roomId);
    }

}
