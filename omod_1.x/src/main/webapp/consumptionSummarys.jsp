<%@ page import="org.openmrs.module.openhmis.inventory.web.ModuleWebConstants" %>
<%@ page import="org.openmrs.module.openhmis.inventory.web.PrivilegeWebConstants" %>

<%@ include file="/WEB-INF/template/include.jsp"%>
<openmrs:require allPrivileges="<%= PrivilegeWebConstants.CONSUMPTION_SUMMARY_PAGE_PRIVILEGES %>" otherwise="/login.htm"
                 redirect="<%= ModuleWebConstants.CONSUMPTIONS_SUMMARY_PAGE %>" />
<%@ include file="/WEB-INF/template/header.jsp"%>
<%@ include file="template/localHeader.jsp"%>

<openmrs:htmlInclude file='<%= ModuleWebConstants.MODULE_RESOURCE_ROOT + "css/items.css" %>' />
<openmrs:htmlInclude file='<%= ModuleWebConstants.MODULE_RESOURCE_ROOT + "js/screen/consumptionSummarys.js" %>' />

<%@ include file="template/linksHeader.jsp"%>
<h2>
	<spring:message code="openhmis.inventory.admin.consumptionSummarys" />
</h2>

<%@ include file="/WEB-INF/template/footer.jsp" %>
