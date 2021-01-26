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

import java.util.Enumeration;
import java.util.Properties;
import javax.jms.*;

/**
 * The DestMetrics example is a JMS application that monitors a
 * destination on a Oracle GlassFish(tm) Server Message Queue broker. It does so by 
 * subscribing to a topic named:
 *	mq.metrics.destination.queue.<dest_name>	OR
 *	mq.metrics.destination.topic.<dest_name>
 * Messages that arrive contain information describing the 
 * destination such as:
 *  - number of messages that flowed into this destination
 *  - number of messages that flowed out of this destination
 *  - size of message bytes that flowed into this destination
 *  - size of message bytes that flowed out of this destination
 *  - etc.
 *
 * Note that this example does not display all the information
 * available in the destination metric message.
 *
 * This application takes the following arguments:
 *	-t dest_type	Specify required destination type. Valid values
 *			are 't' or 'q'.
 *	-n dest_name	Specify required destination name.
 *
 * By default DestMetrics will connect to the broker running on localhost:7676.
 * You can use -DimqAddressList attribute to change the host, port and 
 * transport:
 *
 *	java -DimqAddressList=mq://<host>:<port>/jms DestMetrics
 */
public class DestMetrics implements MessageListener  {
    ConnectionFactory        metricConnectionFactory;
    Connection               metricConnection;
    Session                  metricSession;
    MessageConsumer          metricConsumer;
    Topic                    metricTopic;
    MetricsPrinter           mp;
    String                   metricTopicName = null;
    int                      rowsPrinted = 0;
  
    public static void main(String args[])  {
	String		destName = null, destType = null;

	for (int i = 0; i < args.length; ++i)  {
	    if (args[i].equals("-n"))  {
		destName = args[i+1];
	    } else if (args[i].equals("-t"))  {
		destType = args[i+1];
	    }
	}

	if (destName == null)  {
	    System.err.println("Need to specify destination name with -n");
	    System.exit(1);
	}

	if (destType == null)  {
	    System.err.println("Need to specify destination type (t or q) with -t");
	    System.exit(1);
	}

        DestMetrics bm = new DestMetrics();

        bm.initPrinter(destType, destName);
        bm.initJMS();
        bm.subscribeToMetric(destType, destName);
    }

    public DestMetrics() {
    }

    /*
     * Initializes the class that does the printing, MetricsPrinter.
     * See the MetricsPrinter class for details.
     */
    private void initPrinter(String destType, String destName) {
	String oneRow[] = new String[ 11 ], tmp;
	int    span[] = new int[ 11 ];
	int i = 0;

	mp = new MetricsPrinter(11, 2, "-", MetricsPrinter.CENTER);
	mp.setTitleAlign(MetricsPrinter.CENTER);

	i = 0;
	span[i++] = 2;
	span[i++] = 0;
	span[i++] = 2;
	span[i++] = 0;
	span[i++] = 3;
	span[i++] = 0;
	span[i++] = 0;
	span[i++] = 3;
	span[i++] = 0;
	span[i++] = 0;
	span[i++] = 1;

	i = 0;
	oneRow[i++] = "Msgs";
	oneRow[i++] = "";
	oneRow[i++] = "Msg Bytes";
	oneRow[i++] = "";
	oneRow[i++] = "Msg Count";
	oneRow[i++] = "";
	oneRow[i++] = "";
	oneRow[i++] = "Total Msg Bytes (k)";
	oneRow[i++] = "";
	oneRow[i++] = "";
	oneRow[i++] = "Largest";
	mp.addTitle(oneRow, span);

	i = 0;
        oneRow[i++] = "In";
	oneRow[i++] = "Out";
	oneRow[i++] = "In";
	oneRow[i++] = "Out";
	oneRow[i++] = "Current";
	oneRow[i++] = "Peak";
	oneRow[i++] = "Avg";
	oneRow[i++] = "Current";
	oneRow[i++] = "Peak";
	oneRow[i++] = "Avg";
	oneRow[i++] = "Msg (k)";
	mp.addTitle(oneRow);
    }

    /** 
     * Create the Connection and Session etc.
     */
    public void initJMS() {
        try {
            metricConnectionFactory = new com.sun.messaging.ConnectionFactory();
            metricConnection = metricConnectionFactory.createConnection();
            metricConnection.start();

            //  creating Session
            //	Transaction Mode: None
            //	Acknowledge Mode: Automatic
            metricSession = metricConnection.createSession(false,
				Session.AUTO_ACKNOWLEDGE);
        } catch(Exception e) {
            System.err.println("Cannot create metric connection or session: "
			+ e.getMessage());
            e.printStackTrace();
	    System.exit(1);
        }
    }
  
    public void subscribeToMetric(String destType, String destName) {
        try {

	    if (destType.equals("q"))  {
		metricTopicName = "mq.metrics.destination.queue." + destName;
	    } else  {
		metricTopicName = "mq.metrics.destination.topic." + destName;
	    }

            metricTopic = metricSession.createTopic(metricTopicName);

            metricConsumer = metricSession.createConsumer(metricTopic);
            metricConsumer.setMessageListener(this);
        } catch(JMSException e) {
            System.err.println("Cannot subscribe to metric topic: "
			+ e.getMessage());
            e.printStackTrace();
	    System.exit(1);
        }
    }

    /*
     * When a metric message arrives
     *	- verify it's type
     *	- extract it's fields
     *  - print one row of output
     */
    public void onMessage(Message m)  {
	try  {
	    MapMessage mapMsg = (MapMessage)m;
	    String type = mapMsg.getStringProperty("type");

	    if (type.equals(metricTopicName))  {
	        String oneRow[] = new String[ 11 ];
		int i = 0;

	        /*
	         * Extract destination metrics
	         */
		oneRow[i++] = Long.toString(mapMsg.getLong("numMsgsIn"));
		oneRow[i++] = Long.toString(mapMsg.getLong("numMsgsOut"));
		oneRow[i++] = Long.toString(mapMsg.getLong("msgBytesIn"));
		oneRow[i++] = Long.toString(mapMsg.getLong("msgBytesOut"));

		oneRow[i++] = Long.toString(mapMsg.getLong("numMsgs"));
		oneRow[i++] = Long.toString(mapMsg.getLong("peakNumMsgs"));
		oneRow[i++] = Long.toString(mapMsg.getLong("avgNumMsgs"));

		oneRow[i++] = Long.toString(mapMsg.getLong("totalMsgBytes")/1024);
		oneRow[i++] = Long.toString(mapMsg.getLong("peakTotalMsgBytes")/1024);
		oneRow[i++] = Long.toString(mapMsg.getLong("avgTotalMsgBytes")/1024);

		oneRow[i++] = Long.toString(mapMsg.getLong("peakMsgBytes")/1024);

		mp.add(oneRow);

		if ((rowsPrinted % 20) == 0)  {
		    mp.print();
		} else  {
		    mp.print(false);
		}

		rowsPrinted++;

		mp.clear();
	    } else  {
	        System.err.println("Msg received: not broker metric type");
	    }
	} catch (Exception e)  {
	    System.err.println("onMessage: Exception caught: " + e);
	}
    }
}
