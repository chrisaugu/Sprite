package com.fantastix.sprite;

/**
 * Created by Admin on 30/04/2017.
 */
import java.util.Date;
import java.util.List;
import java.util.Map;

public interface Message {
    String getMessageId();

    Map<String, String> getHeaders();

    String getTextBody();

    List<String> getRecipientIds();

    String getSenderId();

    Date getTimestamp();
}

