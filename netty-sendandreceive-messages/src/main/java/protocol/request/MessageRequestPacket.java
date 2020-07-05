package protocol.request;

import lombok.Data;
import protocol.BasePacket;

import static protocol.command.Command.MESSAGE_REQUEST;

/**
 * @author qian
 */
@Data
public class MessageRequestPacket extends BasePacket {

    private String message;

    @Override
    public Byte getCommand() {
        return MESSAGE_REQUEST;
    }
}
