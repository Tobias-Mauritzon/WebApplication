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

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.Map;

import javax.jms.*;

/**
 * The MessageGetBodyExample.class uses getBody() to extract the body 
 * directly from a Message.
 */

public class MessageGetBodyExample {

	static int exitcode = 0;

	public static void main(String args[]) {

                MessageGetBodyExample msgGetBodyExample = new MessageGetBodyExample();
                try {
                        msgGetBodyExample.runTest();
                }catch(JMSException ex) {
                        ex.printStackTrace();
                        exitcode = 1;
                }
                System.exit(exitcode);
        }


	/**
	 * Message method getBody
	 * 
	 * @param none
	 * @throws JMSException
	 */
	protected void runTest() throws JMSException {
		ConnectionFactory connectionFactory = new com.sun.messaging.ConnectionFactory();
		getBodyForTextMessage(connectionFactory);
		getBodyForObjectMessage(connectionFactory);
		getBodyForBytesMessage(connectionFactory);
		getBodyForMapMessage(connectionFactory);
		getBodyForMessage(connectionFactory);
	}

	/**
	 * Message method getBody with a ObjectMessage
	 * 
	 * @param connectionFactory
	 * @throws JMSException
	 */
	private void getBodyForObjectMessage(ConnectionFactory connectionFactory) throws JMSException {
		String uniqueID = Long.toString(System.currentTimeMillis());
		JMSContext context = connectionFactory.createContext();
                Queue queue1 = (Queue) context.createQueue("queue1");
                ObjectMessage sendMessage = context.createObjectMessage(new MyObject(uniqueID));
                // Send Object Message
		System.out.println("");
                System.out.println("Sending Object Message..");
                context.createProducer().send(queue1, sendMessage);
                JMSConsumer consumer = context.createConsumer(queue1);
                // Receive Object Message
                ObjectMessage objectMessage = (ObjectMessage) consumer.receive(1000);

		if ( objectMessage != null) {
                        System.out.println("Object Message Received..");
                } else {

                        System.out.println("Object Message not Received..");
                        exitcode=1;
			return;
                }

		{
			// Get body as a MyObject
			MyObject body = objectMessage.getBody(MyObject.class);
			System.out.println("Get body as a MyObject from Object Message : " + body.getValue());
		}

		{
			// get body as a Serializable (superinterface of MyObject)
			Serializable body = objectMessage.getBody(Serializable.class);
			System.out.println("Get body as a Serializable from Object Message : " + ((MyObject) body).getValue());
		}

		{
			// get body as a Object (superinterface of MyObject)
			MyObject body = (MyObject) objectMessage.getBody(Object.class);
			System.out.println("Get body as a Object from Object Message : " + body.getValue());
		}

	}


	/**
	 * Test Message method getBody with a TextMessage
	 * 
	 * @param connectionFactory
	 * @throws JMSException
	 */
	private void getBodyForTextMessage(ConnectionFactory connectionFactory) throws JMSException {

		String uniqueID = Long.toString(System.currentTimeMillis());
		JMSContext context = connectionFactory.createContext();
		Queue queue2 = (Queue) context.createQueue("queue2");
		TextMessage sendMessage = context.createTextMessage(uniqueID);
		// Send Text Message
		System.out.println("");
		System.out.println("Sending Text Message..");
		context.createProducer().send(queue2, sendMessage);
		JMSConsumer consumer = context.createConsumer(queue2);
		// Receive Text Message
		TextMessage textMessage = (TextMessage) consumer.receive(2000);

		if ( textMessage != null) {
		   	System.out.println("Text Message Received..");
		} else {

			System.out.println("Text Message not Received..");
			exitcode=1;
			return;
		}
			//Get body as a String
		{
			String body = textMessage.getBody(String.class);
			System.out.println("Get body as a String from Text Message: " + body);
		}

		{
			// get body as a CharSequence (superinterface of String)
			CharSequence body = textMessage.getBody(CharSequence.class);
			System.out.println("Get body as a CharSequence from Text Message : " + (String)body);
		}

		{
			// get body as a Serializable (superinterface of String)
			Serializable body = textMessage.getBody(Serializable.class);
			System.out.println("Get body as a Serializable from Text Message : " + (String)body);
		}
		
		{
			// Get body as a Object (superinterface of String)
			Object body = textMessage.getBody(Object.class);
			System.out.println("Get body as a String from Text Message : " + (String)body);
		}

	}
	
	/**
	 * Test Message method getBody with a (plain) Message
	 * 
	 * @param connectionFactory
	 * @throws JMSException
	 */
	private void getBodyForMessage(ConnectionFactory connectionFactory) throws JMSException {

		JMSContext context = connectionFactory.createContext();
                Queue queue3 = (Queue) context.createQueue("queue3");
                Message sendMessage = context.createMessage();
                // Send plain Message
		System.out.println("");
                System.out.println("Sending plain Message without body..");
                context.createProducer().send(queue3, sendMessage);
                JMSConsumer consumer = context.createConsumer(queue3);
                // Receive plain Message
                Message message = consumer.receive(1000);
		System.out.println("Plain message received..");

		{
			// There is no body, get body as a Float (invalid type, but should
			// still return null)
			Float body = message.getBody(Float.class);
			System.out.println("There is no body in the Message , get body as a Float (invalid type), but should still return null : " + body);
		}
	}
	

