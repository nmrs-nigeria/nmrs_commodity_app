<%@ page import="org.openmrs.module.openhmis.inventory.web.ModuleWebConstants" %>
<%@ page import="org.openmrs.module.openhmis.inventory.web.PrivilegeWebConstants" %>

<%@ include file="/WEB-INF/template/include.jsp"%>

<c:if test="${!showItemExpirationSummaryLink}">
    <c:redirect url="/module/openhmis/inventory/inventory.form"/>
</c:if>
<openmrs:message var="pageTitle" code="openhmis.inventory.title" scope="page"/>

<%@ include file="/WEB-INF/template/header.jsp" %>
<%@ include file="template/localHeader.jsp"%>

<openmrs:htmlInclude file='<%= ModuleWebConstants.MODULE_RESOURCE_ROOT + "css/operations.css" %>' />
<openmrs:htmlInclude file='<%= ModuleWebConstants.MODULE_RESOURCE_ROOT + "js/screen/itemExpirationSummary.js" %>' />

<h2><spring:message code="openhmis.inventory.admin.itemExpirationSummary" /></h2>
<input type=hidden class="isOperationNumberAutoGenerated" value="${isOperationNumberAutoGenerated}"/>
<table style="width: 99%">
    <tr>
        <td style="vertical-align: top; width: 250px;">
            <br />
            <a href="${pageContext.request.contextPath}<%= ModuleWebConstants.INVENTORY_PAGE %>"><spring:message code="openhmis.inventory.admin.pending"/></a><br />
            <a href="${pageContext.request.contextPath}<%= ModuleWebConstants.INVENTORY_CREATION_PAGE %>"><spring:message code="openhmis.inventory.admin.create"/></a><br />
            <b>
                <spring:message code="openhmis.inventory.admin.itemExpirationSummary"/>
            </b><br/>
            <a href="${pageContext.request.contextPath}<%= ModuleWebConstants.INVENTORY_REPORTS_PAGE %>"><spring:message code="openhmis.inventory.admin.reports"/></a>
        </td>
        <td>
            <div id="stockTakeList"></div>
        </td>
    </tr>
</table>
<div id="processingDialog" style="display: none">
    <spring:message htmlEscape="false" code="openhmis.inventory.admin.create.processing"/>
</div>

<%@ include file="/WEB-INF/template/footer.jsp" %>
