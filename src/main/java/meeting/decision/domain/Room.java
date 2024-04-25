package meeting.decision.domain;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

@Data
@Entity
@ToString(exclude = "userList")
@NoArgsConstructor
public class Room {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String roomName;
    private LocalDateTime createTime;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "owner_id")
    private User owner;


    @OneToMany(mappedBy = "room")
    private List<RoomParticipant> userList = new ArrayList<>();


    //vote모아둬야됨
    @OneToMany(mappedBy = "room")
    private List<Vote> voteList = new ArrayList<>();

    public Room(String roomName, User owner) {
        this.roomName = roomName;
        this.owner = owner;
    }

    @PrePersist
    private void setCreateTime(){
        this.createTime = LocalDateTime.now();
    }
}
