<%@ taglib prefix="s" uri="/struts-tags"%>
<!DOCTYPE html>
<html>
<head>

<link rel="stylesheet" href="<s:url value='/css/css.css'/>" type="text/css" media="screen" />
</head>
<body>
<section>
<h3>Team Status</h3>
		<s:iterator value="busyTimes" id="busy">
			<p id="busy" class='<s:property value="#busy.CurrentlyBusy" />'>
				<s:property value="#busy.name" />
				<br>
				<s:property value="#busy.times" />
			</p>
		</s:iterator>
</section>
<section>
<h4>Key</h1>
<p id="busy" class="false">Free
</p>
<p id="busy" class="true">
Busy
</p>
</section>
</body>
</html>