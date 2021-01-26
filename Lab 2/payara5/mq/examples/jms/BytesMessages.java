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

/**
 * The BytesMessages class consists only of a main method, which reads a
 * textfile, creates a BytesMessage from it, then reads the message.  It does 
 * not send the message.
 * <p>
 * Specify an existing text file name on the command line when you run 
 * the program.
 * <p>
 * This is not a realistic example of the use of the BytesMessage message type,
 * which is intended for client encoding of existing message formats.  (If 
 * possible, one of the other message types, such as StreamMessage or
 * MapMessage, should be used instead.)  However, it shows how to use a buffer
 * to write or read a BytesMessage when you do not know its length.
 */
public class BytesMessages {

    /**
     * Main method.
     *
     * @param args	the name of the text file used by the example
     */
    public static void main(String[] args) {
        String               filename = null;
        FileInputStream      inStream = null;
        ConnectionFactory    connectionFactory = null;
        Connection           connection = null;
        Session              session = null;
        BytesMessage         bytesMessage = null;
        int                  bytes_read = 0;
        final int            BUFLEN = 64;
        byte[]               buf1 = new byte[BUFLEN];
        byte[]               buf2 = new byte[BUFLEN];
        int                  length = 0;
        int                  exitResult = 0;

    	/*
    	 * Read text file name from command line and create input stream.
    	 */
    	if (args.length != 1) {
    	    System.out.println("Usage: java BytesMessages <filename>");
    	    System.exit(1);
    	}
    	try {
    	    filename = new String(args[0]);
            inStream = new FileInputStream(filename);
    	} catch (IOException e) {
    	    System.out.println("Problem getting file: " + e.toString());
            System.exit(1);
    	}
    	
        try {
            connectionFactory = 
                SampleUtilities.getConnectionFactory();
            connection = 
                connectionFactory.createConnection();
            session = connection.createSession(false, 
                Session.AUTO_ACKNOWLEDGE);
    	} catch (Exception e) {
            System.out.println("Connection problem: " + e.toString());
            if (connection != null) {
                try {
                    connection.close();
                } catch (JMSException ee) {}
            }
    	    System.exit(1);
    	} 

        try {
            /* 
             * Create a BytesMessage.
             * Read a byte stream from the input stream into a buffer and
             * construct a BytesMessage, using the three-argument form 
             * of the writeBytes method to ensure that the message contains 
             * only the bytes read from the file, not any leftover characters 
             * in the buffer.
             */
            bytesMessage = session.createBytesMessage();
            while ((bytes_read = inStream.read(buf1)) != -1) {
                bytesMessage.writeBytes(buf1, 0, bytes_read);
                System.out.println("Writing " + bytes_read 
                    + " bytes into message");
            }
            
            /*
             * Reset the message to the beginning, then use readBytes to
             * extract its contents into another buffer, casting the byte array
             * elements to char so that they will display intelligibly.
             */
            bytesMessage.reset();
            do {
                length = bytesMessage.readBytes(buf2);
                if (length != -1) {
                    System.out.println("Reading " + length
                        + " bytes from message: ");
                    for (int i = 0; i < length; i++) {
                        System.out.print((char)buf2[i]);
                    }
                }
                System.out.println();
            } while (length >= BUFLEN);
        } catch (JMSException e) {
            System.out.println("JMS exception occurred: " + e.toString());
            exitResult = 1;
        } catch (IOException e) {
            System.out.println("I/O exception occurred: " + e.toString());
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
        SampleUtilities.exit(exitResult);
    }
}
