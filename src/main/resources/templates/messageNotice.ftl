<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>消息通知</title>
</head>

<style type="text/css">
    table {
        font-family: "Trebuchet MS", Arial, Helvetica, sans-serif;
        width: 100%;
        border-collapse: collapse;
    }

    td, th {
        font-size: 1em;
        border: 1px solid #5B4A42;
        padding: 3px 7px 2px 7px;
    }

    th {
        font-size: 1.1em;
        text-align: center;
        padding-top: 5px;
        padding-bottom: 4px;
        background-color: #24A9E1;
        color: #ffffff;
    }
</style>
<body>
<div>
    <h2>${(params.strangerName)!""}，您好！</h2>
        <p>您的授权码（<strong>${(params.authorizationCode)!""}</strong>）将于一小时后过期，如果您还未查看回复问题，请您尽快登录并回复！</p>
        <p>请点击此链接查看问题详情：</p>
        <p><a>http://10.24.200.21:8080</a></p>
        <p> </p>
        <p>注：1.请使用中心网络，或者使用VPN进行访问中心网络！</p>
        <p>    2.请勿回复此邮件！</p>
</div>
</body>
</html>
