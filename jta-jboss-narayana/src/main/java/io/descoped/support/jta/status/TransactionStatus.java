package io.descoped.support.jta.status;

import javax.transaction.Status;

/**
 * Created by oranheim on 07/02/2017.
 */
public enum TransactionStatus {
    ACTIVE(Status.STATUS_ACTIVE),
    COMMITTED(Status.STATUS_COMMITTED),
    COMMITTING(Status.STATUS_COMMITTING),
    MARKED_ROLLBACK(Status.STATUS_MARKED_ROLLBACK),
    NO_TRANSACTION(Status.STATUS_NO_TRANSACTION),
    PREPARED(Status.STATUS_PREPARED),
    PREPARING(Status.STATUS_PREPARING),
    ROLLEDBACK(Status.STATUS_ROLLEDBACK),
    ROLLING_BACK(Status.STATUS_ROLLING_BACK),
    UNKNOWN(Status.STATUS_UNKNOWN);

    private final Integer statusCode;

    TransactionStatus(Integer statusCode) {
        this.statusCode = statusCode;
    }

    public Integer code() {
        return statusCode;
    }

    public static TransactionStatus valueOf(Integer statusCode) {
        for (TransactionStatus value : values()) {
            if (value.statusCode == statusCode) {
                return value;
            }
        }
        return UNKNOWN;
    }

    @Override
    public String toString() {
        return name();
    }
}
