package com.examples.patterns;

import junit.framework.TestCase;
import org.apache.log4j.Logger;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Arrays;
import java.util.Collection;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

@RunWith(Parameterized.class)
public class SingletonTest extends TestCase {
    private static final Logger logger = Logger.getLogger(SingletonTest.class);
    private ExecutorService executor;

    @Parameterized.Parameter
    public int threadNumber;

    @Parameterized.Parameter(1)
    public int expectInstances;

    /** Create 10 threads */
    @Parameterized.Parameters
    public static Collection<Object[]> repeat() {
        return Arrays.asList(new Object[][]{
                {100, 1}
        });
    }

    @Before
    @Override
    public void setUp() {
        executor = Executors.newFixedThreadPool(threadNumber);
    }

    @Test
    public void testSingleton() throws InterruptedException{
        //final Set<Singleton> result = ConcurrentHashMap.newKeySet();
        Singleton[] results = new Singleton[threadNumber];

        for(int i=0; i<threadNumber; i++) {
            final int finalI = i;
            executor.execute(() -> {
                try {
                    long threadId = Thread.currentThread().getId();
                    logger.info("[Thread-" + threadId + "] is running");
                    results[finalI] = Singleton.getInstance();
                    logger.info("[Thread-" + threadId + "] is finished");
                }
                catch (Throwable t) {
                    logger.error(t, t.getCause());
                }
            });
        }
        executor.shutdown();
        executor.awaitTermination(10, TimeUnit.SECONDS);

        long count = Arrays.stream(results).distinct().count();
        assertEquals("Instance number supposed to be only one, but you got " + count, expectInstances, count);
    }
}
