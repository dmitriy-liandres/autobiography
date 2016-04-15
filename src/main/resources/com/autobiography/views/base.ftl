<!DOCTYPE html>
<html lang="en" ng-app="autobiographyApp">
<head>
    <base href="/">
    <meta charset="UTF-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta http-equiv="content-type" content="text/html; charset=UTF-8">

    <script type="text/javascript" src="../../../assets/js/angular/angular.js"></script>
    <script type="text/javascript" src="../../../assets/js/angular/angular-resource.js"></script>
    <script type="text/javascript" src="../../../assets/js/angular/angular-route.js"></script>
    <script type="text/javascript" src="../../../assets/js/angular/angular-animate.js"></script>
    <script type="text/javascript" src="../../../assets/js/angular/angular-touch.js"></script>
    <script type="text/javascript" src="../../../assets/js/angular/ui-bootstrap-tpls-1.2.3.min.js"></script>

    <script type="text/javascript" src="../../../assets/js/factories.js"></script>
    <script type="text/javascript" src="../../../assets/js/controllers.js"></script>
    <script type="text/javascript" src="../../../assets/js/base.js"></script>

    <script type="text/javascript" src="../../../assets/js/messages_js_${getServerLocale()}.js"></script>

    <link rel="stylesheet" href="../../../assets/css/ckeditor.css" type="text/css">

</head>
<body ng-controller="EmptyController">


<header class="header">
    <div class="navTop">
        <a class="logo" href="/"></a>
    </div>
    <nav class="navigation">
        <div class="navContainer">
        <#if loggedInPersonId??>
        ${message("welcome")}, ${(fullName?html)!loggedInPersonId?html}!
            <a class="logout-link" href="logout"> ${message("logout")}</a></li>
            <input type="hidden" value="${loggedInPersonId}" id="loggedInPersonId">
        </#if>
        <#if !loggedInPersonId??>
            <a class="login-link" href="/"> ${message("login")}</a></li>
            <input type="hidden" value="-1" id="loggedInPersonId">
        </#if>
            <a href="?lang=en" target="_self">English</a>
            <a href="?lang=ru" target="_self">Русский</a>
            <input type="text" ng-model="profileSelected" placeholder="${message("topPanel.search.tooltip")}"
                   uib-typeahead="profile as (profile.name + ' ' + profile.surname) for profile in searchProfiles($viewValue)"
                   typeahead-loading="loadingProfiles" typeahead-no-results="noResults" class="form-control"
                   typeahead-on-select="selectProfile($item, $model, $label, $event)">
            <i ng-show="loadingProfiles" class="glyphicon glyphicon-refresh"></i>
            <div ng-show="noResults">
                <i class="glyphicon glyphicon-remove"></i> ${message("topPanel.search.noResults")}
            </div>
            <a class="all-bios-link" href="/all">${message("topPanel.search.all")}</a>
            <a class="all-bios-link" href="/about">${message("about.name")}</a>
            <a class="all-bios-link" href="/contact">${message("contact.name")}</a>


        </div>
    </nav>
</header>
<div class="container">
    <div ng-view></div>
</div>
</body>
</html>