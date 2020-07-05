package protocol.request;

import lombok.Data;
import lombok.NoArgsConstructor;
import protocol.BasePacket;

import static protocol.command.Command.MESSAGE_REQUEST;

/**
 * @author qian
 */
@Data
@NoArgsConstructor
public class MessageRequestPacket extends BasePacket {

    private String toUserId;
    private String message;

    public MessageRequestPacket(String toUserId, String message) {
        this.toUserId = toUserId;
        this.message = message;
    }

    @Override
    public Byte getCommand() {
        return MESSAGE_REQUEST;
    }
}
