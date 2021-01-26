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

import javax.jms.*;

/**
 * The AsynchTopicExample class demonstrates the use of a message listener in 
 * the publish/subscribe model.  The producer publishes several messages, and
 * the consumer reads them asynchronously.
 * <p>
 * The program contains a MultipleProducer class, an AsynchConsumer class
 * with a listener class, a main method, and a method that runs the consumer
 * and producer threads.
 * <p>
 * Specify a topic name on the command line when you run the program.  The 
 * program also uses a queue named "controlQueue", which should be created  
 * before you run the program.
 */
public class AsynchTopicExample {
    final String  CONTROL_QUEUE = "controlQueue";
    String        topicName = null;
    int           exitResult = 0;

    /**
     * The AsynchConsumer class fetches several messages from a topic 
     * asynchronously, using a message listener, TextListener.
     */
    public class AsynchConsumer extends Thread {

        /**
         * The TextListener class implements the MessageListener interface by 
         * defining an onMessage method for the AsynchConsumer class.
         */
        private class TextListener implements MessageListener {
            final SampleUtilities.DoneLatch  monitor =
                new SampleUtilities.DoneLatch();

            /**
             * Casts the message to a TextMessage and displays its text.
             * A non-text message is interpreted as the end of the message 
             * stream, and the message listener sets its monitor state to all 
             * done processing messages.
             *
             * @param message	the incoming message
             */
            public void onMessage(Message message) {
                if (message instanceof TextMessage) {
                    TextMessage  msg = (TextMessage) message;

                    try {
                        System.out.println("CONSUMER THREAD: Reading message: " 
                                           + msg.getText());
                    } catch (JMSException e) {
                        System.out.println("Exception in onMessage(): " 
                                           + e.toString());
                    }
                } else {
                    monitor.allDone();
                }
            }
        }

        /**
         * Runs the thread.
         */
        public void run() {
            ConnectionFactory    connectionFactory = null;
            Connection           connection = null;
            Session              session = null;
            Topic                topic = null;
            MessageConsumer      msgConsumer = null;
            TextListener         topicListener = null;

            try {
                connectionFactory = 
                    SampleUtilities.getConnectionFactory();
                connection = 
                    connectionFactory.createConnection();
                session = connection.createSession(false, 
                    Session.AUTO_ACKNOWLEDGE);
                topic = SampleUtilities.getTopic(topicName, session);
            } catch (Exception e) {
                System.out.println("Connection problem: " + e.toString());
                if (connection != null) {
                    try {
                        connection.close();
                    } catch (JMSException ee) {}
                }
    	        System.exit(1);
            } 

            /*
             * Create consumer.
             * Register message listener (TextListener).
             * Start message delivery.
             * Send synchronize message to producer, then wait till all
             * messages have arrived.
             * Listener displays the messages obtained.
             */
            try {
                msgConsumer = session.createConsumer(topic);
                topicListener = new TextListener();
                msgConsumer.setMessageListener(topicListener);
                connection.start();
                
                // Let producer know that consumer is ready.
                try {
                    SampleUtilities.sendSynchronizeMessage("CONSUMER THREAD: ",
                                                            CONTROL_QUEUE);
                } catch (Exception e) {
                    System.out.println("Queue probably missing: " + e.toString());
                    if (connection != null) {
                        try {
                            connection.close();
                        } catch (JMSException ee) {}
                    }
    	            System.exit(1);
    	        }

                /*
                 * Asynchronously process messages.
                 * Block until producer issues a control message indicating
                 * end of publish stream.
                 */
                topicListener.monitor.waitTillDone();
            } catch (JMSException e) {
                System.out.println("Exception occurred: " + e.toString());
                exitResult = 1;
            } finally {
                if (connection != null) {
                    try {
                        connection.close();
                    } catch (JMSException e) {
                        exitResult = 1;
                    }
                }
            }
        }	    
    }

    /**
     * The MultipleProducer class publishes several message to a topic. 
     */
    public class MultipleProducer extends Thread {

        /**
         * Runs the thread.
         */
        public void run() {
            ConnectionFactory    connectionFactory = null;
            Connection           connection = null;
            Session              session = null;
            Topic                topic = null;
            MessageProducer      msgProducer = null;
            TextMessage          message = null;
            final int            NUMMSGS = 20;
            final String         MSG_TEXT = new String("Here is a message");

            try {
                connectionFactory = 
                    SampleUtilities.getConnectionFactory();
                connection = 
                    connectionFactory.createConnection();
                session = connection.createSession(false, 
                    Session.AUTO_ACKNOWLEDGE);
                topic = SampleUtilities.getTopic(topicName, session);
            } catch (Exception e) {
                System.out.println("Connection problem: " + e.toString());
                if (connection != null) {
                    try {
                        connection.close();
                    } catch (JMSException ee) {}
                }
    	        System.exit(1);
            } 

            /*
             * After synchronizing with consumer, create producer.
             * Create text message.
             * Send messages, varying text slightly.
             * Send end-of-messages message.
             * Finally, close connection.
             */
            try {
                /*
                 * Synchronize with consumer.  Wait for message indicating 
                 * that consumer is ready to receive messages.
                 */
                try {
                    SampleUtilities.receiveSynchronizeMessages("PRODUCER THREAD: ",
                                                              CONTROL_QUEUE, 1);
                } catch (Exception e) {
                    System.out.println("Queue probably missing: " + e.toString());
                    if (connection != null) {
                        try {
                            connection.close();
                        } catch (JMSException ee) {}
                    }
    	            System.exit(1);
    	        }
                
                msgProducer = session.createProducer(topic);
                message = session.createTextMessage();
                for (int i = 0; i < NUMMSGS; i++) {
                    message.setText(MSG_TEXT + " " + (i + 1));
                    System.out.println("PRODUCER THREAD: Publishing message: " 
                        + message.getText());
                    msgProducer.send(message);
                }

                // Send a non-text control message indicating end of messages.
                msgProducer.send(session.createMessage());
            } catch (JMSException e) {
                System.out.println("Exception occurred: " + e.toString());
                exitResult = 1;
            } finally {
                if (connection != null) {
                    try {
                        connection.close();
                    } catch (JMSException e) {
                        exitResult = 1;
                    }
                }
            }
        }
    }
    
    /**
     * Instantiates the consumer and producer classes and starts their
     * threads.
     * Calls the join method to wait for the threads to die.
     * <p>
     * It is essential to start the consumer before starting the producer.
     * In the publish/subscribe model, a consumer can ordinarily receive only 
     * messages published while it is active.
     */
    public void run_threads() {
        AsynchConsumer   asynchConsumer = new AsynchConsumer();
        MultipleProducer  multipleProducer = new MultipleProducer();

        multipleProducer.start();
        asynchConsumer.start();
        try {
            asynchConsumer.join();
            multipleProducer.join();
        } catch (InterruptedException e) {}
    }

    /**
     * Reads the topic name from the command line, then calls the
     * run_threads method to execute the program threads.
     *
     * @param args	the topic used by the example
     */
    public static void main(String[] args) {
        AsynchTopicExample  ate = new AsynchTopicExample();
        
        if (args.length != 1) {
    	    System.out.println("Usage: java AsynchTopicExample <topic_name>");
    	    System.exit(1);
    	}
        ate.topicName = new String(args[0]);
        System.out.println("Topic name is " + ate.topicName);

    	ate.run_threads();
    	SampleUtilities.exit(ate.exitResult);
    }
}
