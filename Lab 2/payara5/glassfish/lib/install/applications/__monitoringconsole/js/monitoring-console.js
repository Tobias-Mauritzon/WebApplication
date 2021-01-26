/*
   DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
  
   Copyright (c) 2019-2020 Payara Foundation and/or its affiliates. All rights reserved.
  
   The contents of this file are subject to the terms of either the GNU
   General Public License Version 2 only ("GPL") or the Common Development
   and Distribution License("CDDL") (collectively, the "License").  You
   may not use this file except in compliance with the License.  You can
   obtain a copy of the License at
   https://github.com/payara/Payara/blob/master/LICENSE.txt
   See the License for the specific
   language governing permissions and limitations under the License.
  
   When distributing the software, include this License Header Notice in each
   file and include the License file at glassfish/legal/LICENSE.txt.
  
   GPL Classpath Exception:
   The Payara Foundation designates this particular file as subject to the "Classpath"
   exception as provided by the Payara Foundation in the GPL Version 2 section of the License
   file that accompanied this code.
  
   Modifications:
   If applicable, add the following below the License Header, with the fields
   enclosed by brackets [] replaced by your own identifying information:
   "Portions Copyright [year] [name of copyright owner]"
  
   Contributor(s):
   If you wish your version of this file to be governed by only the CDDL or
   only the GPL Version 2, indicate your decision by adding "[Contributor]
   elects to include this software in this distribution under the [CDDL or GPL
   Version 2] license."  If you don't indicate a single choice of license, a
   recipient has the option to distribute your version of this file under
   either the CDDL, the GPL Version 2 or to extend the choice of license to
   its licensees as provided above.  However, if you add GPL Version 2 code
   and therefore, elected the GPL Version 2 license, then the option applies
   only if the new code is made subject to such option by the copyright
   holder.
*/

/*jshint esversion: 8 */

Chart.defaults.global.defaultFontColor = "#007FFF";
Chart.defaults.global.tooltips.enabled = false;

/**
 * The different parts of the Monitoring Console are added as the below properties by the individual files.
 */
const MonitoringConsole =  {

  /**
   * Static configuration data for page presets.
   */
  Data: {},

   /**
    * Functions of manipulate the model of the MC (often returns a layout that is applied to the View)
    **/ 
  Model: {},

   /**
    * Functions to update the actual HTML page of the MC
    **/
	View: {},

   /**
    * API functions to talk to the server.
    **/ 
  Controller: {},

   /**
    * Functions specifically to take the data and prepare the display particular chart type using the underlying charting library.
    *
    * Each of the type objects below shares the same public API that is used by Model and View to apply the model to the chart to update the view properly.
    **/
	Chart: {

    /**
     * A collection of general adapter functions for the underlying chart library
     */ 
    Common: {},
   /**
    * Line chart adapter API for monitoring series data
    **/
    Line: {},
   /**
    * Bar chart adapter API for monitoring series data
    **/
    Bar: {},

    /**
     * Trace 'gantt chart' like API, this is not a strict adapter API as the other two as the data to populate this is specific to traces
     */
    Trace: {},

  }
};
MonitoringConsole.Chart.getAPI = function(widget) {
  switch (widget.type) {
    default:
    case 'line': return MonitoringConsole.Chart.Line;
    case 'bar': return MonitoringConsole.Chart.Bar;
    case 'alert': return MonitoringConsole.Chart.Line;
  }
};
/*
   DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
  
   Copyright (c) 2020 Payara Foundation and/or its affiliates. All rights reserved.
  
   The contents of this file are subject to the terms of either the GNU
   General Public License Version 2 only ("GPL") or the Common Development
   and Distribution License("CDDL") (collectively, the "License").  You
   may not use this file except in compliance with the License.  You can
   obtain a copy of the License at
   https://github.com/payara/Payara/blob/master/LICENSE.txt
   See the License for the specific
   language governing permissions and limitations under the License.
  
   When distributing the software, include this License Header Notice in each
   file and include the License file at glassfish/legal/LICENSE.txt.
  
   GPL Classpath Exception:
   The Payara Foundation designates this particular file as subject to the "Classpath"
   exception as provided by the Payara Foundation in the GPL Version 2 section of the License
   file that accompanied this code.
  
   Modifications:
   If applicable, add the following below the License Header, with the fields
   enclosed by brackets [] replaced by your own identifying information:
   "Portions Copyright [year] [name of copyright owner]"
  
   Contributor(s):
   If you wish your version of this file to be governed by only the CDDL or
   only the GPL Version 2, indicate your decision by adding "[Contributor]
   elects to include this software in this distribution under the [CDDL or GPL
   Version 2] license."  If you don't indicate a single choice of license, a
   recipient has the option to distribute your version of this file under
   either the CDDL, the GPL Version 2 or to extend the choice of license to
   its licensees as provided above.  However, if you add GPL Version 2 code
   and therefore, elected the GPL Version 2 license, then the option applies
   only if the new code is made subject to such option by the copyright
   holder.
*/

/*jshint esversion: 8 */

/**
 * API to talk to the server.
 *
 * Main purpose is isolate and document the API between client and server.
 **/
MonitoringConsole.Controller = (function() {

   function requestWithJsonBody(method, url, queryData, onSuccess, onFailure) {
      $.ajax({
         url: url,
         type: method,
         data: JSON.stringify(queryData),
         contentType:"application/json; charset=utf-8",
         dataType:"json",
      }).done(onSuccess).fail(onFailure);
   }

   function requestWithoutBody(method, url, onSuccess, onFailure) {
      $.ajax({ type: method, url: url }).done(onSuccess).fail(onFailure);
   }

   function requestJSON(url, onSuccess, onFailure) {
      $.getJSON(url, onSuccess).fail(onFailure);
   }

   /**
    * @param {array|object} queries   - a JS array with query objects as expected by the server API (object corresponds to java class SeriesQuery)
    *                                   or a JS object corresponding to java class SeriesRequest
    * @param {function}     onSuccess - a callback function with one argument accepting the response object as send by the server (java class SeriesResponse)
    * @param {function}     onFailure - a callback function with no arguments
    */
   function requestListOfSeriesData(queries, onSuccess, onFailure) {
      const request = !Array.isArray(queries) ? queries : { queries: queries }; 
      requestWithJsonBody('POST', 'api/series/data/', request, onSuccess, onFailure);
   }

   /**
    * @param {function} onSuccess - a function with one argument accepting an array of series names
    * @param {function} onFailure - a callback function with no arguments
    */
   function requestListOfSeriesNames(onSuccess, onFailure) {
      requestJSON("api/series/", onSuccess, onFailure);
   }

   /**
    * @param {string}   series    - name of the metric series
    * @param {function} onSuccess - a function with one argument accepting an array request traces as returned by the server (each trace object corresponds to java class RequestTraceResponse)
    * @param {function} onFailure - a callback function with no arguments
    */
   function requestListOfRequestTraces(series, onSuccess, onFailure) {
      requestJSON("api/trace/data/" + series, onSuccess, onFailure);
   }

   /**
    * @param {function} onSuccess - a callback function with no arguments
    * @param {function} onFailure - a callback function with no arguments
    */
   function requestListOfWatches(onSuccess, onFailure) {
      requestJSON("api/watches/data/", (response) => onSuccess(response.watches), onFailure);
   }

   /**
    * @param {object}   watch     - a JS watch object as expected by the server API (object corresponds to java class WatchData)
    * @param {function} onSuccess - a callback function with no arguments
    * @param {function} onFailure - a callback function with no arguments
    */
   function requestCreateWatch(watch, onSuccess, onFailure) {
      requestWithJsonBody('PUT', 'api/watches/data/', watch, onSuccess, onFailure);
   }

   /**
    * @param {string}   name      - name of the watch to delete
    * @param {function} onSuccess - a callback function with no arguments
    * @param {function} onFailure - a callback function with no arguments
    */
   function requestDeleteWatch(name, onSuccess, onFailure) {
      requestWithoutBody('DELETE', 'api/watches/data/' + name + '/', onSuccess, onFailure);
   }

   /**
    * @param {string}   name      - name of the watch to disable
    * @param {function} onSuccess - a callback function with no arguments
    * @param {function} onFailure - a callback function with no arguments
    */
   function requestDisableWatch(name, onSuccess, onFailure) {
      requestWithoutBody('PATCH', 'api/watches/data/' + name + '/?disable=true', onSuccess, onFailure);
   }

   /**
    * @param {string}   name      - name of the watch to enable
    * @param {function} onSuccess - a callback function with no arguments
    * @param {function} onFailure - a callback function with no arguments
    */
   function requestEnableWatch(name, onSuccess, onFailure) {
      requestWithoutBody('PATCH', 'api/watches/data/' + name + '/?disable=false', onSuccess, onFailure);
   }

   /**
    * @param {number}   serial    - serial of the alert to ackknowledge
    * @param {function} onSuccess - a callback function with no arguments
    * @param {function} onFailure - a callback function with no arguments
    */
   function requestAcknowledgeAlert(serial, onSuccess, onFailure) {
      requestWithoutBody('POST', 'api/alerts/ack/' + serial + '/', onSuccess, onFailure);
   }

   /**
    * @param {object}   page      - a JS page object as defined and used by the UI
    * @param {function} onSuccess - a callback function with no arguments
    * @param {function} onFailure - a callback function with no arguments
    */
   function requestUpdateRemotePage(page, onSuccess, onFailure) {
      requestWithJsonBody('PUT', 'api/pages/data/' + page.id + '/', page, onSuccess, onFailure);
   }

   /**
    * @param {string}   pageId    - ID of the page to delete
    * @param {function} onSuccess - a callback function with no arguments
    * @param {function} onFailure - a callback function with no arguments
    */
   function requestDeleteRemotePage(pageId, onSuccess, onFailure)  {
      requestWithoutBody('DELETE', 'api/pages/data/' + pageId + '/', onSuccess, onFailure);
   }

   /**
    * @param {string}   pageId    - ID of the page to get from server
    * @param {function} onSuccess - a function with one argument accepting an array request traces as returned by the server (each trace object corresponds to java class RequestTraceResponse)
    * @param {function} onFailure - a callback function with no arguments
    */
   function requestRemotePage(pageId, onSuccess, onFailure) {
      requestJSON('api/pages/data/' + pageId + '/', onSuccess, onFailure);
   }

   /**
    * @param {function} onSuccess - a callback function with no arguments
    * @param {function} onFailure - a callback function with no arguments
    */
   function requestListOfRemotePages(onSuccess, onFailure) {
      requestJSON('api/pages/data/', onSuccess, onFailure);
   }

   /**
    * @param {function} onSuccess - a function with one argument accepting an array of page names
    * @param {function} onFailure - a callback function with no arguments
    */
   function requestListOfRemotePageNames(onSuccess, onFailure) {
      requestJSON("api/pages/", onSuccess, onFailure);
   }

   /**
    * Public API to talk to the server.
    * 
    * Note that none of the functions have a direct return value.
    * All function "return" data by calling their "onSuccess" callback with the result
    * or the "onFailure" callback in case the equest failed.
    */ 
   return {
      requestListOfSeriesData: requestListOfSeriesData,
      requestListOfSeriesNames: requestListOfSeriesNames,
      requestListOfRequestTraces: requestListOfRequestTraces,
      requestListOfWatches: requestListOfWatches,
      requestCreateWatch: requestCreateWatch,
      requestDeleteWatch: requestDeleteWatch,
      requestDisableWatch: requestDisableWatch,
      requestEnableWatch: requestEnableWatch,
      requestAcknowledgeAlert: requestAcknowledgeAlert,
      requestUpdateRemotePage: requestUpdateRemotePage,
      requestDeleteRemotePage: requestDeleteRemotePage,
      requestRemotePage: requestRemotePage,
      requestListOfRemotePages: requestListOfRemotePages,
      requestListOfRemotePageNames: requestListOfRemotePageNames,
   };
})();
/*
   DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
  
   Copyright (c) 2020 Payara Foundation and/or its affiliates. All rights reserved.
  
   The contents of this file are subject to the terms of either the GNU
   General Public License Version 2 only ("GPL") or the Common Development
   and Distribution License("CDDL") (collectively, the "License").  You
   may not use this file except in compliance with the License.  You can
   obtain a copy of the License at
   https://github.com/payara/Payara/blob/master/LICENSE.txt
   See the License for the specific
   language governing permissions and limitations under the License.
  
   When distributing the software, include this License Header Notice in each
   file and include the License file at glassfish/legal/LICENSE.txt.
  
   GPL Classpath Exception:
   The Payara Foundation designates this particular file as subject to the "Classpath"
   exception as provided by the Payara Foundation in the GPL Version 2 section of the License
   file that accompanied this code.
  
   Modifications:
   If applicable, add the following below the License Header, with the fields
   enclosed by brackets [] replaced by your own identifying information:
   "Portions Copyright [year] [name of copyright owner]"
  
   Contributor(s):
   If you wish your version of this file to be governed by only the CDDL or
   only the GPL Version 2, indicate your decision by adding "[Contributor]
   elects to include this software in this distribution under the [CDDL or GPL
   Version 2] license."  If you don't indicate a single choice of license, a
   recipient has the option to distribute your version of this file under
   either the CDDL, the GPL Version 2 or to extend the choice of license to
   its licensees as provided above.  However, if you add GPL Version 2 code
   and therefore, elected the GPL Version 2 license, then the option applies
   only if the new code is made subject to such option by the copyright
   holder.
*/

/*jshint esversion: 8 */

/**
 * Conains the "static" data of the monitoring console.
 * Most of all this are the page presets.
 */
MonitoringConsole.Data = (function() {

	/**
	 * List of known name-spaces and their text description.
	 */
    const NAMESPACES = {
        web: 'Web Statistics',
        http: 'HTTP Statistics',
        jvm: 'JVM Statistics',
        metric: 'MP Metrics',
        trace: 'Request Tracing',
        map: 'Cluster Map Storage Statistics',
        topic: 'Cluster Topic IO Statistics',
        monitoring: 'Monitoring Console Internals',
        health: 'Health Checks',
        sql: 'SQL Tracing',
        other: 'Other',
    };

    /**
     * Description texts used in the preset pages...
     */
	const TEXT_HTTP_HIGH = "Requires *HTTP monitoring* to be enabled: Goto _Configurations_ => _Monitoring_ and set *'HTTP Service'* to *'HIGH'*.";
	const TEXT_WEB_HIGH = "Requires *WEB monitoring* to be enabled: Goto _Configurations_ => _Monitoring_ and set *'Web Container'* to *'HIGH'*.";
	const TEXT_REQUEST_TRACING = "If you did enable request tracing at _Configurations_ => _Request Tracing_ not seeing any data means no requests passed the tracing threshold which is a good thing.";

	const TEXT_CPU_USAGE = "Requires *CPU Usage HealthCheck* to be enabled: Goto _Configurations_ => _HealthCheck_ => _CPU Usage_ tab and check *'enabled'*";
	const TEXT_HEAP_USAGE = "Requires *Heap Usage HealthCheck* to be enabled: Goto _Configurations_ => _HealthCheck_ => _Heap Usage_ tab and check *'enabled'*";
	const TEXT_GC_PERCENTAGE = "Requires *Garbage Collector HealthCheck* to be enabled: Goto _Configurations_ => _HealthCheck_ => _Garbage Collector_ tab and check *'enabled'*";
	const TEXT_MEM_USAGE = "Requires *Machine Memory HealthCheck* to be enabled: Goto _Configurations_ => _HealthCheck_ => _Machine Memory Usage_ tab and check *'enabled'*";
	const TEXT_POOL_USAGE = "Requires *Connection Pool HealthCheck* to be enabled: Goto _Configurations_ => _HealthCheck_ => _Connection Pool_ tab and check *'enabled'*";
	const TEXT_LIVELINESS = "Requires *MicroProfile HealthCheck Checker* to be enabled: Goto _Configurations_ => _HealthCheck_ => _MicroProfile HealthCheck Checker_ tab and check *'enabled'*";

	/**
	 * Page preset information improted on page load.
	 */
	const PAGES = {
		core: {
			name: 'Core',
			numberOfColumns: 3,
			widgets: [
				{ series: 'ns:jvm HeapUsage', unit: 'percent',  
					grid: { item: 1, column: 0}, 
					axis: { min: 0, max: 100 },
					decorations: {
						thresholds: { reference: 'now', alarming: { value: 50, display: true }, critical: { value: 80, display: true }}}},
				{ series: 'ns:jvm CpuUsage', unit: 'percent',
					grid: { item: 1, column: 1}, 
					axis: { min: 0, max: 100 },
					decorations: {
						thresholds: { reference: 'now', alarming: { value: 50, display: true }, critical: { value: 80, display: true }}}},							
				{ series: 'ns:jvm ThreadCount', unit: 'count',  
					grid: { item: 0, column: 1}},
				{ series: 'ns:http ThreadPoolCurrentThreadUsage', unit: 'percent',
					grid: { item: 1, column: 2},
					status: { missing: { hint: TEXT_HTTP_HIGH }},
					axis: { min: 0, max: 100 },
					decorations: {
						thresholds: { reference: 'avg', alarming: { value: 50, display: true }, critical: { value: 80, display: true }}}},														
				{ series: 'ns:web RequestCount', unit: 'count',
					grid: { item: 0, column: 2}, 
					options: { perSec: true },
					status: { missing: { hint: TEXT_WEB_HIGH }}},
				{ series: 'ns:web ActiveSessions', unit: 'count',
					grid: { item: 0, column: 0},
					status: { missing: { hint: TEXT_WEB_HIGH }}},
			]
		},
		rag: {
			name: 'RAG Status',
			numberOfColumns: 4,
			type: 'query',
			content: { series: 'ns:health ?:* *', maxSize: 32, ttl: 60 },
		},
		request_tracing: {
			name: 'Request Tracing',
			numberOfColumns: 4,
			widgets: [
				{ id: '1 ns:trace @:* Duration', series: 'ns:trace @:* Duration', type: 'bar', unit: 'ms',
					displayName: 'Trace Duration Range',
					grid: { item: 0, column: 0, colspan: 4, rowspan: 1 },
					axis: { min: 0, max: 5000 },
					options: { drawMinLine: true },
					status: { missing: { hint: TEXT_REQUEST_TRACING }},
					coloring: 'instance-series',
					decorations: { alerts: { noAmber: true, noRed: true }}},
				{ id: '2 ns:trace @:* Duration', series: 'ns:trace @:* Duration', type: 'line', unit: 'ms', 
					displayName: 'Trace Duration Above Threshold',
					grid: { item: 1, column: 0, colspan: 2, rowspan: 3 },
					options: { noFill: true },
					coloring: 'instance-series'},
				{ id: '3 ns:trace @:* Duration', series: 'ns:trace @:* Duration', type: 'annotation', unit: 'ms',
					displayName: 'Trace Data',
					grid: { item: 1, column: 2, colspan: 2, rowspan: 3 },
					coloring: 'instance-series'},
			]
		},
		http: {
			name: 'HTTP',
			numberOfColumns: 3,
			widgets: [
				{ series: 'ns:http ConnectionQueueCountOpenConnections', unit: 'count',
					grid: { column: 0, item: 0},
					status: { missing : { hint: TEXT_HTTP_HIGH }}},
				{ series: 'ns:http ThreadPoolCurrentThreadsBusy', unit: 'count',
					grid: { column: 0, item: 1},
					status: { missing : { hint: TEXT_HTTP_HIGH }}},
				{ series: 'ns:http ServerCount2xx', unit: 'count', 
					grid: { column: 1, item: 0},
					options: { perSec: true },
					status: { missing : { hint: TEXT_HTTP_HIGH }}},
				{ series: 'ns:http ServerCount3xx', unit: 'count', 
					grid: { column: 1, item: 1},
					options: { perSec: true },
					status: { missing : { hint: TEXT_HTTP_HIGH }}},
				{ series: 'ns:http ServerCount4xx', unit: 'count', 
					grid: { column: 2, item: 0},
					options: { perSec: true },
					status: { missing : { hint: TEXT_HTTP_HIGH }}},
				{ series: 'ns:http ServerCount5xx', unit: 'count', 
					grid: { column: 2, item: 1},
					options: { perSec: true },
					status: { missing : { hint: TEXT_HTTP_HIGH }}},
			]
		},
		health_checks: {
			name: 'Health Checks',
			numberOfColumns: 4,
			widgets: [
				{ series: 'ns:health CpuUsage', unit: 'percent', displayName: 'CPU',
  					grid: { column: 0, item: 0},
  					axis: { max: 100 },
  					status: { missing : { hint: TEXT_CPU_USAGE }}},
				{ series: 'ns:health HeapUsage', unit: 'percent', displayName: 'Heap',
  					grid: { column: 1, item: 1},
  					axis: { max: 100 },
  					status: { missing : { hint: TEXT_HEAP_USAGE }}},
				{ series: 'ns:health TotalGcPercentage', unit: 'percent', displayName: 'GC',
  					grid: { column: 1, item: 0},
  					axis: { max: 30 },
  					status: { missing : { hint: TEXT_GC_PERCENTAGE }}},
				{ series: 'ns:health PhysicalMemoryUsage', unit: 'percent', displayName: 'Memory',
  					grid: { column: 0, item: 1},
  					axis: { max: 100 },
  					status: { missing : { hint: TEXT_MEM_USAGE }}},
				{ series: 'ns:health @:* PoolUsage', unit: 'percent', coloring: 'series', displayName: 'Connection Pools',
  					grid: { column: 1, item: 2},
  					axis: { max: 100 },          					
  					status: { missing : { hint: TEXT_POOL_USAGE }}},
				{ series: 'ns:health LivelinessUp', unit: 'percent', displayName: 'MP Health',
  					grid: { column: 0, item: 2},
  					axis: { max: 100 },
  					options: { noCurves: true },
  					status: { missing : { hint: TEXT_LIVELINESS }}},
				{ series: 'ns:health ?:* *', unit: 'percent', type: 'alert', displayName: 'Alerts',
  					grid: { column: 2, item: 0, colspan: 2, rowspan: 3}}, 
			]
		},
		monitoring: {
			name: 'Monitoring',
			numberOfColumns: 3,
			widgets: [
				{ series: 'ns:monitoring @:* CollectionDuration', unit: 'ms', displayName: 'Sources Time',
					grid: { column: 0, item: 0, span: 2},
					axis: { max: 200 },
					coloring: 'series'},
				{ series: 'ns:monitoring @:* AlertCount', displayName: 'Alerts',
					grid: { column: 2, item: 2},
					coloring: 'series'},
				{ series: 'ns:monitoring CollectedSourcesCount', displayName: 'Sources',
					grid: { column: 0, item: 2}},
				{ series: 'ns:monitoring CollectedSourcesErrorCount', displayName: 'Sources with Errors', 
					grid: { column: 1, item: 2}},
				{ series: 'ns:monitoring CollectionDuration', unit: 'ms', displayName: 'Metrics Time',
					grid: { column: 2, item: 0},
					axis: { max: 1000},
					options: { drawMaxLine: true }},
				{ series: 'ns:monitoring WatchLoopDuration', unit: 'ms', displayName: 'Watches Time', 
					grid: { column: 2, item: 1},
					options: { drawMaxLine: true }},
			],
		},
		jvm: {
			name: 'JVM',
			numberOfColumns: 3,
			widgets: [
				{ series: 'ns:jvm TotalLoadedClassCount', displayName: 'Loaded Classes', 
					grid: { column: 2, item: 0}},
				{ series: 'ns:jvm UnLoadedClassCount', displayName: 'Unloaded Classes',
					grid: { column: 2, item: 1}},
				{ series: 'ns:jvm CommittedHeapSize', unit: 'bytes', displayName: 'Heap Size',
					grid: { column: 1, item: 0}},
				{ series: 'ns:jvm UsedHeapSize', unit: 'bytes', displayName: 'Used Heap', 
					grid: { column: 0, item: 0}},
				{ series: 'ns:jvm ThreadCount', displayName: 'Live Threads', 
					grid: { column: 1, item: 1}},
				{ series: 'ns:jvm DaemonThreadCount', displayName: 'Daemon Threads',
					grid: { column: 0, item: 1}},
			],
		},
		sql: {
			name: 'SQL',
			numberOfColumns: 3,
			widgets: [
				{ id: '1 ns:sql @:* MaxExecutionTime', 
					unit: 'ms',
					type: 'annotation',
					series: 'ns:sql @:* MaxExecutionTime',
					displayName: 'Slow SQL Queries',
					grid: { column: 0, item: 1, colspan: 2, rowspan: 2},
					mode: 'table',
					sort: 'value',
					fields: ['Timestamp', 'SQL', 'Value']},
				{ id: '2 ns:sql @:* MaxExecutionTime',
					unit: 'ms',
					series: 'ns:sql @:* MaxExecutionTime',
					displayName: 'Worst SQL Execution Time',
					grid: { column: 2, item: 1 },
					coloring: 'series' },						
				{ id: '3 ns:sql @:* MaxExecutionTime',
					unit: 'ms',
					type: 'alert',
					series: 'ns:sql @:* MaxExecutionTime',
					displayName: 'Slow SQL Alerts',
					grid: { column: 2, item: 2 },
					options: { noAnnotations: true }},
			],
		},
		alerts: {
			name: 'Alerts',
			numberOfColumns: 1,
			widgets: [
				{id: '1 ?:* *', series: '?:* *', type: 'alert', displayName: 'Ongoing Alerts',
					grid: {column: 0, item: 1},
					decorations: { alerts: { noStopped: true }},
					options: { noAnnotations: true}},
				{id: '2 ?:* *', series: '?:* *', type: 'alert', displayName: 'Past Unacknowledged Alerts',
					grid: {column: 0, item: 2},
					decorations: { alerts: { noOngoing: true, noAcknowledged: true}},
					options: { noAnnotations: true}},
			],
		},
		threads: {
			name: 'Threads',
			numberOfColumns: 4,
			widgets: [
				{ series: 'ns:health StuckThreadDuration', type: 'annotation', mode: 'table', unit: 'ms',
					displayName: 'Stuck Thread Incidents',
					grid: {column: 0, item: 1, colspan: 3, rowspan: 1},
					fields: ["Thread", "Started", "Value", "Threshold", "Suspended", "Locked", "State"]},
				{ series: 'ns:health HoggingThreadDuration', type: 'annotation', mode: 'table', unit: 'ms',
					displayName: 'Hogging Thread Incidents',
					grid: {column: 0, item: 2, colspan: 3, rowspan: 1},
					fields: ["Thread", "When", "Value", "Usage%", "Threshold%", "Method", "Exited"]},
				{ series: 'ns:jvm ThreadCount', displayName: 'Live Threads', 
					grid: {column: 3, item: 1}},
				{ series: 'ns:jvm DaemonThreadCount', displayName: 'Daemon Threads', 
					grid: {column: 3, item: 2}},							
			],
		},
		application_metrics: {
			name: 'Application Metrics',
			type: 'query',
			numberOfColumns: 4,
			content: { series: 'ns:metric ?:* *', maxSize: 32, ttl: 60 },
		}
	};


	/**
	 * Public API for data access
	 */ 
	return { 
		PAGES: PAGES,
		NAMESPACES: NAMESPACES,
	};
})();
/*
   DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
  
   Copyright (c) 2019-2020 Payara Foundation and/or its affiliates. All rights reserved.
  
   The contents of this file are subject to the terms of either the GNU
   General Public License Version 2 only ("GPL") or the Common Development
   and Distribution License("CDDL") (collectively, the "License").  You
   may not use this file except in compliance with the License.  You can
   obtain a copy of the License at
   https://github.com/payara/Payara/blob/master/LICENSE.txt
   See the License for the specific
   language governing permissions and limitations under the License.
  
   When distributing the software, include this License Header Notice in each
   file and include the License file at glassfish/legal/LICENSE.txt.
  
   GPL Classpath Exception:
   The Payara Foundation designates this particular file as subject to the "Classpath"
   exception as provided by the Payara Foundation in the GPL Version 2 section of the License
   file that accompanied this code.
  
   Modifications:
   If applicable, add the following below the License Header, with the fields
   enclosed by brackets [] replaced by your own identifying information:
   "Portions Copyright [year] [name of copyright owner]"
  
   Contributor(s):
   If you wish your version of this file to be governed by only the CDDL or
   only the GPL Version 2, indicate your decision by adding "[Contributor]
   elects to include this software in this distribution under the [CDDL or GPL
   Version 2] license."  If you don't indicate a single choice of license, a
   recipient has the option to distribute your version of this file under
   either the CDDL, the GPL Version 2 or to extend the choice of license to
   its licensees as provided above.  However, if you add GPL Version 2 code
   and therefore, elected the GPL Version 2 license, then the option applies
   only if the new code is made subject to such option by the copyright
   holder.
*/

/*jshint esversion: 8 */

/**
 * The object that manages the internal state of the monitoring console page.
 * 
 * It depends on the MonitoringConsole.Utils object.
 */
