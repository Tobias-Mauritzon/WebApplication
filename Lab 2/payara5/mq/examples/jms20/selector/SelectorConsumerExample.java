/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright (c) 2000-2017 Oracle and/or its affiliates. All rights reserved.
 *
 * The contents of this file are subject to the terms of either the GNU
 * General Public License Version 2 only ("GPL") or the Common Development
 * and Distribution License("CDDL") (collectively, the "License").  You
 * may not use this file except in compliance with the License.  You can
 * obtain a copy of the License at
 * https://oss.oracle.com/licenses/CDDL+GPL-1.1
 * or LICENSE.txt.  See the License for the specific
 * language governing permissions and limitations under the License.
 *
 * When distributing the software, include this License Header Notice in each
 * file and include the License file at LICENSE.txt.
 *
 * GPL Classpath Exception:
 * Oracle designates this particular file as subject to the "Classpath"
 * exception as provided by Oracle in the GPL Version 2 section of the License
 * file that accompanied this code.
 *
 * Modifications:
 * If applicable, add the following below the License Header, with the fields
 * enclosed by brackets [] replaced by your own identifying information:
 * "Portions Copyright [year] [name of copyright owner]"
 *
 * Contributor(s):
 * If you wish your version of this file to be governed by only the CDDL or
 * only the GPL Version 2, indicate your decision by adding "[Contributor]
 * elects to include this software in this distribution under the [CDDL or GPL
 * Version 2] license."  If you don't indicate a single choice of license, a
 * recipient has the option to distribute your version of this file under
 * either the CDDL, the GPL Version 2 or to extend the choice of license to
 * its licensees as provided above.  However, if you add GPL Version 2 code
 * and therefore, elected the GPL Version 2 license, then the option applies
 * only if the new code is made subject to such option by the copyright
 * holder.
 */

import java.util.*;
import javax.jms.*;
import java.util.concurrent.CountDownLatch;

/**
 * The SelectorConsumerExample class demonstrates the use of multiple 
 * subscribers and message selectors.
 * <p>
 * The program contains a Subscriber class with a listener 
 * class, a main method, and a method that runs the subscriber
 * threads.
 * <p>
 * The program creates four
 * instances of the Subscriber class, one for each of three types and one that 
 * listens for the "Finished" message.  Each subscriber instance uses a 
 * different message selector to fetch messages of only one type.
 * The listener displays the messages that
 * the subscribers receive.  Because all the objects run in threads, the
 * displays are interspersed when the program runs.
 * <p>
 * Specify a topic name on the command line when you run the program.
 */
public class SelectorConsumerExample {

    static final String  MESSAGE_TYPES[] = 
                             {"Nation/World", "Metro/Region", "Business",
                              "Sports", "Living/Arts", "Opinion",
                               // always last type
                              "Finished"
                             };
    static final String  END_OF_MESSAGE_STREAM_TYPE =
                             MESSAGE_TYPES[MESSAGE_TYPES.length-1];

    static int                  exitcode = 0;
    private String destName              = null;
    final int  ARRSIZE                   = 6;


      /**
     * Reads the topic name from the command line, then calls the
     * run_threads method to execute the program threads.
     *
     * @param args      the topic used by the example
     */
    public static void main(String[] args) {

	 if ( args.length < 1 ) {
                  System.out.println("Usage: java SelectorConsumerExample <topic_name>");
                  System.exit(1);
                }

                SelectorConsumerExample receiveMsg = new SelectorConsumerExample();
                receiveMsg.parseArgs(args);
                try {
                        // Receive messages from topic
                        receiveMsg.run_threads();
                }catch(Exception ex) {
                        ex.printStackTrace();
                        exitcode = 1;
                }
                System.exit(exitcode);
   }

    /**
    * parseArgs method.
    *
    * @param args  the arguments that are passed through main method
    */
    public void parseArgs(String[] args){

        destName = new String(args[0]);
        System.out.println("Topic name is " + destName);
    }


    /**
     * Each instance of the Subscriber class creates a subscriber that uses
     * a message selector that is based on the string passed to its 
     * constructor.
     * It registers its message listener, then starts listening
     * for messages.  It does not exit till the listener gets the last
     * message.
     */
    public class Subscriber extends Thread {
        String                  whatKind;
        int                     subscriberNumber;

