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

import java.util.Random;
import java.util.Hashtable;
import javax.jms.Queue;
import javax.jms.Session;
import javax.jms.ConnectionFactory;
import javax.jms.Connection;
import javax.jms.MessageProducer;
import javax.jms.MessageConsumer;
import javax.jms.Message;
import javax.jms.MapMessage;
import javax.jms.MessageListener;
import javax.jms.JMSException;

/**
 * The TransactedExample class demonstrates the use of transactions in a JMS
 * application.  It represents a highly simplified eCommerce application, in
 * which the following things happen:
 *
 * <pre>
 * Legend
 * R - Retailer
 * V - Vendor
 * S - Supplier
 * O - Order Queue
 * C - Confirmation Queue
 * ()- Thread
 * []- Queue
 *
 *                                   2(b)             3
 *          1        2(a)        /+------->[S1 O]<-----------(S1)
 *        /+-->[V O]<----+      /                             |
 *       /                \    /                      3       |
 *      /                  \  /     5      v------------------+
 *    (R)                  ( V )-------->[V C]        4
 *     \                   /  \            ^------------------+
 *      \                 /    \                              |
 *       \   7         6 /      \                             |
 *        +---->[R C]<--+        \   2(c)             4       |
 *                                +------->[SN O]<-----------(SN)
 * </pre>
 *
 * <ol>
 * <li>A retailer sends a message to the vendor order queue ordering a quantity 
 * of computers.  It waits for the vendor's reply.
 * <li>The vendor receives the retailer's order message and places an order 
 * message into each of its suppliers' order queues, all in one transaction. 
 * This JMS transaction combines one synchronous receive with multiple sends.
 * <li>One supplier receives the order from its order queue, checks its
 * inventory, and sends the items ordered to the order message's replyTo
 * field. If it does not have enough in stock, it sends what it has. 
 * The synchronous receive and the send take place in one JMS transaction.
 * <li>The other supplier receives the order from its order queue, checks its
 * inventory, and sends the items ordered to the order message's replyTo
 * field. If it does not have enough in stock, it sends what it has. 
 * The synchronous receive and the send take place in one JMS transaction.
 * <li>The vendor receives the replies from the suppliers from its confirmation 
 * queue and updates the state of the order.  Messages are processed by an 
 * asynchronous message listener; this step illustrates using JMS transactions 
 * with a message listener.
 * <li>When all outstanding replies are processed for a given order, the vendor 
 * sends a message notifying the retailer whether or not it can fulfill the 
 * order.
 * <li>The retailer receives the message from the vendor.
 * </ol>
 * <p>
 * The program contains five classes: Retailer, Vendor, GenericSupplier, 
 * VendorMessageListener, and Order.  It also contains a main method and a 
 * method that runs the threads of the Retail, Vendor, and two supplier classes.
 * <p>
 * All the messages use the MapMessage message type.  Synchronous receives are
 * used for all message reception except for the case of the vendor processing 
 * the replies of the suppliers. These replies are processed asynchronously 
 * and demonstrate how to use transactions within a message listener.
 * <p>
 * All classes except Retailer use transacted sessions.
 * <p>
 * The program uses five queues.  Before you run the program, create the
 * queues and name them A, B, C, D and E.
 * <p>
 * When you run the program, specify on the command line the number of
 * computers to be ordered.
 */
public class TransactedExample {
    public static String  vendorOrderQueueName = null;
    public static String  retailerConfirmationQueueName = null;
    public static String  monitorOrderQueueName = null;
    public static String  storageOrderQueueName = null;
    public static String  vendorConfirmationQueueName = null;
    public static int     exitResult = 0;

    /**
     * The Retailer class orders a number of computers by sending a message
     * to a vendor.  It then waits for the order to be confirmed.
     * <p>
     * In this example, the Retailer places two orders, one for the quantity
     * specified on the command line and one for twice that number.
     * <p>
     * This class does not use transactions.
     */
    public static class Retailer extends Thread {
        int  quantity = 0;

        /**
         * Constructor.  Instantiates the retailer with the quantity of 
         * computers being ordered.
         *
         * @param q	the quantity specified in the program arguments
         */
        public Retailer(int q) {
            quantity = q;
        }

