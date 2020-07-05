package attribute;

import io.netty.util.AttributeKey;
import session.Session;

/**
 * 是否登录成功的标志位
 * @author qian
 */
public interface Attributes {

    AttributeKey<Session> SESSION = AttributeKey.newInstance("session");
}