        /**
         * The MultipleListener class implements the MessageListener interface  
         * by defining an onMessage method for the Subscriber class.
         */
        private class MultipleListener implements MessageListener {

            private CountDownLatch countDownLatch;

	    MultipleListener() {
                   countDownLatch = new CountDownLatch(1);
           }


            /**
             * Displays the message text.
             * If the value of the NewsType property is "Finished", the message 
             * listener sets its monitor state to all done processing messages.
             *
             * @param inMessage	the incoming message
             */
            public void onMessage(Message inMessage) {
                TextMessage  msg = (TextMessage) inMessage;
                String       newsType;

                try {
                    System.out.println("SUBSCRIBER " + subscriberNumber 
                                       + " THREAD: Message received: " 
                                       + msg.getText());
                    newsType = msg.getStringProperty("NewsType");
                    if (newsType.equals(SelectorConsumerExample.END_OF_MESSAGE_STREAM_TYPE)) {
                        System.out.println("SUBSCRIBER " + subscriberNumber 
                             + " THREAD: Received finished-publishing message");
                        countDownLatch.countDown();
	
                    }
                } catch(JMSException e) {
                    System.out.println("Exception in onMessage(): " 
                                       + e.toString());
		    countDownLatch.countDown();
                }
            }
	
	    public void await() {
                   try {
                        countDownLatch.await();
                   } catch (InterruptedException e) {
                        e.printStackTrace();
                   }
            }

        }
        
        /**
         * Constructor.  Sets whatKind to indicate the type of
         * message this Subscriber object will listen for; sets
         * subscriberNumber based on Subscriber array index.
         *
         * @param str	a String from the MESSAGE_TYPES array
         * @param num	the index of the Subscriber array
         */
        public Subscriber(String str, int num) {
            whatKind = str;
            subscriberNumber = num + 1;
        }
 
        /**
         * Runs the thread.
         */
        public void run() {
            ConnectionFactory    connectionFactory = null;
            Topic                topic = null;
            String               selector = null;
            JMSConsumer          msgConsumer = null;
            MultipleListener     multipleListener = new MultipleListener();

            connectionFactory = 
                    new com.sun.messaging.ConnectionFactory();
            try (JMSContext context = 
                    connectionFactory.createContext();) {
                topic = context.createTopic(destName);
		
            
            /*
             * Create subscriber with message selector.
             * Start message delivery.
             * Send synchronize message to publisher, then wait till all
             * messages have arrived.
             * Listener displays the messages obtained.
             */
                selector = new String("NewsType = '" + whatKind + "'" + 
                                      " OR NewsType = '" + END_OF_MESSAGE_STREAM_TYPE + "'");
                System.out.println("SUBSCRIBER " + subscriberNumber 
                                    + " THREAD: selector is \"" + selector + "\"");
                msgConsumer = 
                    context.createConsumer(topic, selector, false);
                msgConsumer.setMessageListener(multipleListener);
                context.start();
                
                /*
                 * Asynchronously process appropriate news messages.
                 * Block until publisher issues a finished message.
                 */
                multipleListener.await();

		}  catch (Exception e) {
                	System.out.println("Exception occurred: " + e.toString());
                	exitcode = 1;
		}
        }

     }
    
    /**
     * Creates an array of Subscriber objects, one for each of three message  
     * types including the Finished type, and starts their threads.
     * Creates a Publisher object and starts its thread.
     * Calls the join method to wait for the threads to die.
     */
    public void run_threads() {
        final       int NUM_SUBSCRIBERS = 3;
        Subscriber  subscriberArray[] = new Subscriber[NUM_SUBSCRIBERS];

        subscriberArray[0] = new Subscriber(MESSAGE_TYPES[2], 0);
        subscriberArray[0].start();        
        subscriberArray[1] = new Subscriber(MESSAGE_TYPES[3], 1);
        subscriberArray[1].start();
        subscriberArray[2] = new Subscriber(MESSAGE_TYPES[4], 2);
        subscriberArray[2].start();    
        
        for (int i = 0; i < subscriberArray.length; i++) {
            try {
                subscriberArray[i].join();
            } catch (InterruptedException e) {}
        }
        
    }
    
}

