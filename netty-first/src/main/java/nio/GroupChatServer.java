package nio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.util.Iterator;

/**
 * @author qian
 * 基于nio的群聊服务端
 */
public class GroupChatServer {

    private Selector selector;

    //用于监听
    private ServerSocketChannel listenChannel;

    private static final int port = 6667;

    public GroupChatServer() {

        try {
            //得到选择器
            selector = Selector.open();
            //得到channel
            listenChannel = ServerSocketChannel.open();
            //绑定端口
            listenChannel.socket().bind(new InetSocketAddress(port));
            //将channel设置为非阻塞
            listenChannel.configureBlocking(false);
            //将该listenChannel注册到selector
            listenChannel.register(selector, SelectionKey.OP_ACCEPT);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        GroupChatServer chatServer = new GroupChatServer();
        chatServer.listen();
    }

    /**
     * 监听方法
     */
    public void listen() {
        try {
            //循环监听
            while (true) {
                int count = selector.select(2000);
                if (count > 0) {
                    //有时间处理
                    Iterator<SelectionKey> iterator = selector.selectedKeys().iterator();
                    while (iterator.hasNext()) {
                        //取出selectionKey
                        SelectionKey selectionKey = iterator.next();

                        //监听到accept
                        if (selectionKey.isAcceptable()) {
                            SocketChannel socketChannel = listenChannel.accept();
                            socketChannel.configureBlocking(false);
                            //将该socketChannel注册到selector
                            socketChannel.register(selector, SelectionKey.OP_READ);
                            //提示
                            System.out.println(socketChannel.getRemoteAddress() + "上线。");
                        }
                        if (selectionKey.isReadable()) {
                            //读数据
                            readData(selectionKey);
                        }else {
                            System.out.println("等待...");
                        }
                        iterator.remove();
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {

        }
    }

    /**
     * 读取客户端消息
     */
    private void readData(SelectionKey selectionKey) {

        //取到关联的channel
        SocketChannel channel = null;

        try {
            //得到channel
            channel = (SocketChannel) selectionKey.channel();
            //创建buffer
            ByteBuffer byteBuffer = ByteBuffer.allocate(1024);
            int count = channel.read(byteBuffer);
            if (count > 0) {
                //把缓冲区的数据转成字符串
                String msg = new String(byteBuffer.array());
                System.out.println("from 客户端：" + msg);

                //向其他客戶端转发消息
                sendMsgToOther(msg, channel);
            }
        } catch (IOException e) {
            try {
                System.out.println(channel.getRemoteAddress() + "离线了。");
                //取消注冊
                selectionKey.cancel();
                //关闭通道
                channel.close();
            } catch (IOException e1) {
                e.printStackTrace();
            }
        }

    }

    /**
     * 转发消息给其它客户端
     *
     * @param msg
     * @param socketChannel
     */
    private void sendMsgToOther(String msg, SocketChannel socketChannel) throws IOException {
        System.out.println("服务器转发消息...");
        //遍历所有注册到selector上的Socketchannel，并排除自己
        for (SelectionKey key : selector.keys()) {
            //通过key取出对应的socketchannel
            Channel targetChannel = key.channel();
            //排除自己
            if (targetChannel instanceof SocketChannel && targetChannel != socketChannel) {
                //转型
                SocketChannel destChannel = (SocketChannel) targetChannel;
                //将msg存储到buffer
                ByteBuffer byteBuffer = ByteBuffer.wrap(msg.getBytes());
                //将buffer的数据写入到通道
                destChannel.write(byteBuffer);
            }
        }
    }
}
