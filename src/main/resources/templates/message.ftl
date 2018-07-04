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
        <p>样本库<strong>${(params.author)!""}</strong>，于 ${(params.occurDate)!""} 向您提出问题，问题类型：${(params.questionType)!""}</p>
        <p>相关项目：${(params.projectCode)!""}(${(params.projectName)!""})</p>
        <p>问题详情：${(params.questionDescription)!""}</p>
        <p>请点击此链接查看问题详情：</p>
        <p><a>${(params.httpUrl)!""}</a></p>
        <p>登录授权码为：（<strong>${(params.authorizationCode)!""}</strong>）。</p>
        <p>注：请勿回复此邮件！</p>
</div>
</body>
</html>
