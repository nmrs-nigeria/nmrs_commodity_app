<%@ page import="org.openmrs.module.openhmis.inventory.web.ModuleWebConstants" %>
<%@ page import="org.openmrs.module.openhmis.inventory.web.PrivilegeWebConstants" %>

<%@ include file="/WEB-INF/template/include.jsp"%>

<%--
  ~ The contents of this file are subject to the OpenMRS Public License
  ~ Version 2.0 (the "License"); you may not use this file except in
  ~ compliance with the License. You may obtain a copy of the License at
  ~ http://license.openmrs.org
  ~
  ~ Software distributed under the License is distributed on an "AS IS"
  ~ basis, WITHOUT WARRANTY OF ANY KIND, either express or implied. See
  ~ the License for the specific language governing rights and
  ~ limitations under the License.
  ~
  ~ Copyright (C) OpenHMIS.  All Rights Reserved.
  --%>
<openmrs:require allPrivileges="<%= PrivilegeWebConstants.INVENTORY_PAGE_PRIVILEGES %>"
                 otherwise="/login.htm"
                 redirect="<%= ModuleWebConstants.INVENTORY_CREATION_PAGE %>" />
<openmrs:message var="pageTitle" code="openhmis.inventory.title" scope="page"/>

<%@ include file="/WEB-INF/template/header.jsp" %>
<%@ include file="template/localHeader.jsp"%>

<openmrs:htmlInclude file='<%= ModuleWebConstants.MODULE_RESOURCE_ROOT + "css/operations.css" %>' />
<openmrs:htmlInclude file='<%= ModuleWebConstants.MODULE_RESOURCE_ROOT + "js/screen/inventoryCreate.js" %>' />

<h2><spring:message code="openhmis.inventory.admin.create" /></h2>
<table style="width: 99%">
    <tr>
        <td style="vertical-align: top; width: 250px;">
            <br />
            <a href="${pageContext.request.contextPath}<%= ModuleWebConstants.INVENTORY_PAGE %>"><spring:message code="openhmis.inventory.admin.pending"/></a><br />
            <b><spring:message code="openhmis.inventory.admin.create"/></b><br />
            <c:if test="${showStockTakeLink}">
	            <a href="${pageContext.request.contextPath}<%= ModuleWebConstants.INVENTORY_STOCK_TAKE_PAGE %>"><spring:message code="openhmis.inventory.admin.stockTake"/></a><br />
            </c:if>
            <a href="${pageContext.request.contextPath}<%= ModuleWebConstants.INVENTORY_REPORTS_PAGE %>"><spring:message code="openhmis.inventory.admin.reports"/></a>
        </td>
        <td>
            <input type=hidden class="isOperationNumberAutoGenerated" value="${isOperationNumberAutoGenerated}"/>

            <div id="newOperation">
                <div id="newOperationDialog" title="New Operation"></div>
            </div>
        </td>
    </tr>
</table>
<div id="processingDialog" style="display: none">
    <spring:message htmlEscape="false" code="openhmis.inventory.admin.create.processing"/>
</div>

<%@ include file="/WEB-INF/template/footer.jsp" %>
