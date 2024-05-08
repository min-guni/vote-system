package meeting.decision.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import meeting.decision.annotation.CheckUser;
import meeting.decision.domain.Room;
import meeting.decision.domain.RoomParticipant;
import meeting.decision.domain.User;
import meeting.decision.dto.room.RoomOutDTO;
import meeting.decision.dto.room.RoomUpdateDTO;
import meeting.decision.dto.user.UserOutDTO;
import meeting.decision.exception.exceptions.RoomNotFoundException;
import meeting.decision.exception.exceptions.UserNotFoundErrorException;
import meeting.decision.exception.exceptions.UserNotInRoomException;
import meeting.decision.repository.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;


@RequiredArgsConstructor
@Service
@Slf4j
@Transactional
public class RoomService {
    private final JpaRoomRepository roomRepository;
    private final JpaUserRepository userRepository;
    private final JpaRoomParticipantRepository roomParticipantRepository;
    private final JpaVotePaperRepository votePaperRepository;
    private final JpaVoteRepository voteRepository;


    //create
    public RoomOutDTO create(String roomName, Long ownerId){
        User owner = userRepository.findById(ownerId).orElseThrow();
        Room room = roomRepository.save(new Room(roomName, owner));
        addUserToRoom(ownerId, room.getId(), ownerId);

        //확인 필요
        return new RoomOutDTO(room.getId(), room.getRoomName(), ownerId, 1L, room.getCreateTime());
    }

    //update

    @CheckUser(isOwner = true)
    public void update(Long ownerId, Long roomId, RoomUpdateDTO updateParam){ //owner인지 확인하는거 aop로 적용 Parameter에 Long 두개 있는지 확인
        Room room = roomRepository.findById(roomId).orElseThrow(RoomNotFoundException::new);
        room.setRoomName(updateParam.getRoomName());
        if(!roomParticipantRepository.existsByRoomIdAndUserId(roomId, updateParam.getOwnerId())){
            throw new UserNotInRoomException(updateParam.getOwnerId());
        }
        User newOwner = userRepository.findById(updateParam.getOwnerId()).orElseThrow(UserNotFoundErrorException::new);
        room.setOwner(newOwner);
    }

    //remove
    @CheckUser(isOwner = true)
    public void delete(Long ownerId, Long roomId){
        //cascade.REMOVE 가 삭제를 해주지만 너무 많은 DELETE 쿼리가 나가므로 직접 DELETE 문으로 삭제
        votePaperRepository.deleteVotePaperByRoomId(roomId);
        voteRepository.deleteVoteByRoomId(roomId);
        roomParticipantRepository.deleteByRoomId(roomId);
        roomRepository.deleteById(roomId);
    }

    //delete user
    @CheckUser(isOwner = true)
    public void deleteUserFromRoom(Long ownerId, Long roomId, Long userId){
        User user = userRepository.findById(userId).orElseThrow();
        Room room = roomRepository.findById(roomId).orElseThrow();
        if(user.getId().equals(ownerId)){   //Owner가 삭제될 경우 Room이 삭제
            delete(ownerId, roomId); // 내부 호출 주의
        }

        roomParticipantRepository.deleteByRoomIdAndUserId(roomId, userId);


//        //Lazy기 때문에 Select Qeury가 나감.
//        room.getUserList().remove(participant.get());
//        user.getRoomList().remove(participant.get());
    }

    @CheckUser(isOwner = true)
    public void addUserToRoom(Long ownerId, Long roomId, Long userId){

        Room room = roomRepository.findById(roomId).orElseThrow(RoomNotFoundException::new);
        User user = userRepository.findById(userId).orElseThrow();

        if(roomParticipantRepository.existsByRoomIdAndUserId(roomId, userId)){
            return;
        }
        RoomParticipant roomParticipant = new RoomParticipant(room, user);
        roomParticipantRepository.save(roomParticipant);
    }


    @CheckUser
    @Transactional(readOnly = true)
    public List<UserOutDTO> findAllUserByRoomId(Long userId, Long roomId){
        return roomRepository.findById(roomId).orElseThrow(RoomNotFoundException::new).getUserList().stream().map((participant -> new UserOutDTO(participant.getUser().getId(), participant.getUser().getUsername(), participant.getUser().getCreateDate(), participant.getUser().getLastUpdateDate()))).collect(Collectors.toList());
    }

    @CheckUser
    @Transactional(readOnly = true)
    public RoomOutDTO findRoomById(Long userId, Long roomId){
        return roomParticipantRepository.findRoomOutDTOByRoomId(roomId).orElseThrow(RoomNotFoundException::new);
    }


    public List<RoomOutDTO> findAllRoomByUserId(Long userId){
        return roomParticipantRepository.findByIdDTO(userId);
    }
}
