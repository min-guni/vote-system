package meeting.decision.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import meeting.decision.annotation.CheckUser;
import meeting.decision.domain.*;
import meeting.decision.dto.vote.VoteInDTO;
import meeting.decision.dto.vote.VoteOutDTO;
import meeting.decision.dto.vote.VoteResultDTO;
import meeting.decision.dto.vote.VoteUpdateDTO;
import meeting.decision.exception.exceptions.*;
import meeting.decision.repository.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;


@Transactional
@RequiredArgsConstructor
@Service
@Slf4j
public class VoteService {
    //create
    private final JpaVoteRepository voteRepository;
    private final JpaUserRepository userRepository;
    private final JpaRoomRepository roomRepository;
    private final JpaRoomParticipantRepository roomParticipantRepository;
    private final JpaVotePaperRepository votePaperRepository;

    @CheckUser(isOwner = true)
    public VoteOutDTO create(Long ownerId, Long roomId, VoteInDTO voteInDTO){
        Room room = roomRepository.findById(roomId).orElseThrow(RoomNotFoundException::new);
        Vote savedVote = voteRepository.save(new Vote(voteInDTO.getVoteName(), voteInDTO.isAnonymous(), room));

        return new VoteOutDTO(savedVote.getId(),
                roomId, savedVote.getVoteName(),
                savedVote.isActivated(), savedVote.isAnonymous(),
                0L,0L, 0L, room.getCreateTime());

    }

    @CheckUser(isOwner = true, isVote = true)
    public void update(Long ownerId, Long voteId, VoteUpdateDTO updateParam){
        Vote vote = voteRepository.findById(voteId).orElseThrow(VoteNotFoundException::new);
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
        //votePaperRepository.deleteVotePaperByVoteId(voteId);
    }


    @CheckUser(isVote = true)
    public VoteOutDTO getResult(Long userId, Long voteId) {
        Vote vote = voteRepository.findVoteByVoteIdWithFetch(voteId).orElseThrow(VoteNotFoundException::new);
        return calResult(vote, vote.getPapers(), vote.isAnonymous());
    }

    @CheckUser(isVote = true)
    public void addVotePaper(Long userId, Long voteId, VoteResultType voteResultType){
        //이미 투표에 투표있는지 확인 있는지 확인 있으면 false 반환
        User user = userRepository.findById(userId).orElseThrow(UserNotFoundErrorException::new);
        Vote vote = voteRepository.findById(voteId).orElseThrow(VoteNotFoundException::new);


        if(!vote.isActivated()){
            throw new VoteIsNotActivatedException(voteId + "vote is not activated");
        }

        if(!roomParticipantRepository.existsByRoomIdAndUserId(vote.getRoom().getId(), userId)){
            throw new UserNotInRoomException(userId);
        }

        if(votePaperRepository.existsByVoteIdAndUserId(voteId, userId)){
            return;
        }

        VotePaper votePaper = new VotePaper(user ,vote, voteResultType);
        votePaperRepository.save(votePaper);
    }

    @CheckUser
    @Transactional(readOnly = true)
    public List<VoteOutDTO> getVotes(Long userId, Long roomId){
        List<Vote> voteList = voteRepository.findVoteByRoomId(roomId);  //수행시간 104ms
        List<VoteOutDTO> voteOutDTOList = new ArrayList<>();
        for(Vote vote : voteList){
            voteOutDTOList.add(calResult(vote, vote.getPapers(), true));
        }
        return voteOutDTOList;
//        return voteRepository.findVoteOutDTOByRoomId(roomId); // 수행시간 38ms
    }


    private VoteOutDTO calResult(Vote vote, List<VotePaper> votePapers, boolean isAnonymous){
        Long yesNum = 0L, noNum = 0L, abstentionNum = 0L;
        List<VoteResultDTO> voteResultDTOList = new ArrayList<>();
        for(VotePaper vp : votePapers){
            switch (vp.getVoteResultType()){
                case YES -> {
                    yesNum++;
                }
                case NO -> {
                    noNum++;
                }
                case ABSTAIN -> {
                    abstentionNum++;
                }
            }
            if(!isAnonymous){
                voteResultDTOList.add(new VoteResultDTO(
                        vp.getUser().getId(),
                        vp.getUser().getUsername(),
                        vp.getVoteResultType()
                ));
            }
        }
        VoteOutDTO voteOutDTO = new VoteOutDTO(
                vote.getId(),
                vote.getRoom().getId(),
                vote.getVoteName(),
                vote.isActivated(),
                isAnonymous,
                yesNum,
                noNum,
                abstentionNum,
                vote.getStartTime()
        );

        if (!isAnonymous) {
            voteOutDTO.setVoteResult(Optional.of(voteResultDTOList));
        }

        return voteOutDTO;
    }
}