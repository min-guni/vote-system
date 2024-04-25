package meeting.decision.domain;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QVotePaper is a Querydsl query type for VotePaper
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QVotePaper extends EntityPathBase<VotePaper> {

    private static final long serialVersionUID = -1873759785L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QVotePaper votePaper = new QVotePaper("votePaper");

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final DateTimePath<java.time.LocalDateTime> timeStamp = createDateTime("timeStamp", java.time.LocalDateTime.class);

    public final QUser user;

    public final QVote vote;

    public final EnumPath<VoteResultType> voteResultType = createEnum("voteResultType", VoteResultType.class);

    public QVotePaper(String variable) {
        this(VotePaper.class, forVariable(variable), INITS);
    }

    public QVotePaper(Path<? extends VotePaper> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QVotePaper(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QVotePaper(PathMetadata metadata, PathInits inits) {
        this(VotePaper.class, metadata, inits);
    }

    public QVotePaper(Class<? extends VotePaper> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.user = inits.isInitialized("user") ? new QUser(forProperty("user")) : null;
        this.vote = inits.isInitialized("vote") ? new QVote(forProperty("vote"), inits.get("vote")) : null;
    }

}

