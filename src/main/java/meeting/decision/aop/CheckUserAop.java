package meeting.decision.aop;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import meeting.decision.annotation.CheckUser;
import meeting.decision.exception.RoomNotFoundException;
import meeting.decision.exception.VoteNotFoundException;
import meeting.decision.repository.JpaRoomParticipantRepository;
import meeting.decision.repository.JpaRoomRepository;
import meeting.decision.repository.JpaVoteRepository;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.security.access.AuthorizationServiceException;
import org.springframework.stereotype.Component;

@Component
@Aspect
@Slf4j
@RequiredArgsConstructor
public class CheckUserAop {

    private final JpaRoomRepository roomRepository;
    private final JpaRoomParticipantRepository participantRepository;
    private final JpaVoteRepository voteRepository;

    @Pointcut("@annotation(meeting.decision.annotation.CheckUser)")
    public void checkUserAnnotation(){}

    @Pointcut("@within(org.springframework.stereotype.Service)")
    public void isService(){}




    @Before("checkUserAnnotation() && isService() && @annotation(annotation) && args(userId,roomIdOrVoteId,..)")
    public void CheckUser(CheckUser annotation, Long userId, Long roomIdOrVoteId){


        if(annotation.isVote()){
            roomIdOrVoteId = voteRepository.findById(roomIdOrVoteId).orElseThrow(VoteNotFoundException::new).getRoom().getId();
        }


        if(annotation.isOwner()){
            //checkOwner
            if(!roomRepository.findById(roomIdOrVoteId).orElseThrow().getOwner().getId().equals(userId)){
                throw new AuthorizationServiceException("YOU ARE NOT "+roomIdOrVoteId + " OWNER");
            }
        }
        else{
            if(!participantRepository.existsByRoomIdAndUserId(roomIdOrVoteId,userId)){
                roomRepository.findById(roomIdOrVoteId).orElseThrow(RoomNotFoundException::new);
                throw new AuthorizationServiceException("YOU ARE NOT IN " +roomIdOrVoteId + " ROOM");
            }
        }
    }
}
