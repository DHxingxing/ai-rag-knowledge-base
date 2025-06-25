package org.iflytek.domain.enums.message;

import lombok.Getter;
import org.iflytek.domain.enums.BaseEnum;

/**
 * 消息状态枚举
 * 描述消息在整个处理生命周期中的状态
 */

/**
 * @Getter 注解是 Lombok 提供的注解，它会根据成员变量的名字自动生成对应的 getter 方法。
 */
@Getter
public enum MessageStatusEnum implements BaseEnum {
    /**
     * 消息已创建，等待处理
     */
    CREATED(0,"已创建"),

    /**
     * 消息正在处理中
     */
    PROCESSING(1,"处理中"),

    /**
     * 消息处理成功
     */
    COMPLETED(2,"已完成"),

    /**
     * 消息处理失败
     */
    FAILED(3,"处理失败"),

    /**
     * 消息已取消
     */
    CANCELLED(4,"已取消"),

    /**
     * 消息处理超时
     */
    TIMEOUT(5,"处理超时");


    private final Integer code;
    private final String desc;

    MessageStatusEnum(int code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    /**
     * 判断状态是否为终态
     * 终态包括：COMPLETED, FAILED, CANCELLED, TIMEOUT
     */
    public boolean isTerminal() {
        return this == COMPLETED ||
               this == FAILED ||
               this == CANCELLED ||
               this == TIMEOUT;
    }

    /**
     * 判断状态是否可以继续处理
     */
    public boolean isProcessable() {
        return this == CREATED || this == PROCESSING;
    }

    /**
     * 判断状态是否表示处理成功
     */
    public boolean isSuccess() {
        return this == COMPLETED;
    }

    /**
     * 判断状态是否表示处理失败
     */
    public boolean isFailed() {
        return this == FAILED || this == TIMEOUT;
    }
}