        /**
         * Runs the thread.
         */
        public void run() {
            ConnectionFactory    connectionFactory = null;
            Connection           connection = null;
            Session              session = null;
            Queue                vendorOrderQueue = null;
            Queue                retailerConfirmationQueue = null;
            MessageProducer      msgProducer = null;
            MapMessage           outMessage = null;
            MessageConsumer      orderConfirmationConsumer = null;
            MapMessage           inMessage = null;

            try {
                connectionFactory =
                    SampleUtilities.getConnectionFactory();
                connection =
                    connectionFactory.createConnection();
                session = connection.createSession(false,
                                                     Session.AUTO_ACKNOWLEDGE);
                vendorOrderQueue =
                    SampleUtilities.getQueue(vendorOrderQueueName, session);
                retailerConfirmationQueue =
                    SampleUtilities.getQueue(retailerConfirmationQueueName, 
                                             session);
            } catch (Exception e) {
                System.out.println("Connection problem: " + e.toString());
                System.out.println("Program assumes five queues named A B C D E");
                if (connection != null) {
                    try {
                        connection.close();
                    } catch (JMSException ee) {}
                }
                System.exit(1);
            }

            /*
             * Create non-transacted session and sender for vendor order
             * queue.
             * Create message to vendor, setting item and quantity values.
             * Send message.
             * Create receiver for retailer confirmation queue.
             * Get message and report result.
             * Send an end-of-message-stream message so vendor will
             * stop processing orders.
             */
            try {
                msgProducer = session.createProducer(vendorOrderQueue);
                outMessage = session.createMapMessage();
                outMessage.setString("Item", "Computer(s)");
                outMessage.setInt("Quantity", quantity);
                outMessage.setJMSReplyTo(retailerConfirmationQueue);
                msgProducer.send(outMessage);
                System.out.println("Retailer: ordered " + quantity
                                   + " computer(s)");

                orderConfirmationConsumer =
                    session.createConsumer(retailerConfirmationQueue);
                connection.start();
                inMessage = (MapMessage) orderConfirmationConsumer.receive();
                if (inMessage.getBoolean("OrderAccepted") == true) {
                    System.out.println("Retailer: Order filled");
                } else {
                    System.out.println("Retailer: Order not filled");
                }
                
                
                System.out.println("Retailer: placing another order");
                outMessage.setInt("Quantity", quantity * 2);
                msgProducer.send(outMessage);
                System.out.println("Retailer: ordered " 
                                   + outMessage.getInt("Quantity")
                                   + " computer(s)");
                inMessage =
                    (MapMessage) orderConfirmationConsumer.receive();
                if (inMessage.getBoolean("OrderAccepted") == true) {
                    System.out.println("Retailer: Order filled");
                } else {
                    System.out.println("Retailer: Order not filled");
                }

                // Send a non-text control message indicating end of messages.
                msgProducer.send(session.createMessage());
            } catch (Exception e) {
                System.out.println("Retailer: Exception occurred: " 
                                   + e.toString());
                e.printStackTrace();
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
     * The Vendor class uses one transaction to receive the computer order from
     * the retailer and order the needed number of monitors and disk drives
     * from its suppliers.  At random intervals, it throws an exception to
     * simulate a database problem and cause a rollback.
     * <p>
     * The class uses an asynchronous message listener to process replies from
     * suppliers. When all outstanding supplier inquiries complete, it sends a 
     * message to the Retailer accepting or refusing the order.
     */
    public static class Vendor extends Thread {
        Random  rgen = new Random();
        int     throwException = 1;

        /**
         * Runs the thread.
         */
        public void run() {
            ConnectionFactory        connectionFactory = null;
            Connection               connection = null;
            Session                  session = null;
            Session                  asyncSession = null;
            Queue                    vendorOrderQueue = null;
            Queue                    monitorOrderQueue = null;
            Queue                    storageOrderQueue = null;
            Queue                    vendorConfirmationQueue = null;
            MessageConsumer          vendorOrderMessageConsumer = null;
            MessageProducer          monitorOrderMessageProducer = null;
            MessageProducer          storageOrderMessageProducer = null;
            MapMessage               orderMessage = null;
            MessageConsumer          vendorConfirmationMessageConsumer = null;
            VendorMessageListener    listener = null;
            Message                  inMessage = null;
            MapMessage               vendorOrderMessage = null;
            Message                  endOfMessageStream = null;
            Order                    order = null;
            int                      quantity = 0;

            try {
                connectionFactory =
                    SampleUtilities.getConnectionFactory();
                connection =
                    connectionFactory.createConnection();
                session = connection.createSession(true, 0);
                asyncSession = connection.createSession(true, 0);
                vendorOrderQueue =
                    SampleUtilities.getQueue(vendorOrderQueueName, session);
                monitorOrderQueue =
                    SampleUtilities.getQueue(monitorOrderQueueName, session);
                storageOrderQueue =
                    SampleUtilities.getQueue(storageOrderQueueName, session);
                vendorConfirmationQueue =
                    SampleUtilities.getQueue(vendorConfirmationQueueName, session);
            } catch (Exception e) {
                System.out.println("Connection problem: " + e.toString());
                System.out.println("Program assumes six queues named A B C D E F");
                if (connection != null) {
                    try {
                        connection.close();
                    } catch (JMSException ee) {}
                }
                System.exit(1);
            }

            try {
                /*
                 * Create receiver for vendor order queue, sender for
                 * supplier order queues, and message to send to suppliers.
                 */
                vendorOrderMessageConsumer = 
                    session.createConsumer(vendorOrderQueue);
                monitorOrderMessageProducer = 
                    session.createProducer(monitorOrderQueue);
                storageOrderMessageProducer = 
                    session.createProducer(storageOrderQueue);
                orderMessage = session.createMapMessage();

                /*
                 * Configure an asynchronous message listener to process
                 * supplier replies to inquiries for parts to fill order.
                 * Start delivery.
                 */
                vendorConfirmationMessageConsumer = 
                    asyncSession.createConsumer(vendorConfirmationQueue);
                listener = new VendorMessageListener(asyncSession, 2);
                vendorConfirmationMessageConsumer.setMessageListener(listener);
                connection.start();

                /*
                 * Process orders in vendor order queue.
                 * Use one transaction to receive order from order queue
                 * and send messages to suppliers' order queues to order 
                 * components to fulfill the order placed with the vendor.
                 */
                while (true) {
                    try {

                        // Receive an order from a retailer.
                        inMessage = vendorOrderMessageConsumer.receive();
                        if (inMessage instanceof MapMessage) {
                            vendorOrderMessage = (MapMessage) inMessage;
                        } else {
                            /*
                             * Message is an end-of-message-stream message from
                             * retailer.  Send similar messages to suppliers,
                             * then break out of processing loop.
                             */
                            endOfMessageStream = session.createMessage();
                            endOfMessageStream.setJMSReplyTo(vendorConfirmationQueue);
                            monitorOrderMessageProducer.send(endOfMessageStream);
                            storageOrderMessageProducer.send(endOfMessageStream);
                            session.commit();
                            break;
                        }

                        /*
                         * A real application would check an inventory database
                         * and order only the quantities needed.  Throw an
                         * exception every few times to simulate a database
                         * concurrent-access exception and cause a rollback.
                         */
                        if (rgen.nextInt(3) == throwException) {
                            throw new JMSException("Simulated database concurrent access exception");
                        }

                        // Record retailer order as a pending order.
                        order = new Order(vendorOrderMessage);
                        
                        /*
                         * Set order number and reply queue for outgoing
                         * message.
                         */
                        orderMessage.setInt("VendorOrderNumber", 
                                            order.orderNumber);
                        orderMessage.setJMSReplyTo(vendorConfirmationQueue);
                        quantity = vendorOrderMessage.getInt("Quantity");
                        System.out.println("Vendor: Retailer ordered " +
                                           quantity + " " +
                                           vendorOrderMessage.getString("Item"));

                        // Send message to monitor supplier.
                        orderMessage.setString("Item", "Monitor");
                        orderMessage.setInt("Quantity", quantity);
                        monitorOrderMessageProducer.send(orderMessage);
                        System.out.println("Vendor: ordered " + quantity + " " 
                                           + orderMessage.getString("Item") 
                                           + "(s)");

                        /*
                         * Reuse message to send to storage supplier, changing
                         * only item name.
                         */
                        orderMessage.setString("Item", "Hard Drive");
                        storageOrderMessageProducer.send(orderMessage);
                        System.out.println("Vendor: ordered " + quantity + " " 
                                           + orderMessage.getString("Item") 
                                           + "(s)");

                        // Commit session.
                        session.commit();
                        System.out.println("  Vendor: committed transaction 1");
                    } catch(JMSException e) {
                        System.out.println("Vendor: JMSException occurred: "
                            + e.toString());
                        e.printStackTrace();
                        session.rollback();
                        System.out.println("  Vendor: rolled back transaction 1");
                        exitResult = 1;
                    }
                }

                // Wait till suppliers get back with answers.
                listener.monitor.waitTillDone();
            } catch (JMSException e) {
                System.out.println("Vendor: Exception occurred: " +
                                   e.toString());
                e.printStackTrace();
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
     * The Order class represents a Retailer order placed with a Vendor.
     * It maintains a table of pending orders.
     */
    public static class Order {
        private static Hashtable  pendingOrders = new Hashtable();
        private static int        nextOrderNumber = 1;

        private static final int  PENDING_STATUS   = 1;
        private static final int  CANCELLED_STATUS = 2;
        private static final int  FULFILLED_STATUS = 3;
        int                       status;

        public final int          orderNumber;
        public int                quantity;
        public final MapMessage   order;   // original order from retailer
        public MapMessage         monitor = null;  // reply from supplier
        public MapMessage         storage = null;  // reply from supplier

        /**
         * Returns the next order number and increments the static variable
         * that holds this value.
         *
         * @return	the next order number
         */
        private static int getNextOrderNumber() {
            int  result = nextOrderNumber;
            nextOrderNumber++;
            return result;
        }

        /**
         * Constructor.  Sets order number; sets order and quantity from  
         * incoming message. Sets status to pending, and adds order to hash 
         * table of pending orders.
         *
         * @param order	the message containing the order
         */
        public Order(MapMessage order) {
            this.orderNumber = getNextOrderNumber();
            this.order = order;
            try {
                this.quantity = order.getInt("Quantity");
            } catch (JMSException je) {
                System.err.println("Unexpected error. Message missing Quantity");
                this.quantity = 0;
            }
            status = PENDING_STATUS;
            pendingOrders.put(new Integer(orderNumber), this);
        }

        /**
         * Returns the number of orders in the hash table.
         *
         * @return	the number of pending orders
         */
        public static int outstandingOrders() {
            return pendingOrders.size();
        }

        /**
         * Returns the order corresponding to a given order number.
         *
         * @param orderNumber	the number of the requested order
         * @return	the requested order
         */
        public static Order getOrder(int orderNumber) {
            return (Order) pendingOrders.get(new Integer(orderNumber));
        }

        /**
         * Called by the onMessage method of the VendorMessageListener class
         * to process a reply from a supplier to the Vendor.
         *
         * @param component	the message from the supplier
         * @return	the order with updated status information
         */
        public Order processSubOrder(MapMessage component) {
            String  itemName = null;

            // Determine which subcomponent this is.
            try {
                itemName = component.getString("Item");
            } catch (JMSException je) {
                System.err.println("Unexpected exception. Message missing Item");
            }
            if (itemName.compareTo("Monitor") == 0) {
                monitor = component;
            } else if (itemName.compareTo("Hard Drive") == 0 ) {
                storage = component;
            }

            /*
             * If notification for all subcomponents has been received,
             * verify the quantities to compute if able to fulfill order.
             */
            if ( (monitor != null) && (storage != null) ) {
                try {
                    if (quantity > monitor.getInt("Quantity")) {
                        status = CANCELLED_STATUS;
                    } else if (quantity > storage.getInt("Quantity")) {
                        status = CANCELLED_STATUS;
                    } else {
                        status = FULFILLED_STATUS;
                    }
                } catch (JMSException je) {
                    System.err.println("Unexpected exception " + je);
                    status = CANCELLED_STATUS;
                }

                /*
                 * Processing of order is complete, so remove it from 
                 * pending-order list.
                 */
                pendingOrders.remove(new Integer(orderNumber));
            }
            return this;
        }

        /**
         * Determines if order status is pending.
         *
         * @return	true if order is pending, false if not
         */
        public boolean isPending() {
            return status == PENDING_STATUS;
        }

        /**
         * Determines if order status is cancelled.
         *
         * @return	true if order is cancelled, false if not
         */
        public boolean isCancelled() {
            return status == CANCELLED_STATUS;
        }

        /**
         * Determines if order status is fulfilled.
         *
         * @return	true if order is fulfilled, false if not
         */
        public boolean isFulfilled() {
            return status == FULFILLED_STATUS;
        }
    }

    /**
     * The VendorMessageListener class processes an order confirmation message
     * from a supplier to the vendor.
     * <p>
     * It demonstrates the use of transactions within message listeners.
     */
    public static class VendorMessageListener implements MessageListener {
        final SampleUtilities.DoneLatch  monitor =
            new SampleUtilities.DoneLatch();
        private final Session session;
        int numSuppliers;

        /**
         * Constructor.  Instantiates the message listener with the session
         * of the consuming class (the vendor).
         *
         * @param session 	the session of the consumer
         * @param numSuppliers	the number of suppliers
         */
        public VendorMessageListener(Session session, int numSuppliers) {
            this.session = session;
            this.numSuppliers = numSuppliers;
        }

        /**
         * Casts the message to a MapMessage and processes the order.
         * A message that is not a MapMessage is interpreted as the end of the  
         * message stream, and the message listener sets its monitor state to  
         * all done processing messages.
         * <p>
         * Each message received represents a fulfillment message from
         * a supplier.
         *
         * @param message	the incoming message
         */
        public void onMessage(Message message) {

            /*
             * If message is an end-of-message-stream message and this is the
             * last such message, set monitor status to all done processing 
             * messages and commit transaction.
             */
            if (! (message instanceof MapMessage)) {
                if (Order.outstandingOrders() == 0) {
                    numSuppliers--;
                    if (numSuppliers == 0) {
  		        monitor.allDone();
  		    }
                }
                try {
                    session.commit();
                } catch (JMSException je) {}
                return;
            }

            /* 
             * Message is an order confirmation message from a supplier.
             */
            int orderNumber = -1;
            try {
                MapMessage component = (MapMessage) message;

                /* 
                 * Process the order confirmation message and commit the
                 * transaction.
                 */
                orderNumber = component.getInt("VendorOrderNumber");
                Order order = 
                    Order.getOrder(orderNumber).processSubOrder(component);
                session.commit();
                
                /*
                 * If this message is the last supplier message, send message
                 * to Retailer and commit transaction.
                 */
                if (! order.isPending()) {
                    System.out.println("Vendor: Completed processing for order "
                        + order.orderNumber);
                    Queue replyQueue = (Queue) order.order.getJMSReplyTo();
                    MessageProducer mp = session.createProducer(replyQueue);
                    MapMessage retailerConfirmationMessage = 
                        session.createMapMessage();
                    if (order.isFulfilled()) {
                        retailerConfirmationMessage.setBoolean("OrderAccepted", 
                                                               true);
                        System.out.println("Vendor: sent " + order.quantity
                                           + " computer(s)");
                    } else if (order.isCancelled()) {
                        retailerConfirmationMessage.setBoolean("OrderAccepted", 
                                                               false);
                        System.out.println("Vendor: unable to send " +
                                           order.quantity + " computer(s)");
                    }
                    mp.send(retailerConfirmationMessage);
                    session.commit();
                    System.out.println("  Vendor: committed transaction 2");
                }
            } catch (JMSException je) {
                je.printStackTrace();
                try {
                    session.rollback();
                } catch (JMSException je2) {}
            } catch (Exception e) {
                e.printStackTrace();
                try {
                    session.rollback();
                } catch (JMSException je2) {}
            }
        }
    }

    /**
     * The GenericSupplier class receives an item order from the
     * vendor and sends a message accepting or refusing it.
     */
    public static class GenericSupplier extends Thread {
        final String  PRODUCT_NAME;
        final String  IN_ORDER_QUEUE;
        int           quantity = 0;

        /**
         * Constructor.  Instantiates the supplier as the supplier for the
         * kind of item being ordered.
         *
         * @param itemName	the name of the item being ordered
         * @param inQueue	the queue from which the order is obtained
         */
        public GenericSupplier(String itemName, String inQueue) {
            PRODUCT_NAME = itemName;
            IN_ORDER_QUEUE = inQueue;
        }

        /**
         * Checks to see if there are enough items in inventory.
         * Rather than go to a database, it generates a random number related
         * to the order quantity, so that some of the time there won't be
         * enough in stock.
         *
         * @return	the number of items in inventory
         */
        public int checkInventory() {
            Random  rgen = new Random();

            return (rgen.nextInt(quantity * 5));
        }

        /**
         * Runs the thread.
         */
        public void run() {
            ConnectionFactory    connectionFactory = null;
            Connection           connection = null;
            Session              session = null;
            Queue                orderQueue = null;
            MessageConsumer      msgConsumer = null;
            Message              inMessage = null;
            MapMessage           orderMessage = null;
            MapMessage           outMessage = null;

            try {
                connectionFactory =
                    SampleUtilities.getConnectionFactory();
                connection =
                    connectionFactory.createConnection();
                session = connection.createSession(true, 0);
                orderQueue =
                    SampleUtilities.getQueue(IN_ORDER_QUEUE, session);
            } catch (Exception e) {
                System.out.println("Connection problem: " + e.toString());
                System.out.println("Program assumes six queues named A B C D E F");
                if (connection != null) {
                    try {
                        connection.close();
                    } catch (JMSException ee) {}
                }
                System.exit(1);
            }
            
            // Create receiver for order queue and start message delivery.
            try {
                msgConsumer = session.createConsumer(orderQueue);
                connection.start();
            } catch (JMSException je) {
                exitResult = 1;
            }

            /*
             * Keep checking supplier order queue for order request until
             * end-of-message-stream message is received.
             * Receive order and send an order confirmation as one transaction.
             */
            while (true) {
                try {
                    inMessage = msgConsumer.receive();
                    if (inMessage instanceof MapMessage) {
                        orderMessage = (MapMessage) inMessage;
                    } else {
                        /*
                         * Message is an end-of-message-stream message.
                         * Send a similar message to reply queue, commit
                         * transaction, then stop processing orders by breaking
                         * out of loop.
                         */
                        MessageProducer msgProducer =
                            session.createProducer((Queue) inMessage.getJMSReplyTo());
                        msgProducer.send(session.createMessage());
                        session.commit();
                        break;
                    }

                    // Extract quantity ordered from order message.
                    quantity = orderMessage.getInt("Quantity");
                    System.out.println(PRODUCT_NAME 
                        + " Supplier: Vendor ordered " + quantity + " " 
                        + orderMessage.getString("Item") + "(s)");

                    /*
                     * Create sender and message for reply queue.
                     * Set order number and item; check inventory and set
                     * quantity available.  
                     * Send message to vendor and commit transaction.
                     */
                    MessageProducer msgProducer =
                        session.createProducer((Queue) orderMessage.getJMSReplyTo());
                    outMessage = session.createMapMessage();
                    outMessage.setInt("VendorOrderNumber",
                                      orderMessage.getInt("VendorOrderNumber"));
                    outMessage.setString("Item", PRODUCT_NAME);
                    int numAvailable = checkInventory();
                    if (numAvailable >= quantity) {
                        outMessage.setInt("Quantity", quantity);
                    } else {
                        outMessage.setInt("Quantity", numAvailable);
                    }
                    msgProducer.send(outMessage);
                    System.out.println(PRODUCT_NAME + " Supplier: sent "
                                       + outMessage.getInt("Quantity") + " " 
                                       + outMessage.getString("Item") + "(s)");
                    session.commit();
                    System.out.println("  " + PRODUCT_NAME
                                       + " Supplier: committed transaction");
                } catch (Exception e) {
                    System.out.println(PRODUCT_NAME 
                        + " Supplier: Exception occurred: " + e.toString());
                    e.printStackTrace();
                    exitResult = 1;
                } 
            }

            if (connection != null) {
                try {
                    connection.close();
                } catch (JMSException e) {
                    exitResult = 1;
                }
            }
        }
    }

    /**
     * Creates the Retailer and Vendor classes and the two supplier classes,
     * then starts the threads.
     *
     * @param quantity	the quantity specified on the command line
     */
    public static void run_threads(int quantity) {
        Retailer         r = new Retailer(quantity);
        Vendor           v = new Vendor();
        GenericSupplier  ms = new GenericSupplier("Monitor", 
                                                 monitorOrderQueueName);
        GenericSupplier  ss = new GenericSupplier("Hard Drive", 
                                                 storageOrderQueueName);

        r.start();
        v.start();
        ms.start();
        ss.start();
        try {
            r.join();
            v.join();
            ms.join();
            ss.join();
        } catch (InterruptedException e) {}
    }

    /**
     * Reads the order quantity from the command line, then
     * calls the run_threads method to execute the program threads.
     *
     * @param args	the quantity of computers being ordered
     */
    public static void main(String[] args) {
        TransactedExample  te = new TransactedExample();
        int                quantity = 0;

        if (args.length != 1) {
            System.out.println("Usage: java TransactedExample <integer>");
            System.out.println("Program assumes five queues named A B C D E");
            System.exit(1);
        }
        te.vendorOrderQueueName = new String("A");
        te.retailerConfirmationQueueName = new String("B");
        te.monitorOrderQueueName = new String("C");
        te.storageOrderQueueName = new String("D");
        te.vendorConfirmationQueueName = new String("E");
        quantity = (new Integer(args[0])).intValue();
        System.out.println("Quantity to be ordered is " + quantity);
        if (quantity > 0) {
            te.run_threads(quantity);
        } else {
            System.out.println("Quantity must be positive and nonzero");
            te.exitResult = 1;
        }
        SampleUtilities.exit(te.exitResult);
    }
}
