package protocol.response;

import lombok.Data;
import protocol.BasePacket;

import static protocol.command.Command.LOGIN_RESPONSE;

/**
 * 服务端返回消息报文
 * @author qian
 */
@Data
public class LoginResponsePacket extends BasePacket {
    private boolean success;

    private String reason;

    @Override
    public Byte getCommand() {
        return LOGIN_RESPONSE;
    }
}
