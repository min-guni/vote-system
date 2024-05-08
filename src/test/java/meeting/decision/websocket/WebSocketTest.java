package meeting.decision.websocket;

import meeting.decision.dto.user.UserOutDTO;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.messaging.converter.MappingJackson2MessageConverter;
import org.springframework.messaging.simp.stomp.StompFrameHandler;
import org.springframework.messaging.simp.stomp.StompHeaders;
import org.springframework.messaging.simp.stomp.StompSession;
import org.springframework.messaging.simp.stomp.StompSessionHandlerAdapter;
import org.springframework.web.socket.WebSocketHttpHeaders;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;
import org.springframework.web.socket.messaging.WebSocketStompClient;

import java.lang.reflect.Type;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class WebSocketTest {

    private WebSocketStompClient stompClient;
    private StompSession stompSession;
    private BlockingQueue<String> blockingQueue;

    @LocalServerPort
    private int port;

    @BeforeEach
    public void setup() throws Exception {
        this.stompClient = new WebSocketStompClient(new StandardWebSocketClient());
        this.stompClient.setMessageConverter(new MappingJackson2MessageConverter());
        this.blockingQueue = new ArrayBlockingQueue<>(1);

        stompSession = stompClient.connectAsync("ws://localhost:" + port + "/ws-stomp", new StompSessionHandlerAdapter() {
        }).get(1, TimeUnit.SECONDS);
    }

    @AfterEach
    public void tearDown() throws Exception {
        if (stompSession != null) {
            stompSession.disconnect();
        }
        if (stompClient != null) {
            stompClient.stop();
        }
    }

    @Test
    public void testAddUser() throws Exception{
//        String websocketUrl = "ws://localhost:" + port + "/ws-stomp";
//        StompSession stompSession = stompClient.connect(websocketUrl, headers, new StompSessionHandlerAdapter() {}).get(1, TimeUnit.SECONDS);
//
//        stompSession.subscribe("/topic/room/{roomId}", new StompFrameHandler() {
//            @Override
//            public Type getPayloadType(StompHeaders headers) {
//                return List.class;
//            }
//
//            @Override
//            public void handleFrame(StompHeaders headers, Object payload) {
//                List<UserOutDTO> response = (List<UserOutDTO>) payload;
//                assertNotNull(response);
//                assertTrue(response.size() > 0);
//            }
//        });
//
//        stompSession.send("/send/room/{roomId}/add", new UserOutDTO(/* 테스트 데이터 */));
//        Thread.sleep(1000); // 메시지 처리를 기다리기 위해 잠시 대기
    }

}