MonitoringConsole.Model = (function() {
	/**
	 * Key used in local stage for the userInterface
	 */
	const LOCAL_UI_KEY = 'fish.payara.monitoring-console.defaultConfigs';
	

	const Data = MonitoringConsole.Data;
	const Controller = MonitoringConsole.Controller;

	//TODO idea: Classification. one can setup a table where a value range is assigned a certain state - this table is used to show that state in the UI, simple but effective

	function getPageId(name) {
    	return name.replace(/[^-a-zA-Z0-9]/g, '_').toLowerCase();
    }

	
	/**
	 * Internal API for managing set model of the user interface.
	 */
	var UI = (function() {

		/**
		 * All page properties must not be must be values as page objects are converted to JSON and back for local storage.
		 * 
		 * {object} - map of pages, name of page as key/field;
		 * {string} name - name of the page
		 * {object} widgets -  map of the chart configurations with series as key
		 * 
		 * Each page is an object describing a page or tab containing one or more graphs by their configuration.
		 */
		var pages = {};
		
		/**
		 * General settings for the user interface
		 */
		var settings = sanityCheckSettings({});
		
		/**
		 * Makes sure the page data structure has all required attributes.
		 */
		function sanityCheckPage(page) {
			if (!page.id)
				page.id = getPageId(page.name);
			if (!page.widgets)
				page.widgets = {};
			if (page.type === undefined)
				page.type = 'manual';
			if (typeof page.sync !== 'object')
				page.sync = { autosync: true };
			if (typeof page.content !== 'object')
				page.content = {};
			if (!page.numberOfColumns || page.numberOfColumns < 1)
				page.numberOfColumns = 1;
			if (page.rotate === undefined)
				page.rotate = true;
			page.widgets = sanityCheckWidgets(page.widgets);
			return page;
		}

		function sanityCheckWidgets(widgets) {
			// make widgets from array to object if needed
			let widgetsArray = Array.isArray(widgets) ? widgets : Object.values(widgets);
			widgetsArray.forEach(sanityCheckWidget);
			let widgetsObj = {};
			for (let widget of widgetsArray)
				widgetsObj[widget.id] = widget;
			return widgetsObj;
		}
		
		/**
		 * Makes sure a widget (configiguration for a chart) within a page has all required attributes
		 */
		function sanityCheckWidget(widget) {
			if (!widget.id)
				widget.id = '1 ' + widget.series;
			widget.target = 'chart-' + widget.id.replace(/[^-a-zA-Z0-9_]/g, '_');
			if (!widget.type)
				widget.type = 'line';
			if (!widget.unit)
				widget.unit = 'count';
			if (typeof widget.options !== 'object')
				widget.options = {};
			//TODO no data can be a good thing (a series hopefully does not come up => render differently to "No Data" => add a config for that switch)
			if (typeof widget.grid !== 'object')
				widget.grid = {};
			if (typeof widget.decorations !== 'object')
				widget.decorations = {};
			if (typeof widget.decorations.waterline !== 'object') {
				let value = typeof widget.decorations.waterline === 'number' ? widget.decorations.waterline : undefined;
				widget.decorations.waterline = { value: value };
			}
			if (typeof widget.decorations.thresholds !== 'object')
				widget.decorations.thresholds = {};
			if (typeof widget.decorations.alerts !== 'object')
				widget.decorations.alerts = {};
			if (typeof widget.decorations.annotations !== 'object')
				widget.decorations.annotations = {};							
			if (typeof widget.decorations.thresholds.alarming !== 'object')
				widget.decorations.thresholds.alarming = {};			
			if (typeof widget.decorations.thresholds.critical !== 'object')
				widget.decorations.thresholds.critical = {};			
			if (typeof widget.axis !== 'object')
				widget.axis = {};
			if (typeof widget.status !== 'object')
				widget.status = {};
			if (typeof widget.status.missing !== 'object')
				widget.status.missing = {};
			if (typeof widget.status.alarming !== 'object')
				widget.status.alarming = {};
			if (typeof widget.status.critical !== 'object')
				widget.status.critical = {};
			return widget;
		}

		function sanityCheckSettings(settings) {
			if (settings === undefined)
				settings = {};
			if (settings.theme === undefined)
				settings.theme = {};
			if (settings.theme.colors === undefined)
				settings.theme.colors = {};
			if (settings.theme.options === undefined)
				settings.theme.options = {};			
			return settings;
		}
		
		function doStore(isPageUpdate, page) {
			if (page === undefined)
				page = pages[settings.home];
			if (isPageUpdate) {
				page.sync.lastModifiedLocally = new Date().getTime();
			}
			window.localStorage.setItem(LOCAL_UI_KEY, doExport());
			if (isPageUpdate && page.sync.autosync && settings.role == 'admin') {
				doPushLocal(undefined, page);
			}
		}

		function doPushLocal(onSuccess, page) {
			if (page === undefined)
				page = pages[settings.home];
			let basedOnRemoteLastModified = page.sync.basedOnRemoteLastModified;
			let lastModifiedLocally = page.sync.lastModifiedLocally;
			let preferredOverRemoteLastModified = page.sync.preferredOverRemoteLastModified;
			page.sync.basedOnRemoteLastModified = lastModifiedLocally || new Date().getTime();
			page.sync.lastModifiedLocally = undefined;
			page.sync.preferredOverRemoteLastModified = undefined;
			Controller.requestUpdateRemotePage(page, 
				() => { // success
					doStore();
					if (onSuccess)
						onSuccess();
				},
				() => { // failure
					page.sync.basedOnRemoteLastModified = basedOnRemoteLastModified;
					page.sync.lastModifiedLocally = lastModifiedLocally;
					page.sync.preferredOverRemoteLastModified = preferredOverRemoteLastModified;
				}
			);			
		}

		function doPushAllLocal() {	
			Controller.requestListOfRemotePageNames((pageIds) => {
				pageIds.forEach(pageId => {
					doPushLocal(undefined, pages[pageId]);
				});
			});
		}

		function doPullRemote(pageId) {
			if (pageId === undefined)
				pageId = settings.home;
			return new Promise(function(resolve, reject) {
				Controller.requestRemotePage(pageId, (page) => {				
					pages[page.id] = page;
					doStore(false, page);
					resolve(page);
				}, () => reject(undefined));
			});
		}

		function providePullRemoteModel(consumer) {
			function createPullRemoteModelItem(localPage, remotePage) {
				let page = localPage !== undefined ? localPage : remotePage;
				let checked = true;
				if ((settings.role == 'admin' || settings.role == 'user') && localPage !== undefined) {
					checked = (localPage.sync.preferredOverRemoteLastModified === undefined 
							|| localPage.sync.preferredOverRemoteLastModified < remotePage.sync.basedOnRemoteLastModified)
							&& !(localPage.sync.basedOnRemoteLastModified == remotePage.sync.basedOnRemoteLastModified && localPage.sync.lastModifiedLocally === undefined);
				}
				return {
					id: page.id,
					name: page.name,
					checked: checked,
					lastLocalChange: localPage != undefined ? localPage.sync.lastModifiedLocally : undefined,
					lastRemoteChange: remotePage !== undefined ? remotePage.sync.basedOnRemoteLastModified : undefined,
					lastRemoteUpdate: localPage != undefined ? localPage.sync.basedOnRemoteLastModified : undefined,
				};
			}

			Controller.requestListOfRemotePages(remotePages => {
				consumer({ 
					pages: Object.values(remotePages).map(remotePage => createPullRemoteModelItem(pages[remotePage.id], remotePage)), 
					onUpdate: async function (pageIds) {
						for (let remotePageId of Object.keys(remotePages)) {
							if (!pageIds.includes(remotePageId)) {
								pages[remotePageId].sync.preferredOverRemoteLastModified = remotePages[remotePageId].sync.basedOnRemoteLastModified;
							}
						}
						await Promise.all(pageIds.map(pageId => doPullRemote(pageId)));
					}
				});
			});
		}
		
		function doDeselect() {
			Object.values(pages[settings.home].widgets)
				.forEach(widget => widget.selected = false);
		}
		
		function doCreate(name) {
			if (!name)
				throw "New page must have a unique name";
			var id = getPageId(name);
			if (pages[id])
				throw "A page with name "+name+" already exist";
			let page = sanityCheckPage({name: name});
			pages[page.id] = page;
			settings.home = page.id;
			return page;
		}

		
		function doImport(userInterface, replaceExisting) {
			if (!userInterface) {
				return false;
			}
			if (userInterface.pages && userInterface.settings)
				settings = sanityCheckSettings(userInterface.settings);
			let importedPages = !userInterface.pages ? userInterface : userInterface.pages;
			// override or add the entry in pages from userInterface
			if (Array.isArray(importedPages)) {
				for (let i = 0; i < importedPages.length; i++) {
					try {
						let page = sanityCheckPage(importedPages[i]);
						if (replaceExisting || pages[page.id] === undefined) {
							pages[page.id] = page;
						}
					} catch (ex) {
					}
				}
			} else {
				for (let [id, page] of Object.entries(importedPages)) {
					try {
						if (replaceExisting || pages[id] === undefined) {
							page.id = id;
							pages[id] = sanityCheckPage(page); 
						}
					} catch (ex) {
					}
				}
			}
			if (settings.home === undefined && Object.keys(pages).length > 0) {
				settings.home = Object.keys(pages)[0];
			}
			doStore();
			return true;
		}
		
		function doExport(prettyPrint) {
			let ui = { pages: pages, settings: settings };
			return prettyPrint ? JSON.stringify(ui, null, 2) : JSON.stringify(ui);
		}

		function readTextFile(file) {
          	return new Promise(function(resolve, reject) {
				let reader = new FileReader();
				reader.onload = function(evt){
				  resolve(evt.target.result);
				};
				reader.onerror = function(err) {
				  reject(err);
				};
				reader.readAsText(file);
          	});
      	}

      	function doLayout(columns) {
			let page = pages[settings.home];
			if (!page)
				return [];
			if (columns)
				page.numberOfColumns = columns;
			let numberOfColumns = page.numberOfColumns || 1;
			// init temporary and result data structure
			let widgetsByColumn = new Array(numberOfColumns);
			var layout = new Array(numberOfColumns);
			for (let col = 0; col < numberOfColumns; col++) {
				widgetsByColumn[col] = [];
				layout[col] = [];
			}
			// organise widgets in columns
			Object.values(page.widgets).forEach(function(widget) {
				let column = widget.grid && widget.grid.column ? widget.grid.column : 0;
				widgetsByColumn[Math.min(Math.max(column, 0), widgetsByColumn.length - 1)].push(widget);
			});
			// order columns by item position
			for (let col = 0; col < numberOfColumns; col++) {
				widgetsByColumn[col] = widgetsByColumn[col].sort(function (a, b) {
					if (!a.grid || !a.grid.item)
						return -1;
					if (!b.grid || !b.grid.item)
						return 1;
					return a.grid.item - b.grid.item;
				});
			}
			// do layout by marking cells with item (left top corner in case of span), null (empty) and undefined (spanned)
			for (let col = 0; col < numberOfColumns; col++) {
				let columnWidgets = widgetsByColumn[col];
				for (let item = 0; item < columnWidgets.length; item++) {
					let widget = columnWidgets[item];
					let colspan = getColSpan(widget, numberOfColumns, col);
					let rowspan = getRowSpan(widget);
					let info = { colspan: colspan, rowspan: rowspan, widget: widget};
					let column0 = layout[col];
					let row0 = getEmptyRowIndex(column0, colspan);
					for (let spanX = 0; spanX < colspan; spanX++) {
						let column = layout[col + spanX];
						if (spanX == 0) {
							if (!widget.grid)
								widget.grid = { column: col, colspan: colspan, rowspan: rowspan }; // init grid
							if (widget.grid.item === undefined)
								widget.grid.item = row0;
							if (widget.grid.colspan === undefined)
								widget.grid.colspan = colspan;
							if (widget.grid.rowspan === undefined)
								widget.grid.rowspan = rowspan;
							widget.grid.span = undefined;						
						} else {
							while (column.length < row0)
								column.push(null); // null marks empty cells
						}
						for (let spanY = 0; spanY < rowspan; spanY++) {
							let cell = spanX === 0 && spanY === 0 ? info : undefined;
							if (row0 + spanY > column.length) {
								column.push(cell);	
							} else {
								column[row0 + spanY] = cell;
							}
						}
					}
				}
			}
			// give the layout a uniform row number
			let maxRows = layout.map(column => column.length).reduce((acc, cur) => acc ? Math.max(acc, cur) : cur);
			for (let col = 0; col < numberOfColumns; col++) {
				while (layout[col].length < maxRows) {
					layout[col].push(null);
				}
			}
			return layout;
      	}

      	function getRowSpan(widget) {
      		let span = widget.grid && widget.grid.span ? widget.grid.span : 1;
      		if (widget.grid && widget.grid.rowspan)
      			span =  widget.grid.rowspan;
      		if (typeof span === 'string')
      			span = parseInt(span);
      		return span;
      	}

      	function getColSpan(widget, numberOfColumns, currentColumn) {
			let span = widget.grid && widget.grid.span ? widget.grid.span : 1;
			if (widget.grid && widget.grid.colspan)
				span = widget.grid.colspan;
			if (typeof span === 'string') {
				if (span === 'full') {
				   span = numberOfColumns;
				} else {
				   span = parseInt(span);
				}
			}
			return span > numberOfColumns - currentColumn ? numberOfColumns - currentColumn : span;
      	}

		/**
		 * @return {number} row position in column where n rows are still empty ('null' marks empty)
		 */
      	function getEmptyRowIndex(column, n) {
      		let row = column.findIndex((elem,index,array) => array.slice(index, index + n).every(e => e === null));
			return row < 0 ? column.length : row;
      	}
		
	   /**
	    * Mapping from a possible MP unit alias to the unit key to use
	    */
	   	const Y_AXIS_UNIT = {
	      days: 'sec',
	      hours: 'sec',
	      minutes: 'sec',	      
	      seconds: 'sec',
	      per_second: 'sec',
	      milliseconds: 'ms',
	      microseconds: 'us',
	      nanoseconds: 'ns',
	      percent: 'percent',
	      bytes: 'bytes',
	      updown: 'updown',
   		};

      	async function doQueryPage() {
      		const page = pages[settings.home];
      		function yAxisUnit(metadata) {
      			if (Y_AXIS_UNIT[metadata.Unit] !== undefined)
      				return Y_AXIS_UNIT[metadata.Unit];
      			if (Y_AXIS_UNIT[metadata.BaseUnit] !== undefined)
      				return Y_AXIS_UNIT[metadata.BaseUnit];
      			return 'count';
      		}
      		if (page.content === undefined)
      			return;
      		const content = new Promise(function(resolve, reject) {
				Controller.requestListOfSeriesData({ groupBySeries: true, queries: [{
	      			widgetId: 'auto', 
	      			series: page.content.series,
	      			truncate: ['ALERTS'],
	      			exclude: []
      			}]}, 
      			(response) => resolve(response.matches),
      			() => reject(undefined));

			});
			let matches = await content;
			matches.sort((a, b) => a.data[0].stableCount - b.data[0].stableCount);
			if (matches.length > page.content.maxSize)
				matches = matches.slice(0, page.content.maxSize);
			const widgets = [];
			const numberOfColumns = page.numberOfColumns;
			let column = 0;
			for (let i = 0; i < matches.length; i++) {
				let match = matches[i];
				let metadata = match.annotations.filter(a => a.permanent)[0];
				let attrs = {};
				let type = 'line';
				if (metadata) {
					if (metadata.atts)
						attrs = metadata.attrs;
				}
				if (attrs.Unit === undefined && match.watches.length > 0) { // is there a watch we can used to get the unit from?
					let watch = match.watches[0];
					attrs.Unit = watch.unit;
					let name = watch.name;
					if (name.indexOf('RAG ') == 0)
						name = name.substring(4);
					attrs.DisplayName = name;
					type = 'rag';
				}
				let data = match.data[0];
				let scaleFactor;
				if (attrs.ScaleToBaseUnit > 1) {
					scaleFactor = Number(attrs.ScaleToBaseUnit);
				}
				let decimalMetric = attrs.Type == 'gauge';
				let unit = yAxisUnit(attrs);
				let max = decimalMetric ? 10000 : 1;
				if (attrs.Unit == 'none' && data.observedMax <= max && data.observedMin >= 0) {
					unit = 'percent';
					scaleFactor = 100;
				}
				widgets.push({
					id: match.series,
					type: type,
					series: match.series,
					displayName: attrs.DisplayName,
					description: attrs.Description,
					grid: { column: column % numberOfColumns, item: column },
					unit: unit,
					options: { 
						decimalMetric: decimalMetric,
					},
					scaleFactor: scaleFactor,
				});
				column++;
			}
			page.widgets = sanityCheckWidgets(widgets);
			page.content.expires = new Date().getTime() + ((page.content.ttl || (60 * 60 * 24 * 365)) * 1000);
			doStore(true, page);
			return page;
      	}

		return {
			themeConfigure(fn) {
				fn(settings.theme);
				doStore();
			},

			themePalette: function(colors) {
				return settings.theme.palette;
			},

			themeOption: function(name, defaultValue) {
				let value = settings.theme.options[name];
				return Number.isNaN(value) || value === undefined ? defaultValue : value;
			},

			themeColor: function(name) {
				return settings.theme.colors[name];
			},

			currentPage: function() {
				return pages[settings.home];
			},			
			
			listPages: function() {
				return Object.values(pages).map(function(page) { 
					return { id: page.id, name: page.name, active: page.id === settings.home };
				});
			},
			
			exportPages: function() {
				return doExport(true);
			},
			
			/**
			 * @param {FileList|object} userInterface - a plain user interface configuration object or a file containing such an object
			 * @param {function} onImportComplete - optional function to call when import is done
			 */
			importPages: async (userInterface, onImportComplete) => {
				if (userInterface instanceof FileList) {
					let file = userInterface[0];
					if (file) {
						let json = await readTextFile(file);
						doImport(JSON.parse(json), true);
					}
				} else {
					doImport(userInterface, true);
				}
				if (onImportComplete)
					onImportComplete();
			},

			queryPage: () => doQueryPage(),
			
			/**
			 * Loads and returns the userInterface from the local storage
			 */
			load: function() {
				let localStorage = window.localStorage;
				let ui = localStorage.getItem(LOCAL_UI_KEY);
				if (ui)
				doImport(JSON.parse(ui), true);
				doImport(JSON.parse(JSON.stringify({ pages: Data.PAGES })), false);
				return pages[settings.home];
			},
			
			/**
			 * Creates a new page with given name, ID is derived from name.
			 * While name can be changed later on the ID is fixed.
			 */
			createPage: function(name) {
				return doCreate(name);
			},
			
			renamePage: function(name) {
				let pageId = getPageId(name);
				if (pages[pageId])
					return false;
				let page = pages[settings.home];
				page.name = name;
				page.id = pageId;
				pages[pageId] = page;
				delete pages[settings.home];
				settings.home = pageId;
				doStore(true);
				return true;
			},

			configurePage: function(change) {
				const page = pages[settings.home];
				change(page);
				doStore(true, page);
			},
			
			/**
			 * Deletes the active page and changes to the first page.
			 * Does not delete the last page.
			 */
			deletePage: function() {
				let pageIds = Object.keys(pages);
				if (pageIds.length <= 1)
					return undefined;
				let deletion = () => {
					delete pages[settings.home];
					settings.home = pageIds[0];
				};
				if (settings.role === 'admin') {
					let page = pages[settings.home];
					if (page.sync.basedOnRemoteLastModified !== undefined) {
						Controller.requestDeleteRemotePage(settings.home, deletion);
					}
				} else {
					deletion();
				}
				return pages[settings.home];
			},

			resetPage: function() {
				let presets = Data.PAGES;
				let currentPageId = settings.home;
				if (presets && presets[currentPageId]) {
					let preset = presets[currentPageId];
					pages[currentPageId] = sanityCheckPage(JSON.parse(JSON.stringify(preset)));
					doStore(true);
					return true;
				}
				return false;
			},

			hasPreset: function() {
				let presets = Data.PAGES;
				return presets && presets[settings.home];
			},
			
			switchPage: function(pageId) {
				if (pageId === undefined) { // rotate to next page from current page
					let pageIds = Object.values(pages).filter(page => page.rotate).map(page => page.id);
					pageId = pageIds[(pageIds.indexOf(settings.home) + 1) % pageIds.length];
				}
				if (!pages[pageId])
					return undefined;
				settings.home = pageId;
				return pages[settings.home];
			},

			rotatePage: function(rotate) {
				if (rotate === undefined)
					return pages[settings.home].rotate;
				pages[settings.home].rotate = rotate;
				doStore(true);
			},
			
			removeWidget: function(widgetId) {
				let widgets = pages[settings.home].widgets;
				if (widgetId && widgets) {
					delete widgets[widgetId];
				}
				doStore(true);
			},
			
			addWidget: function(series, grid) {
				if (!(typeof series === 'string' || Array.isArray(series) && series.length > 0 && typeof series[0] === 'string'))
					throw 'configuration object requires string property `series`';
				doDeselect();
				let layout = doLayout();
				let page = pages[settings.home];
				let widgets = page.widgets;
				let id = (Object.values(widgets)
					.filter(widget => widget.series == series)
					.reduce((acc, widget) => Math.max(acc, widget.id.substr(0, widget.id.indexOf(' '))), 0) + 1) + ' ' + series;				
				let widget = sanityCheckWidget({ id: id, series: series });
				widgets[widget.id] = widget;
				widget.selected = true;
				if (grid !== undefined) {
					widget.grid = grid;
				} else {
					// automatically fill most empty column
					let usedCells = new Array(layout.length);
					for (let i = 0; i < usedCells.length; i++) {
						usedCells[i] = 0;
						for (let j = 0; j < layout[i].length; j++) {
							let cell = layout[i][j];
							if (cell === undefined || cell !== null && typeof cell === 'object')
								usedCells[i]++;
						}
					}
					let indexOfLeastUsedCells = usedCells.reduce((iMin, x, i, arr) => x < arr[iMin] ? i : iMin, 0);
					widget.grid.column = indexOfLeastUsedCells;
					widget.grid.item = Object.values(widgets)
						.filter(widget => widget.grid.column == indexOfLeastUsedCells)
						.reduce((acc, widget) => widget.grid.item ? Math.max(acc, widget.grid.item) : acc, 0) + 1;
				}
				doStore(true);
			},
			
			configureWidget: function(widgetUpdate, widgetId) {
				let selected = widgetId
					? [pages[settings.home].widgets[widgetId]]
					: Object.values(pages[settings.home].widgets).filter(widget => widget.selected);
				selected.forEach(widget => widgetUpdate(widget));
				doStore(selected.length);
			},
			
			select: function(widgetId, exclusive) {
				let page = pages[settings.home];
				let widget = page.widgets[widgetId];
				widget.selected = widget.selected !== true;
				if (exclusive) {
					Object.values(page.widgets).forEach(function(widget) {
						if (widget.id != widgetId) {
							widget.selected = false;
						}
					});
				}
				doStore();
				return widget.selected === true;
			},
			
			deselect: function() {
				doDeselect();
				doStore();
			},
			
			selected: function() {
				return Object.values(pages[settings.home].widgets)
					.filter(widget => widget.selected)
					.map(widget => widget.id);
			},
			
			arrange: function(columns) {
				let layout = doLayout(columns);
				doStore(columns !== undefined);
				return layout;
			},
			
			/*
			 * Settings
			 */
			
			showSettings: function() {
				return settings.display === true
					|| Object.keys(pages[settings.home].widgets).length == 0
					|| Object.values(pages[settings.home].widgets).filter(widget => widget.selected).length > 0;
			},
			openSettings: function() {
				settings.display = true;
				doStore();
			},
			closeSettings: function() {
				settings.display = false;
				doDeselect();
				doStore();
			},

			Role: {
				get: () => settings.role || 'user',
				set: (role) => {
					settings.role = role;
					doStore();					
				},
				isAdmin: () => settings.role == 'admin',
				isGuest: () => settings.role == 'guest',
				isUser:  () => settings.role === undefined || settings.role == 'user',
				isDefined: () => settings.role !== undefined,
			}, 

			Sync: {

				providePullRemoteModel: providePullRemoteModel,

				/*
				 * Updates the local current page with the remote page
				 */
				pullRemote: doPullRemote,

				/*
				 * Updates the remote page with the current local page
				 */
				pushLocal: doPushLocal,

				/*
				 * Updates all remote pages with the respective local page
				 */
				pushAllLocal: doPushAllLocal,

				/*
				 * Should a page automatically be pushed to remote when changed by an admin?
				 */ 
				auto: (autosync) => {
					if (autosync === undefined)
						return pages[settings.home].sync.autosync !== false;
					pages[settings.home].sync.autosync = autosync;
				},

				isLocallyChanged: () => pages[settings.home].sync.lastModifiedLocally,
				isLocal: () => pages[settings.home].sync.basedOnRemoteLastModified === undefined,
				isRemote: () => pages[settings.home].sync.basedOnRemoteLastModified !== undefined,
			},

			Rotation: {
				isEnabled: () => settings.rotation && settings.rotation.enabled,
				enabled: function(enabled) {
					settings.rotation.enabled = enabled === undefined ? true : enabled;
					doStore();
				},
				interval: function(duration) {
					if (!settings.rotation)
						settings.rotation = {};
					if (duration === undefined)
						return settings.rotation.interval;
					if (typeof duration === 'number') {
						settings.rotation.interval = duration;
						doStore();
					}
				}
			},

			Refresh: {
				isPaused: () => settings.refresh && settings.refresh.paused,
				paused: function(paused) {
					settings.refresh.paused = paused;
					doStore();
				},
				interval: function(duration) {
					if (!settings.refresh)
						settings.refresh = {};
					if (duration == undefined)
						return settings.refresh.interval;
					if (typeof duration === 'number') {
						settings.refresh.interval = duration;
						doStore();
					}
				}
			}
		};
	})();
	
	/**
	 * Internal API for managing charts on a page
	 */
	var Charts = (function() {

		/**
		 * {object} - map of the charts objects for active page as created by Chart.js with series as key
		 */
		var charts = {};
		
		function doDestroy(widgetId) {
			let chart = charts[widgetId];
			if (chart) {
				delete charts[widgetId];
				chart.destroy();
			}
		}

		return {
			/**
			 * Returns a new Chart.js chart object initialised for the given MC level configuration to the charts object
			 */
			getOrCreate: function(widget) {
				let widgetId = widget.id;
				let chart = charts[widgetId];
				if (chart)
					return chart;
				chart = MonitoringConsole.Chart.getAPI(widget).onCreation(widget);			
				charts[widgetId] = chart;
				return chart;
			},
			
			clear: function() {
				Object.keys(charts).forEach(doDestroy);
			},
			
			destroy: function(widgetId) {
				doDestroy(widgetId);
			},
			
			update: function(widget) {
				let chart = charts[widget.id];
				if (chart) {
					MonitoringConsole.Chart.getAPI(widget).onConfigUpdate(widget, chart);
					chart.update();
				}
			},
		};
	})();
	
	const DEFAULT_INTERVAL = 2;

	/**
	 * Internal API for data loading from server
	 */
	var Interval = (function() {
		
	    /**
	     * {function} - a function called with no extra arguments when interval tick occured
	     */
	    var onIntervalTick;

		/**
		 * {function} - underlying interval function causing the ticks to occur
		 */
		var intervalFn;
		
		/**
		 * {number} - tick interval in seconds
		 */
		var refreshInterval = DEFAULT_INTERVAL;
		
		function pause() {
			if (intervalFn) {
				clearInterval(intervalFn);
				intervalFn = undefined;
			}
		}

		function resume(atRefreshInterval) {
			onIntervalTick();
			if (atRefreshInterval && atRefreshInterval != refreshInterval) {
				pause();
				refreshInterval = atRefreshInterval;
			}
			if (refreshInterval === 0)
				refreshInterval = DEFAULT_INTERVAL;
			if (intervalFn === undefined) {
				intervalFn = setInterval(onIntervalTick, refreshInterval * 1000);
			}
		}
		
		return {
			
			init: function(onIntervalFn) {
				onIntervalTick = onIntervalFn;
			},
			
			/**
			 * Causes an immediate invocation of the tick target function
			 */
			tick: function() {
				onIntervalTick(); //OBS wrapper function needed as onIntervalTick is set later
			},
			
			/**
			 * Causes an immediate invocation of the tick target function and makes sure an interval is present or started
			 */
			resume: resume,
			
			pause: pause,
			isPaused: () => intervalFn === undefined,

			interval: function(duration) {
				if (duration === undefined)
					return refreshInterval;
				resume(duration);
			}
		};
	})();

	/**
	 * Internal API for the page rotation interval handling.
	 */ 
	let Rotation = (function() {

	    /**
	     * {function} - a function called with no extra arguments when interval tick occured
	     */
	    var onIntervalTick;

		/**
		 * {function} - underlying interval function causing the ticks to occur
		 */
		var intervalFn;

		return {

			init: function(onIntervalFn) {
				onIntervalTick = onIntervalFn;
			},

			resume: function(atRefreshInterval) {
				if (intervalFn)
					clearInterval(intervalFn); // free old 
				if (atRefreshInterval)
					intervalFn = setInterval(onIntervalTick, atRefreshInterval * 1000);
			}
		};
	})();
	
	/**
	 * Internal API for creating data update messages send to the view from server responses.
	 */ 
	let Update = (function() {

		/**
		 * Shortens the shown time frame to one common to all series but at least to the last minute.
		 */
		function retainCommonTimeFrame(data) {
			if (!data || data.length == 0)
				return [];
			let now = Date.now();
			let startOfLastMinute = now - 60000;
			let startOfShortestSeries = data.reduce((high, e) => Math.max(e.points[0], high), 0);
			let startCutoff = data.length == 1 ? startOfShortestSeries : Math.min(startOfLastMinute, startOfShortestSeries);
			let endOfShortestSeries = data.reduce((low, e) =>  {
				let endTime = e.points[e.points.length - 2];
				return endTime > now - 4000 ? Math.min(endTime, low) : low;
			}, now);
			let endCutoff = endOfShortestSeries;
			data.forEach(function(seriesData) {
				let src = seriesData.points;
				if (src.length == 4 && src[2] >= startCutoff) {
					if (src[1] == src[3]) {
						// extend a straight line between 2 points to cutoff
						seriesData.points = [Math.max(seriesData.observedSince, Math.min(startOfShortestSeries, src[2] - 59000)), src[1], src[2], src[3]];						
					}
				} else {
					let points = [];
					for (let i = 0; i < src.length; i += 2) {
						if (src[i] >= startCutoff && src[i] <= endCutoff) {
							points.push(src[i]);
							points.push(src[i+1]);
						}
					}
					seriesData.points = points;				
				}
			});
			return data.filter(seriesData => seriesData.points.length >= 2 
				&& seriesData.points[seriesData.points.length - 2] > startOfLastMinute);
		}

		function adjustDecimals(data, factor, divisor) {
			data.forEach(function(seriesData) {
				seriesData.points = seriesData.points.map((val, index) => index % 2 == 0 ? val : val * factor / divisor);
				seriesData.observedMax = seriesData.observedMax * factor / divisor; 
				seriesData.observedMin = seriesData.observedMin * factor / divisor;
				seriesData.observedSum = seriesData.observedSum * factor / divisor;
			});
		}

		function perSecond(data) {
			data.forEach(function(seriesData) {
				let points = seriesData.points;
				if (!points)
				  return;
				let pointsPerSec = new Array(points.length - 2);
				for (let i = 0; i < pointsPerSec.length; i+=2) {
				  let t0 = points[i];
				  let t1 = points[i+2];
				  let y0 = points[i+1];
				  let y1 = points[i+3];
				  let dt = t1 - t0;
				  let dy = y1 - y0;
				  let y = (dt / 1000) * dy;
				  pointsPerSec[i] = t1;
				  pointsPerSec[i+1] = y;				  
				}
				if (pointsPerSec.length === 2)
				  pointsPerSec = [points[0], pointsPerSec[1], pointsPerSec[0], pointsPerSec[1]];
				seriesData.points = pointsPerSec;
				//TODO update min/max/avg per sec 
			});
		}

		function addAssessment(widget, data, alerts, watches) {
			data.forEach(function(seriesData) {
				let instance = seriesData.instance;
				let series = seriesData.series;
				let status = 'normal';
				if (Array.isArray(watches) && watches.length > 0) {
					let states = watches
						.filter(watch => !watch.disabled && !watch.stopped)
						.map(watch => watch.states[series]).filter(e => e != undefined)
						.map(states => states[instance]).filter(e => e != undefined);
					if (states.includes('red')) {
						status = 'red';
					} else if (states.includes('amber')) {
						status = 'amber';
					} else if (states.includes('green')) {
						status = 'green';
					} else if (states.length > 0) {
						status = 'white';
					}
				}
				let thresholds = widget.decorations.thresholds;
				if (thresholds.reference && thresholds.reference !== 'off') {
					let value = seriesData.points[seriesData.points.length-1];
					switch (thresholds.reference) {
						case 'min': value = seriesData.observedMin; break;
						case 'max': value = seriesData.observedMax; break;
						case 'avg': value = seriesData.observedSum / seriesData.observedValues; break;
					}
					let alarming = thresholds.alarming.value;
					let critical = thresholds.critical.value;
					let desc = alarming && critical && critical < alarming;
					if (alarming && ((!desc && value >= alarming) || (desc && value <= alarming))) {
						status = 'alarming';
					}
					if (critical && ((!desc && value >= critical) || (desc && value <= critical))) {
						status = 'critical';
					}
				}
				if (Array.isArray(alerts) && alerts.length > 0) {					
					if (alerts.filter(alert => alert.instance == instance && alert.level === 'red').length > 0) {
						status = 'red';
					} else if (alerts.filter(alert => alert.instance == instance && alert.level === 'amber').length > 0) {
						status = 'amber';
					}
				}
				seriesData.assessments = { status: status };
			});
		}

		function createOnSuccess(widgets, onDataUpdate) {
			return function(response) {
				Object.values(widgets).forEach(function(widget, index) {
					let allMatches = response.matches;
					let widgetMatches = allMatches.filter(match => match.widgetId == widget.id);
					let data = [];
					let alerts = [];
					let watches = [];
					let annotations = [];
					for (let i = 0; i  < widgetMatches.length; i++)  {
						data = data.concat(widgetMatches[i].data);
						alerts = alerts.concat(widgetMatches[i].alerts);
						watches = watches.concat(widgetMatches[i].watches);
						annotations = annotations.concat(widgetMatches[i].annotations);
					}
					data = retainCommonTimeFrame(data);
					if (widget.options.decimalMetric || widget.scaleFactor !== undefined && widget.scaleFactor !== 1)
						adjustDecimals(data, widget.scaleFactor ? widget.scaleFactor : 1,  widget.options.decimalMetric ? 10000 : 1);
					if (widget.options.perSec)
						perSecond(data);
					addAssessment(widget, data, alerts, watches);
					onDataUpdate({
						widget: widget,
						data: data,
						alerts: alerts,
						watches: watches,
						annotations: annotations,
						chart: () => Charts.getOrCreate(widget),
					});
				});
			};
		}

		function createOnError(widgets, onDataUpdate) {
			return function() {
				Object.values(widgets).forEach(function(widget) {
					onDataUpdate({
						widget: widget,
						chart: () => Charts.getOrCreate(widget),
					});
				});
			};
		}

		function createQuery(widgets) {
			let queries = [];
			const widgetsArray = Object.values(widgets);
			for (let i = 0; i < widgetsArray.length; i++) {
				const widget = widgetsArray[i];
				let truncate = [];
				let exclude = [];
				let alerts = widget.decorations.alerts;
				let noAlerts = alerts.noOngoing === true && alerts.noStopped === true
					|| alerts.noAcknowledged === true && alerts.noUnacknowledged === true
					|| alerts.noAmber === true && alerts.noRed === true;
				if (noAlerts)
					exclude.push('ALERTS');
				if (widget.options.noAnnotations)
					exclude.push('ANNOTATIONS');
				switch (widget.type) {
					case 'alert':
						exclude.push('POINTS');
						exclude.push('WATCHES');
						break;
					case 'annotation':
						exclude.push('ALERTS');
						exclude.push('POINTS');
						exclude.push('WATCHES');
						break;
					default:
						truncate.push('ALERTS');
				}
				pushQueryItems(widget, queries, truncate, exclude);
			}
			return queries;
		}

		function pushQueryItems(widget, queries, truncate, exclude) {
			const series = widget.series;
			const id = widget.id;
			if (Array.isArray(series)) {
				series.forEach(s => queries.push({ widgetId: id, series: s, truncate: truncate, exclude: exclude, instances: undefined}));
			} else {
				queries.push({ widgetId: id, series: series, truncate: truncate, exclude: exclude, instances: undefined}); 
			}
		}

		return {
			createQuery: createQuery,
			createOnSuccess: createOnSuccess,
			createOnError: createOnError,
		};
	})();




	function doInit(onDataUpdate, onPageUpdate) {
		UI.load();
		Interval.init(async function() {
			let page = UI.currentPage();
			if (page.type == 'query') {
				const now = new Date().getTime();
				if (page.widgets === undefined || page.content.expires === undefined || now >= page.content.expires) {
					page = await UI.queryPage();
					onPageUpdate(doSwitchPage(page.id));
				}
			}
			let widgets = page.widgets;
			Controller.requestListOfSeriesData(Update.createQuery(widgets), 
				Update.createOnSuccess(widgets, onDataUpdate),
				Update.createOnError(widgets, onDataUpdate));
		});
		if (UI.Refresh.interval() === undefined) {
			UI.Refresh.interval(DEFAULT_INTERVAL);
		}
		if (!UI.Refresh.isPaused())
			Interval.resume(UI.Refresh.interval());
		Rotation.init(() => onPageUpdate(doSwitchPage()));
		if (UI.Rotation.isEnabled()) {
			Rotation.resume(UI.Rotation.interval());	
		}
		return UI.arrange();
	}

	function doConfigureSelection(widgetUpdate) {
		UI.configureWidget(createWidgetUpdate(widgetUpdate));
		return UI.arrange();
	}

	function doConfigureWidget(widgetId, widgetUpdate) {
		UI.configureWidget(createWidgetUpdate(widgetUpdate), widgetId);
		return UI.arrange();
	}

	function createWidgetUpdate(widgetUpdate) {
		return function(widget) {
			let type = widget.type;
			widgetUpdate(widget);
			if (widget.type === type) {
				Charts.update(widget);
			} else {
				Charts.destroy(widget.id);
			}
		};
	}

	function doSwitchPage(pageId) {
		if (UI.switchPage(pageId)) {
			Charts.clear();
			Interval.tick();
			window.location.hash = pageId;
		}
		return UI.arrange();		
	}

	/**
	 * The public API object ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	 */
	return {
		
		init: doInit,
		
		/**
		 * @param {function} consumer - a function with one argument accepting the array of series names
		 */
		listSeries: (consumer) => Controller.requestListOfSeriesNames(consumer),

		listPages: UI.listPages,
		exportPages: UI.exportPages,
		importPages: function(userInterface, onImportComplete) {
			UI.importPages(userInterface, () => onImportComplete(UI.arrange()));
		},
		
		/**
		 * API to control the chart refresh interval.
		 */
		Refresh: {
			pause: function() { 
				Interval.pause();
				UI.Refresh.paused(true);
			},
			paused: function(paused) {
				if (paused === undefined)
					return UI.Refresh.isPaused();
				UI.Refresh.paused(paused);
				if (paused) {
					Interval.pause();
				} else {
					Interval.resume(UI.Refresh.interval());
				}
			},
			isPaused: UI.Refresh.isPaused,
			resume: function(duration) {
				if (duration !== undefined) {
					UI.Refresh.interval(duration);
				}
				UI.Refresh.paused(false);
				Interval.resume(UI.Refresh.interval());
			},
			interval: function(duration) {
				if (duration === undefined)
					return UI.Refresh.interval();
				UI.Refresh.interval(duration);
				Interval.resume(UI.Refresh.interval());				
			},
		},

		Theme: {
			palette: UI.themePalette,
			option: UI.themeOption,
			color: UI.themeColor,
			configure: UI.themeConfigure,
		},
		
		Role: UI.Role,

		Settings: {
			isDispayed: UI.showSettings,
			open: UI.openSettings,
			close: UI.closeSettings,
			toggle: () => UI.showSettings() ? UI.closeSettings() : UI.openSettings(),

			Rotation: {
				isEnabled: UI.Rotation.isEnabled,
				enabled: function(enabled) {
					UI.Rotation.enabled(enabled);
					Rotation.resume(UI.Rotation.isEnabled() ? UI.Rotation.interval() : 0);
				},
				interval: function(duration) {
					if (duration === undefined)
						return UI.Rotation.interval();
					UI.Rotation.interval(duration);
					Rotation.resume(UI.Rotation.isEnabled() ? UI.Rotation.interval() : 0);
				}
			}
		},
		
		/**
		 * API to control the active page manipulate the set of charts contained on it.
		 */
		Page: {
			
			current: () => UI.currentPage(),
			configure: function(change) {
				UI.configurePage(change);
				return UI.arrange();
			},
			id: () => UI.currentPage().id,
			name: () => UI.currentPage().name,
			rename: UI.renamePage,
			rotate: UI.rotatePage,
			isEmpty: () => (Object.keys(UI.currentPage().widgets).length === 0),
			
			create: function(name) {
				UI.createPage(name);
				Charts.clear();
				return UI.arrange();
			},
			
			erase: function() {
				if (UI.deletePage()) {
					Charts.clear();
					Interval.tick();
				}
				return UI.arrange();
			},
			
			reset: function() {
				if (UI.resetPage()) {
					Charts.clear();
					Interval.tick();
				}
				return UI.arrange();
			},

			hasPreset: UI.hasPreset,
			
			changeTo: function(pageId) {
				return doSwitchPage(pageId);
			},

			Sync: UI.Sync,
			
			/**
			 * Returns a layout model for the active pages charts and the given number of columns.
			 * This also updates the grid object of the active pages configuration.
			 * 
			 * @param {number} numberOfColumns - the number of columns the charts should be arrange in
			 */
			arrange: UI.arrange,
			
			Widgets: {
				
				add: function(series, grid) {
					if (Array.isArray(series) && series.length == 1)
						series = series[0];
					if (Array.isArray(series) || series.trim()) {
						UI.addWidget(series, grid);
						Interval.tick();
					}
					return UI.arrange();
				},
				
				remove: function(widgetId) {
					Charts.destroy(widgetId);
					UI.removeWidget(widgetId);
					return UI.arrange();
				},
				
				configure: doConfigureWidget,

				moveLeft: (widgetId) => doConfigureWidget(widgetId, function(widget) {
	                if (!widget.grid.column || widget.grid.column > 0) {
	                    widget.grid.column = widget.grid.column ? widget.grid.column - 1 : 1;
	                }
	            }),

	            moveRight: (widgetId) => doConfigureWidget(widgetId, function(widget) {
	                if (!widget.grid.column || widget.grid.column < 4) {
	                    widget.grid.column = widget.grid.column ? widget.grid.column + 1 : 1;
	                }
	            }),

				/**
				 * API for the set of selected widgets on the current page.
				 */
				Selection: {
					
					listSeries: UI.selected,
					isSingle: () => UI.selected().length == 1,
					first: () => UI.currentPage().widgets[UI.selected()[0]],
					toggle: UI.select,
					clear: UI.deselect,
					
					/**
					 * @param {function} widgetUpdate - a function accepting chart configuration applied to each chart
					 */
					configure: doConfigureSelection,

				},
			},
			
		},

	};
})();
/*
   DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
  
   Copyright (c) 2019-2020 Payara Foundation and/or its affiliates. All rights reserved.
  
   The contents of this file are subject to the terms of either the GNU
   General Public License Version 2 only ("GPL") or the Common Development
   and Distribution License("CDDL") (collectively, the "License").  You
   may not use this file except in compliance with the License.  You can
   obtain a copy of the License at
   https://github.com/payara/Payara/blob/master/LICENSE.txt
   See the License for the specific
   language governing permissions and limitations under the License.
  
   When distributing the software, include this License Header Notice in each
   file and include the License file at glassfish/legal/LICENSE.txt.
  
   GPL Classpath Exception:
   The Payara Foundation designates this particular file as subject to the "Classpath"
   exception as provided by the Payara Foundation in the GPL Version 2 section of the License
   file that accompanied this code.
  
   Modifications:
   If applicable, add the following below the License Header, with the fields
   enclosed by brackets [] replaced by your own identifying information:
   "Portions Copyright [year] [name of copyright owner]"
  
   Contributor(s):
   If you wish your version of this file to be governed by only the CDDL or
   only the GPL Version 2, indicate your decision by adding "[Contributor]
   elects to include this software in this distribution under the [CDDL or GPL
   Version 2] license."  If you don't indicate a single choice of license, a
   recipient has the option to distribute your version of this file under
   either the CDDL, the GPL Version 2 or to extend the choice of license to
   its licensees as provided above.  However, if you add GPL Version 2 code
   and therefore, elected the GPL Version 2 license, then the option applies
   only if the new code is made subject to such option by the copyright
   holder.
*/

