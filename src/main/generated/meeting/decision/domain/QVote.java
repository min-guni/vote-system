package meeting.decision.domain;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QVote is a Querydsl query type for Vote
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QVote extends EntityPathBase<Vote> {

    private static final long serialVersionUID = 566270133L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QVote vote = new QVote("vote");

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final BooleanPath isActivated = createBoolean("isActivated");

    public final BooleanPath isAnonymous = createBoolean("isAnonymous");

    public final ListPath<VotePaper, QVotePaper> papers = this.<VotePaper, QVotePaper>createList("papers", VotePaper.class, QVotePaper.class, PathInits.DIRECT2);

    public final QRoom room;

    public final DateTimePath<java.time.LocalDateTime> timeStamp = createDateTime("timeStamp", java.time.LocalDateTime.class);

    public final StringPath voteName = createString("voteName");

    public QVote(String variable) {
        this(Vote.class, forVariable(variable), INITS);
    }

    public QVote(Path<? extends Vote> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QVote(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QVote(PathMetadata metadata, PathInits inits) {
        this(Vote.class, metadata, inits);
    }

    public QVote(Class<? extends Vote> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.room = inits.isInitialized("room") ? new QRoom(forProperty("room"), inits.get("room")) : null;
    }

}

