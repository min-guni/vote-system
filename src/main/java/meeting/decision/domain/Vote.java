package meeting.decision.domain;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import meeting.decision.dto.vote.VoteUpdateDTO;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
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


    @OneToMany(mappedBy = "vote", cascade = CascadeType.REMOVE ,orphanRemoval = true)
    private List<VotePaper> papers = new ArrayList<>();


    private LocalDateTime startTime;

    private boolean isAnonymous;
    private boolean isActivated;

    public Vote(String voteName, boolean isAnonymous , Room room) {
        this.voteName = voteName;
        this.room = room;
        this.isAnonymous = isAnonymous;
        this.isActivated = true;
    }

    public void updateVote(VoteUpdateDTO voteUpdateDTO){
        this.voteName = voteUpdateDTO.getVoteName();
        this.isActivated = voteUpdateDTO.isActivated();
    }

    @PrePersist
    private void setTime(){
        this.startTime = LocalDateTime.now();
    }
}