/*jshint esversion: 8 */

/**
 * Utilities to convert values given in units from and to string.
 **/
MonitoringConsole.View.Units = (function() {

   const PERCENT_FACTORS = {
      '%': 1
   };

   /**
    * Factors used for any time unit to milliseconds
    */
   const SEC_FACTORS = {
      h: 60 * 60, hours: 60 * 60,
      m: 60, min: 60, mins: 60,
      s: 1, sec: 1, secs: 1,
      _: [['h', 'm'], ['h', 'm', 's'], ['m', 's']]
   };

   /**
    * Factors used for any time unit to milliseconds
    */
   const MS_FACTORS = {
      d: 24 * 60 * 60 * 1000, days: 24* 60 * 60 * 1000,
      h: 60 * 60 * 1000, hours: 60 * 60 * 1000,
      m: 60 * 1000, min: 60 * 1000, mins: 60 * 1000,
      s: 1000, sec: 1000, secs: 1000,
      ms: 1,
      us: 1/1000, μs: 1/1000,
      ns: 1/1000000,
      _: [['d', 'h', 'm'], ['d', 'h', 'm', 's'], ['h', 'm'], ['h', 'm', 's'], ['h', 'm', 's', 'ms'], ['m', 's'], ['m', 's', 'ms'], ['s', 'ms']]
   };

   /**
    * Factors used for any time unit to microseconds
    */
   const US_FACTORS = {
      h: 60 * 60 * 1000 * 1000, hours: 60 * 60 * 1000 * 1000,
      m: 60 * 1000 * 1000, min: 60 * 1000 * 1000, mins: 60 * 1000 * 1000,
      s: 1000 * 1000, sec: 1000 * 1000, secs: 1000 * 1000,
      ms: 1000,
      us: 1, μs: 1,
      ns: 1/1000,
      _: [['h', 'm'], ['h', 'm', 's'], ['h', 'm', 's', 'ms'], ['m', 's'], ['m', 's', 'ms'], ['s', 'ms'], ['ms', 'us']]
   };

   /**
    * Factors used for any time unit to nanoseconds
    */
   const NS_FACTORS = {
      h: 60 * 60 * 1000 * 1000000, hours: 60 * 60 * 1000 * 1000000,
      m: 60 * 1000 * 1000000, min: 60 * 1000 * 1000000, mins: 60 * 1000 * 1000000,
      s: 1000 * 1000000, sec: 1000 * 1000000, secs: 1000 * 1000000,
      ms: 1000000,
      us: 1000, μs: 1000,
      ns: 1,
      _: [['h', 'm'], ['h', 'm', 's'], ['h', 'm', 's', 'ms'], ['m', 's'], ['m', 's', 'ms'], ['s', 'ms'], ['ms', 'us', 'ns'], ['ms', 'us']]
   };

   /**
    * Factors used for any memory unit to bytes
    */
   const BYTES_FACTORS = {
      kB: 1024, kb: 1024,
      MB: 1024 * 1024, mb: 1024 * 1024,
      GB: 1024 * 1024 * 1024, gb: 1024 * 1024 * 1024,
      TB: 1024 * 1024 * 1024 * 1024, tb: 1024 * 1024 * 1024 * 1024,
   };

   /**
    * Factors used for usual (unit less) count values
    */
   const COUNT_FACTORS = {
      K: 1000,
      M: 1000 * 1000
   };

   /**
    * Factors by unit
    */
   const FACTORS = {
      count: COUNT_FACTORS,
      sec: SEC_FACTORS,
      ms: MS_FACTORS,
      us: US_FACTORS,
      ns: NS_FACTORS,
      bytes: BYTES_FACTORS,
      percent: PERCENT_FACTORS,
      updown: {},
   };

   const UNIT_NAMES = {
      count: 'Count',
      sec: 'Seconds', 
      ms: 'Milliseconds',
      us: 'Microseconds',
      ns: 'Nanoseconds', 
      bytes: 'Bytes', 
      percent: 'Percentage',
      updown: 'Up/Down',
   };

   const ALERT_STATUS_NAMES = { 
      white: 'Normal', 
      green: 'Healthy', 
      amber: 'Degraded', 
      red: 'Unhealthy' 
   };

   function parseNumber(valueAsString, factors) {
      if (!valueAsString || typeof valueAsString === 'string' && valueAsString.trim() === '')
         return undefined;
      let valueAsNumber = Number(valueAsString);
      if (!Number.isNaN(valueAsNumber))
         return valueAsNumber;
      let valueAndUnit = valueAsString.split(/(-?[0-9]+\.?[0-9]*)/);
      let sum = 0;
      for (let i = 1; i < valueAndUnit.length; i+=2) {
         valueAsNumber = Number(valueAndUnit[i].trim());
         let unit = valueAndUnit[i+1].trim().toLowerCase();
         let factor = factors[unit];
         sum += valueAsNumber * factor;               
      }
      return sum;
   }

   function formatDecimal(valueAsNumber) {
      if (!hasDecimalPlaces(valueAsNumber)) {
         return Math.round(valueAsNumber).toString();
      } 
      let text = valueAsNumber.toFixed(1);
      return text.endsWith('.0') ? Math.round(valueAsNumber).toString() : text;
   }

   function formatNumber(valueAsNumber, factors, useDecimals) {
      if (valueAsNumber === undefined)
         return undefined;
      if (valueAsNumber === 0)
         return '0';
      if (!factors)
         return formatDecimal(valueAsNumber);
      if (valueAsNumber < 0)
         return '-' + formatNumber(-valueAsNumber, factors, useDecimals);
      let largestFactorUnit;
      let largestFactor = 0;
      let factor1Unit = '';
      for (let [unit, factor] of Object.entries(factors)) {
         if (unit != '_' && (valueAsNumber >= 0 && factor > 0 || valueAsNumber < 0 && factor < 0)
            && (useDecimals && (valueAsNumber / factor) >= 1 || !hasDecimalPlaces(valueAsNumber / factor))
            && factor > largestFactor) {
            largestFactor = factor;
            largestFactorUnit = unit;
         }
         if (factor === 1)
            factor1Unit = unit;
      }
      if (!largestFactorUnit)
         return formatDecimal(valueAsNumber) + factor1Unit;
      if (useDecimals)
         return formatDecimal(valueAsNumber / largestFactor) + largestFactorUnit;
      let valueInUnit = Math.round(valueAsNumber / largestFactor);
      if (factors._) {
         for (let i = 0; i < factors._.length; i++) {
            let combination = factors._[i];
            let rest = valueAsNumber;
            let text = '';
            if (combination[combination.length - 1] == largestFactorUnit) {
               for (let j = 0; j < combination.length; j++) {
                  let unit = combination[j];
                  let factor = factors[unit];
                  let times = Math.floor(rest / factor);
                  if (times === 0)
                     break;
                  rest -= times * factor;
                  text += times + unit;                      
               }
            }
            if (rest === 0) {
               return text;
            }
         }
      }
      // TODO look for a combination that has the found unit as its last member
      // try if value can eben be expressed as the combination, otherwise try next that might fulfil firs condition
      return valueInUnit + largestFactorUnit;
   }

   function hasDecimalPlaces(number) {
      return number % 1 != 0;
   }

   function formatTime(hourOrDateOrTimestamp, minute, second, millis) {
      if (typeof hourOrDateOrTimestamp === 'string')
         hourOrDateOrTimestamp = Number(hourOrDateOrTimestamp);
      if (typeof hourOrDateOrTimestamp === 'number' && hourOrDateOrTimestamp > 24) { // assume timestamp
         hourOrDateOrTimestamp = new Date(hourOrDateOrTimestamp);
      }
      if (typeof hourOrDateOrTimestamp === 'object') { // assume Date
         minute = hourOrDateOrTimestamp.getMinutes();
         second = hourOrDateOrTimestamp.getSeconds();
         millis = hourOrDateOrTimestamp.getMilliseconds();
         hourOrDateOrTimestamp = hourOrDateOrTimestamp.getHours();
      }
      let str = as2digits(hourOrDateOrTimestamp);
      str += ':' + as2digits(minute ? minute : 0);
      if (second)
         str += ':' +  as2digits(second);
      if (millis)
         str += '.' + as3digits(millis);
      return str;
   }

   function formatDateTime(dateOrTimestamp) {
      if (dateOrTimestamp === undefined)
         return '';
      if (typeof dateOrTimestamp === 'string')
         dateOrTimestamp = Number(dateOrTimestamp);
      if (typeof dateOrTimestamp === 'number')
         dateOrTimestamp = new Date(dateOrTimestamp);
      // now dateOrTimestamp should be a Date object
      let diffMs = new Date() - dateOrTimestamp;
      diffMs = Math.round(diffMs / 1000) * 1000; // truncate ms
      return formatNumber(diffMs, MS_FACTORS) + ' ago, ' + formatTime(Math.round(dateOrTimestamp.getTime() / 1000) * 1000);
   }

   function as2digits(number) {
      return number.toString().padStart(2, '0');
   }

   function as3digits(number) {
      return number.toString().padStart(3, '0');
   }

   const DECIMAL_NUMBER_PATTERN = '([0-9]+\.)?[0-9]+';

   function pattern(factors) {
      if (!factors)
         return DECIMAL_NUMBER_PATTERN;
      return '(' + DECIMAL_NUMBER_PATTERN + '(' + Object.keys(factors).filter(unit => unit != '_').join('|') + ')? *)+';
   }

   function patternHint(factors) {
      if (!factors)
         return 'a integer or decimal number';
      return 'a integer or decimal number, optionally followed by a unit: ' + Object.keys(factors).filter(unit => unit != '_').join(', ');
   }

   function maxAlertLevel(a, b) {
      const table = ['white', 'normal', 'green', 'alarming', 'amber', 'critical', 'red'];
      return table[Math.max(0, Math.max(table.indexOf(a), table.indexOf(b)))];
   }

   /**
    * Public API below:
    */
   return {
      Alerts: {
         maxLevel: maxAlertLevel,
         name: (level) => ALERT_STATUS_NAMES[level == undefined ? 'white' : level],
      },
      
      names: () => UNIT_NAMES,
      formatTime: formatTime,
      formatDateTime: formatDateTime,
      formatNumber: formatNumber,
      formatMilliseconds: (valueAsNumber) => formatNumber(valueAsNumber, MS_FACTORS),
      formatNanoseconds: (valueAsNumber) => formatNumber(valueAsNumber, NS_FACTORS),
      formatBytes: (valueAsNumber) => formatNumber(valueAsNumber, BYTES_FACTORS),
      parseNumber: parseNumber,
      parseMilliseconds: (valueAsString) => parseNumber(valueAsString, MS_FACTORS),
      parseNanoseconds: (valueAsString) => parseNumber(valueAsString, NS_FACTORS),
      parseBytes: (valueAsString) => parseNumber(valueAsString, BYTES_FACTORS),
      converter: function(unit) {
         if (unit == 'updown')
            return {
               format: (stateAsNumber) => stateAsNumber == 0 ? 'Down' : 'Up',
               parse: (stateAsString) => stateAsString == 'Down' || stateAsString == '0' ? 0 : 1,
               pattern: () => 'up|down|0|1',
               patternHint: () => 'up, down, 0, 1'
            };
         let factors = FACTORS[unit];
         return {
            format: (valueAsNumber, useDecimals) => formatNumber(valueAsNumber, factors, useDecimals),
            parse: (valueAsString) => parseNumber(valueAsString, factors),
            pattern: () =>  pattern(factors),
            patternHint: () => patternHint(factors),
         };
      }
   };
})();
/*
   DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
  
   Copyright (c) 2019-2020 Payara Foundation and/or its affiliates. All rights reserved.
  
   The contents of this file are subject to the terms of either the GNU
   General Public License Version 2 only ("GPL") or the Common Development
   and Distribution License("CDDL") (collectively, the "License").  You
   may not use this file except in compliance with the License.  You can
   obtain a copy of the License at
   https://github.com/payara/Payara/blob/master/LICENSE.txt
   See the License for the specific
   language governing permissions and limitations under the License.
  
   When distributing the software, include this License Header Notice in each
   file and include the License file at glassfish/legal/LICENSE.txt.
  
   GPL Classpath Exception:
   The Payara Foundation designates this particular file as subject to the "Classpath"
   exception as provided by the Payara Foundation in the GPL Version 2 section of the License
   file that accompanied this code.
  
   Modifications:
   If applicable, add the following below the License Header, with the fields
   enclosed by brackets [] replaced by your own identifying information:
   "Portions Copyright [year] [name of copyright owner]"
  
   Contributor(s):
   If you wish your version of this file to be governed by only the CDDL or
   only the GPL Version 2, indicate your decision by adding "[Contributor]
   elects to include this software in this distribution under the [CDDL or GPL
   Version 2] license."  If you don't indicate a single choice of license, a
   recipient has the option to distribute your version of this file under
   either the CDDL, the GPL Version 2 or to extend the choice of license to
   its licensees as provided above.  However, if you add GPL Version 2 code
   and therefore, elected the GPL Version 2 license, then the option applies
   only if the new code is made subject to such option by the copyright
   holder.
*/

/*jshint esversion: 8 */

