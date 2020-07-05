package server;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import protocol.BasePacket;
import protocol.PacketCodeC;
import protocol.request.LoginRequestPacket;
import protocol.response.LoginResponsePacket;

import java.util.Date;

/**
 * @author qian
 */
public class FirstServerHandler extends ChannelInboundHandlerAdapter {

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {

        System.out.println(new Date() + ": 客户端开始登录……");
        ByteBuf byteBuf = (ByteBuf) msg;

        // 解码
        BasePacket packet = PacketCodeC.INSTANCE.decode(byteBuf);

        // 判断是否是登录请求数据包
        if (packet instanceof LoginRequestPacket) {
            //登录流程
            LoginRequestPacket loginRequestPacket = (LoginRequestPacket) packet;

            LoginResponsePacket loginResponsePacket = new LoginResponsePacket();
            loginResponsePacket.setVersion(packet.getVersion());
            // 登录校验
            if (valid(loginRequestPacket)) {
                loginResponsePacket.setSuccess(true);
                System.out.println(new Date() + ": 登录成功!");
            } else {
                loginResponsePacket.setReason("账号密码校验失败");
                loginResponsePacket.setSuccess(false);
                System.out.println(new Date() + ": 登录失败!");
            }
            //登录响应给客户端
            ByteBuf responseByteBuf = PacketCodeC.INSTANCE.encode(ctx.alloc(), loginResponsePacket);
            ctx.channel().writeAndFlush(responseByteBuf);
        }
    }

    /**
     * 验证用户名密码是否正确
     *
     * @param loginRequestPacket
     * @return
     */
    private boolean valid(LoginRequestPacket loginRequestPacket) {
        if ("flash".equals(loginRequestPacket.getUsername()) && "pwd".equals(loginRequestPacket.getPassword())) {
            return true;
        } else {
            return false;
        }
    }
}
