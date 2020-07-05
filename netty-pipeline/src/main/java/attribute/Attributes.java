package attribute;

import io.netty.util.AttributeKey;

/**
 * 是否登录成功的标志位
 * @author qian
 */
public interface Attributes {

    AttributeKey<Boolean> LOGIN = AttributeKey.newInstance("login");
}
