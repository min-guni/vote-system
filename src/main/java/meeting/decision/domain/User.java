package meeting.decision.domain;


import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;


@Getter @Setter
@ToString(exclude = "roomList")
@Entity
@Table(name = "users")
@NoArgsConstructor
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String username;

    private String hashedPassword;

    private LocalDateTime createDate;
    private LocalDateTime lastUpdateDate;

    @OneToMany(mappedBy = "user", cascade = CascadeType.REMOVE, orphanRemoval = true)
    private List<RoomParticipant> roomList = new ArrayList<>();

    @PrePersist
    private void setCreateDate(){
        this.createDate = LocalDateTime.now();
        this.lastUpdateDate = LocalDateTime.now();
    }

    @PreUpdate
    private void setLastUpdateDate(){
        this.lastUpdateDate = LocalDateTime.now();
    }

    public User(String username, String hashedPassword) {
        this.username = username;
        this.hashedPassword = hashedPassword;
    }
}
