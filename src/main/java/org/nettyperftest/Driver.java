package org.nettyperftest;

import java.util.UUID;

/**
 * Created by m on 5/5/18.
 */
public class Driver {

    public static void main(String[] args) throws Exception {

        Client client = new Client();

        client.start("localhost", 9090);

        int numThreads = 8*2;
        final int numOps = 100_000 ;
        Thread[] threads = new Thread[numThreads];

        long ts1 = System.currentTimeMillis();

        for (int x = 0; x < numThreads; x++) {
            Runnable r = () -> {
                UUID id = UUID.randomUUID();
                for (int y = 0; y < numOps; y++) {
                    Message msg = new Message(0, id);
                    client.channel.writeAndFlush(msg);
                }
            };

            threads[x] = new Thread(r);
            threads[x].start();
        }


        for (int x = 0; x < numThreads; x++) {
            threads[x].join();
        }

        long ts2 = System.currentTimeMillis();

        System.out.println((numThreads * numOps * 1.0)/(ts2 - ts1));
    }
}
