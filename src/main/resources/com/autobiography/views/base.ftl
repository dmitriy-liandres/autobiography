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
    <script type="text/javascript" src="../../../assets/js/angular/ui-bootstrap-tpls-1.3.2.min.js"></script>

    <script type="text/javascript" src="../../../assets/js/factories.js"></script>
    <script type="text/javascript" src="../../../assets/js/controllers.js"></script>
    <script type="text/javascript" src="../../../assets/js/route.js"></script>

    <script type="text/javascript" src="../../../assets/js/messages_js_${getServerLocale()}.js"></script>

    <link rel="stylesheet" href="../../../assets/css/ckeditor.css" type="text/css">
    <!--<link rel="stylesheet" href="../../../assets/css/bootstrap.min.css" type="text/css">-->
    <link rel="stylesheet" href="../../../assets/css/main.css" type="text/css">

    <script>
        var host = window.location.hostname;
        if(!host.indexOf("historyabout") >=0 ) {
            window.ga=window.ga||function(){(ga.q=ga.q||[]).push(arguments)};ga.l=+new Date;
            ga('create', 'UA-77196908-1', 'auto');
            ga('require', 'autotrack');
            ga('send', 'pageview');
        }
   </script>
    <script async src='https://www.google-analytics.com/analytics.js'></script>
    <script type="text/javascript" src="../../../assets/js/autotrack.js"></script>

</head>
<body ng-controller="EmptyController">

<div class="wrapper">
    <header class="header">
        <div class="navTop">
            <a class="logo" href="/"></a>
        </div>
        <nav class="navigation">
            <div>
                <div class="topPanelBiography">${message("topPanel.biography")}</div>
                <input type="text" ng-model="profileSelected" placeholder="${message("topPanel.search.tooltip")}"
                       uib-typeahead="profile as (profile.name + ' ' + profile.surname) for profile in searchProfiles($viewValue)"
                       typeahead-loading="loadingProfiles" typeahead-no-results="noResults" class="form-control search"
                       typeahead-on-select="selectProfile($item, $model, $label, $event)">
                <i ng-show="loadingProfiles" class="glyphicon glyphicon-refresh"></i>

                <div ng-show="noResults">
                    <i class="glyphicon glyphicon-remove"></i> ${message("topPanel.search.noResults")}
                </div>
                <div class="logged">
                    <#if loggedInPersonId??>
                        <a class="logout-link" href="logout"> ${message("topPanel.logout")}</a></li>
                        <input type="hidden" value="${loggedInPersonId}" id="loggedInPersonId">
                    </#if>
                    <#if !loggedInPersonId??>
                        <a class="all-bios-link" href="/">${message("topPanel.registration")}</a>&nbsp;|
                        <a class="all-bios-link" href="/">${message("topPanel.signIn")}</a>
                        <input type="hidden" value="-1" id="loggedInPersonId">
                    </#if>
                </div>
                <div class="clear"></div>
            </div>
            <div class="navContainer">
                <a class="all-bios-link" href="/">${message("topPanel.main")}</a>
                <a class="all-bios-link" href="/about">${message("about.name")}</a>
                <a class="all-bios-link" href="/all">${message("topPanel.search.all")}</a>
                <a class="all-bios-link" href="/about">${message("about.name")}</a>
                <a class="all-bios-link" href="/contact">${message("contact.name")}</a>
                <a href="?lang=en" target="_self"><img src="/assets/img/United-Kingdom.png">English</a>
                <a href="?lang=ru" target="_self"><img src="/assets/img/Russia.png">Русский</a>
                <div class="clear"></div>
            </div>
        </nav>
    </header>
    <div class="container">
        <div ng-view></div>
    </div>
</div>
</body>
</html>