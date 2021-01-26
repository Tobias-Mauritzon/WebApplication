#!/usr/bin/python
#
# DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
#
# Copyright (c) 2000-2017 Oracle and/or its affiliates. All rights reserved.
#
# The contents of this file are subject to the terms of either the GNU
# General Public License Version 2 only ("GPL") or the Common Development
# and Distribution License("CDDL") (collectively, the "License").  You
# may not use this file except in compliance with the License.  You can
# obtain a copy of the License at
# https://oss.oracle.com/licenses/CDDL+GPL-1.1
# or LICENSE.txt.  See the License for the specific
# language governing permissions and limitations under the License.
#
# When distributing the software, include this License Header Notice in each
# file and include the License file at LICENSE.txt.
#
# GPL Classpath Exception:
# Oracle designates this particular file as subject to the "Classpath"
# exception as provided by Oracle in the GPL Version 2 section of the License
# file that accompanied this code.
#
# Modifications:
# If applicable, add the following below the License Header, with the fields
# enclosed by brackets [] replaced by your own identifying information:
# "Portions Copyright [year] [name of copyright owner]"
#
# Contributor(s):
# If you wish your version of this file to be governed by only the CDDL or
# only the GPL Version 2, indicate your decision by adding "[Contributor]
# elects to include this software in this distribution under the [CDDL or GPL
# Version 2] license."  If you don't indicate a single choice of license, a
# recipient has the option to distribute your version of this file under
# either the CDDL, the GPL Version 2 or to extend the choice of license to
# its licensees as provided above.  However, if you add GPL Version 2 code
# and therefore, elected the GPL Version 2 license, then the option applies
# only if the new code is made subject to such option by the copyright
# holder.
#

import sys, getopt, datetime, httplib, urllib

DEFAULT_CONTEXT_ROOT = "/ums"
DEFAULT_TIMEOUT = "15000"

host = ""
dst = "simpleQ"
domain = "queue"
user = "guest"
pwd = "guest"

# Function to print usage
def usage():
   print
   print "usage: python ReceiveMsg.py [options]"
   print
   print "where options include:"
   print "  -h               Usage"
   print "  -s <host:port>   Specify the server host and port."
   print "  -d <name>        Specify the destination name. Default is simpleQ."
   print "  -q               Specify domain is a queue. Default is queue."
   print "  -t               Specify domain is a topic."
   print "  -u <user>        Specify the user name. Default is guest."
   print "  -p <password>    Specify the password. Default is guest."
   print
   sys.exit(1)

# Function to parse command line arguments
def parseArgs():
   global dst, host, domain, user, pwd

   try:
      opts, args = getopt.getopt(sys.argv[1:], 'hqtd:s:u:p:')
   except getopt.GetoptError:
      print "Error: parsing command line arguments"
      usage()

   for opt in opts:
      if opt[0] == '-h':
         usage()
      if opt[0] == '-d':
         dst = opt[1]
      if opt[0] == '-s':
         host = opt[1]
      if opt[0] == '-q':
         domain = "queue"
      if opt[0] == '-t':
         domain = "topic"
      if opt[0] == '-u':
         user = opt[1]
      if opt[0] == '-p':
         pwd = opt[1]

   if len(host) == 0:
      print "Please specify the UMS server host and port!"
      usage()

# Function to post request
#    Returns 0 if successfull and server's responsed data
def doPost(conn, url, body):
   rtnCode = 0
   respData = ""
   headers = {
      "Content-type": "text/plain;charset=UTF-8",
      "Accept": "text/plain"
   }

   try:
      conn.request("POST", url, body, headers)
      resp = conn.getresponse()

      # Get the response
      if resp.status == 200:
         respData = resp.read()
      elif resp.status == 404:
         #print "No message available"
         respData = ""
      else:
         print "Failed to post data to http://" + host + url
         print "Response: ", resp.status, resp.reason
         rtnCode = -1
   except KeyboardInterrupt:
      rtnCode = -1
   except Exception, e:
      print "Error: ", e.__class__, "Cannot post data to http://" + host + url
      rtnCode = -1

   return rtnCode, respData

# Main program
def main():

   # Process command line args
   parseArgs()

   print "UMS Server:", host + ", Destination:", dst + ", Domain:", domain
   print

   # Open a connection to the server
   conn = httplib.HTTPConnection(host)

   # Login to UMS
   url = DEFAULT_CONTEXT_ROOT + "/simple?service=login" + \
      "&user=" + urllib.quote(user) + "&password=" + urllib.quote(pwd);
   (rtnCode, sid) = doPost(conn, url, "")
   if rtnCode == -1:
      print "Failed to login to UMS server."
      sys.exit(1)

   # Get a message from the server (if any)
   url = DEFAULT_CONTEXT_ROOT + "/simple?service=receive" \
      "&destination=" + dst + \
      "&domain=" + domain + \
      "&sid=" + sid + \
      "&timeout=" + DEFAULT_TIMEOUT

   while 1:
      # Send request to retrieve a JMS message
      (rtnCode, respMsg) = doPost(conn, url, "")
      if rtnCode == -1:
         break

      if len(respMsg) > 0:
         currentTime = datetime.datetime.now()
         print currentTime.strftime("[%d/%m/%Y:%H:%M:%S] ") + \
            "Received msg: " + respMsg

   # Close the UMS session
   #    Connection could be bad due to CTRL-C so create a new one
   print
   print "Closing UMS connection, please wait..."

   conn.close()
   conn = httplib.HTTPConnection(host)
   url = DEFAULT_CONTEXT_ROOT + "/simple?service=close&sid=" + sid
   (rtnCode, respMsg) = doPost(conn, url, "")
   conn.close()

if __name__ == '__main__':
   main()
