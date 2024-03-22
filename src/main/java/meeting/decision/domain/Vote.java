package meeting.decision.domain;


import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Data
@Table(name = "votes")
public class Vote {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String voteName;

    @ManyToOne
    @JoinColumn(name = "room_id")
    private Room room;


    @OneToMany(cascade = CascadeType.ALL ,orphanRemoval = true)
    private Set<VotePaper> papers = new HashSet<>();


    private LocalDateTime timeStamp;
    private boolean isActivated;

    public Vote() {
    }

    public Vote(String voteName,  Room room) {
        this.voteName = voteName;
        this.room = room;
        this.isActivated = true;
        this.timeStamp = LocalDateTime.now();
    }
}
