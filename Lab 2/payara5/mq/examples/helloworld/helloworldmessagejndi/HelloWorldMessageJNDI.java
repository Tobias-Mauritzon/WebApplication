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

/*
 * Simple message producer and consumer which obtains its ConnectionFactory
 * and Queue from Administered Objects using a JNDI lookup.
 *
 * This example requires two administered objects to be stored before it
 * will work.
 *
 * 1. A ConnectionFactory Object stored with a lookup name of 
 *    ``MyConnectionFactory''
 * 2. A Queue Object stored with a lookup name of ``MyQueue''
 *
 * The initial context factory is configured in this example to use the File 
 * Store (an alternative would be to use LDAP).  The provider url (where 
 * the file store is located) is defined by default to be C:/Temp
 * This is a Windows specific path.
 * When running this example on Unix, the first command line parameter
 * must be set to the Unix directory url where the File Store has been created.
 * e.g. file:///tmp on Solaris/Linux.
 *
 * What this means is that an MQ Object Store should be created (via imqadmin
 * or imqobjmgr) at C:/Temp.  When that store is created the following JNDI 
 * Naming Service Properties should be set (via the imqadmin or imqobjmgr):
 *
 *  java.naming.provider.url    = file:///C:/Temp
 *  java.naming.factory.initial = com.sun.jndi.fscontext.RefFSContextFactory
 *
 * If this example is run on Solaris/Linux, use the following value instead
 *
 *  java.naming.provider.url    = file:///tmp
 *
 * If you have turned off auto-creation of destinations on the MQ broker
 * then a physical Queue destination matching the destination name used
 * in the queue administered object needs to be created on the broker.
 * That is best accomplished by using the Administration Console.
 *
 * For this example to compile and run the following must be
 * in your CLASSPATH (in addition to the directory containing this example):
 *
 * jms.jar, imq.jar, fscontext.jar (these are located in IMQ_HOME/lib)
 */

import javax.naming.*;
import javax.jms.ConnectionFactory;
import javax.jms.Connection;
import javax.jms.Session;
import javax.jms.MessageProducer;
import javax.jms.MessageConsumer;
import javax.jms.Queue;
import javax.jms.Session;
import javax.jms.DeliveryMode;
import javax.jms.TextMessage;
import javax.jms.JMSException;
import java.util.*;

public class HelloWorldMessageJNDI {

    static String		def_windows_url = "file:///C:/Temp";
    static String		def_unix_url = "file:///tmp";

    String			MYCF_LOOKUP_NAME = "MyConnectionFactory";
    String			MYQUEUE_LOOKUP_NAME = "MyQueue";

    ConnectionFactory		cf;
    Connection			connection;
    Session			session;
    MessageProducer		msgProducer;
    MessageConsumer		msgConsumer;
    Queue			queue;
    TextMessage			msg,
    				rcvMsg;

    public static void main(String args[]) {
        String url = def_windows_url;

        if (args.length > 0) {
            url = args[0];
        }
        System.out.println("\nUsing "
		+ url
		+ " for Context.PROVIDER_URL");

        HelloWorldMessageJNDI simple_client = new HelloWorldMessageJNDI(url);
    }

    public HelloWorldMessageJNDI(String url) {
        Hashtable env;
        Context	ctx = null;

        env = new Hashtable();

        // Store the environment variables that tell JNDI which initial context
        // to use and where to find the provider.

        // For use with the File System JNDI Service Provider
        env.put(Context.INITIAL_CONTEXT_FACTORY, 
		"com.sun.jndi.fscontext.RefFSContextFactory");
        env.put(Context.PROVIDER_URL, url);

        try {
	    // Create the initial context.
	    ctx = new InitialContext(env);
        } catch (NamingException ne)  {
	    System.err.println("Failed to create InitialContext.");
	    System.err.println("The Context.PROVIDER_URL used/specified was: " + url);
	    System.err.println("Please make sure that the path to the above URL exists");
	    System.err.println("and matches with the objstore.attrs.java.naming.provider.url");
	    System.err.println("property value specified in the imqobjmgr command files:");
	    System.err.println("\tadd_cf.props");
	    System.err.println("\tadd_q.props");
	    System.err.println("\tdelete_cf.props");
	    System.err.println("\tdelete_q.props");
	    System.err.println("\tlist.props\n");

	    usage();

	    System.err.println("\nThe exception details:");
	    ne.printStackTrace();
	    System.exit(-1);
        }

	System.out.println("");

        try {
            // Lookup my connection factory from the admin object store.
            // The name used here here must match the lookup name 
            // used when the admin object was stored.
	    System.out.println("Looking up Connection Factory object with lookup name: "
			+ MYCF_LOOKUP_NAME);
            cf = (javax.jms.ConnectionFactory) ctx.lookup(MYCF_LOOKUP_NAME);
	    System.out.println("Connection Factory object found.");
        } catch (NamingException ne)  {
	    System.err.println("Failed to lookup Connection Factory object.");
	    System.err.println("Please make sure you have created the Connection Factory object using the command:");
	    System.err.println("\timqobjmgr -i add_cf.props");

	    System.err.println("\nThe exception details:");
	    ne.printStackTrace();
	    System.exit(-1);
        }

	System.out.println("");

        try {
            // Lookup my queue from the admin object store.
            // The name I search for here must match the lookup name used when
            // the admin object was stored.
	    System.out.println("Looking up Queue object with lookup name: "
			+ MYQUEUE_LOOKUP_NAME);
            queue = (javax.jms.Queue)ctx.lookup(MYQUEUE_LOOKUP_NAME);
	    System.out.println("Queue object found.");
        } catch (NamingException ne)  {
	    System.err.println("Failed to lookup Queue object.");
	    System.err.println("Please make sure you have created the Queue object using the command:");
	    System.err.println("\timqobjmgr -i add_q.props");

	    System.err.println("\nThe exception details:");
	    ne.printStackTrace();
	    System.exit(-1);
        }

	System.out.println("");

        try {
	    System.out.println("Creating connection to broker.");
            connection = cf.createConnection();
	    System.out.println("Connection to broker created.");
        } catch (JMSException e)  {
	    System.err.println("Failed to create connection.");
	    System.err.println("Please make sure that the broker was started.");

	    System.err.println("\nThe exception details:");
	    e.printStackTrace();
	    System.exit(-1);
        }

	System.out.println("");

        try {
            session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);

            // Create the MessageProducer and MessageConsumer 
            msgProducer = session.createProducer(queue); 
            msgConsumer = session.createConsumer(queue); 

            // Tell the provider to start sending messages.
            connection.start();

            msg = session.createTextMessage("Hello World");

            // Publish the message
            System.out.println("Publishing a message to Queue: " + queue.getQueueName());
            msgProducer.send(msg, DeliveryMode.NON_PERSISTENT, 4, 0);

            // Wait for it to be sent back.
            rcvMsg = (TextMessage) msgConsumer.receive();

            System.out.println("Received the following message: " + rcvMsg.getText());

            connection.close();

        } catch (JMSException e)  {
	    System.err.println("JMS Exception: " + e);
	    e.printStackTrace();
	    System.exit(-1);
        }
    }

    private static void usage()  {
        System.err.println("Usage: " +
            "\tjava HelloWorldMessageJNDI [Context.PROVIDER_URL]\n" +
            "\nOn Unix:\n\tjava HelloWorldMessageJNDI " + def_unix_url +
            "\nOn Windows:\n\tjava HelloWorldMessageJNDI " + def_windows_url);
    }

}

