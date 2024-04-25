package meeting.decision.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Getter @Setter
@Table(name = "vote_papers")
@NoArgsConstructor
public class VotePaper {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne//유저
    @JoinColumn(name = "user_id")
    private User user;

    private LocalDateTime timeStamp;

    @Enumerated
    private VoteResultType voteResultType;

    @ManyToOne
    @JoinColumn(name = "vote_id")
    private Vote vote;

    public VotePaper(User user, Vote vote, VoteResultType voteResultType) {
        this.user = user;
        this.vote = vote;
        vote.getPapers().add(this);

        this.voteResultType = voteResultType;
    }

    @PrePersist
    public void setTimeStamp(){
        timeStamp = LocalDateTime.now();
    }
}
