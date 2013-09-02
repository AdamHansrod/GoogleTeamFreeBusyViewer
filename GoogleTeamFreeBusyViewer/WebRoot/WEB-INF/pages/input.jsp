<%@ taglib prefix="s" uri="/struts-tags"%>
<!DOCTYPE html>
<html>
<head>

<link rel="stylesheet" href="<s:url value='/css/css.css'/>" type="text/css" media="screen" />
</head>
<body>
<section>
		<h2>Login</h2>
		<p> Please enter your email and password as well as the Google Group email url to continue.</p><br>
		<section >
				<s:form action="freebusy">
					<s:textfield name="email" label="Email" labelSeparator=":" required="true"  />
					<s:password name="password" label="Password" labelSeparator=":" required="true" />
					<s:textfield name="googleGroupURl" label="Google Group Email URL" labelSeparator=":" required="true" />
					<s:submit id="Button" value="Login" />
				</s:form>
			</section>		
			
		</section>
		
</body>
</html>