/**
 * Utilities to convert color values and handle color schemes.
 *
 * Color schemes are named sets of colors that are applied to the model.
 * This is build purely on top of the model. 
 * The model does not remember a scheme, 
 * schemes are just a utility to reset color configurations to a certain set.
 **/
MonitoringConsole.View.Colors = (function() {

   const Theme = MonitoringConsole.Model.Theme;

   const SCHEMES = {
      Payara: {
         name: 'Payara',
         palette: [ 
            '#4363d8', '#42d4f4', '#469990', '#aaffc3', 
            '#bfef45', '#ffe119', '#ffd8b1', '#9A6324', 
            '#800000', '#911eb4', '#f032e6', '#fabebe', '#e6beff', '#fffac8' ],
         opacity: 10,
         colors:  { 
            error: '#e6194B', missing: '#0096D6',
            waterline: '#3cb44b', alarming: '#f58231', critical: '#e6194B',
            white: '#ffffff', green: '#3cb44b', amber: '#f58231', red: '#e6194B',
         }
      },

      a: {
         name: '80s',
         opacity: 10,
         palette: [ '#c04df9', '#f3ea5f', '#08c1ef', '#d28f47', '#b86739'],
         colors:  { 
            waterline: '#2bd1fc', alarming: '#e8a634', critical: '#ff3f3f',
            white: '#ffffff', green: '#b6f778', amber: '#e8a634', red: '#ff3f3f',
         }
      },

      b: {
         name: 'Pastels',
         opacity: 10,
         palette: [ '#deccff', '#96c0bc', '#dbd259', '#bd6283', '#08c7f7' ],
         colors:  { 
            waterline: '#82e69f', alarming: '#dbd259', critical: '#b95f51',
            white: '#ffffff', green: '#82e69f', amber: '#d49e54', red: '#b95f51',
         }
      },

      c: {
         name: 'Neon',
         opacity: 10,
         palette: [ '#f700d8', '#eff109', '#0ed4f7', '#00b8aa', '#0000f7'],
         colors:  { 
            waterline: '#00aff3', alarming: '#f64e0c', critical: '#dc143c',
            white: '#ffffff', green: '#6cf700', amber: '#f64e0c', red: '#dc143c',
         }
      },

      d: {
         name: 'Vapor Wave',
         opacity: 10,
         palette: [ '#b8a9df', '#01cdfe', '#b967ff', '#fffb96', '#05ffa1'],
         colors:  { 
            waterline: '#01cdfe', alarming: '#edae50', critical: '#FB637A',
            white: '#ffffff', green: '#a2dda9', amber: '#edae50', red: '#e05267', 
         }
      },
   };

   /**
    * Object used as map to remember the colors by coloring stratgey.
    * Each strategy leads to an object map that remebers the key as fields 
    * and the index associated with the key as value.
    * This is a mapping from e.g. the name 'DAS' to index 0. 
    * The index is then used to pick a color form the palette.
    * This makes sure that same key, for example the instance name,
    * always uses the same color accross widgets and pages.
    */
   let colorIndexMaps = {};

   function lookup(coloring, key, palette) {
      let mapName = coloring || 'instance';
      let map = colorIndexMaps[mapName];
      if (map === undefined) {
         map = {};
         colorIndexMaps[mapName] = map;
      }
      let index = map[key];
      if (index === undefined) {
         index = Object.keys(map).length;
         map[key] = index;
      }
      return derive(palette, index);
   }

   /**
    * Returns a palette color.
    * If index is outside of the palette given a color is derived from the palette.
    * The derived colors are intentionally biased towards light non-grayish colors.
    */
   function derive(palette, index = 1) {
      let color = palette[index % palette.length];
      if (index < palette.length)
         return color;
      let [r, g, b] = hex2rgb(color);
      let offset = index - palette.length + 1;
      let shift = (offset * 110) % 255;
      r = (r + shift) % 255;
      g = (g + shift) % 255;
      b = (b + shift) % 255;
      let variant = offset % 6;
      if (variant == 0 || variant == 3 || variant == 4)
         r = Math.round(r / 2);
      if (variant == 1 || variant == 3 || variant == 5)
         g = Math.round(g / 2);
      if (variant == 2 || variant == 4 || variant == 5)
         b = Math.round(b / 2);
      if (r + g + b < 380 && r < 120) r = 255 - r;
      if (r + g + b < 380 && g < 120) g = 255 - g;
      if (r + g + b < 380 && b < 120) b = 255 - b;
      return rgb2hex(r, g, b);
   }

   function random(palette) {
      if (Array.isArray(palette))
         return derive([palette[0]], palette.length); 
      const letters = '0123456789ABCDEF';
      let color = '#';
      for (let i = 0; i < 6; i++) {
         color += letters[Math.floor(Math.random() * 16)];
      }
      return color;
   }

   function hex2rgb(hex) {
      return hex.match(/\w\w/g).map(x => parseInt(x, 16));
   }

   function rgb2hex(r, g, b) {
      return "#" + ((1 << 24) + (r << 16) + (g << 8) + b).toString(16).slice(1);
   }

   function hex2rgba(hex, alpha = 1) {
      const [r, g, b] = hex2rgb(hex);
      return `rgba(${r},${g},${b},${alpha})`;
   }

   function schemeOptions() {
      return Object.keys(SCHEMES).reduce(function(result, key) {
         result[key] = SCHEMES[key].name;
         return result;
      }, { _: '(Select to apply)' });
   }

   function applyScheme(name, override = true) {
      const createSetColorFn = (name, color) => (theme) => theme.colors[name] = color;
      let scheme = SCHEMES[name];
      if (scheme) {
         if (override || Theme.palette() === undefined)
            Theme.configure(theme => theme.palette = scheme.palette);
         if (override || Theme.option('opacity') === undefined)
            Theme.configure(theme => theme.options.opacity = scheme.opacity);
         if (scheme.colors) {
            for (let [name, color] of Object.entries(scheme.colors)) {
               if (override || Theme.color(name) === undefined)
                  Theme.configure(createSetColorFn(name, color));
            }
         }            
      }
   }

   /**
    * Public API of the color utility module.
    */
   return {
      lookup: lookup,
      random: random,
      hex2rgba: hex2rgba,
      schemes: schemeOptions,
      scheme: applyScheme,
   };
})();
/*
   DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
  
   Copyright (c) 2019 Payara Foundation and/or its affiliates. All rights reserved.
  
   The contents of this file are subject to the terms of either the GNU
   General Public License Version 2 only ("GPL") or the Common Development
   and Distribution License("CDDL") (collectively, the "License").  You
   may not use this file except in compliance with the License.  You can
   obtain a copy of the License at
   https://github.com/payara/Payara/blob/master/LICENSE.txt
   See the License for the specific
   language governing permissions and limitations under the License.
  
   When distributing the software, include this License Header Notice in each
   file and include the License file at glassfish/legal/LICENSE.txt.
  
   GPL Classpath Exception:
   The Payara Foundation designates this particular file as subject to the "Classpath"
   exception as provided by the Payara Foundation in the GPL Version 2 section of the License
   file that accompanied this code.
  
   Modifications:
   If applicable, add the following below the License Header, with the fields
   enclosed by brackets [] replaced by your own identifying information:
   "Portions Copyright [year] [name of copyright owner]"
  
   Contributor(s):
   If you wish your version of this file to be governed by only the CDDL or
   only the GPL Version 2, indicate your decision by adding "[Contributor]
   elects to include this software in this distribution under the [CDDL or GPL
   Version 2] license."  If you don't indicate a single choice of license, a
   recipient has the option to distribute your version of this file under
   either the CDDL, the GPL Version 2 or to extend the choice of license to
   its licensees as provided above.  However, if you add GPL Version 2 code
   and therefore, elected the GPL Version 2 license, then the option applies
   only if the new code is made subject to such option by the copyright
   holder.
*/

/*jshint esversion: 8 */

/**
 * Data/Model driven view components.
 *
 * Each of them gets passed a model creates a DOM structure in form of a jQuery object 
 * that should be inserted into the page DOM using jQuery.
 *
 * Besides general encapsulation the idea is to benefit of a function approch 
 * that utilises pure functions to compute the page context from a fixed state input.
 * This makes the code much easier to understand and maintain as it is free of overall page state.
 **/
