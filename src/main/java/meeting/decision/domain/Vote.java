package meeting.decision.domain;


import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Getter @Setter
@NoArgsConstructor
@Table(name = "votes")
public class Vote {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String voteName;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "room_id")
    private Room room;


    @OneToMany(mappedBy = "vote", cascade = CascadeType.ALL ,orphanRemoval = true)
    private List<VotePaper> papers = new ArrayList<>();


    private LocalDateTime timeStamp;

    private boolean isAnonymous;
    private boolean isActivated;

    //pre 뭐시기도 사용해보기
    public Vote(String voteName, boolean isAnonymous , Room room) {
        this.voteName = voteName;
        this.room = room;
        this.isAnonymous = isAnonymous;
        this.isActivated = true;
        this.timeStamp = LocalDateTime.now();
    }
}