	/**
	 * Message method getBody with a MapMessage
	 * 
	 * @param connectionFactory
	 * @throws JMSException
	 */
	private void getBodyForMapMessage(ConnectionFactory connectionFactory) throws JMSException {
		String uniqueID = Long.toString(System.currentTimeMillis());
		JMSContext context = connectionFactory.createContext();
                Queue queue4 = (Queue) context.createQueue("queue4");
                MapMessage sendMessage = context.createMapMessage();
		sendMessage.setString("id", uniqueID);
                sendMessage.setInt("foo", 123);
                sendMessage.setBoolean("bar", false);

                // Send Map Message
		System.out.println("");
                System.out.println("Sending Map Message..");
                context.createProducer().send(queue4, sendMessage);
                JMSConsumer consumer = context.createConsumer(queue4);
                // Receive Map Message
                MapMessage mapMessage = (MapMessage) consumer.receive(1000);
		if ( mapMessage != null) {
                        System.out.println("Map Message Received..");
                } else {

                        System.out.println("Map Message not Received..");
                        exitcode=1;
			return;
                }

		{
			// Get body as a Map
			Map body = mapMessage.getBody(Map.class);
			System.out.println("Get body as a Map from Map Message");
			System.out.println("Get body as a Map (id) : " + body.get("id"));
			System.out.println("Get body as a Map (foo) : " + body.get("foo"));
			System.out.println("Get body as a Map (bar) : " + body.get("bar"));
		}
		
		{
			// Get body as an Object (superinterface of Map)
			Map body = (Map) mapMessage.getBody(Object.class);
			System.out.println("Get body as an Object from Map Message");
			System.out.println("Get body as a Map (id) : " + body.get("id"));
			System.out.println("Get body as a Map (foo) : " + body.get("foo"));
			System.out.println("Get body as a Map (bar) : " + body.get("bar"));
		}

	}

	/**
	 * Message method getBody with a BytesMessage
	 * 
	 * @param connectionFactory
	 * @throws JMSException
	 */
	private void getBodyForBytesMessage(ConnectionFactory connectionFactory) throws JMSException {
		JMSContext context = connectionFactory.createContext();
                Queue queue5 = (Queue) context.createQueue("queue5");
                BytesMessage sendMessage = context.createBytesMessage();
		byte[] bytes = new byte[4];
                bytes[0] = 5;
                bytes[1] = 6;
                bytes[2] = 7;
                bytes[3] = 8;
                sendMessage.writeBytes(bytes);
		// Send Bytes Message
		System.out.println("");
                System.out.println("Sending Bytes Message..");
                context.createProducer().send(queue5, sendMessage);
                JMSConsumer consumer = context.createConsumer(queue5);
                // Receive Bytes Message
                BytesMessage bytesMessage = (BytesMessage) consumer.receive(1000);
                if ( bytesMessage != null) {
                        System.out.println("Bytes Message Received..");
                } else {

                        System.out.println("Bytes Message not Received..");
                        exitcode=1;
			return;
                }


		{
			// leave message in write-only mode - this checks that the message is reset before use
			System.out.println("Get body as bytes[] from Bytes Message");
			byte[] body1 = bytesMessage.getBody(byte[].class);
			System.out.println("Get body as a byte[](length) : " +body1.length);
			System.out.println("Get body as a byte[](0) : " + body1[0]);
			System.out.println("Get body as a byte[](1) : " + body1[1]);
			System.out.println("Get body as a byte[](2) : " + body1[2]);
			System.out.println("Get body as a byte[](3) : " + body1[3]);
		}
		
		{
			// Get body as a Object - superinterface of byte[] 
			System.out.println("Get body as a Object - superinterface of byte[]");
			Object body = bytesMessage.getBody(Object.class);
			byte[] bodyAsBytes = (byte[])body;
			System.out.println("Get body as a byte[](length) : "+bodyAsBytes.length);
			System.out.println("Get body as a byte[](0) : "+bodyAsBytes[0]);
			System.out.println("Get body as a byte[](1) : "+bodyAsBytes[1]);
			System.out.println("Get body as a byte[](2) : "+bodyAsBytes[2]);
			System.out.println("Get body as a byte[](3)) : "+bodyAsBytes[3]);
		}

	}

	static class MyObject implements Serializable {

		private static final long serialVersionUID = -7277008188156855782L;
		private String value;

		MyObject(String value) {
			this.value = value;
		}

		String getValue() {
			return value;
		}

	}

}