MonitoringConsole.View.Components = (function() {

  const Controller = MonitoringConsole.Controller;
  const Units = MonitoringConsole.View.Units;
  const Colors = MonitoringConsole.View.Colors;
  const Selection = MonitoringConsole.Model.Page.Widgets.Selection;

   /**
    * This is the side panel showing the details and settings of widgets
    */
   let Settings = (function() {

      function createHeaderRow(model) {
        let caption = model.label;
        let config = {colspan: 2};
        if (model.description)
          config.title = model.description;
        let th = $('<th/>', config);
        let showHide = function() {
          let tr = th.closest('tr').next();
          let toggleAll = tr.children('th').length > 0;
          while (tr.length > 0 && (toggleAll || tr.children('th').length == 0)) {
              if (tr.children('th').length == 0) {
                  tr.toggle();                    
              }
              tr = tr.next();
          }
        };
        return $('<tr/>').append(
            th.html(caption).click(showHide));
      }

      function createTable(model) {
        let table = $('<table />', { id: model.id });
        if (model.caption)
          table.append(createHeaderRow({ label: model.caption, description: model.description, collapsed: model.collapsed }));
        return table;
      }

      function createRow(model, inputs) {
        let components = $.isFunction(inputs) ? inputs() : inputs;
        if (typeof components === 'string')
            components = document.createTextNode(components);
        let config = {};
        if (model.description)
          config.title = model.description;
        let tr = $('<tr/>');
        tr.append($('<td/>', config).text(model.label)).append($('<td/>').append(components));
        if (model.collapsed)
          tr.css('display', 'none');
        return tr;
      }

      function enhancedOnChange(onChange, updatePage) {
        if (onChange.length == 2) {
          return (value) => {
            let layout = Selection.configure((widget) => onChange(widget, value));
            if (updatePage) {
              MonitoringConsole.View.onPageUpdate(layout);
            }
          };
        }
        return onChange;
      }

      function createCheckboxInput(model) {
        let config = { id: model.id, type: 'checkbox', checked: model.value };
        if (model.description && !model.label)
          config.title = model.description;
        let onChange = enhancedOnChange(model.onChange);
        return $("<input/>", config)
          .on('change', function() {
            let checked = this.checked;
            onChange(checked);
          });
      }

      function createRangeInput(model) {
        let config = { id: model.id, type: 'number', value: model.value};
        if (model.min)
          config.min = model.min;
        if (model.max)
          config.max = model.max;
        if (model.description && !model.label)
          config.title = model.description;
        let onChange = enhancedOnChange(model.onChange, true);
        return $('<input/>', config)
          .on('input change', function() {  
            let val = this.valueAsNumber;
            if (Number.isNaN(val))
              val = undefined;
            onChange(val);
          });
      }

      function createDropdownInput(model) {
        let config = { id: model.id };
        if (model.description && !model.label)
          config.title = description;
        let dropdown = $('<select/>',  );
        if (Array.isArray(model.options)) {
          model.options.forEach(option => dropdown.append($('<option/>',
            { text: option, value: option, selected: model.value === option})));
        } else {
          Object.keys(model.options).forEach(option => dropdown.append($('<option/>', 
            { text:model.options[option], value:option, selected: model.value === option})));          
        }
        let onChange = enhancedOnChange(model.onChange, true);
        dropdown.change(() => onChange(dropdown.val()));
        return dropdown;
      }

      function createValueInput(model) {
        let unit = model.unit;
        if (typeof unit === 'string') {
          if (unit === 'percent')
            return createRangeInput({id: model.id, min: 0, max: 100, value: model.value, onChange: model.onChange });
          if (unit === 'count')
            return createRangeInput(model);
        }
        return createTextInput(model);
      }

      function createTextInput(model) {
        function getConverter() {
          if (model.unit === undefined)
            return { format: (str) => str, parse: (str) => str };
          if (typeof model.unit === 'function')
            return Units.converter(model.unit());
          return Units.converter(model.unit);
        }
        let value = model.value;
        if (Array.isArray(value))
          return createMultiTextInput(model);
        let converter = getConverter();
        let config = { 
          id: model.id,
          type: 'text', 
          value: converter.format(model.value), 
          'class': 'input-' + model.type,
        };
        if (model.description && !model.label)
          config.title = description;
        let readonly = model.onChange === undefined;
        if (!readonly && typeof model.unit === 'string') {
          if (converter.pattern !== undefined)
            config.pattern = converter.pattern();
          if (converter.patternHint !== undefined)
            config.title = (config.title ? config.title + ' ' : '') + converter.patternHint();
        }
        let input = $('<input/>', config);
        if (!readonly) {
          let onChange = enhancedOnChange(model.onChange, true);
          input.on('input change', function() {
            let val = getConverter().parse(this.value);
            onChange(val);
          });          
        } else {
          input.prop('readonly', true);
        }
        return input;
      }

      function createMultiTextInput(model) {
        let value = model.value;
        if (value === undefined && model.defaultValue !== undefined)
          value = model.defaultValue;
        if (!Array.isArray(value))
          value = [value];
        const list = $('<span/>');
        let texts = [...value];
        let i = 0;
        for (i = 0; i < value.length; i++) {
          list.append(createMultiTextInputItem(list, model, value, texts, i));
        }
        const add = $('<button/>', { text: '+'});
        add.click(() => {
          texts.push('');
          createMultiTextInputItem(list, model, '', texts, i++).insertBefore(add);
        });
        list.append(add);
        return list;
      }

      function createMultiTextInputItem(list, model, values, texts, index) {
        const id = model.id + '-' + (index + 1);
        return createTextInput({
            id: id,
            unit: model.unit,
            type: model.type,
            value: values[index],
            onChange: (widget, text) => {
              const isNotEmpty = text => text !== undefined && text != '';
              texts[index] = text;
              let nonEmptyTexts = texts.filter(isNotEmpty);
              if (!isNotEmpty(text)) {
                if (nonEmptyTexts.length > 0)
                  list.children('#' + id).remove();
              }
              model.onChange(widget, nonEmptyTexts.length == 1 ? nonEmptyTexts[0] : nonEmptyTexts);
            }
          });
      }

      function createColorInput(model) {
        let value = model.value;
        if (value === undefined && model.defaultValue !== undefined)
          value = model.defaultValue;
        if (Array.isArray(value))
          return createMultiColorInput(model);
        let config = { id: model.id, type: 'color', value: value };
        if (model.description && !model.label)
          config.title = model.description;
        let onChange = enhancedOnChange(model.onChange);
        let input = $('<input/>', config)
          .on('input change', function() { 
            let val = this.value;
            if (val === model.defaultValue)
              val = undefined;
            onChange(val);
          });
        let wrapper = $('<span/>', {'class': 'color-picker'}).append(input);
        if (model.defaultValue === undefined)
          return wrapper;
        return $('<span/>').append(wrapper).append($('<input/>', { 
          type: 'button', 
          value: ' ',
          title: 'Reset to default color', 
          style: 'background-color: ' + model.defaultValue,
          'class': 'color-reset',
        }).click(() => {
          onChange(undefined);
          input.val(model.defaultValue);
        }));
      }

      function createMultiColorInput(model) {
        let value = model.value;
        if (value === undefined && model.defaultValue !== undefined)
          value = model.defaultValue;
        if (!Array.isArray(value))
          value = [value];
        let list = $('<span/>');
        //TODO model id goes where?
        let colors = [...value];
        let onChange = enhancedOnChange(model.onChange);
        for (let i = 0; i < value.length; i++) {
          list.append(createMultiColorItemInput(colors, i, onChange));
        }
        let add = $('<button/>', {text: '+', 'class': 'color-list'});
        add.click(() => {
          colors.push(Colors.random(colors));
          createMultiColorItemInput(colors, colors.length-1, onChange).insertBefore(add);
          onChange(colors);
        });
        let remove = $('<button/>', {text: '-', 'class': 'color-list'});
        remove.click(() => {
          if (colors.length > 1) {
            colors.length -= 1;
            list.children('.color-picker').last().remove();
            onChange(colors);
          }
        });
        list.append(add);
        list.append(remove);
        return list;
      }

      function createMultiColorItemInput(colors, index, onChange) {
        return createColorInput({ value: colors[index], onChange: (val) => {
          colors[index] = val;
          onChange(colors);
        }});
      }

      function createInput(model) {
         switch (model.type) {
            case 'checkbox': return createCheckboxInput(model);
            case 'dropdown': return createDropdownInput(model);
            case 'range'   : return createRangeInput(model);
            case 'value'   : return createValueInput(model);
            case 'text'    : return createTextInput(model);
            case 'color'   : return createColorInput(model);
            default        : return model.input;
         }
      }

      function createComponent(model) {
         let panel = $('<div/>', { id: model.id });
         let syntheticId = 0;
         let collapsed = false;
         for (let t = 0; t < model.groups.length; t++) {
            let group = model.groups[t];
            if (group.available !== false) {
              let table = createTable(group);
              collapsed = group.collapsed === true;
              panel.append(table);
              for (let r = 0; r < group.entries.length; r++) {
                 syntheticId++;
                 let entry = group.entries[r];
                 if (entry.available !== false) {
                   let type = entry.type;
                   let auto = type === undefined;
                   let input = entry.input;
                   if (entry.id === undefined)
                     entry.id = 'setting_' + syntheticId;
                   entry.collapsed = collapsed;
                   if (type == 'header' || auto && input === undefined) {
                      collapsed = entry.collapsed === true;
                      table.append(createHeaderRow(entry));
                   } else if (!auto) {
                      table.append(createRow(entry, createInput(entry)));
                   } else {
                      if (Array.isArray(input)) {
                        let [innerInput, innerSyntheticId] = createMultiInput(input, syntheticId, 'x-input');
                        input = innerInput;
                        syntheticId = innerSyntheticId;
                      }
                      table.append(createRow(entry, input));
                   }
                }
              }
            }
         }
         return panel;
      }

      function createMultiInput(inputs, syntheticId, css) {
        let box = $('<div/>', {'class': css});
        for (let i = 0; i < inputs.length; i++) {
          let entry = inputs[i];
          if (Array.isArray(entry)) {
            let [innerBox, innerSyntheticId] = createMultiInput(entry, syntheticId);
            box.append(innerBox);
            syntheticId = innerSyntheticId;
          } else if (entry.available !== false) {
            syntheticId++;
            if (entry.id === undefined)
              entry.id = 'setting_' + syntheticId;
            let input = createInput(entry);
            if (entry.label) {
              let config = { 'for': entry.id };
              if (entry.description)
                config.title = entry.description;
              box.append($('<span/>').append(input).append($('<label/>', config).html(entry.label))).append("\n");
            } else {
              box.append(input);
            }
          }                    
        }
        return [box, syntheticId];
      }

      return { 
        createComponent: createComponent,
        createInput: createInput,
      };
   })();

  /**
  * Legend is a generic component showing a number of current values annotated with label and color.
  */ 
  let Legend = (function() {

    function createItem(item) {
      let label = item.label;
      let value = item.value;
      let color = item.color;
      let strong = value;
      let normal = '';
      if (typeof value === 'string' && value.indexOf(' ') > 0) {
        strong = value.substring(0, value.indexOf(' '));
        normal = value.substring(value.indexOf(' '));
      }
      let attrs = { style: 'border-color: ' + color + ';' };
      if (item.status)
        attrs.class = 'status-' + item.status;
      let label0 = Array.isArray(label) ? label[0] : label;
      if (label0 === 'server') { // special rule for DAS
        label0 = 'DAS'; 
        attrs.title = "Data for the Domain Administration Server (DAS); plain instance name is 'server'";
      } else if (label0.startsWith('server:')) {
        label0 = 'DAS:' + label0.substring(7);
      }
      let textAttrs = {};
      if (item.highlight)
       textAttrs.style = 'color: '+ item.highlight + ';';
      let mainLabel = $('<span/>').text(label0);
      if (Array.isArray(label) && label.length > 1) {
        for (let i = 1; i < label.length; i++) {
          mainLabel.append(' - ' + label[i]);
        }
      }
      return $('<li/>', attrs)
        .append(mainLabel)
        .append($('<strong/>', textAttrs).text(strong))
        .append($('<span/>').text(normal));
    }

    function createComponent(model) {
      let legend = $('<ol/>',  {'class': 'Legend'});
      for (let item of model) {
        legend.append(createItem(item));
      }
      return legend;
    }

    return { createComponent: createComponent };
  })();

  /**
   * Component drawn for each widget legend item to indicate data status.
   */
  const Indicator = (function() {

    function createComponent(model) {
       if (!model.text) {
          return $('<div/>', {'class': 'Indicator', style: 'display: none;'});
       }
       let html = model.text.replace(/\*([^*]+)\*/g, '<b>$1</b>').replace(/_([^_]+)_/g, '<i>$1</i>');
       return $('<div/>', { 'class': 'Indicator status-' + model.status, style: 'color: ' + model.color + ';' }).html(html);
    }

    return { createComponent: createComponent };
  })();


  const RAGIndicator = (function() {

    function createComponent(model) {
      const indicator = $('<div/>', { 'class': 'RAGIndicator' });
      const itemHeight = Math.floor(100 / model.items.length);
      for (let item of model.items) {
        let text = item.label;
        if (text == 'server')
          text = 'DAS';
        indicator.append($('<div/>', { 
          title: item.state, 
          'class': 'Item', 
          style: 'background-color: '+item.background+ '; height: '+itemHeight+'%; border-left-color: '+item.color+';' })
        .append($('<span/>').text(text)));
      }
      return indicator;
    }

    return { createComponent: createComponent };

  })();

  /**
   * Component for any of the text+icon menus/toolbars.
   */
  const Menu = (function() {

    function createComponent(model) {
      let attrs = { 'class': 'Menu' };
      if (model.id)
        attrs.id = model.id;
      let menu = $('<span/>', attrs);
      let groups = model.groups;
      for (let g = 0; g < groups.length; g++) {
        let group = groups[g];
        if (group.items) {
          let groupBox = $('<span/>', { class: 'Group' });
          let groupLabel = $('<a/>').html(createText(group));
          let groupItem = $('<span/>', { class: 'Item' })
            .append(groupLabel)
            .append(groupBox)
            ;
          if (group.clickable) {
            groupLabel
              .click(group.items.find(e => e.hidden !== true && e.disabled !== true).onClick)
              .addClass('clickable');
          }
          menu.append(groupItem);
          for (let i = 0; i < group.items.length; i++) {
            let item = group.items[i];
            if (item.hidden !== true)
              groupBox.append(createButton(item));
          }          
        } else {
          if (group.hidden !== true)
            menu.append(createButton(group).addClass('Item'));
        }
      }
      return menu;
    }

    function createText(button) {
      let text = '';
      if (button.icon)
        text += '<strong>'+button.icon+'</strong>';
      if (button.label)
        text += button.label;
      if (button.label && button.items)
        text += " &#9013;";
      return text;
    }

    function createButton(button) {
      let attrs = { title: button.description };
      if (button.disabled)
        attrs.disabled = true;
      return $('<button/>', attrs)
            .html(createText(button))
            .click(button.onClick)
            .addClass('clickable');
    }

    return { createComponent: createComponent };
  })();



  /**
   * An alert table is a widget that shows a table of alerts that have occured for the widget series.
   */
  let AlertTable = (function() {

    function createComponent(model) {
      let items = model.items === undefined ? [] : model.items.sort(sortMostUrgetFirst);
      config = { 'class': 'AlertTable' };
      if (model.id)
        config.id = model.id;
      if (items.length == 0)
        config.style = 'display: none';
      let table = $('<div/>', config);
      for (let i = 0; i < items.length; i++) {
        table.append(createAlertRow(items[i], model.verbose));
      }
      return table;
    }

    function createAlertRow(item, verbose) {
      item.frames = item.frames.sort(sortMostRecentFirst); //NB. even though sortMostUrgetFirst does this as well we have to redo it here - JS...
      let endFrame = item.frames[0];
      let ongoing = endFrame.until === undefined;
      let level = endFrame.level;
      let color = ongoing ? endFrame.color : Colors.hex2rgba(endFrame.color, 0.6);
      let box = $('<div/>', { style: 'border-color:' + color + ';' });
      box.append($('<input/>', { type: 'checkbox', checked: item.acknowledged, disabled: item.acknowledged })
        .change(() => acknowledge(item)));
      box.append(createGeneralGroup(item, verbose));
      box.append(createStatisticsGroup(item, verbose));
      if (ongoing && verbose)
        box.append(createConditionGroup(item));
      if (verbose && Array.isArray(item.annotations) && item.annotations.length > 0)
        box.append(createAnnotationGroup(item));
      let row = $('<div/>', { id: 'Alert-' + item.serial, class: 'Item ' + level, style: 'border-color:'+item.color+';' });
      row.append(box);
      return row;
    }

    function acknowledge(item) {
      Controller.requestAcknowledgeAlert(item.serial);
    }

    function createAnnotationGroup(item) {
      let id = 'Alert-' + item.serial + '-Annotations';
      let element = $('#' + id); 
      let display = element.length == 0 || element.is(":visible") ? 'block' : 'none';
      let groups = $('<div/>', { id: id, style: 'display: ' + display + ';' });
      let baseColor = item.frames[0].color;
      for (let i = 0; i < item.annotations.length; i++) {
        let annotation = item.annotations[i];
        groups.append(AnnotationTable.createEntry({
          unit: item.unit,
          time: annotation.time,
          value: annotation.value,
          attrs: annotation.attrs,
          color: Colors.hex2rgba(baseColor, 0.45),
          fields: annotation.fields,
        }));
      }
      let label = display == 'none' ? '[ + ]' : '[ - ]';
      let group = $('<div/>');
      appendProperty(group, 'Annotations', $('<a/>').text(label).click(() => groups.toggle()));
      group.append(groups);
      return group;
    }

    function createConditionGroup(item) {
      let endFrame = item.frames[0];
      let circumstance = item.watch[endFrame.level];
      let group = $('<div/>', { 'class': 'Group' });
      appendProperty(group, 'Start', formatCondition(circumstance.start, item.unit));
      if (circumstance.stop) {
        appendProperty(group, 'Stop', formatCondition(circumstance.stop, item.unit));
      }
      return group;
    }

    function createStatisticsGroup(item, verbose) {
        let endFrame = item.frames[0];
        let startFrame = item.frames[item.frames.length - 1];
        let duration = durationMs(startFrame, endFrame);
        let amberStats = computeStatistics(item, 'amber');
        let redStats = computeStatistics(item, 'red');
        let group = $('<div/>', { 'class': 'Group' });
        appendProperty(group, 'Since', Units.formatTime(startFrame.since));
        appendProperty(group, 'For', formatDuration(duration));
        if (redStats.count > 0 && verbose)
          appendProperty(group, 'Red', redStats.text);
        if (amberStats.count > 0 && verbose)
          appendProperty(group, 'Amber', amberStats.text);
      return group;
    }

    function createGeneralGroup(item, verbose) {
      let group = $('<div/>', { 'class': 'Group' });
      appendProperty(group, 'Alert', item.serial);
      appendProperty(group, 'Watch', item.name);
      if (item.series)
        appendProperty(group, 'Series', item.series);
      if (item.instance && verbose)
        appendProperty(group, 'Instance', item.instance === 'server' ? 'DAS' : item.instance);
      return group;
    }

    function computeStatistics(item, level) {
      const reduceCount = (count, frame) => count + 1;
      const reduceDuration = (duration, frame) => duration + durationMs(frame, frame);
      let frames = item.frames;
      let matches = frames.filter(frame => frame.level == level);
      let count = matches.reduce(reduceCount, 0);
      let duration = matches.reduce(reduceDuration, 0);
      return { 
        count: count,
        duration: duration,
        text: formatDuration(duration) + ' x' + count, 
      };
    }

    function durationMs(frame0, frame1) {
      return (frame1.until === undefined ? new Date() : frame1.until) - frame0.since;
    }

    function formatDuration(ms) {
      return Units.converter('sec').format(Math.round(ms/1000));
    }

    /**
     * Sorts alerts starting with ongoing most recent red and ending with ended most past amber.
     */
    function sortMostUrgetFirst(a, b) {
      a.frames = a.frames.sort(sortMostRecentFirst);
      b.frames = b.frames.sort(sortMostRecentFirst);
      let aFrame = a.frames[0]; // most recent frame is not at 0
      let bFrame = b.frames[0];
      let aOngoing = aFrame.until === undefined;
      let bOngoing = bFrame.until === undefined;
      if (aOngoing != bOngoing)
        return aOngoing ? -1 : 1;
      let aLevel = aFrame.level;
      let bLevel = bFrame.level;
      if (aLevel != bLevel)
        return aLevel === 'red' ? -1 : 1;
      return bFrame.since - aFrame.since; // start with most recent item
    }

    function sortMostRecentFirst(a, b) {
      return b.since - a.since; // sort most recent frame first 
    }

    return { createComponent: createComponent };
  })();


  /**
   * The annotation table is shown for widgets of type 'annotation'.
   * Alert lists with annotations visible also use the list entry to add annotations to alert entries.
   * 
   * The interesting part with annotations is that the attributes can be any set of string key-value pairs.
   * Still, the values should be formatted meaningfully. Therefore formatters can be set which analyse each
   * key-value-pair to determine how to display them.
   *
   * The annotation table can either display a list style similar to alert table or an actual table with
   * rows and columns. An that case all items are expected to have the same fields value.
   */
  let AnnotationTable = (function() {

    function inRange(x, min, max) {
      return x >= min && x <= max;
    }

    let SQLValueFormatter = {
      applies: (item, attrKey, attrValue) => attrValue.includes(' ') && attrValue.trim().endsWith(';'),
      format:  (item, attrValue) => attrValue,
      type: 'pre',
    };

    let TimeValueFormatter = {
      applies: (item, attrKey, attrValue) => inRange(new Date().getTime() - Number(attrValue), 0, 86400000), // 1 day in millis
      format:  (item, attrValue) => Units.formatTime(attrValue),  
    };

    let UnitValueFormatter = (function(names) {
      return {
        applies: (item, attrKey, attrValue) => names.indexOf(attrKey) >= 0 && !Number.isNaN(parseInt(attrValue)),
        format: (item, attrValue) => Units.converter(item.unit).format(Number(attrValue)),
      };
    });

    let SeriesValueFormatter = {
      applies: (item, attrKey, attrValue) => attrKey == 'Series' || attrValue.startsWith('ns:'),
      format: (item, attrValue) => attrValue,
      type: 'code',
    };

    let PlainValueFormatter = {
      applies: (item, attrKey, attrValue) => true,
      format: (item, attrValue) => attrValue,
    };

    let DEFAULT_FORMATTERS = [
      TimeValueFormatter,
      UnitValueFormatter('Threshold'),
      SeriesValueFormatter,
      SQLValueFormatter,
      PlainValueFormatter,
    ];

    function createComponent(model) {
      let items = model.items || [];
      config = { 'class': 'AnnotationTable' };
      if (model.id)
        config.id = model.id;
      if (items.length == 0)
        config.style = 'display: none';
      let isTable = model.mode === 'table';
      let tag = isTable ? '<table/>' : '<div/>';
      let table = $(tag, config);
      if (model.sort === undefined && isTable || model.sort == 'value')
        items = items.sort((a, b) => b.value - a.value);
      if (model.sort == 'time')
        items = items.sort((a, b) => b.time - a.time);
      for (let item of items) {
        if (isTable) {
          if (table.children().length == 0) {
            let tr = $('<tr/>');
            for (let key of Object.keys(createAttributesModel(item)))
              tr.append($('<th/>').text(key));
            table.append(tr);
          }
          table.append(createTableEntry(item));  
        } else {
          table.append(createListEntry(item));  
        }
      }
      return table;
    }

    function createListEntry(item) {      
      let attrs = createAttributesModel(item);
      let group = $('<div/>', { 'class': 'Group Annotation', style: 'border-color:' + item.color + ';' });
      for (let [key, entry] of Object.entries(attrs)) {
        appendProperty(group, key, entry.value, entry.type);
      }      
      return group;
    }

    function createTableEntry(item) {
      let attrs = createAttributesModel(item);
      let row = $('<tr/>', { 'class': 'Annotation' });
      let style = { 'style': 'border-left-color: ' + item.color + ';' };
      for (let entry of Object.values(attrs)) {
        let td = $('<td/>', style);
        style = {}; // clear after 1. column
        if (entry.type) {
          td.append($('<' + entry.type + '/>').append(entry.value));
        } else {
          td.text(entry.value);
        }
        row.append(td);
      }
      return row;
    }

    function createAttributesModel(item) {
      let attrs = {}; // new object is both sorted by default order and accessible by key
      if (item.series)
        attrs.Series = { value: item.series, type: 'code' };        
      if (item.instance)
        attrs.Instance = { value: item.instance };
      attrs.When = { value: Units.formatTime(item.time) };
      attrs.Value = { value: Units.converter(item.unit).format(item.value) };
      for (let [key, value] of Object.entries(item.attrs)) {
        let formatter = selectFormatter(item, key, value, item.formatters);
        attrs[key] = { value: formatter.format(item, value), type: formatter.type };        
      }
      if (!item.fields)
        return attrs;
      let model = {};
      for (let field of item.fields) {
        let entry = attrs[field];
        if (entry)
          model[field] = entry;
      }
      return model;
    }

    function selectFormatter(item, attrKey, attrValue, formatters) {
      if (formatters === undefined)
        return selectFormatter(item, attrKey, attrValue, DEFAULT_FORMATTERS);
      for (let formatter of formatters) 
        if (formatter.applies(item, attrKey, attrValue))
          return formatter;
      if (formatters !== DEFAULT_FORMATTERS)
        return selectFormatter(item, attrKey, attrValue, DEFAULT_FORMATTERS);
      return PlainValueFormatter;
    }

    return { 
      createComponent: createComponent,
      createEntry: createListEntry, 
    };
  })();

  /**
   * Lists existing watches to explain their logic to the user.
   */
  const WatchList = (function() {

    function createComponent(model) {
      const config = { 'class': 'WatchList' };
      if (model.id)
        config.id = model.id;
      const list = $('<div/>', config);
      for (let item of model.items) {
        list.append(createItem(item, model.colors, model.actions));
      }
      return list;
    }

    function createItem(item, colors, actions) {
      const watch = $('<div/>', { 'class': 'WatchItem ' + 'state-' + (item.disabled || item.stopped ? 'disabled' : 'enabled') });
      const disableButton = Settings.createInput({ type: 'checkbox', value: !item.disabled, onChange: (checked) => {
        if (checked) {
          actions.onEnable(item.name);
        } else {
          actions.onDisable(item.name);
        }
      }});
      const menu = [
        { icon: '&#128295;', label: item.programmatic ? 'Duplicate' : 'Edit', onClick: () => actions.onEdit(item) }
      ];
      if (item.disabled) {
        menu.push({ icon: '&#9745;', label: 'Enable', onClick: () => actions.onEnable(item.name) });        
      } else {
        menu.push({ icon: '&#9746;', label: 'Disable', onClick: () => actions.onDisable(item.name) });        
      }
      if (!item.programmatic) {
        menu.push({ icon: '&times;', label: 'Delete', onClick: () => actions.onDelete(item.name) });
      }
      const general = $('<h3/>').append(disableButton).append(item.name + (item.stopped ? ' (stopped)' : '')).click(() => actions.onEdit(item));
      watch
        .append(Menu.createComponent({ groups: [{ icon: '&#9881;', items: menu }]}))
        .append(general);
      for (let level of ['red', 'amber', 'green'])
        if (item[level])
          watch.append(createCircumstance(level, item[level], item.unit, item.series, colors[level]));
      return watch;
    }

    function createCircumstance(level, model, unit, series, color) {
      function plainText(condition) {
        let text = condition.text();
        return text.substring(text.indexOf('value') + 5);
      }
      const circumstance = $('<div/>', { 'class': 'WatchCondition', style: 'color: '+ color +';'});
      let levelText = paddedLeftWith('&nbsp;', Units.Alerts.name(level), 'Unhealthy'.length);
      let text = '<b>' + levelText + ':</b> <em>If</em> ' + series + ' <em>in</em> ' + Units.names()[unit] + ' <em>is</em> ';
      text += plainText(formatCondition(model.start, unit));
      if (model.suppress)
        text += ' <em>unless</em> ' + model.surpressingSeries + ' ' + plainText(formatCondition(model.suppress, model.surpressingUnit));
      if (model.stop)
        text += ' <em>until</em> ' + plainText(formatCondition(model.stop, unit));
      return circumstance.html(text);
    }

    return { createComponent: createComponent };
  })();

  /**
   * A component that creates the form to compose a single new watch
   */
  const WatchBuilder = (function() {
    
    function createComponent(model, watch) {
      const config = { 'class': 'WatchBuilder WatchItem' };
      if (model.id)
        config.id = model.id;
      let editedWatch = watch || { unit: 'count', name: 'New Watch' };
      if (editedWatch.programmatic) {
        editedWatch = JSON.parse(JSON.stringify(watch));
        editedWatch.name = 'Copy of ' + watch.name;
        editedWatch.programmatic = false;
      }
      const builder = $('<div/>', config);
      const nameInput = Settings.createInput({ type: 'text', value: editedWatch.name, onChange: (name) => editedWatch.name = name });
      builder.append($('<h3/>').append(nameInput));
      const unitDropdowns = [];
      const seriesInputs = [];
      for (let level of ['red', 'amber', 'green']) {
        builder.append(createLevelBuilder(level, editedWatch, model.colors[level], unitDropdowns, seriesInputs));
      }
      builder.append($('<button/>').text('Save or Update').click(() => model.actions.onCreate(editedWatch)));
      return builder;
    }

    function createLevelBuilder(level, editedWatch, color, unitDropdowns, seriesInputs) {
      const editedCircumstance = editedWatch[level] || { level: level };
      const editedStartCondition = editedCircumstance.start || { operator: '>', forTimes: 1 };
      const editedStopCondition = editedCircumstance.stop || { operator: '<', forTimes: 1 };
      const defined = editedWatch[level] !== undefined;
      const levelBox = $('<span/>', defined ? {} : { style: 'display: none;'});
      let enableCheckbox = Settings.createInput({type: 'checkbox', value: defined, onChange: (checked) => {
        if (checked) {
          levelBox.show();
          editedWatch[level] = editedCircumstance;
          editedCircumstance.start = editedStartCondition;
        } else {
          levelBox.hide();
          editedWatch[level] = undefined;
        }
      }});
      const unitDropdown = Settings.createInput({ type: 'dropdown', value: editedWatch.unit, options: Units.names(), onChange: (selected) => {
        editedWatch.unit = selected;
        unitDropdowns.forEach(dropdown => dropdown.val(selected));
      }});
      unitDropdowns.push(unitDropdown);
      const seriesInput = Settings.createInput({ type: 'text', value: editedWatch.series, onChange: (series) => {
        editedWatch.series = series;
        seriesInputs.forEach(input => input.val(series));
      }});
      seriesInputs.push(seriesInput);
      const isUntilDefined = editedCircumstance.stop !== undefined;
      const untilBox = $('<span/>', isUntilDefined ? {} : { style: 'display: none;'})
        .append(createConditionBuilder(editedWatch, editedCircumstance, editedStopCondition));      
      const untilCheckbox = Settings.createInput({ type: 'checkbox', value: isUntilDefined, onChange: (checked) => {
        if (checked) {
          untilBox.show();
          editedCircumstance.stop = editedStopCondition;
        } else {
          untilBox.hide();
          editedCircumstance.stop = undefined;
        }
      }});
      levelBox
        .append(' <em>If</em> ').append(seriesInput)
        .append(' <em>in</em> ').append(unitDropdown)
        .append(' <em>is<em/> ').append(createConditionBuilder(editedWatch, editedCircumstance, editedStartCondition))
        .append(' <em>until</em> ').append(untilCheckbox).append(untilBox);
      return $('<div/>', {'class': 'WatchCondition', style: 'color: ' + color + ';' })
        .append(enableCheckbox).append('<b>' + paddedLeftWith('&nbsp;', Units.Alerts.name(level), 'Unhealthy'.length) + ':</b>')
        .append(levelBox);
    }

    function createConditionBuilder(editedWatch, editedCircumstance, editedCondition) {
      function getKind() {
        if (editedCondition.forTimes === 0 || editedCondition.forMillis === 0)
          return 'inSample';
        if (editedCondition.forTimes < 0 || editedCondition.forMillis < 0)
          return 'inLast';
        if (editedCondition.onAverage)
          return 'forAvgOfLast';
        return 'forLast';
      }
      const kindOptions = {
        forLast: 'for last', 
        forAvgOfLast: 'for average of last', 
        inLast: 'in last', 
        inSample: 'in sample'
      };
      const forInBox = $('<span/>', getKind() != 'inSample' ? {} : { style: 'display: none;' });
      function updateEditedCondition(selectedKind, forLastInputValue) {
        if (selectedKind === undefined)
          selectedKind = getKind();
        const isCount = forLastInputValue === undefined 
          ? editedCondition.forTimes !== undefined
          : /^[0-9]+$/i.test(forLastInputValue);
        const forLast = forLastInputValue === undefined
          ? editedCondition.forTimes !== undefined ? Math.abs(editedCondition.forTimes) : Math.abs(editedCondition.forMillis)
          : Units.converter(isCount ? 'count' : 'ms').parse(forLastInputValue);
        editedCondition.onAverage = selectedKind === 'forAvgOfLast';
        if (selectedKind == 'forLast' || selectedKind == 'forAvgOfLast') {
          editedCondition.forTimes = isCount ? Math.abs(forLast) : undefined;
          editedCondition.forMillis = isCount ? undefined : Math.abs(forLast);
        } else if (selectedKind == 'inLast') {
          editedCondition.forTimes = isCount ? - Math.abs(forLast) : undefined;
          editedCondition.forMillis = isCount ? undefined : - Math.abs(forLast);
        }
        if (selectedKind == 'inSample') {
          forInBox.hide();
          editedCondition.forTimes = 0;
          editedCondition.forMillis = undefined;
        } else {
          forInBox.show();
        }        
      }
      const forInValue = editedCondition.forTimes !== undefined 
        ? Math.abs(editedCondition.forTimes) 
        : editedCondition.forMillis !== undefined ? Units.converter('ms').format(Math.abs(editedCondition.forMillis)) : 1;
      const operatorDropdown = Settings.createInput({ type: 'dropdown', value: editedCondition.operator, options: ['<', '<=', '=', '>', '>='], onChange: (selected) => editedCondition.operator = selected});
      const thresholdInput = Settings.createInput({ type: 'value', unit: () => editedWatch.unit, value: editedCondition.threshold, onChange: (value) => editedCondition.threshold = value});
      const forInInput = Settings.createInput({ type: 'text', value: forInValue, onChange: (value) => updateEditedCondition(undefined, value)});     
      const kindDropdown = Settings.createInput({ type: 'dropdown', value: getKind(), options: kindOptions, onChange: (selected) => updateEditedCondition(selected, undefined)});
      return $('<span/>')
        .append(operatorDropdown)
        .append(thresholdInput)
        .append(kindDropdown)
        .append(forInBox.append(forInInput));
    }

    return { createComponent: createComponent };

  })();


  /**
   * Combines the WatchList and WatchBuilder into one component to list and create watches.
   */ 
  const WatchManager = (function() {

    function createComponent(model) {
      const config = { 'class': 'WatchManager' };
      if (model.id)
        config.id = model.id;
      const manager = $('<div/>', config);
      model.id = undefined; // id should not be set by sub-components
      let builder = WatchBuilder.createComponent(model);
      const list = WatchList.createComponent(model);
      model.actions.onEdit = (watch) => {
        const newBuilder = WatchBuilder.createComponent(model, watch);
        builder.replaceWith(newBuilder);
        builder = newBuilder;
      };
      manager.append(list);
      manager.append(builder);
      return manager;
    }

    return { createComponent: createComponent };
  })();


  /**
   * A component that creates a tabular overview for pages and their synchronisation state.
   * Users select which pages to synchronise (pull from remote).
   */
  const PageManager = (function() {

    function createComponent(model) {
      const config = { 'class': 'PageManager' };
      if (model.id)
        config.id = model.id;
      const manager = $('<div/>', config);
      const list = $('<table/>').append($('<tr/>')
        .append($('<th/>'))
        .append($('<th/>').text('Page'))
        .append($('<th/>').text('Local Version'))
        .append($('<th/>').text('Based on Remote Version'))
        .append($('<th/>').text('Remote Version'))
      );
      const selection = {};
      model.pages.forEach(page => list.append(createItem(page, selection)));
      const updateBtn = $('<button/>', { text: 'Update', title: 'Updates checked pages locally with the remote configuration for these pages'})
        .click(() => model.onUpdate(Object.keys(selection)));
      const cancelBtn = $('<button/>', { text: 'Cancel'}).click(model.onCancel);
      return manager.append($('<div/>', {'class': 'content'})
        .append($('<h3/>').text('Page Synchronisation'))
        .append($('<p/>').text('Please select the pages that should be updated with their server (remote) configuration (newest highlighted in green):'))
        .append(list)
        .append(updateBtn)
        .append(cancelBtn)
      );
    }

    function createItem(page, selection) {
      if (page.checked)
        selection[page.id] = true;
      const onChange = (checked) => { selection[page.id] = checked; };
      const checkbox = $('<input/>', { type: 'checkbox', checked: page.checked })
        .on('change', function() {
          if (this.checked) {
            selection[page.id] = true;
          } else {
            delete selection[page.id];
          }
        });
      const localAttrs = page.lastLocalChange >= page.lastRemoteChange ? {'class': 'recent'} : {};
      const remoteAttrs = page.lastLocalChange == undefined || page.lastLocalChange <= page.lastRemoteChange ? {'class': 'recent'} : {};
      const baseAttrs = page.lastRemoteUpdate != undefined && page.lastRemoteUpdate == page.lastRemoteChange ? {'class': 'recent'} : {};
      const localText = page.lastLocalChange === undefined && page.lastRemoteUpdate !== undefined ? '(not modified)' : Units.formatDateTime(page.lastLocalChange);
      return $('<tr/>')
        .append($('<td/>').append(checkbox))
        .append($('<th/>').text(page.name))
        .append($('<td/>', localAttrs).text(localText))
        .append($('<td/>', baseAttrs).text(Units.formatDateTime(page.lastRemoteUpdate)))
        .append($('<td/>', remoteAttrs).text(Units.formatDateTime(page.lastRemoteChange)));
    }

    return { createComponent: createComponent };
  })();


  /**
   * A component that creates a panel (for modal dialogue) that presents possible roles for user to select one.
   */
  const RoleSelector = (function(){

    function createComponent(model) {
      const config = { 'class': 'RoleSelector' };
      if (model.id)
        config.id = model.id;
      const selector = $('<div/>', config);
      const box = $('<div/>', {'class': 'content'}).append($('<h3/>').text('Select User Role'));
      const list = $('<ul/>');
      box.append(list);
      let seletion = [undefined];
      model.items.forEach(role => list.append(createItem(role, seletion)));
      box.append($('<button/>', { text: 'Select' }).click(() => model.onChange(seletion[0])));
      return selector.append(box);
    }

    function createItem(role, seletion) {
      const id = 'role-'+role.name;
      return $('<li/>')
        .append($('<input/>', { type: 'radio', name: 'role', value: role.name, id: id }).change(function() {
            seletion[0] = this.value;
          }))
        .append($('<label/>', { for: id }).append($('<b/>').text(role.label)).append('<br/>').append(role.description));
    }

    return { createComponent: createComponent };
  })();



  /**
    * A component that creates a wizard for series selection
    */
  const SelectionWizard = (function() {

    function createComponent(model) {
      const config = { 'class': 'SelectionWizard'};
      if (model.id)
        config.id = model.id;
      const wizard = $('<div/>', config);
      
      const state = {
        selection: {},  // key propertys of selected matches
        properties: {}, // those bound to a specific value by chosing a filter option
        filters: new Array(model.filters.length) // state for each filter: input (text), selected (index), filter (fn)
      };
      for (let i = 0; i < state.filters.length; i++)
        state.filters[i] = {};

      for (let i = 0; i < model.filters.length; i++)
        model.filters[i].id = i;

      // fixed UI state
      const searchBox = $('<div/>', { 'class': 'Search' });
      const filterBox = $('<div/>', { 'class': 'Filters' });
      const matchList = $('<div/>', { 'class': 'Matches'});
      
      // applying the state to the UI
      let matches;

      const update = async function() {
        if (matches === undefined) {
          matches = (await model.onSearch(state.properties))
            .sort((a, b) => a[model.key].localeCompare(b[model.key]));
          if (model.selection)
            for (let key of model.selection)
              state.selection[key] = matchForKey(key, model.key, matches);

        } else {
          model.onChange(Object.keys(state.selection));
        }
        matches.forEach(match => match.filtered = false);
        filterBox.empty();
        for (let filter of model.filters)
          filterBox.append(createFilter(model, filter, matches, state));
        recreateMatchList(model, state, matchList, matches);
      };
      state.changed = update;
      update();
      searchBox.append(filterBox);
      return wizard.append(searchBox).append(matchList);
    }

    function matchForKey(key, keyProperty, matches) {
      return matches.find(match => match[keyProperty] == key);
    }

    function recreateMatchList(model, state, list, matches) {
      list.empty();

      const selectionCount = Object.keys(state.selection).length;
      if (selectionCount > 0) {
        const selection = $('<div/>', { 'class': 'Selection' });
        selection.append($('<b/>').text(selectionCount + ' Selected'));
        for (let match of Object.values(state.selection))
          selection.append(createMatchEntry(model, state, match, true));
        list.append(selection);
      }

      let c = 0;
      for (let match of matches)
        if (!match.filtered)
          c++;

      if (c == matches.length) {
        list.append($('<b/>').text('Please select a filter...'));
        return;
      }

      list.append($('<b/>').text(c + ' matches')).append($('<span/>').text(' for total of ' + matches.length + ' metrics'));
      for (let match of matches)
        if (!match.filtered)
          list.append(createMatchEntry(model, state, match, false));
      list.append($('<div/>', { style: 'clear:both;' }));
    }

    function createMatchEntry(model, state, match, describe) {
      const keyAccessor = model.properties[model.key];
      const key = keyAccessor(match);
      const id = 'match-' + key.replace(/[^-a-zA-Z0-9_]/g, '_');
      const checked = state.selection[key] !== undefined;
      const input = $('<input/>', { type: 'checkbox', id: id, checked: checked }).change(function() {
        if (this.checked) {
          state.selection[key] = match;  
        } else {
          delete state.selection[key];
        }
        state.changed();
      });
      const entry = {};
      for (let property of model.entry)
        entry[property] = model.properties[property].call(this, match);
      entry.selected = state.selection[key] !== undefined;
      entry.describe = describe;
      return $('<div/>').append(input).append($('<label/>', { for: id }).append(model.render(entry)));
    }

    function createFilter(model, filter, matches, state) {
      if (!isSatisfied(filter.requires, state)) {     
        return $('<span/>');
      }
      filter.type = computeFilterType(filter);
      
      const label = $('<label/>', { for: 'filter-' + filter.id, text: filter.label });
      const filterInput = createFilterInput(model, filter, state, matches);

      const filterState = state.filters[filter.id];
      const active = filterState !== filterState.filter !== undefined || filterState.input !== undefined;
      if (active) {
        applyFilter(model, filter, state, matches);
      }
      return $('<div/>', { 'class': 'Filter' })
        .append(label)
        .append(filterInput);
    }

    function applyFilter(model, filter, state, matches) {
      for (let match of matches) {
        if (!match.filtered && !matchesFilter(match, model, filter, state)) {
          match.filtered = true;
        }
      }
    }

    function matchesFilter(match, model, filter, state, option) {
      const filterState = state.filters[filter.id];
      const match2property = model.properties[filter.property];          
      const propertyValue = match2property(match);  
      if (filter.type == 'text') { // filter uses input and predicate function
        let input = filterState.input;
        return input == undefined || input == '' || filter.filter(propertyValue, input);
      }
      const optionFilter = option === undefined ? filterState.filter : option.filter;                 
      // type 'list' and 'auto' below
      if (typeof optionFilter === 'string') // option uses a constant value
        return propertyValue == optionFilter;
      if (typeof optionFilter === 'function') // option uses a predicate function              
        return optionFilter(propertyValue);
      return true;
    }

    function countMatches(model, filter, state, matches, option) {
      let c = 0;
      for (let match of matches)
        if (matchesFilter(match, model, filter, state, option))
          c++;
      return c;
    }

    function computeFilterType(filter) {
      if (typeof filter.filter === "function")
        return 'text';
      if (filter.options !== undefined)
        return 'list';
      return 'auto';
    }

    function createFilterInput(model, filter, state, matches) {
      switch (filter.type) {
      case 'text': return createFilterWithTextInput(model, filter, state, matches);
      case 'list': return createFilterWithListInput(model, filter, state, matches);
      default:
      case 'auto': return createFilterWithAutoInput(model, filter, state, matches);
      }
    }

    function createFilterWithTextInput(model, filter, state, matches) {
      const filterState = state.filters[filter.id];
      const active = filterState !== undefined;
      const id = 'filter-text-' + filter.id;
      const field = $('<input/>', { id: id, type: 'text', value: active ? filterState.input || '' : '' });
      field.on('input change', function() {
        state.filters[filter.id].input = field.val();
        state.changed().then(() => {
          const input = $('#' + id);
          const val = input.val();
          input.focus().val('').val(val); // gains focus and moves caret to end
        });
      });
      return field;
    }

    function createFilterWithListInput(model, filter, state, matches) {
      const filterState = state.filters[filter.id];
      const options = typeof filter.options === 'function' ? filter.options() : filter.options;
      const selectField = $('<select/>');
      selectField.change(() => {
        let index = selectField.val();
        if (index >= 0) {
          let f = options[index].filter;
          state.filters[filter.id].filter = f;
          state.filters[filter.id].selected = index;
          state.properties[filter.property] = typeof f === 'string' ? f : undefined;          
        } else {
          state.filters[filter.id] = {};
          state.properties[filter.property] = undefined;
        }
        state.changed();
      });
      selectField.append($('<option/>', { value: -1, text: '(any)' }));
      for (let i = 0; i < options.length; i++) {       
        let label = options[i].label + ' (' + countMatches(model, filter, state, matches, options[i]) +  ')';
        selectField.append($('<option/>', { value: i, text: label, selected: filterState.selected == i }));
      }
      return selectField;      
    }

    function createFilterWithAutoInput(model, filter, state, matches) {
      const filterState = state.filters[filter.id];
      const match2property = model.properties[filter.property];
      // options are derived from the matches as the set of actual values
      const set = {};
      for (let match of matches) {        
        let propertyValue = match2property(match);
        if (propertyValue !== undefined)
          set[propertyValue] = set[propertyValue] === undefined ? 1 : set[propertyValue] + 1;
      }
      const options = Object.keys(set);
      const selectField = $('<select/>');
      selectField.change(() => {
        let f = selectField.val();
        if (f != '') {
          state.filters[filter.id].filter = f;
          state.properties[filter.property] = f;
        } else {
          state.filters[filter.id] = {};
          state.properties[filter.property] = undefined;
        }
        state.changed();
      });
      selectField.append($('<option/>', { value: '', text: '(any)' }));
      for (let option of options) {
        let text = option + ' ('+ set[option] +')';
        selectField.append($('<option/>', { value: option, text: text, selected: filterState.filter == option }));      
      }
      return selectField;
    }

    function isSatisfied(requires, state) {
      if (requires === undefined)
        return true;
      for (let [property, required] of Object.entries(requires)) {
        let bound = state.properties[property];
        if (typeof required === 'string') {
          if (bound != required)
            return false;          
        } else if (typeof required === 'function') {
          if (!required(bound))
            return false;
        }

      }
      return true;
    }

    return { createComponent: createComponent };
  })();


  /**
    * A component that creates a model window.
    */
  const ModalDialog = (function() {

    function createComponent(model) {
      const config = { 'class' : 'ModalDialog' };
      if (model.id)
        config.id = model.id;
      const dialog = $('<div/>', config);
      const box = $('<div/>', {'class': 'content'});
      if (model.title !== undefined && model.title != '') {
        box.append($('<h3/>', { text: model.title }));
      }
      const content = model.content();
      box.append(content);
      if (model.buttons) {
        const bar = $('<div/>', { 'class': 'Buttons' });
        for (let button of model.buttons)
          bar.append(createButton(model, button));        
        box.append(bar);
      }
      return dialog.append(box);
    }

    function createButton(model, button) {
      return $('<button/>', { text: button.label }).click(() => {
        $('#' + model.id).hide();
        if (typeof model.onExit === 'function')
          model.onExit(model.results[button.property]);
      });
    }

    return { createComponent: createComponent };
  })();

  /*
   * Shared functions
   */

  function appendProperty(parent, label, value, tag = "strong") {
    parent.append($('<span/>')
      .append($('<small>', { text: label + ':' }))
      .append($('<' + tag + '/>').append(value))
    ).append('\n'); // so browser will line break;
  }

  function formatCondition(condition, unit) {
    if (condition === undefined)
      return '';
    const forTimes = condition.forTimes;
    const forMillis = condition.forMillis;
    let any = forTimes === 0 || forMillis === 0;
    let anyN = forTimes < 0 || forMillis < 0;
    let threshold = Units.converter(unit).format(condition.threshold);
    let text = ''; 
    let forText = '';
    let forValue;
    text += 'value ' + condition.operator + ' ' + threshold;
    if (forTimes !== undefined || forMillis !== undefined) {
      if (any) {
        forText += ' in sample';
      } else if (anyN) {
        forText += ' in last ';
      } else if (condition.onAverage) {
        forText += ' for average of last ';
      } else {
        forText += ' for last ';
      }
    }
    if (forTimes !== undefined && forTimes !== 0)
      forValue = Math.abs(condition.forTimes) + 'x';
    if (forMillis !== undefined && forMillis !== 0)
      forValue = Units.converter('ms').format(Math.abs(condition.forMillis));
    let desc = $('<span/>').append(text);
    if (forText != '')
      desc.append($('<small/>', { text: forText})).append(forValue);
    return desc;
  }

  function paddedLeftWith(char, text, length) {
    let n = length - text.length;
    for (let i = 0; i < n; i++)
      text = char + text;
    return text;
  }

  /*
  * Public API below:
  *
  * All methods return a jquery element reflecting the given model to be inserted into the DOM using jQuery
  */
  return {
      createSettings: (model) => Settings.createComponent(model),
      createLegend: (model) => Legend.createComponent(model),
      createIndicator: (model) => Indicator.createComponent(model),
      createMenu: (model) => Menu.createComponent(model),
      createAlertTable: (model) => AlertTable.createComponent(model),
      createAnnotationTable: (model) => AnnotationTable.createComponent(model),
      createWatchManager: (model) => WatchManager.createComponent(model),
      createPageManager: (model) => PageManager.createComponent(model),
      createRoleSelector: (model) => RoleSelector.createComponent(model),
      createModalDialog: (model) => ModalDialog.createComponent(model),
      createSelectionWizard: (model) => SelectionWizard.createComponent(model),
      createRAGIndicator: (model) => RAGIndicator.createComponent(model),
  };

})();
/*
   DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
  
   Copyright (c) 2019 Payara Foundation and/or its affiliates. All rights reserved.
  
   The contents of this file are subject to the terms of either the GNU
   General Public License Version 2 only ("GPL") or the Common Development
   and Distribution License("CDDL") (collectively, the "License").  You
   may not use this file except in compliance with the License.  You can
   obtain a copy of the License at
   https://github.com/payara/Payara/blob/master/LICENSE.txt
   See the License for the specific
   language governing permissions and limitations under the License.
  
   When distributing the software, include this License Header Notice in each
   file and include the License file at glassfish/legal/LICENSE.txt.
  
   GPL Classpath Exception:
   The Payara Foundation designates this particular file as subject to the "Classpath"
   exception as provided by the Payara Foundation in the GPL Version 2 section of the License
   file that accompanied this code.
  
   Modifications:
   If applicable, add the following below the License Header, with the fields
   enclosed by brackets [] replaced by your own identifying information:
   "Portions Copyright [year] [name of copyright owner]"
  
   Contributor(s):
   If you wish your version of this file to be governed by only the CDDL or
   only the GPL Version 2, indicate your decision by adding "[Contributor]
   elects to include this software in this distribution under the [CDDL or GPL
   Version 2] license."  If you don't indicate a single choice of license, a
   recipient has the option to distribute your version of this file under
   either the CDDL, the GPL Version 2 or to extend the choice of license to
   its licensees as provided above.  However, if you add GPL Version 2 code
   and therefore, elected the GPL Version 2 license, then the option applies
   only if the new code is made subject to such option by the copyright
   holder.
*/

