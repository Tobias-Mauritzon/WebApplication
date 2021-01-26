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

import java.io.*;
import javax.jms.*;
import org.w3c.dom.Document;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

/**
 * The XMLMessageExample class consists a main method which creates a
 * Producer and a Consumer objects (Producer and Consumer classes are defined
 * in this file).  The Producer reads a XML file to a StreamMessage and sends
 * it to a queue.  The Consumer receives the StreamMessage and read the XML
 * document from it then using JAXP API to parse the XML document into a
 * DOM object.
 * <p>
 * The command line options for running this program include (in order)
 * a Oracle GlassFish(tm) Server Message Queue Queue name
 * an XML filename (sample.xml and its DTD file - sample.dtd are provided)
 * an optional system ID URL (for use by the XML parser to resolve any
 *                            external entity URI)
 *
 */
public class XMLMessageExample {

    /**
     * Main method.
     *
     * @param args  the queue used by the example
     *              the xml filename used by the example
     *              and optionally, the system identifier 
     */
    public static void main(String[] args) {
    	if ( (args.length < 2) || (args.length > 3) ) {
    	    System.out.println("Usage: java XMLMessageExample "
                    + "<queue_name> <xml_filename> [<systemid_url>]");
    	    System.exit(1);
    	} 
        String queueName = new String(args[0]);
        System.out.println("Queue name is " + queueName);

        Producer producer = null;
        Consumer consumer = null;
        try {
            producer = new Producer(queueName);
            consumer = new Consumer(queueName);
            producer.send(args[1]);
            consumer.receive(args.length > 2 ? args[2]:null);
        }
        catch (Exception e) {
            System.out.println("Exception occurred : " + e.toString());
            e.printStackTrace();
        }
        finally {
            if (producer != null) producer.close();
            if (consumer != null) consumer.close();
        }
    }

}

class Producer {
    ConnectionFactory    connectionFactory = null;
    Connection           connection = null;
    Session              session = null;
    Queue                queue = null;
    MessageProducer      msgProducer = null;

    public Producer(String queueName) throws Exception {
        try {
            connectionFactory = SampleUtilities.getConnectionFactory();
            connection = connectionFactory.createConnection();
            session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
            queue = SampleUtilities.getQueue(queueName, session);
            msgProducer = session.createProducer(queue);
        }
        catch (Exception e) {
            close();
            throw e;
        }
    }

    public void close() {
        if (connection != null) {
            try {
                 connection.close();
                 connection = null;
            } catch (JMSException e) {} 
        }
    }

    public void send(String xmlfile) throws Exception {
        StreamMessage           streamMessage = null;

        /*
         * Create input stream from the xml file
         * Read bytes from the input stream into a buffer
         * and construct a StreamMessage
         * Send the message
         */
        File f = new File(xmlfile);
        int length = (int)f.length();
        FileInputStream inStream = new FileInputStream(f);
        byte[] buf = new byte[length];
        inStream.read(buf);
        inStream.close();

        streamMessage = session.createStreamMessage();
        streamMessage.writeObject(buf);
        System.out.println("Write " + length + " bytes into message");

        /*
         * Set a property so that the consumer can check to know
         * this message has a XML document body.  This is helpful
         * if there are other messages in the queue.
         */
        streamMessage.setBooleanProperty("MyXMLMessage", true);
        msgProducer.send(streamMessage);
    }
}

class Consumer {
    DocumentBuilder      docBuilder = null;
    ConnectionFactory    connectionFactory = null;
    Connection           connection = null;
    Session              session = null;
    Queue                queue = null;
    MessageConsumer      msgConsumer = null;

    public Consumer(String queueName) throws Exception {
        try {
            DocumentBuilderFactory docBuilderFactory = DocumentBuilderFactory.newInstance();
            docBuilder = docBuilderFactory.newDocumentBuilder();
 
            connectionFactory = SampleUtilities.getConnectionFactory();
            connection = connectionFactory.createConnection();
            session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
            queue = SampleUtilities.getQueue(queueName, session);
            msgConsumer = session.createConsumer(queue);
            connection.start();
        }
        catch (Exception e) {
            close();
            throw e;
        }
    }

    public void close() {
        if (connection != null) {
            try {
                 connection.close();
                 connection = null;
            } catch (JMSException ee) {}
        }
    }

    public void  receive(String systemid) throws Exception {
        StreamMessage           streamMessage = null;
        Message                 message = null;

        /*
         * Receive the message from the queue
         * Process the message  
         */
        while(true) {
            message = msgConsumer.receive();
            if (!(message instanceof StreamMessage)) {
                //not our XML message 
                continue;
            }
            streamMessage = (StreamMessage)message;
            try {
                if (!streamMessage.getBooleanProperty("MyXMLMessage")) {
                    //not our XML message
                    continue;
                }
            } catch (NullPointerException e) { //the property not exist 
                //not our XML message
                continue;
            }

            //got our XML message
            byte[] bytes = (byte[])streamMessage.readObject();
            System.out.println("Read " + bytes.length + " bytes from message");
            ByteArrayInputStream bais = new ByteArrayInputStream(bytes);
            InputSource is =new InputSource(bais);
            if (systemid !=  null) {
                is.setSystemId(systemid);
            }
            parse(is);
            break;
        }
    }

    private void parse(InputSource is) throws Exception {
        try {
            Document doc = docBuilder.parse(is);

            // normalize text representation
            doc.getDocumentElement().normalize();
 
            System.out.println ("Root element of the doc is " +
                             doc.getDocumentElement().getNodeName());
 
        } catch (SAXParseException e) {
            System.out.println ("** Parsing error"
                        + ", line " + e.getLineNumber()
                        + ", uri " + e.getSystemId());
            System.out.println("   " + e.getMessage());
            Exception   x = e.getException();
            ((x == null) ? e : x).printStackTrace();
            throw e;

        } catch (SAXException e) {
            Exception   x = e.getException();
            ((x == null) ? e : x).printStackTrace();
            throw e;
 
        } 
    }
}

