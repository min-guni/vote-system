package meeting.decision.service;

import lombok.RequiredArgsConstructor;
import meeting.decision.domain.RoomParticipant;
import meeting.decision.domain.User;
import meeting.decision.dto.user.UserUpdateDTO;
import meeting.decision.exception.LoginFailedException;
import meeting.decision.exception.UsernameAlreadyExistsException;
import meeting.decision.repository.JpaUserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class UserService {

    private final JpaUserRepository repository;
    private final PasswordEncoder passwordEncoder;
    //create
    public User create(String username, String hassedPassword){
        //이미 같은 username이 있는 지 확인
        Optional<User> byUsername = repository.findByUsername(username);
        if(byUsername.isPresent()){
            throw new UsernameAlreadyExistsException(username);
        }
        return repository.save(new User(username, hassedPassword));
    }
    //update
    public void update(Long userId, UserUpdateDTO updateParam){
        User updateUser = repository.findById(userId).orElseThrow();
        updateUser.setUsername(updateParam.getUsername());
        updateUser.setHashedPassword(updateParam.getHashedPassword());
    }

    public User findById(Long userId){
        return repository.findById(userId).orElseThrow();
    }

    public Long checkUser(String username, String plainPassword){
        User user = repository.findByUsername(username).orElseThrow();
        if(passwordEncoder.matches(plainPassword, user.getHashedPassword())){
            return user.getId();
        }
        else{
            throw new LoginFailedException();
        }
    }

    public List<RoomParticipant> findAllRoomsById(Long userId){
        return repository.findById(userId).orElseThrow().getRoomList(); // N + 1 문제 나올 가능성 100퍼 ㅋㅋ
    }


}
