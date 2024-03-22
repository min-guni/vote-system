package meeting.decision.domain;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Data
@Table(name = "vote_papers")
public class VotePaper {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne//유저
    @JoinColumn(name = "user_id")
    private User user;

    private LocalDateTime timeStamp;

    private VoteResultType voteResultType;

    public VotePaper() {
    }

    public VotePaper(User user, VoteResultType voteResultType) {
        this.user = user;
        this.timeStamp = LocalDateTime.now();
        this.voteResultType = voteResultType;
    }
}
