package meeting.decision.service;

import lombok.RequiredArgsConstructor;
import meeting.decision.domain.Room;
import meeting.decision.domain.User;
import meeting.decision.dto.RoomUpdateDTO;
import meeting.decision.repository.JpaRoomRepository;
import meeting.decision.repository.JpaUserRepository;
import org.springframework.security.access.AuthorizationServiceException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;


@RequiredArgsConstructor
@Service
@Transactional
public class RoomService {
    private final JpaRoomRepository roomRepository;
    private final JpaUserRepository userRepository;


    //create
    public Room create(String roomName, Long ownerId){
        User owner = userRepository.findById(ownerId).orElseThrow();
        Room room = roomRepository.save(new Room(roomName, owner));
        addUserToRoom(room.getId(), ownerId, owner.getUsername());
        return room;
    }

    //update
    public void update(Long ownerId, Long roomId, RoomUpdateDTO updateParam){
        Room room = roomRepository.findById(roomId).orElseThrow();
        if(!room.getOwner().getId().equals(ownerId)){
            throw new AuthorizationServiceException("ONLY ROOM OWNER CAN UPDATE ROOM");
        }
        room.setRoomName(updateParam.getRoomName());
        room.setOwner(updateParam.getOwner());
    }

    //remove
    public void delete(Long ownerId, Long roomId){
        Room room = roomRepository.findById(roomId).orElseThrow();
        if (!room.getOwner().getId().equals(ownerId)) {
            throw new AuthorizationServiceException("ONLY ROOM OWNER CAN DELETE ROOM");
        }
        roomRepository.deleteById(roomId);
    }

    public boolean isUserInRoom(Long roomId, Long userId){
        //Room room = roomRepository.findById(roomId).orElseThrow();
        //return room.getUserList().stream().anyMatch(user->user.getId().equals(userId));
        return roomRepository.existsUserInRoom(roomId, userId) > 0;
    }

    //delete user
    public void deleteUserFromRoom(Long roomId, Long ownerId, String username){
        User owner = userRepository.findById(ownerId).orElseThrow();
        Room room = roomRepository.findById(roomId).orElseThrow();
        if (!room.getOwner().getId().equals(ownerId)) {
            throw new AuthorizationServiceException("Only Room Owner Can Delete User");
        }
        User user = userRepository.findByUsername(username).orElseThrow();
        room.getUserList().remove(user);
        user.getRoomSet().remove(room);
    }
    //

    public void addUserToRoom(Long roomId, Long ownerId, String username){

        User owner = userRepository.findById(ownerId).orElseThrow();
        Room room = roomRepository.findById(roomId).orElseThrow();

        //주인이아니라면
        if(!room.getOwner().getId().equals(ownerId)){
            throw new AuthorizationServiceException("Only Room Owner Can Add User");
        }
        if(owner.getUsername().equals(username)){
            room.getUserList().add(owner);
            owner.getRoomSet().add(room);
        }
        else{
            User user = userRepository.findByUsername(username).orElseThrow();
            room.getUserList().add(user);
            user.getRoomSet().add(room);
        }
    }

    public Set<User> findAllUser(Long roomId){
        return roomRepository.findById(roomId).orElseThrow().getUserList();
    }

    public List<Room> findAll(){
        return roomRepository.findAll();
    }
}
