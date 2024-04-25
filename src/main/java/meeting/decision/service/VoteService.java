package meeting.decision.service;

import com.sun.jdi.request.DuplicateRequestException;
import lombok.RequiredArgsConstructor;
import meeting.decision.annotation.CheckUser;
import meeting.decision.domain.*;
import meeting.decision.dto.vote.VoteInDTO;
import meeting.decision.dto.vote.VoteOutDTO;
import meeting.decision.dto.vote.VoteResultDTO;
import meeting.decision.dto.vote.VoteUpdateDTO;
import meeting.decision.exception.UserNotInVoteRoomException;
import meeting.decision.exception.VoteIsNotActivatedException;
import meeting.decision.repository.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@Transactional
@RequiredArgsConstructor
@Service
public class VoteService {
    //create
    private final JpaVoteRepository voteRepository;
    private final JpaUserRepository userRepository;
    private final JpaRoomRepository roomRepository;
    private final JpaRoomParticipantRepository roomParticipantRepository;
    private final JpaVotePaperRepository votePaperRepository;

    @CheckUser(isOwner = true)
    public VoteOutDTO create(Long ownerId, Long roomId, VoteInDTO voteInDTO){  // vote dto로 변경하기
        Room room = roomRepository.findById(roomId).orElseThrow();
        Vote savedVote = voteRepository.save(new Vote(voteInDTO.getVoteName(), voteInDTO.isAnonymous(), room));

        return new VoteOutDTO(savedVote.getId(),
                roomId, savedVote.getVoteName(),
                savedVote.isActivated(), savedVote.isAnonymous(),
                new VoteResultDTO(0L,0L));

    }

    @CheckUser(isOwner = true, isVote = true)
    public void update(Long ownerId, Long voteId, VoteUpdateDTO updateParam){
        Vote vote = voteRepository.findById(voteId).orElseThrow();
        vote.setVoteName(updateParam.getVoteName());
        vote.setActivated(updateParam.isActivated());
    }
    //delete

    @CheckUser(isOwner = true, isVote = true)
    public void delete(Long ownerId, Long voteId){
        voteRepository.deleteById(voteId);
    }

    //reset VotePaper
    @CheckUser(isOwner = true, isVote = true)
    public void resetVote(Long ownerId, Long voteId){
        Vote vote = voteRepository.findById(voteId).orElseThrow();
        vote.getPapers().clear();
    }

    //투표 결과 보여주기 inactivate일때만 보여주기 vote타입에 따라 다름
    @CheckUser(isOwner = true, isVote = true)
    public List<VotePaper> getResult(Long ownerId, Long voteId){   //dto로 변경해서 내보내기

        return voteRepository.findById(voteId).orElseThrow().getPapers();
    }


    //void 로 바꾸고 Exception 발생
    @CheckUser(isVote = true)
    public void addVotePaper(Long userId, Long voteId, VoteResultType voteResultType){
        //이미 투표에 투표있는지 확인 있는지 확인 있으면 false 반환
        User user = userRepository.findById(userId).orElseThrow();
        Vote vote = voteRepository.findById(voteId).orElseThrow();

        if(!vote.isActivated()){
            throw new VoteIsNotActivatedException(voteId + "vote is not activated");
        }

        if(votePaperRepository.existsByVoteIdAndUserId(voteId, userId)){
            throw new DuplicateRequestException();
        }

        VotePaper votePaper = new VotePaper(user ,vote, voteResultType);
        votePaperRepository.save(votePaper);
    }

    //투표들 다 보여주기 vote dto를 내보내는데 찬성수 반대수만
    @CheckUser
    public List<Vote> getVotes(Long userId, Long roomId){
        if(roomParticipantRepository.existsByRoomIdAndUserId(roomId, userId)){
            throw new UserNotInVoteRoomException(String.valueOf(userId));
        }
        return voteRepository.findByRoomId(roomId);
    }
}
