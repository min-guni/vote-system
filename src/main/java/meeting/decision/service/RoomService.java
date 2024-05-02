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
import meeting.decision.exception.DuplicateUserExeption;
import meeting.decision.exception.RoomNotFoundException;
import meeting.decision.exception.UserNotFoundErrorException;
import meeting.decision.repository.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
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
        return new RoomOutDTO(room.getId(), room.getRoomName(), ownerId, 1L);
    }

    //update

    @CheckUser(isOwner = true)
    public void update(Long ownerId, Long roomId, RoomUpdateDTO updateParam){ //owner인지 확인하는거 aop로 적용 Parameter에 Long 두개 있는지 확인
        Room room = roomRepository.findById(roomId).orElseThrow(RoomNotFoundException::new);
        room.setRoomName(updateParam.getRoomName());
        room.setOwner(updateParam.getOwner());
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
        if(user.getId().equals(ownerId)){   //Owner가 삭제될 경우 Room이 삭제
            delete(ownerId, roomId); // 내부 호출 주의
            return;
        }

        Optional<RoomParticipant> participant = roomParticipantRepository.findByRoomIdAndUserId(roomId, user.getId());
        if(participant.isEmpty()){
            return;
        }

        roomParticipantRepository.deleteByRoomIdAndUserId(roomId, userId);

//        //Lazy기 때문에 Select Qeury가 나감.
//        room.getUserList().remove(participant.get());
//        user.getRoomList().remove(participant.get());
    }
    //내일 이거 테이블 바꿔보기
    //add User에서 select join이 너무 많이 일어나는 문제가 발생

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

    public List<RoomParticipant> findAllUser(Long roomId){
        return roomRepository.findById(roomId).orElseThrow(RoomNotFoundException::new).getUserList();
    }

    public List<Room> findAll(){
        return roomRepository.findAll();
    }

    @CheckUser
    public List<UserOutDTO> findAllUserByRoomId(Long userId, Long roomId){
        return roomRepository.findById(roomId).orElseThrow(RoomNotFoundException::new).getUserList().stream().map((participant -> new UserOutDTO(participant.getUser().getId(), participant.getUser().getUsername()))).collect(Collectors.toList());
    }

    @CheckUser
    public RoomOutDTO findRoomById(Long userId, Long roomId){
        return roomParticipantRepository.findRoomOutDTOByRoomId(roomId).orElseThrow(RoomNotFoundException::new);
    }


    public List<RoomOutDTO> findAllRoom(){
        return roomParticipantRepository.findAllDTO();
        //return roomRepository.findAll().stream().map(room -> new RoomOutDTO(room.getId(), room.getRoomName(), room.getOwner().getId() ,room.getUserList().size())).collect(Collectors.toList());
    }

    public List<RoomOutDTO> findAllRoomByUserId(Long userId){
        return roomParticipantRepository.findByIdDTO(userId);
    }
}