/*jshint esversion: 8 */

MonitoringConsole.Chart.Common = (function() {

   function createCustomTooltipFunction(createHtmlTooltip) {
      return function(tooltipModel) {
        let tooltip = $('#chartjs-tooltip');
        if (tooltipModel.opacity === 0) {
          tooltip.css({opacity: 0}); // without this the tooptip sticks and is not removed when moving the mouse away
          return;
        }
        tooltipModel.opacity = 1;
        $(tooltip).empty().append(createHtmlTooltip(tooltipModel.dataPoints));
        var position = this._chart.canvas.getBoundingClientRect(); // `this` will be the overall tooltip
        tooltip.css({opacity: 1, left: position.left + tooltipModel.caretX, top: position.top + tooltipModel.caretY - 20});
      };
   }

   function formatDate(date) {
      if (typeof date === 'number') {
         date = new Date(date);
      }
      let dayOfMonth = date.getDate();
      let month = date.getMonth() + 1;
      let year = date.getFullYear();
      let hour = date.getHours();
      let min = date.getMinutes().toString().padStart(2, '0');
      let sec = date.getSeconds().toString().padStart(2, '0');
      let ms = date.getMilliseconds().toString().padStart(3, '0');
      let now = new Date();
      let diffMs =  now - date;
      let text = `Today ${hour}:${min}:${sec}.${ms}`; 
      if (diffMs < 5000) {
         return text + ' (just now)';
      }
      if (diffMs < 60 * 1000) { // less then a minute ago
         let diffSecs = diffMs / 1000;
         return text + ' (about '+ diffSecs.toFixed(0) + ' seconds ago)';
      }
      if (diffMs < 60 * 60 * 1000) { // less then an hour ago
         let diffMins = diffMs / (60*1000);
         return text + ' (about '+ diffMins.toFixed(0) + ' minutes ago)';  
      }
      let dayOfMonthNow = now.getDate();
      if (dayOfMonth == dayOfMonthNow) {
         return text;
      }
      if (dayOfMonthNow - 1 == dayOfMonth) {
         return `Yesterday ${hour}:${min}:${sec}.${ms}`; 
      }
      return `${dayOfMonth}.${month}.${year} ${hour}:${min}:${sec}.${ms}`; 
   }

   /**
    * Public API below:
    */
   return {
      /**
       * @param {function} createHtmlTooltip - a function that given dataPoints (see Chartjs docs) returns the tooltip HTML jQuery object
       */
      createCustomTooltipFunction: (createHtmlTooltip) => createCustomTooltipFunction(createHtmlTooltip),
      formatDate: (date) => formatDate(date),
   };

})();
/*
   DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
  
   Copyright (c) 2019-2020 Payara Foundation and/or its affiliates. All rights reserved.
  
   The contents of this file are subject to the terms of either the GNU
   General Public License Version 2 only ("GPL") or the Common Development
   and Distribution License("CDDL") (collectively, the "License").  You
   may not use this file except in compliance with the License.  You can
   obtain a copy of the License at
   https://github.com/payara/Payara/blob/master/LICENSE.txt
   See the License for the specific
   language governing permissions and limitations under the License.
  
   When distributing the software, include this License Header Notice in each
   file and include the License file at glassfish/legal/LICENSE.txt.
  
   GPL Classpath Exception:
   The Payara Foundation designates this particular file as subject to the "Classpath"
   exception as provided by the Payara Foundation in the GPL Version 2 section of the License
   file that accompanied this code.
  
   Modifications:
   If applicable, add the following below the License Header, with the fields
   enclosed by brackets [] replaced by your own identifying information:
   "Portions Copyright [year] [name of copyright owner]"
  
   Contributor(s):
   If you wish your version of this file to be governed by only the CDDL or
   only the GPL Version 2, indicate your decision by adding "[Contributor]
   elects to include this software in this distribution under the [CDDL or GPL
   Version 2] license."  If you don't indicate a single choice of license, a
   recipient has the option to distribute your version of this file under
   either the CDDL, the GPL Version 2 or to extend the choice of license to
   its licensees as provided above.  However, if you add GPL Version 2 code
   and therefore, elected the GPL Version 2 license, then the option applies
   only if the new code is made subject to such option by the copyright
   holder.
*/

/*jshint esversion: 8 */

/**
 * Adapter to line charts of Chart.js
 */ 
MonitoringConsole.Chart.Line = (function() {
	
  const Units = MonitoringConsole.View.Units;
  const Colors = MonitoringConsole.View.Colors;
  const Theme = MonitoringConsole.Model.Theme;

  function timeLable(secondsAgo, index, lastIndex, secondsInterval) {
    if (index == lastIndex && secondsAgo == 0)
      return 'now';
    if (index == 0 || index == lastIndex && secondsAgo > 0) {
      if (Math.abs(secondsAgo - 60) <= secondsInterval * 2)
        return '60s ago'; // this corrects off by 1 which is technically inaccurate but still 'more readable' for the user
      if (Math.abs((secondsAgo % 60) - 60) <= secondsInterval)
        return Math.round(secondsAgo / 60) + 'mins ago';
      if (secondsAgo <= 60)
        return secondsAgo +'s ago';
      return Math.floor(secondsAgo / 60) + 'mins ' + (secondsAgo % 60) + 's ago';
    }
    return undefined;
  }

  /**
   * This is like a constant but it needs to yield new objects for each chart.
   */
  function onCreation(widget) {
    let options = {
      responsive: true,
      maintainAspectRatio: false,
      scales: {
        xAxes: [{
          display: true,
          type: 'time',
          gridLines: {
            color: 'rgba(0, 127, 255,0.5)',
            lineWidth: 0.5,
          },
          time: {
            minUnit: 'second',
            round: 'second',
          },
          ticks: {
            minRotation: 0,
            maxRotation: 0,
            callback: function(value, index, values) {
              if (values.length == 0)
                return value;
              let lastIndex = values.length - 1;
              let reference = new Date(values[lastIndex].value);
              let now = new Date();
              let isLive = now - reference < 5000; // is within the last 5 secs
              if (values.length == 1)
                return isLive ? 'now' : Units.formatTime(new Date(reference));
              let secondsInterval = (values[1].value - values[0].value) / 1000;
              let secondsAgo = (values[lastIndex].value - values[index].value) / 1000;
              if (isLive) {
                return timeLable(secondsAgo, index, lastIndex, secondsInterval);
              }
              let reference2 = new Date(values[lastIndex-1].value);
              let isRecent = now - reference < (5000 + (reference - reference2));
              if (isRecent) {
                return timeLable(secondsAgo, index, lastIndex, secondsInterval);
              }
              if (index != 0 && index != lastIndex)
                return undefined;
              return Units.formatTime(new Date(values[index].value));
            },
          }
        }],
        yAxes: [{
          display: true,
          gridLines: {
            color: 'rgba(0, 127, 255,0.5)',
            lineWidth: 0.5,
          },
          ticks: {
            beginAtZero: true,
            precision:0, // no decimal places in labels
          },
        }],
      },
      legend: {
          display: false,
      }
    };
    let thresholdsPlugin = {
      beforeDraw: function (chart) {
        let yAxis = chart.chart.scales["y-axis-0"];
        let areas = chart.chart.data.areas;
        if (!Array.isArray(areas) || areas.length === 0)
          return;
        let ctx = chart.chart.ctx;
        ctx.save();
        let xAxis = chart.chart.scales["x-axis-0"];
        function yOffset(y) {
          let yMax = yAxis.ticksAsNumbers[0];
          if (y === undefined)
            y = yMax;
          let yMin = yAxis.ticksAsNumbers[yAxis.ticksAsNumbers.length - 1];
          let yVisible = y - yMin;
          let yRange = yMax - yMin;
          return yAxis.bottom - Math.max(0, (yAxis.height * yVisible / yRange));
        }
        let offsetRight = 0;
        let barWidth = areas.length < 3 ? 5 : 3;
        for (let i = 0; i < areas.length; i++) {
          let group = areas[i];
          let offsetBar = false;
          for (let j = 0; j < group.length; j++) {
            const area = group[j];
            let yAxisMin = yOffset(area.min);
            let yAxisMax = yOffset(area.max);
            let barLeft = xAxis.right + 1 + offsetRight;
            // solid fill
            if (area.min != area.max) {
              offsetBar = true;
              let barHeight = yAxisMax - yAxisMin;
              if (area.style != 'outline') {
                ctx.fillStyle = area.color;
                ctx.fillRect(barLeft, yAxisMin, barWidth, barHeight);                
              } else {
                ctx.strokeStyle = area.color;
                ctx.lineWidth = 1;
                ctx.setLineDash([]);
                ctx.beginPath();
                ctx.rect(barLeft, yAxisMin, barWidth, barHeight);
                ctx.stroke();
              }
            }
            // and the line
            let yLine = area.type == 'lower' ? yAxisMax : yAxisMin;
            ctx.setLineDash([5, 3]);
            ctx.strokeStyle = area.color;
            ctx.lineWidth = 1;
            ctx.beginPath();
            ctx.moveTo(xAxis.left, yLine);
            ctx.lineTo(barLeft, yLine);
            ctx.stroke();
          }
          // gradients between colors
          for (let j = 0; j < group.length; j++) {
            let area = group[j];
            if (area.style != 'outline') {
              let yAxisMin = yOffset(area.min);
              let yAxisMax = yOffset(area.max);
              let barLeft = xAxis.right + 1 + offsetRight;
              if (area.min != area.max) {
                let barHeight = yAxisMax - yAxisMin;
                let colors = [];
                if (j + 1 < group.length && group[j+1].max == area.min) {
                  colors = [area.color, group[j+1].color];
                } else if (j > 0 && group[j-1].max == area.min) {
                  colors = [area.color, group[j-1].color];
                }
                if (colors.length == 2) {
                  let yTop = area.type == 'lower' ? yAxisMin - 6 : yAxisMin;
                  let gradient = ctx.createLinearGradient(0, yTop, 0, yTop+6);
                  gradient.addColorStop(0, colors[0]);
                  gradient.addColorStop(1, colors[1]);
                  ctx.fillStyle = gradient;
                  ctx.fillRect(barLeft, yTop, barWidth, 6);                
                }
              }
            }          
          }
          if (offsetBar)
            offsetRight += barWidth + 1;
        }
        ctx.restore();
      }
    };
    return new Chart(widget.target, {
      type: 'line',
      data: { datasets: [], },
      options: options,
      plugins: [ thresholdsPlugin ],       
    });
  }

  /**
   * Convertes a array of points given as one dimensional array with alternativ time value elements 
   * to a 2-dimensional array of points with t and y attribute.
   */
  function points1Dto2D(points1d) {
    if (!points1d)
      return [];
    let points2d = new Array(points1d.length / 2);
    for (let i = 0; i < points2d.length; i++)
      points2d[i] = { t: new Date(points1d[i*2]), y: points1d[i*2+1] };
    return points2d;      
  }
	
  function createMinimumLineDataset(seriesData, points, lineColor) {
		return createHorizontalLineDataset(' min ', points, seriesData.observedMin, lineColor, [3, 3]);
  }
    
  function createMaximumLineDataset(seriesData, points, lineColor) {
  	return createHorizontalLineDataset(' max ', points, seriesData.observedMax, lineColor, [15, 3]);
  }
    
  function createAverageLineDataset(seriesData, points, lineColor) {
		return createHorizontalLineDataset(' avg ', points, seriesData.observedSum / seriesData.observedValues, lineColor, [9, 3]);
  }

  function createHorizontalLineDataset(label, points, y, lineColor, dash) {
    let line = {
      data: [{t:points[0].t, y:y}, {t:points[points.length-1].t, y:y}],
      label: label,
      fill:  false,
      borderColor: lineColor,
      borderWidth: 1,
      pointRadius: 0
    };
    if (dash)
      line.borderDash = dash;
    return line;
  }  
    
  function createCurrentLineDataset(widget, seriesData, points, lineColor, bgColor) {
		let pointRadius = widget.options.drawPoints ? 2 : 0;
    let label = seriesData.instance;
    if (widget.series.indexOf('*') > 0)
      label += ': '+ (seriesData.series.replace(new RegExp(widget.series.replace('*', '(.*)')), '$1'));
    let lineWidth = Theme.option('line-width', 3) / 2;
    return {
			data: points,
			label: label,
      fill: widget.options.noFill !== true,
			backgroundColor: bgColor,
			borderColor: lineColor,
			borderWidth: lineWidth,
      pointRadius: pointRadius,
		};
  }
    
  /**
   * Creates one or more lines for a single series dataset related to the widget.
   * A widget might display multiple series in the same graph generating one or more dataset for each of them.
   */
  function createSeriesDatasets(widget, seriesData, watches) {
    let lineColor = seriesData.legend.color;
    let bgColor = seriesData.legend.background;
  	let points = points1Dto2D(seriesData.points);
  	let datasets = [];
  	datasets.push(createCurrentLineDataset(widget, seriesData, points, lineColor, bgColor));
  	if (points.length > 0 && widget.options.drawAvgLine) {
			datasets.push(createAverageLineDataset(seriesData, points, lineColor));
		}
		if (points.length > 0 && widget.options.drawMinLine && seriesData.observedMin > 0) {
			datasets.push(createMinimumLineDataset(seriesData, points, lineColor));
		}
		if (points.length > 0 && widget.options.drawMaxLine) {
			datasets.push(createMaximumLineDataset(seriesData, points, lineColor));
		}
	  return datasets;
  }

  function createBackgroundAreas(widget, watches) {    
    let areas = [];
    let decoAreas = createDecorationBackgroundAreas(widget);
    if (decoAreas.length > 0) {
      areas.push(decoAreas);
    }
    if (Array.isArray(watches) && watches.length > 0) {
      for (let i = 0; i < watches.length; i++) {
        areas.push(createWatchBackgroundAreas(watches[0]));
      }
    }
    return areas;
  }

  function createDecorationBackgroundAreas(widget) {
    let areas = [];
    let decorations = widget.decorations;
    let critical = decorations.thresholds.critical.value;
    let alarming = decorations.thresholds.alarming.value;
    if (decorations.thresholds.critical.display && critical !== undefined) {
      let color = decorations.thresholds.critical.color || Theme.color('critical');        
      if (alarming > critical) {
        areas.push({ color: color, min: 0, max: critical, type: 'lower' });
      } else {
        areas.push({ color: color, min: critical, type: 'upper' });
      }
    }
    if (decorations.thresholds.alarming.display && alarming !== undefined) {
      let color = decorations.thresholds.alarming.color || Theme.color('alarming');
      if (alarming > critical) {
        areas.push({ color: color, min: critical, max: alarming, type: 'lower' });
      } else {
        areas.push({ color: color, min: alarming, max: critical, type: 'upper' });
      }
    }
    if (decorations.waterline && decorations.waterline.value) {
      let color = decorations.waterline.color || Theme.color('waterline');
      let value = decorations.waterline.value;
      areas.push({ color: color, min: value, max: value });
    }
    return areas;    
  }

  function createWatchBackgroundAreas(watch) { 
    let areas = [];
    let enabled = !watch.disabled;
    if (watch.red)
      areas.push(createBackgroundArea(watch.red, [watch.amber, watch.green], enabled));
    if (watch.amber)
      areas.push(createBackgroundArea(watch.amber, [watch.red, watch.green], enabled)); 
    if (watch.green)
      areas.push(createBackgroundArea(watch.green, [watch.amber, watch.red], enabled));
    return areas;
  }   

  function createBackgroundArea(level, levels, enabled) {
    let color = Theme.color(level.level);
    let min = 0;
    let max;
    let type = 'upper';
    if (level.start.operator == '>' || level.start.operator == '>=') {
      min = level.start.threshold;
      for (let i = 0; i < levels.length; i++) {
        let other = levels[i];
        if (other !== undefined && other.start.threshold > min) {
          max = max === undefined ? other.start.threshold : Math.min(max, other.start.threshold);
        }
      }
    } else if (level.start.operator == '<' || level.start.operator == '<=') {
      max = level.start.threshold;
      type = 'lower';
      for (let i = 0; i < levels.length; i++) {
        let other = levels[i];
        if (other !== undefined && other.start.threshold < max) {
          min = Math.max(min, other.start.threshold);
        }
      }
    }
    return { color: color, min: min, max: max, type: type, style: enabled ? 'fill' : 'outline' };
  }

  /**
   * Should be called whenever the configuration of the widget changes in way that needs to be transfered to the chart options.
   * Basically translates the MC level configuration options to Chart.js options
   */
  function onConfigUpdate(widget, chart) {
    let options = chart.options;
    options.elements.line.tension = widget.options.drawCurves ? 0.4 : 0;
    let time = 0; //widget.options.drawAnimations ? 1000 : 0;
    options.animation.duration = time;
    options.responsiveAnimationDuration = time;
    let yAxis = options.scales.yAxes[0];
    let converter = Units.converter(widget.unit);
    yAxis.ticks.callback = function(value, index, values) {
      let text = converter.format(value, widget.unit === 'bytes');
      return widget.options.perSec ? text + ' /s' : text;
    };
    yAxis.ticks.suggestedMin = widget.axis.min;
    yAxis.ticks.suggestedMax = widget.axis.max;
    let xAxis = options.scales.xAxes[0];
    xAxis.ticks.source = 'data'; // 'auto' does not allow to put labels at first and last point
    xAxis.ticks.display = widget.options.noTimeLabels !== true;
    options.elements.line.fill = widget.options.noFill !== true;
    return chart;
  }

  function onDataUpdate(update) {
    let data = update.data;
    let widget = update.widget;
    let chart = update.chart();
    let datasets = [];
    for (let j = 0; j < data.length; j++) {
      datasets = datasets.concat(createSeriesDatasets(widget, data[j], update.watches));
    }
    chart.data.datasets = datasets;
    chart.data.areas = createBackgroundAreas(widget, update.watches);
    chart.update(0);  
  }
  
  /**
   * Public API if this chart type (same for all types).
   */
	return {
    onCreation: (widget) => onConfigUpdate(widget, onCreation(widget)),
    onConfigUpdate: (widget, chart) => onConfigUpdate(widget, chart),
    onDataUpdate: (update) => onDataUpdate(update),
	};
})();
/*
   DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
  
   Copyright (c) 2019-2020 Payara Foundation and/or its affiliates. All rights reserved.
  
   The contents of this file are subject to the terms of either the GNU
   General Public License Version 2 only ("GPL") or the Common Development
   and Distribution License("CDDL") (collectively, the "License").  You
   may not use this file except in compliance with the License.  You can
   obtain a copy of the License at
   https://github.com/payara/Payara/blob/master/LICENSE.txt
   See the License for the specific
   language governing permissions and limitations under the License.
  
   When distributing the software, include this License Header Notice in each
   file and include the License file at glassfish/legal/LICENSE.txt.
  
   GPL Classpath Exception:
   The Payara Foundation designates this particular file as subject to the "Classpath"
   exception as provided by the Payara Foundation in the GPL Version 2 section of the License
   file that accompanied this code.
  
   Modifications:
   If applicable, add the following below the License Header, with the fields
   enclosed by brackets [] replaced by your own identifying information:
   "Portions Copyright [year] [name of copyright owner]"
  
   Contributor(s):
   If you wish your version of this file to be governed by only the CDDL or
   only the GPL Version 2, indicate your decision by adding "[Contributor]
   elects to include this software in this distribution under the [CDDL or GPL
   Version 2] license."  If you don't indicate a single choice of license, a
   recipient has the option to distribute your version of this file under
   either the CDDL, the GPL Version 2 or to extend the choice of license to
   its licensees as provided above.  However, if you add GPL Version 2 code
   and therefore, elected the GPL Version 2 license, then the option applies
   only if the new code is made subject to such option by the copyright
   holder.
*/

/*jshint esversion: 8 */

/**
 * Adapter to horizontal bar charts of Chart.js
 */ 
MonitoringConsole.Chart.Bar = (function() {

  const Units = MonitoringConsole.View.Units;

   function createData(widget, response) {
      let series = [];
      let labels = [];
      let zeroToMinValues = [];
      let observedMinToMinValues = [];
      let minToMaxValues = [];
      let maxToObservedMaxValues = [];
      let showObservedMin = widget.options.drawMinLine;
      let lineColors = [];
      let bgColors = [];
      for (let i = 0; i < response.length; i++) {
        let seriesData = response[i];
        let points = seriesData.points;
        let min = points[1];
        let max = points[1];
        for (let j = 0; j < points.length; j+=2) {
              let value = points[j+1];
              min = Math.min(min, value);
              max = Math.max(max, value);
        }
        labels.push(seriesData.series);
        series.push(seriesData.series);          
        zeroToMinValues.push(showObservedMin ? seriesData.observedMin : min);
        observedMinToMinValues.push(min - seriesData.observedMin);
        minToMaxValues.push(max - min);
        maxToObservedMaxValues.push(seriesData.observedMax - max);
        lineColors.push(seriesData.legend.color);
        bgColors.push(seriesData.legend.background);
      }
      let datasets = [];
      let offset = {
        data: zeroToMinValues,
        backgroundColor: 'transparent',
        borderWidth: {right: 1},
        borderColor: lineColors,
      };
      datasets.push(offset);
      if (showObservedMin) {
         datasets.push({
            data: observedMinToMinValues,
            backgroundColor: bgColors,
            borderWidth: 0,
         });       
      }
      datasets.push({
         data: minToMaxValues,
         backgroundColor: bgColors,
         borderColor: lineColors,
         borderWidth: 1,
         borderSkipped: false,
      });
      if (widget.options.drawMaxLine) {
         datasets.push({
           data: maxToObservedMaxValues,
           backgroundColor: bgColors,
           borderWidth: 0,
         }); 
      }
      return {
        labels: labels,
        series: series,
        datasets: datasets,
      };
   }

   function onCreation(widget) {
      return new Chart(widget.target, {
         type: 'horizontalBar',
         data: { datasets: [] },
         options: {
            maintainAspectRatio: false,
            scales: {
               xAxes: [{
                  stacked: true,
                  gridLines: {
                    color: 'rgba(0, 127, 255,0.3)',
                    lineWidth: 0.5,
                  },                  
                  ticks: {
                    callback: function(value, index, values) {
                      let converter = Units.converter(widget.unit);
                      return converter.format(converter.parse(value));
                    },
                  },                
               }],
               yAxes: [{
                  maxBarThickness: 15, //px
                  barPercentage: 1.0,
                  categoryPercentage: 1.0,
                  borderSkipped: false,
                  stacked: true,
                  ticks: {
                     display: false,
                  },
                  gridLines: {
                    color: 'rgba(0, 127, 255,0.7)',
                    lineWidth: 0.5,
                  },
               }]
            },
            legend: {
               display: false,
            },
            onClick: function (event) {
               let bar = this.getElementsAtEventForMode(event, "y", 1)[0];
               let series = bar._chart.config.data.series[bar._index]; 
               if (series.startsWith('ns:trace ') && series.endsWith(' Duration')) {
                  MonitoringConsole.Model.Settings.close();
                  MonitoringConsole.View.onPageMenu();
                  MonitoringConsole.Chart.Trace.onOpenPopup(series);
               }
            }
         }
      });
   }

   function onConfigUpdate(widget, chart) {
      let options = chart.options; 
      return chart;
   }

   function onDataUpdate(update) {
      let data = update.data;
      let widget = update.widget;
      let chart = update.chart();
      chart.data = createData(widget, data);
      chart.update(0);
   }

  /**
   * Public API if this chart type (same for all types).
   */
   return {
      onCreation: (widget) => onConfigUpdate(widget, onCreation(widget)),
      onConfigUpdate: (widget, chart) => onConfigUpdate(widget, chart),
      onDataUpdate: (update) => onDataUpdate(update),
   };
})();
/*
   DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
  
   Copyright (c) 2019-2020 Payara Foundation and/or its affiliates. All rights reserved.
  
   The contents of this file are subject to the terms of either the GNU
   General Public License Version 2 only ("GPL") or the Common Development
   and Distribution License("CDDL") (collectively, the "License").  You
   may not use this file except in compliance with the License.  You can
   obtain a copy of the License at
   https://github.com/payara/Payara/blob/master/LICENSE.txt
   See the License for the specific
   language governing permissions and limitations under the License.
  
   When distributing the software, include this License Header Notice in each
   file and include the License file at glassfish/legal/LICENSE.txt.
  
   GPL Classpath Exception:
   The Payara Foundation designates this particular file as subject to the "Classpath"
   exception as provided by the Payara Foundation in the GPL Version 2 section of the License
   file that accompanied this code.
  
   Modifications:
   If applicable, add the following below the License Header, with the fields
   enclosed by brackets [] replaced by your own identifying information:
   "Portions Copyright [year] [name of copyright owner]"
  
   Contributor(s):
   If you wish your version of this file to be governed by only the CDDL or
   only the GPL Version 2, indicate your decision by adding "[Contributor]
   elects to include this software in this distribution under the [CDDL or GPL
   Version 2] license."  If you don't indicate a single choice of license, a
   recipient has the option to distribute your version of this file under
   either the CDDL, the GPL Version 2 or to extend the choice of license to
   its licensees as provided above.  However, if you add GPL Version 2 code
   and therefore, elected the GPL Version 2 license, then the option applies
   only if the new code is made subject to such option by the copyright
   holder.
*/

/*jshint esversion: 8 */

/**
 * Horizontal bar charts of Chart.js as used for the gantt chart like trace details view
 */ 
