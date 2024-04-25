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

import java.util.*;


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
                0L,0L, 0L);

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



    //이것도 COUNT하는 걸로 바꿉시다 근데 성능테스트 해보자
    public VoteOutDTO getResult(Long ownerId, Long voteId) {
        Vote vote = voteRepository.findById(voteId).orElseThrow();
        List<Object[]> results = votePaperRepository.countVoteResultByType(voteId);
        Long[] counts = new Long[3]; // 0: yes, 1: no, 2: abstain
        Arrays.fill(counts, 0L);

        for (Object[] result : results) {
            VoteResultType type = (VoteResultType) result[0];
            long count = (Long) result[1];

            switch (type.getDesc()) {
                case "YES":
                    counts[0] = count;
                    break;
                case "NO":
                    counts[1] = count;
                    break;
                case "ABSTAIN":
                    counts[2] = count;
                    break;
            }
        }
        return new VoteOutDTO(voteId, vote.getRoom().getId(),
                vote.getVoteName(),
                vote.isActivated(),
                vote.isAnonymous(),
                counts[0], counts[1], counts[2],
                votePaperRepository.getVoteResultDTOByVoteID(voteId));
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
            throw new DuplicateRequestException(userId + " already voted to "+ voteId);
        }

        VotePaper votePaper = new VotePaper(user ,vote, voteResultType);
        votePaperRepository.save(votePaper);
    }

    //투표들 다 보여주기 vote dto를 내보내는데 찬성수 반대수만
    @CheckUser
    @Transactional(readOnly = true)
    public List<VoteOutDTO> getVotes(Long userId, Long roomId){

        return voteRepository.findVoteOutDTOByRoomId(roomId);
    }
}
