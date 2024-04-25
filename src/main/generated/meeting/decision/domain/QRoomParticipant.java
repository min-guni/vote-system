package meeting.decision.domain;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QRoomParticipant is a Querydsl query type for RoomParticipant
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QRoomParticipant extends EntityPathBase<RoomParticipant> {

    private static final long serialVersionUID = -272479059L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QRoomParticipant roomParticipant = new QRoomParticipant("roomParticipant");

    public final DateTimePath<java.time.LocalDateTime> enterTime = createDateTime("enterTime", java.time.LocalDateTime.class);

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final QRoom room;

    public final QUser user;

    public QRoomParticipant(String variable) {
        this(RoomParticipant.class, forVariable(variable), INITS);
    }

    public QRoomParticipant(Path<? extends RoomParticipant> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QRoomParticipant(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QRoomParticipant(PathMetadata metadata, PathInits inits) {
        this(RoomParticipant.class, metadata, inits);
    }

    public QRoomParticipant(Class<? extends RoomParticipant> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.room = inits.isInitialized("room") ? new QRoom(forProperty("room"), inits.get("room")) : null;
        this.user = inits.isInitialized("user") ? new QUser(forProperty("user")) : null;
    }

}

