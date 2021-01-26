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
import java.util.concurrent.CountDownLatch;


/**
 * The AsyncTopicProducerExample.class sends several messages to a topic using asynchronous send.
 * <p>
 * Run this program in conjunction with AsyncTopicConsumerExample.
 * Specify a topic name on the command line when you run
 * the program.  By default, the program sends one message.  Specify a number
 * after the topic name to send that number of messages.
 */


public class AsyncTopicProducerExample {


	private String destName          = null;
        private int noOfMsgs;
        final String MSG_TEXT            = new String("Message");
	static int exitcode = 0;

	/**
        * Main method.
        *
        * @param args      the topic used by the example and, optionally, the
        *                   number of messages to send
        */
	public static void main(String args[]) {


	       if ( (args.length < 1) || (args.length > 2) ) {
                  System.out.println("Usage: java AsyncTopicProducerExample <topic_name> [<number_of_messages>]");
                  System.exit(1);
        	}

		AsyncTopicProducerExample sendMsg = new AsyncTopicProducerExample();
		sendMsg.parseArgs(args);
		try {
			// Send messages to topic
			sendMsg.sendmsgs();
		}catch(JMSException ex) {
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
        	if (args.length == 2){
            	   noOfMsgs = (new Integer(args[1])).intValue();
        	} else {
            	   noOfMsgs = 1;
        	}

	}

	 /**
         * Send messages asynchronously to the topic destination
         *
         * @param  none
         * @throws JMSException
         */
	
	public void sendmsgs() throws JMSException {

		ConnectionFactory cf = new com.sun.messaging.ConnectionFactory();

		// JMSContext will be closed automatically with the try-with-resources block
		try (JMSContext jmsContext = cf.createContext(JMSContext.AUTO_ACKNOWLEDGE);) {
		    System.out.println("Created jms context successfully");

		    // Create topic
		    Topic topic = jmsContext.createTopic(destName);
		    System.out.println("Created topic successfully");

		    // Create producer
		    JMSProducer producer = jmsContext.createProducer();
		    MyCompletionListener completionListener = new MyCompletionListener(noOfMsgs);

		    // Set completionlistener on producer
                    producer.setAsync(completionListener);

		    /*
         	    * Create text message.
         	    * Send messages, varying text slightly.
         	    */
		    for (int i = 0; i < noOfMsgs; i++) {
                        String data = MSG_TEXT + " " + (i + 1);
			//System.out.println("Async send started for message "+data);
                        producer.send(topic,data);
			System.out.println("Async send started for message "+data);

            	    }
		    
		    completionListener.await();
		    System.out.println("All async sends completed");
		}
	}

	 /**
         * The MyCompletionListener class implements the CompletionListener interface by
         * defining an onCompletion method for the AsyncTopicProduceExampler class.
         */
	 static class MyCompletionListener implements CompletionListener {

                private CountDownLatch countDownLatch;
		private int msgsSend;
                private int totalMsgs;

                MyCompletionListener(int noOfMsgs) {
		   totalMsgs = noOfMsgs;
                   countDownLatch = new CountDownLatch(1);
                }

		/**
                * Casts the message to a TextMessage and displays its text.
                * The completion listener sets its CounDownLatch to all
                * done sending messages.
                *
                * @param message   the sent message
                */
                public void onCompletion(Message message) {

			TextMessage  msg = (TextMessage) message;
			try {
			    System.out.println("Async send completed for message : "+msg.getText());
			    msgsSend ++;
			     if ( msgsSend == totalMsgs) {
                                countDownLatch.countDown();
                             }
                	}catch(JMSException ex){
			     System.out.println("Exception in onComplete(): " + ex.toString());
                             countDownLatch.countDown();
			}
                }
	
		/**
		* Notifies the user of Exception when sending messages asynchronously
		*
		* @param message    the  sent message
		* @param exception  the  exception thats thrown
		*/	
                public void onException(Message message, Exception exception) {
                        exception.printStackTrace();
			exitcode=1;
                        countDownLatch.countDown();
                }

		public void await() {
                  try {
                          countDownLatch.await();
                  } catch (InterruptedException e) {
                          e.printStackTrace();
                  }
                }

	}

}
