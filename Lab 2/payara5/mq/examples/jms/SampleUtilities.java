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

import javax.naming.*;
import javax.jms.*;

/**
 * Utility class for JMS sample programs.
 * <p>
 * Set the <code>USE_JNDI</code> variable to true or false depending on whether
 * your provider uses JNDI. 
 * <p>
 * Contains the following methods: 
 * <ul> 
 *   <li> getConnectionFactory
 *   <li> getQueueConnectionFactory
 *   <li> getTopicConnectionFactory
 *   <li> getQueue
 *   <li> getTopic
 *   <li> jndiLookup
 *   <li> exit
 *   <li> receiveSynchronizeMessages
 *   <li> sendSynchronizeMessages
 * </ul>
 *
 * Also contains the class DoneLatch, which contains the following methods:
 * <ul> 
 *   <li> waitTillDone
 *   <li> allDone
 * </ul>
 */
public class SampleUtilities {
    /*
     Define the System Property "USE_JNDI" true to use JNDI lookup()
     e.g. $JAVA -DUSE_JNDI=true ....
    */
    public static final boolean USE_JNDI = Boolean.getBoolean("USE_JNDI");
    /*
     The prefix to use for JNDI lookup names
    */
    public static final String jndiNamePrefix = "cn=";

    public static final String  CONFAC = "ConnectionFactory";
    public static final String  QUEUECONFAC = "QueueConnectionFactory";
    public static final String  TOPICCONFAC = "TopicConnectionFactory";
    private static Context      jndiContext = null;
    
    /**
     * Returns a ConnectionFactory object.
     * If provider uses JNDI, serves as a wrapper around jndiLookup method.
     * If provider does not use JNDI, substitute provider-specific code here.
     *
     * @return		a ConnectionFactory object
     * @throws		javax.naming.NamingException (or other exception)
     *                   if name cannot be found
     */
    public static javax.jms.ConnectionFactory getConnectionFactory() 
      throws Exception {
        if (USE_JNDI) {
            return (javax.jms.ConnectionFactory) jndiLookup(CONFAC);
        } else {
            // return new provider-specific ConnectionFactory
            return new com.sun.messaging.ConnectionFactory();
        }
    }

    /**
     * Returns a QueueConnectionFactory object.
     * If provider uses JNDI, serves as a wrapper around jndiLookup method. 
     * If provider does not use JNDI, substitute provider-specific code here.
     *
     * @return		a QueueConnectionFactory object
     * @throws		javax.naming.NamingException (or other exception)
     *                   if name cannot be found
     */
    public static javax.jms.QueueConnectionFactory getQueueConnectionFactory() 
      throws Exception {
        if (USE_JNDI) {
            return (javax.jms.QueueConnectionFactory) jndiLookup(QUEUECONFAC);
        } else {
            // return new provider-specific QueueConnectionFactory
            return new com.sun.messaging.QueueConnectionFactory();
        }
    }
    
    /**
     * Returns a TopicConnectionFactory object.
     * If provider uses JNDI, serves as a wrapper around jndiLookup method.
     * If provider does not use JNDI, substitute provider-specific code here.
     *
     * @return		a TopicConnectionFactory object
     * @throws		javax.naming.NamingException (or other exception)
     *                   if name cannot be found
     */
    public static javax.jms.TopicConnectionFactory getTopicConnectionFactory() 
      throws Exception {
        if (USE_JNDI) {
            return (javax.jms.TopicConnectionFactory) jndiLookup(TOPICCONFAC);
        } else {
            // return new provider-specific TopicConnectionFactory
            return new com.sun.messaging.TopicConnectionFactory();
        }
    }
    
    /**
     * Returns a Queue object.
     * If provider uses JNDI, serves as a wrapper around jndiLookup method.
     * If provider does not use JNDI, substitute provider-specific code here.
     *
     * @param name      String specifying queue name
     * @param session   a QueueSession object
     *
     * @return		a Queue object
     * @throws		javax.naming.NamingException (or other exception)
     *                   if name cannot be found
     */
    public static javax.jms.Queue getQueue(String name, 
                                           javax.jms.Session session) 
      throws Exception {
        if (USE_JNDI) {
            return (javax.jms.Queue) jndiLookup(name);
        } else {
            return session.createQueue(name);
        }
    }
    
    /**
     * Returns a Topic object.
     * If provider uses JNDI, serves as a wrapper around jndiLookup method.
     * If provider does not use JNDI, substitute provider-specific code here.
     *
     * @param name      String specifying topic name
     * @param session   a TopicSession object
     *
     * @return		a Topic object
     * @throws		javax.naming.NamingException (or other exception)
     *                   if name cannot be found
     */
    public static javax.jms.Topic getTopic(String name, 
                                           javax.jms.Session session) 
      throws Exception {
        if (USE_JNDI) {
            return (javax.jms.Topic) jndiLookup(name);
        } else {
            return session.createTopic(name);
        }
    }
    
