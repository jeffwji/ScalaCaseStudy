package com.examples.patterns;

import org.apache.log4j.Logger;

public class Singleton {
    private static final Logger logger = Logger.getLogger(Singleton.class);

    private Singleton(){
        logger.debug("Singleton instance is creating.");
    }

    private static class Keeper {
        final static Singleton instance = new Singleton();
    }

    public static Singleton getInstance() {
        return Keeper.instance;
    }
}
