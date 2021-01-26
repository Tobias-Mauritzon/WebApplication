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

/**
 * The SelectorProducerExample class demonstrates the use of multiple 
 * subscribers and message selectors.
 * <p>
 * The SelectorProducerExample class publishes 30 messages of 6 different types, randomly
 * selected.
 * <p>
 * Specify a topic name on the command line when you run the program.
 */
public class SelectorProducerExample {

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
    * Main method.
    *
    * @param args      the topic used by the example
    */
    public static void main(String args[]) {


               if ( args.length < 1 ) {
                  System.out.println("Usage: java SelectorProducerExample <topic_name>");
                  System.exit(1);
                }

                SelectorProducerExample sendMsg = new SelectorProducerExample();
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
        }

        /**
         * Chooses a message type by using the random number generator
         * found in java.util.
         *
         * @return	the String representing the message type
         */
        public String chooseType() {
           int     whichMsg;
           Random  rgen = new Random();
           
           whichMsg = rgen.nextInt(ARRSIZE);
           return MESSAGE_TYPES[whichMsg];
        }
        
	/**
         * Send messages to the topic destination
         *
         * @param  none
         * @throws JMSException
         */
        public void sendmsgs() throws JMSException {
            ConnectionFactory    connectionFactory = null;
            Topic                topic = null;
            JMSProducer          msgProducer = null;
            TextMessage          message = null;
            int                  numMsgs = ARRSIZE * 5;
            String               messageType = null;

            connectionFactory = 
                    new com.sun.messaging.ConnectionFactory();
	    // JMSContext will be closed automatically with the try-with-resources block
            try ( JMSContext context = 
                    connectionFactory.createContext();) {
            topic = context.createTopic(destName);
            
            /*
             * Create producer.
             * Create and send news messages.
             */
             msgProducer = context.createProducer();
             message = context.createTextMessage();
             for (int i = 0; i < numMsgs; i++) {
                 messageType = chooseType();
                 message.setStringProperty("NewsType", messageType);
                 message.setText("Item " + i + ": " + messageType);
                 System.out.println("PUBLISHER THREAD: Setting message text to: " 
                        + message.getText());
                 msgProducer.send(topic,message);
              }
	      message.setStringProperty("NewsType", END_OF_MESSAGE_STREAM_TYPE);
              message.setText("That's all the news for today.");
              System.out.println("PUBLISHER THREAD: Setting message text to: "
                   + message.getText());
              msgProducer.send(topic,message);
          }
	}	
}