MonitoringConsole.Chart.Trace = (function() {

   const Controller = MonitoringConsole.Controller;
   const Components = MonitoringConsole.View.Components;
   const Colors = MonitoringConsole.View.Colors;
   const Common = MonitoringConsole.Chart.Common;
   const Theme = MonitoringConsole.Model.Theme;

   var model = {};
   var chart;

   function onDataUpdate(data) {
      model.data = data;
      onSortByDuration();
   }

   function updateChart() {
      let data = model.data;
      let zeroToMinValues = [];
      let minToMaxValues = [];
      let spans = [];
      let labels = [];
      let operations = {};
      let colorCounter = 0;
      let colors = [];
      let bgColors = [];
      let alpha = Theme.option('opacity') / 100;
      let palette = Theme.palette();
      data.sort(model.sortBy);
      for (let i = 0; i < data.length; i++) {
         let trace = data[i]; 
         let startTime = trace.startTime;
         for (let j = 0; j < trace.spans.length; j++) {
            let span = trace.spans[j];
            spans.push(span);
            zeroToMinValues.push(span.startTime - startTime);
            minToMaxValues.push(span.endTime - span.startTime);
            labels.push(span.operation);
            if (!operations[span.operation]) {
               let color = Colors.lookup('index', 'line-' + colorCounter, palette);
               colorCounter++;
               operations[span.operation] = {
                  color: color,
                  bgColor: Colors.hex2rgba(color, alpha),
                  count: 1,
                  duration: span.endTime - span.startTime,
               };
            } else {
               let op = operations[span.operation];
               op.count += 1;
               op.duration += span.endTime - span.startTime;
            }
            colors.push(operations[span.operation].color);
            bgColors.push(operations[span.operation].bgColor);
         }
         spans.push(null);
         zeroToMinValues.push(0);
         minToMaxValues.push(0);
         labels.push('');
         colors.push('transparent');
         bgColors.push('transparent');
      }
      let datasets = [ {
            data: zeroToMinValues,
            backgroundColor: 'transparent',
         }, {
            data: minToMaxValues,
            backgroundColor: bgColors, //'rgba(153, 153, 153, 0.2)',
            borderColor: colors,
            borderWidth: {top: 1, right: 1},
         }
      ];
      if (!chart) {
         chart = onCreation();
      }
      let legend = [];
      for (let [label, operationData] of Object.entries(operations)) {
         legend.push({label: label, value: (operationData.duration / operationData.count).toFixed(2) + 'ms (avg)', color: operationData.color});
      }
      $('#trace-legend').empty().append(Components.createLegend(legend));
      $('#trace-chart-box').height(10 * spans.length + 30);
      chart.data = { 
         datasets: datasets,
         spans: spans,
         labels: labels,
      };
      chart.options.onClick = function(event)  {
        updateDomSpanDetails(data, spans[this.getElementsAtEventForMode(event, "y", 1)[0]._index]); 
      };
      addCustomTooltip(chart, spans);
      chart.update(0);
   }

   function autoLink(text) {
      if (text.startsWith('http://') || text.startsWith('https://'))
         return $('<a/>', { href: text, text: text});
      return $('<span/>', { text: text });
   }

   function addCustomTooltip(chart, spans) {
      chart.options.tooltips.custom = Common.createCustomTooltipFunction(function(dataPoints) {
         let index = dataPoints[0].index;
         let span = spans[index];
         let body = $('<div/>', {'class': 'Tooltip'});
         body
            .append($('<div/>').text("ID: "+span.id))
            .append($('<div/>').text("Start: "+Common.formatDate(span.startTime)))
            .append($('<div/>').text("End: "+Common.formatDate(span.endTime)));
         return body;
      });      
   }

   function onCreation() {
      return new Chart('trace-chart', {
         type: 'horizontalBar',
         data: { datasets: [] },
         options: {
            maintainAspectRatio: false,
            scales: {
               xAxes: [{
                  stacked: true,
                  position: 'top',
                  ticks: {
                     callback: function(value, index, values) {
                        if (value > 1000) {
                           return (value / 1000).toFixed(1)+"s";
                        }
                        return value+"ms";
                     }
                  },
                  scaleLabel: {
                     display: true,
                     labelString: 'Relative Timeline'
                  }
               }],
               yAxes: [{
                  maxBarThickness: 15, //px
                  barPercentage: 1.0,
                  categoryPercentage: 1.0,
                  borderSkipped: false,
                  stacked: true,
                  gridLines: {
                     display:false
                  },
                  ticks: {
                     display: false,
                  },
               }]
            },
            legend: {
               display: false,
            },
            tooltips: {
               enabled: false,
               position: 'nearest',
               filter: (tooltipItem) => tooltipItem.datasetIndex > 0, // remove offsets (not considered data, just necessary to offset the visible bars)
            },
         }
      });
   }

   function updateDomSpanDetails(data, span) {
      if (!span)
         return;
      let tags = { id: 'settings-tags', caption: 'Tags' , entries: []};
      let groups = [
         { id: 'settings-span', caption: 'Span' , entries: [
            { label: 'ID', input: span.id},
            { label: 'Operation', input: span.operation},
            { label: 'Start', input: Common.formatDate(new Date(span.startTime))},
            { label: 'End', input:  Common.formatDate(new Date(span.endTime))},
            { label: 'Duration', input: (span.duration / 1000000) + 'ms'},
         ]},
         tags,
      ];
      for (let [key, value] of Object.entries(span.tags)) {
         if (value.startsWith('[') && value.endsWith(']')) {
            value = value.slice(1,-1);
         }
         tags.entries.push({ label: key, input: autoLink(value)});
      }
      $('#Settings').replaceWith(Components.createSettings({id: 'Settings', groups: groups }));
   }


   function onDataRefresh() {
      Controller.requestListOfRequestTraces(model.series, onDataUpdate);
   }

   function onOpenPopup(series) {
      $('#chart-grid').hide();
      $('#panel-trace').show();
      model.series = series;
      let menu = { id: 'TraceMenu', groups: [
         { icon: '&#128472;', description: 'Refresh', onClick: onDataRefresh },
         { label: 'Sorting', items: [
            { icon: '&#9202;', label: 'Sort By Wall Time (past to recent)', onClick: onSortByWallTime },
            { icon: '&#8987;', label: 'Sort By Duration (slower to faster)', onClick: onSortByDuration },
         ]},
         { icon: '&times;', description: 'Back to main view', onClick: onClosePopup },
      ]};
      $('#trace-menu').replaceWith(Components.createMenu(menu));
      onDataRefresh();
   }

   function onClosePopup() {
      if (chart) {
         chart.destroy();
         chart = undefined;
      }
      $('#panel-trace').hide();
      $('#chart-grid').show();
   }

   function onSortByWallTime() {
      model.sortBy = (a,b) => a.startTime - b.startTime; // past to recent
      updateChart();
   }

   function onSortByDuration() {
      model.sortBy = (a,b) => b.elapsedTime - a.elapsedTime; // slow to fast
      updateChart();
   }

   /**
    * Public API below:
    */
   return {
      onOpenPopup: (series) => onOpenPopup(series),
      onClosePopup: () => onClosePopup(),
      onDataRefresh: () => onDataRefresh(),
      onSortByWallTime: () => onSortByWallTime(),
      onSortByDuration: () => onSortByDuration(),
   };
})();
/*
   DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
  
   Copyright (c) 2019-2020 Payara Foundation and/or its affiliates. All rights reserved.
  
   The contents of this file are subject to the terms of either the GNU
   General Public License Version 2 only ("GPL") or the Common Development
   and Distribution License("CDDL") (collectively, the "License").  You
   may not use this file except in compliance with the License.  You can
   obtain a copy of the License at
   https://github.com/payara/Payara/blob/master/LICENSE.txt
   See the License for the specific
   language governing permissions and limitations under the License.
  
   When distributing the software, include this License Header Notice in each
   file and include the License file at glassfish/legal/LICENSE.txt.
  
   GPL Classpath Exception:
   The Payara Foundation designates this particular file as subject to the "Classpath"
   exception as provided by the Payara Foundation in the GPL Version 2 section of the License
   file that accompanied this code.
  
   Modifications:
   If applicable, add the following below the License Header, with the fields
   enclosed by brackets [] replaced by your own identifying information:
   "Portions Copyright [year] [name of copyright owner]"
  
   Contributor(s):
   If you wish your version of this file to be governed by only the CDDL or
   only the GPL Version 2, indicate your decision by adding "[Contributor]
   elects to include this software in this distribution under the [CDDL or GPL
   Version 2] license."  If you don't indicate a single choice of license, a
   recipient has the option to distribute your version of this file under
   either the CDDL, the GPL Version 2 or to extend the choice of license to
   its licensees as provided above.  However, if you add GPL Version 2 code
   and therefore, elected the GPL Version 2 license, then the option applies
   only if the new code is made subject to such option by the copyright
   holder.
*/

/*jshint esversion: 8 */

/**
 * Main API to update or manipulate the view of the generic page.
 **/
