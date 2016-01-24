<!DOCTYPE html>
<html lang="en" ng-app="autobiographyApp">
<head>
    <base href="/">
    <meta charset="UTF-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta http-equiv="content-type" content="text/html; charset=UTF-8">

    <script type="text/javascript" src="../../../assets/js/angular.js"></script>
    <script type="text/javascript" src="../../../assets/js/angular-resource.js"></script>
    <script type="text/javascript" src="../../../assets/js/angular-route.js"></script>

    <script type="text/javascript" src="../../../assets/js/factories.js"></script>
    <script type="text/javascript" src="../../../assets/js/controllers.js"></script>
    <script type="text/javascript" src="../../../assets/js/base.js"></script>

    <script type="text/javascript" src="../../../assets/js/messages_js_${getServerLocale()}.js"></script>

    <!--<script type="text/javascript" src="../../../assets/js/ckeditor/ckeditor.js"></script>
        -->
</head>
<body ng-controller="EmptyController">


<header class="header">
    <div class="navTop">
        <a class="logo" href="/"></a>
    </div>
    <nav class="navigation">
        <div class="navContainer">
            <#if username??>
            ${message("welcome")}, ${(fullName?html)!username?html}!
                <a class="logout-link" href="logout"> ${message("logout")}</a></li>
                <input type="hidden" value="true" id="isLoggedIn"></a>
            </#if>
            <a href="?lang=en" target="_self">English</a>
            <a href="?lang=ru" target="_self">Русский</a>

        </div>
    </nav>
</header>
<div class="container">
    <div ng-view></div>
</div>
</body>
</html>