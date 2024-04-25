package meeting.decision.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "room_participants")
@Getter @NoArgsConstructor
public class RoomParticipant {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "room_id")
    private Room room;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;


    private LocalDateTime enterTime;

    public RoomParticipant(Room room, User user) {
        this.room = room;
        room.getUserList().add(this);

        this.user = user;
        user.getRoomList().add(this);
    }

    @PrePersist
    public void setEnterTime(){
        enterTime = LocalDateTime.now();
    }
}