MonitoringConsole.View = (function() {

    const Controller = MonitoringConsole.Controller;
    const Components = MonitoringConsole.View.Components;
    const Units = MonitoringConsole.View.Units;
    const Colors = MonitoringConsole.View.Colors;
    const Theme = MonitoringConsole.Model.Theme;

    /**
     * Updates the DOM with the page navigation tabs so it reflects current model state
     */ 
    function updatePageNavigation(selectedPage) {
        let pages = MonitoringConsole.Model.listPages();
        let activePage = selectedPage || pages.find(page => page.active).name;
        let items = pages.map(function(page) {
            return { label: page.name ? page.name : '(Unnamed)', onClick: () => onPageChange(MonitoringConsole.Model.Page.changeTo(page.id)) };
        });
        items.push({ label: 'Watches', onClick: onOpenWatchSettings });
        let nav = { id: 'Navigation', groups: [
            {label: activePage, items: items }
        ]};
        $('#Navigation').replaceWith(Components.createMenu(nav));
    }

    function updateMenu() {
        let hasPreset = MonitoringConsole.Model.Page.hasPreset();
        let isPaused = MonitoringConsole.Model.Refresh.isPaused();
        let settingsOpen = MonitoringConsole.Model.Settings.isDispayed();
        let toggleSettings = function() { MonitoringConsole.View.onPageMenu(); updateMenu(); };
        let menu = { id: 'Menu', groups: [
            { icon: '&#128463;', label: 'Page', items: [
                { label: 'New...', icon: '&#128459;', description: 'Add a new blank page...', onClick: () => MonitoringConsole.View.onPageChange(MonitoringConsole.Model.Page.create('(Unnamed)')) },
                { label: 'Delete', icon: '&times;', description: 'Delete current page', disabled: hasPreset, onClick: MonitoringConsole.View.onPageDelete },
                { label: 'Reset', icon: '&#128260;', description: 'Reset Page to Preset', disabled: !hasPreset, onClick: MonitoringConsole.View.onPageReset },
            ]},
            { icon: '&#128472;', label: 'Data Refresh', items: [
                { label: 'Pause', icon: '&#9208;', description: 'Pause Data Refresh', hidden: isPaused, onClick: function() { MonitoringConsole.Model.Refresh.pause(); updateMenu(); updateSettings(); } },
                { label: 'Unpause', icon: '&#9654;', description: 'Unpause Data Refresh', hidden: !isPaused, onClick: function() { MonitoringConsole.Model.Refresh.resume(); updateMenu(); updateSettings(); } },
                { label: 'Slow', icon: '&#128034;', description: 'Slow Data Refresh Rate', onClick: function() { MonitoringConsole.Model.Refresh.resume(4); updateMenu(); updateSettings(); } },
                { label: 'Normal', icon: '&#128008;', description: 'Normal Data Refresh Rate', onClick: function() { MonitoringConsole.Model.Refresh.resume(2); updateMenu(); updateSettings(); } },
                { label: 'Fast', icon: '&#128007;', description: 'Fast Data Refresh Rate', onClick: function() { MonitoringConsole.Model.Refresh.resume(1); updateMenu(); updateSettings(); } },
            ]},
            { icon: '&#9707;', label: 'Layout', items: [
                { label: '1 Column', icon: '&#11034;', description: 'Use one column layout', onClick: () => MonitoringConsole.View.onPageLayoutChange(1) },
                { label: ' 2 Columns', icon: '&#11034;&#11034;', description: 'Use two column layout', onClick: () => MonitoringConsole.View.onPageLayoutChange(2) },
                { label: ' 3 Columns', icon: '&#11034;&#11034;&#11034;', description: 'Use three column layout', onClick: () => MonitoringConsole.View.onPageLayoutChange(3) },
                { label: ' 4 Columns', icon: '&#11034;&#11034;&#11034;&#11034;', description: 'Use four column layout', onClick: () => MonitoringConsole.View.onPageLayoutChange(4) },
            ]},
            { icon: '&#9881;', label: 'Settings', clickable: true, items: [
                { label: 'Hide', icon: '&times;', hidden: !settingsOpen, onClick: toggleSettings },
                { label: 'Show', icon: '&plus;', hidden: settingsOpen, onClick: toggleSettings },
                { label: 'Import...', icon: '&#9111;', description: 'Import Configuration...', onClick: () => $('#cfgImport').click() },
                { label: 'Export...', icon: '&#9112;', description: 'Export Configuration...', onClick: MonitoringConsole.View.onPageExport },
            ]},
        ]};
        $('#Menu').replaceWith(Components.createMenu(menu));
    }

    /**
     * Updates the DOM with the page and selection settings so it reflects current model state
     */ 
    function updateSettings() {
        let panelConsole = $('#console');
        if (MonitoringConsole.Model.Settings.isDispayed()) {
            if (!panelConsole.hasClass('state-show-settings')) {
                panelConsole.addClass('state-show-settings');                
            }
            let singleSelection = MonitoringConsole.Model.Page.Widgets.Selection.isSingle();
            let groups = [];
            groups.push(createGlobalSettings(singleSelection));
            groups.push(createColorSettings());
            groups.push(createPageSettings());
            if (singleSelection) {
                groups = groups.concat(createWidgetSettings(MonitoringConsole.Model.Page.Widgets.Selection.first()));
            }
            $('#Settings').replaceWith(Components.createSettings({id: 'Settings', groups: groups }));
        } else {
            panelConsole.removeClass('state-show-settings');
        }
    }

    function updateDomOfWidget(parent, widget) {
        if (!parent) {
            parent = $('#widget-'+widget.target);
            if (!parent) {
                return; // can't update
            }
        }
        if (parent.children().length == 0) {
            let previousParent = $('#widget-'+widget.target);
            if (previousParent.length > 0 && previousParent.children().length > 0) {
                previousParent.children().appendTo(parent);
            } else {
                parent.append(createWidgetToolbar(widget));
                parent.append(createWidgetTargetContainer(widget));
                parent.append(Components.createAlertTable({}));
                parent.append(Components.createAnnotationTable({}));
                parent.append(Components.createLegend([]));                
                parent.append(Components.createIndicator({}));
            }
        }
        if (widget.selected) {
            parent.addClass('chart-selected');
        } else {
            parent.removeClass('chart-selected');
        }
    }

    /**
     * Each chart needs to be in a relative positioned box to allow responsive sizing.
     * This fuction creates this box including the canvas element the chart is drawn upon.
     */
    function createWidgetTargetContainer(widget) {
        return $('<div/>', { id: widget.target + '-box', "class": "widget-chart-box" })
            .append($('<canvas/>',{ id: widget.target }));
    }

    function toWords(str) {
        // camel case to words
        let res = str.replace(/([A-Z]+)/g, " $1").replace(/([A-Z][a-z])/g, " $1");
        if (res.indexOf('.') > 0) {
            // dots to words with upper casing each word
            return res.replace(/\.([a-z])/g, " $1").split(' ').map((s) => s.charAt(0).toUpperCase() + s.substring(1)).join(' ');
        }
        return res;
    }

    function formatSeriesName(series) {
        if (Array.isArray(series))
            return 'Multi-Series without Display Name';
        let endOfTags = series.lastIndexOf(' ');
        let metric = endOfTags <= 0 ? series : series.substring(endOfTags + 1);
        if (endOfTags <= 0 )
            return toWords(metric);
        let tags = series.substring(0, endOfTags).split(' ');
        let text = '';
        let metricText = toWords(metric);
        let grouped = false;
        for (let i = 0; i < tags.length; i++) {
            let tag = tags[i];
            let tagName = tag.substring(0, tag.indexOf(':'));
            let tagValue = tag.substring(tag.indexOf(':') + 1);
            if (tagName ===  '@') {
                grouped = true;
                text += metricText;
                text += ': <strong>'+tagValue+'</strong> ';
            } else {
                text +=' <span>'+tagName+':<strong>'+tagValue+'</strong></span> ';
            }
        }
        if (!grouped)
            text += metricText;
        return text;
    }

    function createWidgetToolbar(widget) {
        const Widgets = MonitoringConsole.Model.Page.Widgets;
        let widgetId = widget.id;
        let items = [
            { icon: '&times;', label: 'Remove', onClick: () => onWidgetDelete(widgetId)},
            { icon: '&ltri;', label: 'Move Left', onClick: () => onPageUpdate(Widgets.moveLeft(widgetId)) },
            { icon: '&rtri;', label: 'Move Right', onClick: () => onPageUpdate(Widgets.moveRight(widgetId)) },                
        ];
        if (widget.type === 'annotation') {
            items.push({ icon: '&#9202', label: 'Sort By Wall Time', onClick: () => Widgets.configure(widgetId, (widget) => widget.sort = 'time') });
            items.push({ icon: '&#128292;', label: 'Sort By Value', onClick: () => Widgets.configure(widgetId, (widget) => widget.sort = 'value') });
        }
        items.push({ icon: '&#128295;', label: 'Edit...', onClick: () => onOpenWidgetSettings(widgetId) });
        let menu = { groups: [
            { icon: '&#9881;', items: items },
        ]};
        let title = widget.displayName ? widget.displayName : formatSeriesName(widget.series);
        return $('<div/>', {"class": "widget-title-bar"})
            .append(Components.createMenu(menu))
            .append($('<h3/>', {title: widget.description != '' ? widget.description : widget.series})
                .html(title)
                .click(() => onWidgetToolbarClick(widget)))            
            ;
    }

    function createGlobalSettings(initiallyCollapsed) {
        const  pushAvailable = MonitoringConsole.Model.Role.isAdmin();
        const  pullAvailable = !MonitoringConsole.Model.Role.isGuest();
        return { id: 'settings-global', caption: 'Global', collapsed: initiallyCollapsed, entries: [
            { label: 'Data Refresh', input: [
                { type: 'value', unit: 'sec', value: MonitoringConsole.Model.Refresh.interval(), onChange: (val) => MonitoringConsole.Model.Refresh.interval(val) },
                { label: 'paused', type: 'checkbox', value: MonitoringConsole.Model.Refresh.isPaused(), onChange: function(checked) { MonitoringConsole.Model.Refresh.paused(checked); updateMenu(); } },
            ]},
            { label: 'Page Rotation', input: [
                { type: 'value', unit: 'sec', value: MonitoringConsole.Model.Settings.Rotation.interval(), onChange: (val) => MonitoringConsole.Model.Settings.Rotation.interval(val) },
                { label: 'enabled', type: 'checkbox', value: MonitoringConsole.Model.Settings.Rotation.isEnabled(), onChange: (checked) => MonitoringConsole.Model.Settings.Rotation.enabled(checked) },
            ]},
            { label: 'Role', type: 'dropdown', options: {guest: 'Guest', user: 'User', admin: 'Administrator'}, value: MonitoringConsole.Model.Role.get(), onChange: (val) => { MonitoringConsole.Model.Role.set(val); updateSettings(); } },
            { label: 'Page Sync', available: pushAvailable || pullAvailable, input: [
                { available: pushAvailable, input: () => $('<button />', { text: 'Update Remote Pages', title: 'Push local state of all know remote pages to server'}).click(MonitoringConsole.Model.Page.Sync.pushAllLocal) },
                { available: pullAvailable, input: () => $('<button/>', { text: 'Update Local Pages', title: 'Open Page synchronisation dialoge'}).click(onPagesSync) }, 
            ]},
            { label: 'Watches', input: $('<button/>').text('Open Settings').click(onOpenWatchSettings) },
        ]};
    }

    function createColorSettings() {
        function createChangeColorDefaultFn(name) {
            return (color) => { Theme.configure(theme => theme.colors[name] = color); updateSettings(); };
        }
        function createChangeOptionFn(name) {
            return (value) => { Theme.configure(theme => theme.options[name] = value); };
        }    
        function createColorDefaultSettingMapper(name) {
            let label = Units.Alerts.name(name);
            if (label === undefined)
                label = name[0].toUpperCase() + name.slice(1);
            return { label: label, type: 'color', value: Theme.color(name), onChange: createChangeColorDefaultFn(name) };
        }
        let collapsed = $('#settings-colors').children('tr:visible').length <= 1;
        return { id: 'settings-colors', caption: 'Colors', collapsed: collapsed, entries: [
            { label: 'Scheme', type: 'dropdown', options: Colors.schemes(), value: undefined, onChange: (name) => { Colors.scheme(name); updateSettings(); } },
            { label: 'Data #', type: 'color', value: Theme.palette(), onChange: (colors) => Theme.palette(colors) },
            { label: 'Defaults', input: [
                ['error', 'missing'].map(createColorDefaultSettingMapper),
                ['alarming', 'critical', 'waterline'].map(createColorDefaultSettingMapper),
                ['white', 'green', 'amber', 'red'].map(createColorDefaultSettingMapper)]},
            { label: 'Opacity', description: 'Fill transparency 0-100%', input: [
                { type: 'value', unit: 'percent', value: Theme.option('opacity'), onChange: createChangeOptionFn('opacity') },
            ]},
            { label: 'Thickness', description: 'Line thickness 1-8 (each step is equivalent to 0.5px)', input: [
                { type: 'range', min: 1, max: 8, value: Theme.option('line-width'), onChange: createChangeOptionFn('line-width') },
            ]},
        ]};
    }

    function createWidgetSettings(widget) {
        function changeSeries(selectedSeries) {
            if (selectedSeries !== undefined && selectedSeries.length > 0)
                onPageChange(MonitoringConsole.Model.Page.Widgets.configure(widget.id, 
                    widget => widget.series = selectedSeries.length == 1 ? selectedSeries[0] : selectedSeries));
        }
        let seriesInput = $('<span/>');
        if (Array.isArray(widget.series)) {
            seriesInput.append(widget.series.join(', ')).append(' ');                    
        } else {
            seriesInput.append(widget.series).append(' ');
        }
        seriesInput.append($('<br/>')).append($('<button/>', { text: 'Change metric(s)...' })
                .click(() => $('#ModalDialog').replaceWith(Components.createModalDialog(createWizardModalDialogModel(widget.series, changeSeries)))));
        let options = widget.options;
        let unit = widget.unit;
        let thresholds = widget.decorations.thresholds;
        let settings = [];
        let collapsed = $('#settings-widget').children('tr:visible').length <= 1;
        let typeOptions = { line: 'Time Curve', bar: 'Range Indicator', alert: 'Alerts', annotation: 'Annotations', rag: 'RAG Status' };
        let modeOptions = widget.type == 'annotation' ? { table: 'Table', list: 'List' } : { list: '(Default)' };
        settings.push({ id: 'settings-widget', caption: 'Widget', collapsed: collapsed, entries: [
            { label: 'Display Name', type: 'text', value: widget.displayName, onChange: (widget, value) => widget.displayName = value},
            { label: 'Type', type: 'dropdown', options: typeOptions, value: widget.type, onChange: (widget, selected) => widget.type = selected},
            { label: 'Mode', type: 'dropdown', options: modeOptions, value: widget.mode, onChange: (widget, selected) => widget.mode = selected},
            { label: 'Column / Item', input: [
                { type: 'range', min: 1, max: 4, value: 1 + (widget.grid.column || 0), onChange: (widget, value) => widget.grid.column = value - 1},
                { type: 'range', min: 1, max: 8, value: 1 + (widget.grid.item || 0), onChange: (widget, value) => widget.grid.item = value - 1},
            ]},             
            { label: 'Size', input: [
                { label: '&nbsp;x', type: 'range', min: 1, max: 4, value: widget.grid.colspan || 1, onChange: (widget, value) => widget.grid.colspan = value},
                { type: 'range', min: 1, max: 4, value: widget.grid.rowspan || 1, onChange: (widget, value) => widget.grid.rowspan = value},
            ]},
        ]});
        settings.push({ id: 'settings-data', caption: 'Data', entries: [
            { label: 'Series', input: seriesInput },
            { label: 'Unit', input: [
                { type: 'dropdown', options: Units.names(), value: widget.unit, onChange: function(widget, selected) { widget.unit = selected; updateSettings(); }},
                { label: '1/sec', type: 'checkbox', value: options.perSec, onChange: (widget, checked) => widget.options.perSec = checked},
            ]},
            { label: 'Upscaling', description: 'Upscaling is sometimes needed to convert the original value range to a more user freindly display value range', input: [
                { type: 'range', min: 1, value: widget.scaleFactor, onChange: (widget, value) => widget.scaleFactor = value, 
                    description: 'A factor multiplied with each value to upscale original values in a graph, e.g. to move a range 0-1 to 0-100%'},
                { label: 'decimal value', type: 'checkbox', value: options.decimalMetric, onChange: (widget, checked) => widget.options.decimalMetric = checked,
                    description: 'Values that are collected as decimal are converted to a integer with 4 fix decimal places. By checking this option this conversion is reversed to get back the original decimal range.'},
            ]},
            { label: 'Extra Lines', input: [
                { label: 'Min', type: 'checkbox', value: options.drawMinLine, onChange: (widget, checked) => widget.options.drawMinLine = checked},
                { label: 'Max', type: 'checkbox', value: options.drawMaxLine, onChange: (widget, checked) => widget.options.drawMaxLine = checked},
                { label: 'Avg', type: 'checkbox', value: options.drawAvgLine, onChange: (widget, checked) => widget.options.drawAvgLine = checked},            
            ]},
            { label: 'Lines', input: [
                { label: 'Points', type: 'checkbox', value: options.drawPoints, onChange: (widget, checked) => widget.options.drawPoints = checked},
                { label: 'Curvy', type: 'checkbox', value: options.drawCurves, onChange: (widget, checked) => widget.options.drawCurves = checked},
            ]},
            { label: 'Background', input: [
                { label: 'Fill', type: 'checkbox', value: !options.noFill, onChange: (widget, checked) => widget.options.noFill = !checked},
            ]},
            { label: 'X-Axis', input: [
                { label: 'Labels', type: 'checkbox', value: !options.noTimeLabels, onChange: (widget, checked) => widget.options.noTimeLabels = !checked},
            ]},            
            { label: 'Y-Axis', input: [
                { label: 'Min', type: 'value', unit: unit, value: widget.axis.min, onChange: (widget, value) => widget.axis.min = value},
                { label: 'Max', type: 'value', unit: unit, value: widget.axis.max, onChange: (widget, value) => widget.axis.max = value},
            ]},
            { label: 'Coloring', type: 'dropdown', options: { instance: 'Instance Name', series: 'Series Name', index: 'Result Set Index', 'instance-series': 'Instance and Series Name' }, value: widget.coloring, onChange: (widget, value) => widget.coloring = value,
                description: 'What value is used to select the index from the color palette' },
            { label: 'Fields', type: 'text', value: (widget.fields || []).join(' '), onChange: (widget, value) => widget.fields = value == undefined || value == '' ? undefined : value.split(/[ ,]+/),
                description: 'Selection and order of annotation fields to display, empty for auto selection and default order' },
            {label: 'Annotations', type: 'checkbox', value: !options.noAnnotations, onChange: (widget, checked) => widget.options.noAnnotations = !checked}                
        ]});
        settings.push({ id: 'settings-decorations', caption: 'Decorations', entries: [
            { label: 'Waterline', input: [
                { type: 'value', unit: unit, value: widget.decorations.waterline.value, onChange: (widget, value) => widget.decorations.waterline.value = value },
                { type: 'color', value: widget.decorations.waterline.color, defaultValue: Theme.color('waterline'), onChange: (widget, value) => widget.decorations.waterline.color = value },
            ]},
            { label: 'Alarming Threshold', input: [
                { type: 'value', unit: unit, value: thresholds.alarming.value, onChange: (widget, value) => widget.decorations.thresholds.alarming.value = value },
                { type: 'color', value: thresholds.alarming.color, defaultValue: Theme.color('alarming'), onChange: (widget, value) => thresholds.alarming.color = value },
                { label: 'Line', type: 'checkbox', value: thresholds.alarming.display, onChange: (widget, checked) => thresholds.alarming.display = checked },
            ]},
            { label: 'Critical Threshold', input: [
                { type: 'value', unit: unit, value: thresholds.critical.value, onChange: (widget, value) => widget.decorations.thresholds.critical.value = value },
                { type: 'color', value: thresholds.critical.color, defaultValue: Theme.color('critical'), onChange: (widget, value) => widget.decorations.thresholds.critical.color = value },
                { label: 'Line', type: 'checkbox', value: thresholds.critical.display, onChange: (widget, checked) => widget.decorations.thresholds.critical.display = checked },
            ]},                
            { label: 'Threshold Reference', type: 'dropdown', options: { off: 'Off', now: 'Most Recent Value', min: 'Minimum Value', max: 'Maximum Value', avg: 'Average Value'}, value: thresholds.reference, onChange: (widget, selected) => widget.decorations.thresholds.reference = selected},
        ]});
        settings.push({ id: 'settings-status', caption: 'Status', collapsed: true, description: 'Set a text for an assessment status', entries: [
            { label: '"No Data"', type: 'text', value: widget.status.missing.hint, onChange: (widget, text) => widget.status.missing.hint = text},
            { label: '"Alaraming"', type: 'text', value: widget.status.alarming.hint, onChange: (widget, text) => widget.status.alarming.hint = text},
            { label: '"Critical"', type: 'text', value: widget.status.critical.hint, onChange: (widget, text) => widget.status.critical.hint = text},
        ]});
        let alerts = widget.decorations.alerts;
        settings.push({ id: 'settings-alerts', caption: 'Alerts', collapsed: true, entries: [
            { label: 'Filter', input: [
                [
                    { label: 'Ambers', type: 'checkbox', value: alerts.noAmber, onChange: (widget, checked) => widget.decorations.alerts.noAmber = checked},
                    { label: 'Reds', type: 'checkbox', value: alerts.noRed, onChange: (widget, checked) => widget.decorations.alerts.noRed = checked},
                ],            
                [
                    { label: 'Ongoing', type: 'checkbox', value: alerts.noOngoing, onChange: (widget, checked) => widget.decorations.alerts.noOngoing = checked},
                    { label: 'Stopped', type: 'checkbox', value: alerts.noStopped, onChange: (widget, checked) => widget.decorations.alerts.noStopped = checked},
                ],
                [
                    { label: 'Acknowledged', type: 'checkbox', value: alerts.noAcknowledged, onChange: (widget, checked) => widget.decorations.alerts.noAcknowledged = checked},
                    { label: 'Unacknowledged', type: 'checkbox', value: alerts.noUnacknowledged, onChange: (widget, checked) => widget.decorations.alerts.noUnacknowledged = checked},
                ],
            ], description: 'Properties of alerts to show. Graphs hide stopped or acknowledged alerts automatically.' },
        ]});
        return settings;       
    }

    function createPageSettings() {
        function addWidgets(selectedSeries) {
            if (selectedSeries !== undefined && selectedSeries.length > 0)
                onPageChange(MonitoringConsole.Model.Page.Widgets.add(selectedSeries));
        }
        const addWidgetsInput = $('<button/>', { text: 'Select metric(s)...' })
            .click(() => $('#ModalDialog').replaceWith(Components.createModalDialog(createWizardModalDialogModel([], addWidgets))));
        let pageNameOnChange = MonitoringConsole.Model.Page.hasPreset() ? undefined : function(text) {
            if (MonitoringConsole.Model.Page.rename(text)) {
                updatePageNavigation();                        
            }
        };
        let collapsed = $('#settings-page').children('tr:visible').length <= 1;
        let pushAvailable = !MonitoringConsole.Model.Role.isGuest() && MonitoringConsole.Model.Page.Sync.isLocallyChanged() && MonitoringConsole.Model.Role.isAdmin();
        let pullAvailable = !MonitoringConsole.Model.Role.isGuest();
        let autoAvailable = MonitoringConsole.Model.Role.isAdmin();
        let page = MonitoringConsole.Model.Page.current();
        let queryAvailable = page.type === 'query';
        const configure =  MonitoringConsole.Model.Page.configure;
        return { id: 'settings-page', caption: 'Page', collapsed: collapsed, entries: [
            { label: 'Name', type: 'text', value: MonitoringConsole.Model.Page.name(), onChange: pageNameOnChange },
            { label: 'Page Rotation', input: [
                { label: 'Include in Rotation', type: 'checkbox', value: MonitoringConsole.Model.Page.rotate(), onChange: (checked) => MonitoringConsole.Model.Page.rotate(checked) },
            ]},
            { label: 'Type', type: 'dropdown', options: {manual: 'Manual', query: 'Query'}, value: page.type, onChange: (type) => { onPageUpdate(configure(page => page.type = type)); updateSettings(); } },            
            { label: 'Max Size', available: queryAvailable, type: 'value', min: 1, unit: 'count', value: page.content.maxSize,  onChange: (value) => configure(page => page.content.maxSize = value) },
            { label: 'Query Series', available: queryAvailable, type: 'text', value: page.content.series, onChange: (value) => configure(page => page.content.series = value) },
            { label: 'Query Interval', available: queryAvailable, input: [
                { type: 'value', min: 1, unit: 'sec', value: page.content.ttl, onChange: (value) => configure(page => page.content.ttl = value) },
                { input: $('<button/>', {text: 'Update'}).click(() => configure(page => page.content.expires = undefined)) },
            ]},
            { label: 'Add Widgets', available: !queryAvailable, input: addWidgetsInput },
            { label: 'Sync', available: pushAvailable || pullAvailable, input: [
                { available: autoAvailable, label: 'auto', type: 'checkbox', value: MonitoringConsole.Model.Page.Sync.auto(), onChange: (checked) => MonitoringConsole.Model.Page.Sync.auto(checked),
                    description: 'When checked changed to the page are automatically pushed to the remote server (shared with others)' },
                { available: pushAvailable, input: () => $('<button />', { text: 'Push', title: 'Push local page to server (update remote)' }).click(onPagePush) },
                { available: pullAvailable, input: () => showIfRemotePageExists($('<button />', { text: 'Pull', title: 'Pull remote page from server (update local)', style: 'display: none'}).click(onPagePull)) },
            ]}
        ]};
    }

    function showIfRemotePageExists(jQuery) {
        Controller.requestListOfRemotePageNames((pageIds) => { // OBS: this asynchronously makes the button visible
            if (pageIds.indexOf(MonitoringConsole.Model.Page.id()) >= 0) {
                jQuery.show();
            }
        });                        
        return jQuery;
    }

    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~[ Event Handlers ]~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    function onWidgetToolbarClick(widget) {
        MonitoringConsole.Model.Page.Widgets.Selection.toggle(widget.id, true);
        updateDomOfWidget(undefined, widget);
        updateSettings();
    }

    function onWidgetDelete(widgetId) {
        if (window.confirm('Do you really want to remove the chart from the page?')) {
            onPageChange(MonitoringConsole.Model.Page.Widgets.remove(widgetId));
        }
    }

    function onPageExport(filename, text) {
        let pom = document.createElement('a');
        pom.setAttribute('href', 'data:text/plain;charset=utf-8,' + encodeURIComponent(text));
        pom.setAttribute('download', filename);

        if (document.createEvent) {
            let event = document.createEvent('MouseEvents');
            event.initEvent('click', true, true);
            pom.dispatchEvent(event);
        }
        else {
            pom.click();
        }
    }

    function createLegendModel(widget, data, alerts, annotations) {
        if (!data)
            return [{ label: 'Connection Lost', value: '?', color: 'red', assessments: { status: 'error' } }];
        if (widget.type == 'alert')
            return createLegendModelFromAlerts(widget, alerts);
        if (widget.type == 'annotation')
            return createLegendModelFromAnnotations(widget, annotations);
        if (Array.isArray(data) && data.length == 0)
            return [{ label: 'No Data', value: '?', color: '#0096D6', assessments: {status: 'missing' }}];
        let legend = [];
        let format = Units.converter(widget.unit).format;
        let palette = Theme.palette();
        let alpha = Theme.option('opacity') / 100;
        for (let j = 0; j < data.length; j++) {
            const seriesData = data[j];
            const series = widget.series;
            const isMultiSeries = Array.isArray(series) && series.length > 1;
            let label = seriesData.instance;
            if (isMultiSeries)
                label += ': ' + seriesData.series.split(" ").pop();
            if (!isMultiSeries && series.includes('*') && !series.includes('?')) {
                let tag = seriesData.series.replace(new RegExp(series.replace('*', '(.*)')), '$1').replace('_', ' ');                
                label = widget.coloring == 'series' ? tag : [label, tag];
            }
            let points = seriesData.points;
            let avgOffN = widget.options.perSec ? Math.min(points.length / 2, 4) : 1;
            let avg = 0;
            for (let n = 0; n < avgOffN; n++)
                avg += points[points.length - 1 - (n * 2)];
            avg /= avgOffN;
            let value = format(avg, widget.unit === 'bytes' || widget.unit === 'ns');
            if (widget.options.perSec)
                value += ' /s';
            let coloring = widget.coloring;
            let color = Colors.lookup(coloring, getColorKey(widget, seriesData.series, seriesData.instance, j), palette);
            let background = Colors.hex2rgba(color, alpha);
            if (Array.isArray(alerts) && alerts.length > 0) {
                let level;
                for (let i = 0; i < alerts.length; i++) {
                    let alert = alerts[i];
                    if (alert.instance == seriesData.instance && alert.series == seriesData.series && !alert.stopped) {
                        level = Units.Alerts.maxLevel(level, alert.level);
                    }
                }
                if (level == 'red' || level == 'amber') {
                    background = Colors.hex2rgba(Theme.color(level), Math.min(1, alpha * 2));
                }
            }
            let status = seriesData.assessments.status;
            let highlight = status === undefined ? undefined : Theme.color(status);
            let item = { 
                label: label, 
                value: value, 
                color: color,
                background: background,
                status: status,
                highlight: highlight,
            };
            legend.push(item);
            seriesData.legend = item;
        }
        return legend;
    }

    function createLegendModelFromAlerts(widget, alerts) {
        if (!Array.isArray(alerts))
            return []; //TODO use white, green, amber and red to describe the watch in case of single watch
        let palette = Theme.palette();
        let alpha = Theme.option('opacity') / 100;
        let instances = {};
        for (let alert of alerts) {
            instances[alert.instance] = Units.Alerts.maxLevel(alert.level, instances[alert.instance]);
        }
        
        return Object.entries(instances).map(function([instance, level]) {
            let color = Colors.lookup('instance', instance, palette);
            return {
                label: instance,
                value: Units.Alerts.name(level),
                color: color,
                background: Colors.hex2rgba(color, alpha),
                status: level, 
                highlight: Theme.color(level),                
            };
        });
    }

    function createLegendModelFromAnnotations(widget, annotations) {
        let coloring = widget.coloring || 'instance';
        if (!Array.isArray(annotations) || coloring === 'index')
            return [];
        let palette = Theme.palette();
        let entries = {};
        let index = 1;
        for (let annotation of annotations) {
            let series = annotation.series;
            let instance = annotation.instance;
            let label = coloring === 'series' ? [series] : coloring === 'instance' ? [instance] : [instance, series];
            let key = label.join('-');
            let entry = entries[key];
            if (entry === undefined) {
                let colorKey = getColorKey(widget, series, instance, index);
                entries[key] = { label: label, count: 1, color: Colors.lookup(coloring, colorKey, palette) };
            } else {
                entry.count += 1;
            }
            index++;
        }
        return Object.values(entries).map(function(entry) {
            return {
                label: entry.label,
                value: entry.count + 'x',
                color: entry.color,                
            };
        });
    }

    function getColorKey(widget, series, instance, index) {
        switch (widget.coloring) {
            case 'index': return 'line-' + index;
            case 'series': return series;
            case 'instance-series': return instance + ' ' + series;
            case 'instance': 
            default: return instance;
        }
    } 

    function createIndicatorModel(widget, data) {
        if (!data)
            return { status: 'error', color: Theme.color('error') };
        if (Array.isArray(data) && data.length == 0)
            return { status: 'missing', color: Theme.color('missing'), text: widget.status.missing.hint };
        let status = 'normal';
        for (let seriesData of data)
            status = Units.Alerts.maxLevel(status, seriesData.assessments.status);
        const infoKey = status == 'red' ? 'critical' : status == 'amber' ? 'alarming' : status;
        let statusInfo = widget.status[infoKey] || {};
        return { status: status, color: Theme.color(status), text: statusInfo.hint };
    }

    function createRAGIndicatorModel(widget, legend) {
        const items = [];
        for (let item of legend) {
            items.push({
                label: item.label,
                status: item.status,
                state: item.value,
                color: item.color,
                background: item.highlight,
            });
        }
        return { items: items };
    }

    function createAlertTableModel(widget, alerts, annotations) {
        if (widget.type === 'annotation')
            return {};
        function createAlertAnnotationsFilter(alert) {
          return (annotation) => widget.options.noAnnotations !== true
                && annotation.series == alert.series 
                && annotation.instance == alert.instance
                && Math.round(annotation.time / 1000) >= Math.round(alert.since / 1000) // only same second needed
                && annotation.time <= (alert.until || new Date().getTime());  
        }
        let items = [];
        if (Array.isArray(alerts)) {
            let palette = Theme.palette();
            let fields = widget.fields;
            for (let i = 0; i < alerts.length; i++) {
                let alert = alerts[i];
                let autoInclude = widget.type === 'alert' || ((alert.level === 'red' || alert.level === 'amber') && !alert.acknowledged);
                let filters = widget.decorations.alerts;
                let lastAlertLevel = alert.frames[alert.frames.length - 1].level;
                if (lastAlertLevel == 'green' || lastAlertLevel == 'white')
                    lastAlertLevel = alert.frames[alert.frames.length - 2].level;
                let visible = (alert.acknowledged && filters.noAcknowledged !== true || !alert.acknowledged && filters.noUnacknowledged !== true)
                           && (alert.stopped && filters.noStopped !== true || !alert.stopped && filters.noOngoing !== true)
                           && (lastAlertLevel == 'red' && filters.noRed !== true || lastAlertLevel == 'amber' && filters.noAmber !== true);                  
                if (autoInclude && visible) {
                    let frames = alert.frames.map(function(frame) {
                        return {
                            level: frame.level,
                            since: frame.start,
                            until: frame.end,
                            color: Theme.color(frame.level),
                        };
                    });
                    let instanceColoring = widget.coloring === 'instance' || widget.coloring === undefined;
                    items.push({
                        serial: alert.serial,
                        name: alert.initiator.name,
                        unit: alert.initiator.unit,
                        acknowledged: alert.acknowledged,
                        series: alert.series == widget.series ? undefined : alert.series,
                        instance: alert.instance,
                        color: instanceColoring ? Colors.lookup('instance', alert.instance, palette) : undefined,
                        frames: frames,
                        watch: alert.initiator,
                        annotations: annotations.filter(createAlertAnnotationsFilter(alert)).map(function(annotation) {
                            return {
                                time: annotation.time,
                                value: annotation.value,
                                attrs: annotation.attrs,
                                fields: fields,
                            };
                        }),
                    });
                }
            }
        }
        return { id: widget.target + '_alerts', verbose: widget.type === 'alert', items: items };
    }

    function createAnnotationTableModel(widget, annotations) {
        if (widget.type !== 'annotation')
            return {};
        let items = [];
        if (Array.isArray(annotations)) {
            let palette = Theme.palette();
            let index = 1;
            for (let annotation of annotations) {
                let colorKey = getColorKey(widget, annotation.series, annotation.instance, index);
                items.push({
                    color: Colors.lookup(widget.coloring, colorKey, palette),
                    series: annotation.series,
                    instance: annotation.instance,
                    unit: widget.unit,
                    time: annotation.time,
                    value: annotation.value,
                    attrs: annotation.attrs,
                    fields: widget.fields,
                });
                index++;
            }
        }
        return { id: widget.target + '_annotations', mode: widget.mode, sort: widget.sort, items: items };
    }

    function createRoleSelectionModel(onExit) {
        return { id: 'RoleSelector', onChange: role => {
                MonitoringConsole.Model.Role.set(role);
                $('#RoleSelector').hide();
                if (onExit !== undefined)
                    onExit();
            }, items: [
            { name: 'guest', label: 'Guest' , description: 'Automatically uses latest server page configuration. Existing local changes are overridden. Local changes during the session do not affect the remote configuration.'},
            { name: 'user', label: 'User' , description: 'Can select for each individual page if server configuration replaces local page. Can manually update local page with server page configuration during the session.' },
            { name: 'admin', label: 'Administrator' , description: 'Can select for each individual page if server configuration replaces local page. Can manually update local pages with server page configuration or update server configuration with local changes. For pages with automatic synchronisation local changes do affect server page configurations.' },
        ]};
    }

    function createWizardModalDialogModel(initiallySelectedSeries, onExit) {
        if (initiallySelectedSeries !== undefined && !Array.isArray(initiallySelectedSeries))
            initiallySelectedSeries = [ initiallySelectedSeries ];
        function objectToOptions(obj) {
            const options = [];
            for (const [key, value] of Object.entries(obj))
                options.push({ label: value, filter: key });
            return options;
        }

        function loadSeries() {
            return new Promise(function(resolve, reject) {
                Controller.requestListOfSeriesData({ groupBySeries: true, queries: [{
                    widgetId: 'auto', 
                    series: '?:* *',
                    truncate: ['ALERTS', 'POINTS'],
                    exclude: ['ALERTS', 'WATCHES']
                }]}, 
                (response) => resolve(response.matches),
                () => reject(undefined));
            });
        }

        function metadata(match, attr) {
            const metadata = match.annotations.filter(a => a.permanent)[0];
            return metadata === undefined ? undefined : metadata.attrs[attr];
        }

        function matchesText(value, input) {
            return value.toLowerCase().includes(input.toLowerCase());
        }

        const results = {
            ok: initiallySelectedSeries,
            cancel: initiallySelectedSeries,
        };

        const wizard = { 
            key: 'series', 
            entry: ['series', 'displayName', 'description', 'unit'],
            selection: initiallySelectedSeries,
            render: entry => {
                const span = $('<span/>', { title: entry.description || '' });
                if (entry.displayName)
                    span.append($('<b/>').text(entry.displayName)).append(' ');
                span.append($('<code/>').text(entry.series));
                if (entry.unit)
                    span.append(' ').append($('<em/>').text('[' + entry.unit + ']'));
                if (entry.describe && entry.description)
                    span.append($('<p/>').text(entry.description));
                return span;
            },
            // the function that produces match entries
            onSearch: loadSeries,
            // these are the how to get a filter property from a match entry
            properties: {
                ns: match => match.series.startsWith('ns:') ? match.series.substring(3, match.series.indexOf(' ')) : undefined,
                series: match => match.series,
                app: match => metadata(match, 'App'),
                name: match => metadata(match, 'Name'),
                displayName: match => metadata(match, 'DisplayName'),
                description: match => metadata(match, 'Description'),
                type: match => metadata(match, 'Type'),
                property: match => metadata(match, 'Property'),
                unit: match => metadata(match, 'Unit'),
                group: match =>  {
                    let groupIndex = match.series.indexOf(' @:');
                    return groupIndex < 0 ? undefined : match.series.substring(groupIndex + 3, match.series.indexOf(' ', groupIndex + 3));
                },
                metric: match => match.series.substring(match.series.lastIndexOf(' ') + 1),
            },            
            // filters link to the above properties to extract match data
            filters: [
                { label: 'Source', property: 'ns', options: [
                    { label: 'Server Metrics', filter: ns => ns != 'metric' },
                    { label: 'MicroProfile Metrics', filter: 'metric' }
                ]},
                { label: 'MicroProfile Application', property: 'app', requires: { ns: 'metric' }},
                { label: 'MicroProfile Type', property: 'type', requires: { ns: 'metric' }, options: [ // values are as used by MP metrics type
                    { label: 'Counter', filter: 'counter' },
                    { label: 'Timer', filter: 'timer' },
                    { label: 'Gauge', filter: 'gauge' },
                    { label: 'Concurrent Gauge', filter: 'concurrent gauage' },
                    { label: 'Meter', filter: 'meter' },
                    { label: 'Histogram', filter: 'histogram' },
                    { label: 'Simple Timer', filter: 'simple timer' }
                ]},
                { label: 'MicroProfile Unit', property: 'unit', requires: { ns: 'metric' }},
                { label: 'Namespace', property: 'ns', requires: { ns: ns => ns != 'metric' }, 
                    options: () => objectToOptions(MonitoringConsole.Data.NAMESPACES)
                        .filter(option => option.filter != 'metric' && option.filter != 'other') },
                { label: 'MicroProfile Property', property: 'property', requires: { ns: 'metric'} },
                { label: 'MicroProfile Name', property: 'name', requires: { ns: 'metric' }, 
                    filter: matchesText },                
                { label: 'MicroProfile Display Name', property: 'displayName', requires: { ns: 'metric' }, 
                    filter: matchesText },                
                { label: 'MicroProfile Description', property: 'description', requires: { ns: 'metric' }, 
                    filter: matchesText },                
                { label: 'Group', property: 'group' },
                { label: 'Metric', property: 'metric' },
                { label: 'Series', property: 'series', filter: matchesText },
            ],
            // what should happen if the selection made by the user changes
            onChange: selectedSeries => results.ok = selectedSeries,
        };

        return { id: 'ModalDialog', 
            title: 'Select Metric Series...',
            content: () => Components.createSelectionWizard(wizard),
            buttons: [
                { property: 'ok', label: 'OK' },
                { property: 'cancel', label: 'Cancel' },
            ],
            results: results,
            onExit: onExit,
        };
    }

    /**
     * This function is called when the watch details settings should be opened
     */
    function onOpenWatchSettings() {
        function wrapOnSuccess(onSuccess) {
            return () => {
                if (typeof onSuccess === 'function')
                    onSuccess();
                onOpenWatchSettings();
            };
        }
        Controller.requestListOfWatches((watches) => {
            const manager = { 
                id: 'WatchManager', 
                items: watches, 
                colors: { red: Theme.color('red'), amber: Theme.color('amber'), green: Theme.color('green') },
                actions: { 
                    onCreate: (watch, onSuccess, onFailure) => Controller.requestCreateWatch(watch, wrapOnSuccess(onSuccess), onFailure),
                    onDelete: (name, onSuccess, onFailure) => Controller.requestDeleteWatch(name, wrapOnSuccess(onSuccess), onFailure),
                    onDisable: (name, onSuccess, onFailure) => Controller.requestDisableWatch(name, wrapOnSuccess(onSuccess), onFailure),
                    onEnable: (name, onSuccess, onFailure) => Controller.requestEnableWatch(name, wrapOnSuccess(onSuccess), onFailure),
                },
            };
            updatePageNavigation('Watches');
            $('#chart-grid').hide();
            $('#WatchManager').replaceWith(Components.createWatchManager(manager));
        });
    }

    /**
     * This function is called when data was received or was failed to receive so the new data can be applied to the page.
     *
     * Depending on the update different content is rendered within a chart box.
     */
    function onDataUpdate(update) {
        let widget = update.widget;
        let data = update.data;
        let alerts = update.alerts;
        let annotations = update.annotations;
        updateDomOfWidget(undefined, widget);
        let widgetNode = $('#widget-' + widget.target);
        let legendNode = widgetNode.find('.Legend').first();
        let indicatorNode = widgetNode.find('.Indicator').first();
            if (indicatorNode.length == 0)
                indicatorNode = widgetNode.find('.RAGIndicator').first();
        let alertsNode = widgetNode.find('.AlertTable').first();
        let annotationsNode = widgetNode.find('.AnnotationTable').first();
        let legend = createLegendModel(widget, data, alerts, annotations); // OBS this has side effect of setting .legend attribute in series data
        if (data !== undefined && (widget.type === 'line' || widget.type === 'bar')) {
            MonitoringConsole.Chart.getAPI(widget).onDataUpdate(update);
        }
        if (widget.type == 'rag') {
            alertsNode.hide();
            legendNode.hide();
            indicatorNode.replaceWith(Components.createRAGIndicator(createRAGIndicatorModel(widget, legend)));
            annotationsNode.hide();
        } else {
            alertsNode.replaceWith(Components.createAlertTable(createAlertTableModel(widget, alerts, annotations)));
            legendNode.replaceWith(Components.createLegend(legend));
            indicatorNode.replaceWith(Components.createIndicator(createIndicatorModel(widget, data)));
            annotationsNode.replaceWith(Components.createAnnotationTable(createAnnotationTableModel(widget, annotations)));            
        }
    }

    /**
     * This function refleshes the page with the given layout.
     */
    function onPageUpdate(layout) {
        function addWidgets(selectedSeries, row, col) {
            if (selectedSeries !== undefined && selectedSeries.length > 0) {
                const grid = { column: col, item: row };
                onPageChange(MonitoringConsole.Model.Page.Widgets.add(selectedSeries, grid));
            }
        }
        function createPlusButton(row, col) {
            return $('<button/>', { text: '+', 'class': 'big-plus' })
                .click(() => $('#ModalDialog').replaceWith(Components.createModalDialog(
                    createWizardModalDialogModel([], selectedSeries => addWidgets(selectedSeries, row, col))))); 
        }              
        let numberOfColumns = layout.length;
        let maxRows = layout[0].length;
        let table = $("<table/>", { id: 'chart-grid', 'class': 'columns-'+numberOfColumns + ' rows-'+maxRows });
        let padding = 30;
        let headerHeight = 40;
        let minRowHeight = 160;
        let rowsPerScreen = maxRows;
        let windowHeight = $(window).height();
        let rowHeight = 0;
        while (rowsPerScreen > 0 && rowHeight < minRowHeight) {
            rowHeight = Math.round((windowHeight - headerHeight) / rowsPerScreen) - padding; // padding is subtracted
            rowsPerScreen--; // in case we do another round try one less per screen
        }
        if (rowHeight == 0) {
            rowHeight = windowHeight - headerHeight - padding;
        }
        for (let row = 0; row < maxRows; row++) {
            let tr = $("<tr/>");
            for (let col = 0; col < numberOfColumns; col++) {
                let cell = layout[col][row];
                if (cell) {
                    let rowspan = cell.rowspan;
                    let height = (rowspan * rowHeight);
                    let td = $("<td/>", { id: 'widget-'+cell.widget.target, colspan: cell.colspan, rowspan: rowspan, 'class': 'widget', style: 'height: '+height+"px;"});
                    updateDomOfWidget(td, cell.widget);
                    tr.append(td);
                } else if (cell === null) {
                    tr.append($("<td/>", { 'class': 'widget empty', style: 'height: '+rowHeight+'px;'}).append(createPlusButton(row, col)));                  
                }
            }
            table.append(tr);
        }
        $('#chart-container').empty();
        $('#chart-container').append(table);
    }

    function onPagePush() {
        MonitoringConsole.Model.Page.Sync.pushLocal(onPageRefresh);
    }

    async function onPagePull() {
        await MonitoringConsole.Model.Page.Sync.pullRemote();
        onPageRefresh();
    }

    function onPagesSync() {
        MonitoringConsole.Model.Page.Sync.providePullRemoteModel(model => {
            let onUpdate = model.onUpdate;
            model.id = 'PageManager';
            model.onUpdate = async function(pageIds) {
                await onUpdate(pageIds);
                $('#PageManager').hide();
                onPageRefresh();
            };
            model.onCancel = () => $('#PageManager').hide();
            $('#PageManager').replaceWith(Components.createPageManager(model));
        });
    }    

    function onPageRefresh() {
        onPageChange(MonitoringConsole.Model.Page.changeTo(MonitoringConsole.Model.Page.id()));
    }

    /**
     * Method to call when page changes to update UI elements accordingly
     */
    function onPageChange(layout) {
        MonitoringConsole.Chart.Trace.onClosePopup();
        $('#WatchManager').hide();
        $('#chart-grid').show();
        onPageUpdate(layout);
        updatePageNavigation();
        updateSettings();
        updateMenu();
    }

    function onOpenWidgetSettings(widgetId) {
        MonitoringConsole.Model.Page.Widgets.Selection.clear();
        MonitoringConsole.Model.Page.Widgets.Selection.toggle(widgetId);
        MonitoringConsole.Model.Settings.open();
        updateSettings();
    }

    /**
     * Public API of the View object:
     */
    return {
        Units: Units,
        Colors: Colors,
        Components: Components,
        onPageReady: function() {
            let hash = window.location.hash;
            let targetPageId = hash.length <= 1 ? undefined : hash.substring(1);
            // connect the view to the model by passing the 'onDataUpdate' function to the model
            // which will call it when data is received
            let layout = MonitoringConsole.Model.init(onDataUpdate, onPageChange);
            if (targetPageId === undefined)
                onPageChange(layout);
            Colors.scheme('Payara', false);
            if (targetPageId)
                onPageChange(MonitoringConsole.Model.Page.changeTo(targetPageId));
            $(window).on('hashchange', function(e) {
                let pageId = window.location.hash.substring(1);
                if (pageId != MonitoringConsole.Model.Page.id()) {
                    onPageChange(MonitoringConsole.Model.Page.changeTo(pageId));
                }
            });
            if (!MonitoringConsole.Model.Role.isDefined()) {
                $('#RoleSelector').replaceWith(Components.createRoleSelector(createRoleSelectionModel(onPagesSync)));
            } else {
                onPagesSync();
            }
        },
        onPageChange: (layout) => onPageChange(layout),
        onPageUpdate: (layout) => onPageUpdate(layout),
        onPageReset: () => onPageChange(MonitoringConsole.Model.Page.reset()),
        onPageImport: (src) => MonitoringConsole.Model.importPages(src, onPageChange),
        onPageExport: () => onPageExport('monitoring-console-config.json', MonitoringConsole.Model.exportPages()),
        onPageMenu: function() { MonitoringConsole.Model.Settings.toggle(); updateSettings(); },
        onPageLayoutChange: (numberOfColumns) => onPageUpdate(MonitoringConsole.Model.Page.arrange(numberOfColumns)),
        onPageDelete: function() {
            if (window.confirm("Do you really want to delete the current page?")) { 
                onPageUpdate(MonitoringConsole.Model.Page.erase());
                updatePageNavigation();
            }
        },
        onOpenWidgetSettings: (widgetId) => onOpenWidgetSettings(widgetId),
    };
})();