    /**
     * Creates a JNDI InitialContext object if none exists yet.  Then looks up 
     * the string argument and returns the associated object.
     *
     * @param name	the name of the object to be looked up
     *
     * @return		the object bound to <code>name</code>
     * @throws		javax.naming.NamingException if name cannot be found
     */
    public static Object jndiLookup(String name) throws NamingException {
        Object    obj = null;

        if (jndiContext == null) {
            try {
                jndiContext = new InitialContext();
            } catch (NamingException e) {
                System.out.println("Could not create JNDI context: " + 
                    e.toString());
                throw e;
            }
        }
        try {
           obj = jndiContext.lookup(jndiNamePrefix + name);
        } catch (NamingException e) {
            System.out.println("JNDI lookup failed for:" + name + ": " + e.toString());
            throw e;
        }
        return obj;
    }
   
    /**
     * Calls System.exit().
     * 
     * @param result	The exit result; 0 indicates no errors
     */
    public static void exit(int result) {
        System.exit(result);
    }
   
    /**
     * Wait for 'count' messages on controlQueue before continuing.  Called by
     * a publisher to make sure that subscribers have started before it begins
     * publishing messages.
     * <p>
     * If controlQueue doesn't exist, the method throws an exception.
     *
     * @param prefix	prefix (publisher or subscriber) to be displayed
     * @param controlQueueName	name of control queue 
     * @param count	number of messages to receive
     */
    public static void receiveSynchronizeMessages(String prefix,
                                                  String controlQueueName, 
                                                  int count) 
      throws Exception {
        QueueConnectionFactory  queueConnectionFactory = null;
        QueueConnection         queueConnection = null;
        QueueSession            queueSession = null;
        Queue                   controlQueue = null;
        QueueReceiver           queueReceiver = null;

        try {
            queueConnectionFactory = 
                SampleUtilities.getQueueConnectionFactory();
            queueConnection = queueConnectionFactory.createQueueConnection();
            queueSession = queueConnection.createQueueSession(false, 
                                                 Session.AUTO_ACKNOWLEDGE);
            controlQueue = getQueue(controlQueueName, queueSession);
            queueConnection.start();
        } catch (Exception e) {
            System.out.println("Connection problem: " + e.toString());
            if (queueConnection != null) {
                try {
                    queueConnection.close();
                } catch (JMSException ee) {}
            }
            throw e;
        } 

        try {
            System.out.println(prefix + "Receiving synchronize messages from "
                               + controlQueueName + "; count = " + count);
            queueReceiver = queueSession.createReceiver(controlQueue);
            while (count > 0) {
                queueReceiver.receive();
                count--;
                System.out.println(prefix 
                                   + "Received synchronize message; expect " 
                                   + count + " more");
            }
        } catch (JMSException e) {
            System.out.println("Exception occurred: " + e.toString());
            throw e;
        } finally {
            if (queueConnection != null) {
                try {
                    queueConnection.close();
                } catch (JMSException e) {}
            }
        }
    }

    /**
     * Send a message to controlQueue.  Called by a subscriber to notify a
     * publisher that it is ready to receive messages.
     * <p>
     * If controlQueue doesn't exist, the method throws an exception.
     *
     * @param prefix	prefix (publisher or subscriber) to be displayed
     * @param controlQueueName	name of control queue
     */
    public static void sendSynchronizeMessage(String prefix,
                                              String controlQueueName) 
      throws Exception {
        QueueConnectionFactory  queueConnectionFactory = null;
        QueueConnection         queueConnection = null;
        QueueSession            queueSession = null;
        Queue                   controlQueue = null;
        QueueSender             queueSender = null;
        TextMessage             message = null;

        try {
            queueConnectionFactory = 
                SampleUtilities.getQueueConnectionFactory();
            queueConnection = queueConnectionFactory.createQueueConnection();
            queueSession = queueConnection.createQueueSession(false,
                                                 Session.AUTO_ACKNOWLEDGE);
            controlQueue = getQueue(controlQueueName, queueSession);
        } catch (Exception e) {
            System.out.println("Connection problem: " + e.toString());
            if (queueConnection != null) {
                try {
                    queueConnection.close();
                } catch (JMSException ee) {}
            }
            throw e;
        } 

        try {
            queueSender = queueSession.createSender(controlQueue);
            message = queueSession.createTextMessage();
            message.setText("synchronize");
            System.out.println(prefix + "Sending synchronize message to " 
                               + controlQueueName);
            queueSender.send(message);
        } catch (JMSException e) {
            System.out.println("Exception occurred: " + e.toString());
            throw e;
        } finally {
            if (queueConnection != null) {
                try {
                    queueConnection.close();
                } catch (JMSException e) {}
            }
        }
    }

    /**
     * Monitor class for asynchronous examples.  Producer signals end of
     * message stream; listener calls allDone() to notify consumer that the 
     * signal has arrived, while consumer calls waitTillDone() to wait for this 
     * notification.
     */
    static public class DoneLatch {
        boolean  done = false;

        /**
         * Waits until done is set to true.
         */
        public void waitTillDone() {
            synchronized (this) {
                while (! done) {
                    try {
                        this.wait();
                    } catch (InterruptedException ie) {}
                }
            }
        }
        
        /**
         * Sets done to true.
         */
        public void allDone() {
            synchronized (this) {
                done = true;
                this.notify();
            }
        }
    }
}
