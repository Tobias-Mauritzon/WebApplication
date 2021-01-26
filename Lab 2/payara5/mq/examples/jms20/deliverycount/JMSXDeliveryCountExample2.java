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
import com.sun.messaging.jmq.jmsclient.ConnectionMetaDataImpl;

/**
 * The JMSXDeliveryCountExample2.class checks the JMSXDeliverycount on
 * redelivered transacted message.
 * Specify a queue name on the command line when you run
 * the program.
 *
 */

public class JMSXDeliveryCountExample2 {

        private String destName          = null;
        static int exitcode = 0;

	/**
        * Main method.
        *
        * @param args      the queue used by the example
        */
        public static void main(String args[]) {

		if ( args.length < 1 ) {
                  System.out.println("Usage: java JMSXDeliveryCountExample1 <queue_name>");
                  System.exit(1);
                }
                JMSXDeliveryCountExample2 deliveryCountExample2 = new JMSXDeliveryCountExample2();
		deliveryCountExample2.parseArgs(args);
                try {
                        deliveryCountExample2.runTest();
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
                System.out.println("Queue name is " + destName);
        }


    	/*
     	* Receive msg with in txn
     	* Rollback txn but don't close the connection
     	* Receive the redelivered msg again.
     	*
     	* @param  none
     	* @throws JMSException
     	*/
    	protected void runTest() throws JMSException {
		ConnectionFactory connectionFactory = new com.sun.messaging.ConnectionFactory();
        	String uniqueID = Long.toString(System.currentTimeMillis());

        	// send 1 messages to the queue
        	JMSContext context = connectionFactory.createContext(JMSContext.SESSION_TRANSACTED);
        	JMSProducer producer = context.createProducer();
        	producer.send(context.createQueue(destName), context.createTextMessage(uniqueID));
		System.out.println("Sent message to queue ");
		context.commit();
		context.close();

        	// receive 1 message from the queue and rollback
	
		context = connectionFactory.createContext(JMSContext.SESSION_TRANSACTED);
		context.start();
        	JMSConsumer consumer = context.createConsumer(context.createQueue(destName));
        	TextMessage textMessage = (TextMessage) consumer.receive(10000);
		if ( textMessage != null) {
			System.out.println("Message Received..");
		} else {
			System.out.println("Message not Received..");
			exitcode=1;
			return;
		}
	     
        	String payload = textMessage.getText();
	
        	System.out.println("Message received : "+payload);

		// Check the JMSDeliveryCount for the message
        	int deliveryCount = textMessage.getIntProperty(ConnectionMetaDataImpl.JMSXDeliveryCount);
        	System.out.println("JMSXDeliveryCount for the received message : " + deliveryCount);
        	context.rollback();
		System.out.println("Rollback the transaction");

		System.out.println("Try to receive the Message again");
        	textMessage = (TextMessage) consumer.receive(10000);
        	payload = textMessage.getText();
        	System.out.println("Message redelivered : " + payload);

		// Check the JMSXDeliveryCount for the redelivered message
        	deliveryCount = textMessage.getIntProperty(ConnectionMetaDataImpl.JMSXDeliveryCount);
        	System.out.println("JMSXDeliveryCount for the redelivered message : " +deliveryCount);

		// Commit the transaction
        	context.commit();

        	context.close();
    	}

}
