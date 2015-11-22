<!DOCTYPE html>
<html lang="en" ng-app="autobiographyApp">
<head>
    <base href="/">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta http-equiv="content-type" content="text/html; charset=utf-8">


    <script src="../../../assets/js/angular.js"></script>
    <script src="../../../assets/js/angular-route.js"></script>

    <script src="../../../assets/js/controllers.js"></script>
    <script src="../../../assets/js/base.js"></script>

</head>
<body ng-controller="AutoBioController">


<header class="header">
    <div class="navTop">
        <a class="logo" href="/"></a>
    </div>
    <nav class="navigation">
        <div class="navContainer">
            <#if username??>
                Welcome, ${username?html}!
                <a class="logout-link" href="logout">logout</a></li>
            </#if>

        </div>
    </nav>
</header>
<div class="container">
    <div ng-view></div>
</div>
</body>
</html>