package meeting.decision.service;

import lombok.RequiredArgsConstructor;
import meeting.decision.domain.*;
import meeting.decision.dto.VoteUpdateDTO;
import meeting.decision.exception.UserNotInVoteRoomException;
import meeting.decision.repository.JpaRoomRepository;
import meeting.decision.repository.JpaUserRepository;
import meeting.decision.repository.JpaVoteRepository;
import org.springframework.security.access.AuthorizationServiceException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;


@Transactional
@RequiredArgsConstructor
@Service
public class VoteService {
    //create
    private final JpaVoteRepository voteRepository;
    private final JpaUserRepository userRepository;
    private final JpaRoomRepository roomRepository;
    public Vote create(Long ownerId, String voteName, Long roomId){
        User owner = userRepository.findById(ownerId).orElseThrow();
        Room room = roomRepository.findById(roomId).orElseThrow();
        //주인이 아니면
        if(!room.getOwner().getId().equals(owner.getId())){
            throw new AuthorizationServiceException("Only Owner Can Create Vote");
        }
        return voteRepository.save(new Vote(voteName, room));
    }
    public void update(Long ownerId, Long voteId, VoteUpdateDTO updateParam){
        Vote vote = voteRepository.findById(voteId).orElseThrow();
        if(!vote.getRoom().getOwner().getId().equals(ownerId)){
            throw new AuthorizationServiceException("Only Owner Can Update Vote");
        }
        vote.setVoteName(updateParam.getVoteName());
        vote.setActivated(updateParam.isActivated());
    }
    //delete
    public void delete(Long ownerId, Long voteId){
        Vote vote = voteRepository.findById(voteId).orElseThrow();
        if(!vote.getRoom().getOwner().getId().equals(ownerId)){
            throw new AuthorizationServiceException("Only Owner Can Delete Vote");
        }
        voteRepository.deleteById(voteId);
    }

    public Vote findById(Long voteId){
        return voteRepository.findById(voteId).orElseThrow();
    }


    //reset VotePaper
    public void resetVote(Long ownerId, Long voteId){
        Vote vote = voteRepository.findById(voteId).orElseThrow();
        if(!vote.getRoom().getOwner().getId().equals(ownerId)){
            throw new AuthorizationServiceException("Only Owner Can Reset Vote");
        }
        Set<VotePaper> papers = vote.getPapers();
        papers.clear();
    }

    //투표 결과 보여주기
    public Set<VotePaper> getResult(Long ownerId, Long voteId){
        Vote vote = voteRepository.findById(voteId).orElseThrow();
        if(!vote.getRoom().getOwner().getId().equals(ownerId)){
            throw new AuthorizationServiceException("Only Owner Can Request VoteResult");
        }
        return voteRepository.findById(voteId).orElseThrow().getPapers();
    }

    public boolean addVotePaper(Long voteId, Long userId, VoteResultType voteResultType){
        //이미 투표에 투표있는지 확인 있는지 확인 있으면 false 반환
        User user = userRepository.findById(userId).orElseThrow();
        Vote vote = voteRepository.findById(voteId).orElseThrow();
        if(vote.getPapers().stream().anyMatch(paper->paper.getUser().getId().equals(user.getId()))){
            return false;
        }

        //해당 룸 안에 사용자가 있어야 투표를 할 수 있음.
        if(roomRepository.existsUserInRoom(vote.getRoom().getId(), user.getId()) == 0){
            throw new UserNotInVoteRoomException(user.getUsername());
        }

        VotePaper votePaper = new VotePaper(user, voteResultType);
        return vote.getPapers().add(votePaper);
    }

    //투표들 다 보여주기
    public Set<Vote> getVotes(Long userId, Long roomId){
        if(roomRepository.existsUserInRoom(roomId, userId) == 0){
            throw new UserNotInVoteRoomException(String.valueOf(userId));
        }
        return voteRepository.findByRoomId(roomId);
    }
}
