package protocol.request;

import lombok.Data;
import protocol.BasePacket;

import static protocol.command.Command.LOGIN_REQUEST;

/**
 * 客户端请求消息报文
 * @author qian
 */
@Data
public class LoginRequestPacket extends BasePacket {

    private String userId;

    private String userName;

    private String password;

    @Override
    public Byte getCommand() {
        return LOGIN_REQUEST;
    }
}
