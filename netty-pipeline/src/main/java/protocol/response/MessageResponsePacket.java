package protocol.response;

import lombok.Data;
import protocol.BasePacket;

import static protocol.command.Command.MESSAGE_RESPONSE;

/**
 * @author qian
 */
@Data
public class MessageResponsePacket extends BasePacket {

    private String message;

    @Override
    public Byte getCommand() {

        return MESSAGE_RESPONSE;
    }
}
