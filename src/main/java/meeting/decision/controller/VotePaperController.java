package meeting.decision.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import meeting.decision.argumentresolver.Login;
import meeting.decision.domain.VoteResultType;
import meeting.decision.dto.vote.VoteOutDTO;
import meeting.decision.service.VoteService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/votePaper")
@Slf4j
@RequiredArgsConstructor
public class VotePaperController {
    private final VoteService voteService;

    @PostMapping("/{voteId}")
    public String addVotePaper(@Login Long userId, @PathVariable Long voteId, @RequestBody VoteResultType voteResultType){
        voteService.addVotePaper(userId, voteId, voteResultType);
        return "success";
    }

    @DeleteMapping("/{voteId}")
    public String clearVote(@Login Long userId, @PathVariable Long voteId){
        voteService.resetVote(userId, voteId);
        return "success";
    }

    @GetMapping("/{voteId}")
    public VoteOutDTO findVoteResult(@Login Long userId, @PathVariable Long voteId){
        return voteService.getResult(userId, voteId);
    }
}
