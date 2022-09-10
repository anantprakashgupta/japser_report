<%@page import="jasper_export.*"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%

jasper e=new jasper();
e.exportPDFToFile(jasperInputStream, models, reportParam, outputFile);

%>

<form>

  <input type="submit" onClick="e.exportPDFToFile()"/>
</form>
