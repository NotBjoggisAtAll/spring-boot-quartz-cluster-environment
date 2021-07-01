package com.bjoggis.dev;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class TestBean {
    static final Logger LOGGER = LoggerFactory.getLogger(TestBean.class);

    public void refresh(List<String> args) {
        String message = args.get(0);
        boolean shouldPrintNextMessage = Boolean.parseBoolean(args.get(1));
        String nextMessage = args.get(2);

        refresh(message, shouldPrintNextMessage, nextMessage);
    }

    public void refresh(String message, boolean shouldPrintNextMessage, String nextMessage) {

        LOGGER.info("Begin");
        LOGGER.info(message);
        if (shouldPrintNextMessage) {
            LOGGER.info(nextMessage);
        }
        LOGGER.info("End");
    }

    public void noe() {
        LOGGER.info("Begin noe");
        LOGGER.info("End noe");
    }
}
